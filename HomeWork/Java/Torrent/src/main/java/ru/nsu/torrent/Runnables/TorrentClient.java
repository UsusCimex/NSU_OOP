package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.*;
import ru.nsu.torrent.Messages.Message;
import ru.nsu.torrent.Messages.NotInterested;
import ru.nsu.torrent.Messages.Request;
import ru.nsu.torrent.Torrent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class TorrentClient implements Runnable {
    private TorrentFile torrentFile = null;
    private final Selector selector;
    private final TorrentManager torrentManager;
    private final Handshake handshake;
    public TorrentClient(TorrentManager torrentManager) throws IOException {
        this.torrentManager = torrentManager;
        this.handshake = new Handshake(torrentManager);
        this.selector = Selector.open();
    }
    public void changeFile(TorrentFile file) {
        torrentFile = file;
    }
    public TorrentFile getTorrentFile() {
        return torrentFile;
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (torrentFile == null) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            if (torrentFile.getPieceHashes().size() == torrentFile.getDownloadedPieces()) {
                System.err.println("[TorrentClient] File was downloaded!");
                torrentFile = null;
                continue;
            }

            System.err.println("[TorrentClient] Start download file: " + torrentFile.getName());
            try {
                for (InetSocketAddress address : torrentFile.getTracker().getAddresses()) {
                    SocketChannel socketChannel = SocketChannel.open();
                    socketChannel.configureBlocking(false);
                    System.err.println("[TorrentClient] connecting to: " + address);
                    socketChannel.connect(address);
                    socketChannel.register(this.selector, SelectionKey.OP_CONNECT);
                    Peer peer = new Peer(socketChannel, torrentFile.getInfoHash());
                    torrentManager.getClientSession().put(socketChannel, peer);
                }
            } catch (IOException e) {
                System.err.println("[TorrentClien] Connection failed!");
            }

            boolean complete = false;
            while (!complete) {
                try {
                    this.selector.select(100);
                } catch (IOException e) {
                    System.err.println("[TorrentClient] Selector destroyed!");
                    throw new RuntimeException(e);
                }
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (key.isConnectable()) {
                        try {
                            if (((SocketChannel) key.channel()).finishConnect()) {
                                connect(key);
                            }
                        } catch (IOException e) {
                            System.err.println("[TorrentClient] Connection failed!");
                            throw new RuntimeException(e);
                        }
                    } else if (key.isReadable()) {
                        try {
                            read(key);
                        } catch (IOException e) {
                            System.err.println("[TorrentClient] Read failed!");
                            throw new RuntimeException(e);
                        }
                    } else if (key.isWritable()) {
                        sendRequest(key);
                    }
                }
                complete = torrentFile.getPieceManager().getNumberOfAvailablePieces() == torrentFile.getPieceManager().getNumberPieces();
            }
            if (complete) System.err.println("[TorrentClient] File download complete: " + torrentFile.getName());
        }
    }

    private void connect(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        Peer peer = torrentManager.getClientSession().get(socketChannel);
        if (peer == null) {
            System.err.println("Peer not found!");
            return;
        }
        handshake.sendHandshake(socketChannel, torrentFile.getInfoHash(), new byte[20]);
        key.interestOps(SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        Peer peer = torrentManager.getClientSession().get(socketChannel);

        if (peer == null) {
            System.err.println("[TorrentClient] Peer not found... Socket exception!");
            socketChannel.close();
            key.cancel();
            return;
        }

        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        int numRead = socketChannel.read(lengthBuffer);
        if (numRead == -1) {
            System.err.println("[Torrent] Session closed: " + socketChannel.getRemoteAddress());
            torrentManager.getClientSession().remove(socketChannel);
            socketChannel.close();
            key.cancel();
            return;
        }
        lengthBuffer.flip();
        int messageLength = lengthBuffer.getInt();

        ByteBuffer byteBuffer = ByteBuffer.allocate(messageLength + 4);
        lengthBuffer.flip();
        byteBuffer.put(lengthBuffer);
        while (byteBuffer.hasRemaining()) {
            numRead = socketChannel.read(byteBuffer);
            if (numRead == -1) {
                System.err.println("[TorrentClient] Session closed: " + socketChannel.getRemoteAddress());
                torrentManager.getClientSession().remove(socketChannel);
                socketChannel.close();
                key.cancel();
                return;
            }
        }

        Message message = Message.fromBytes(byteBuffer.flip().array());
        Handler handler = new Handler(peer, message, torrentManager);
        torrentManager.executeMessage(handler);
    }
    private void sendRequest(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        Peer peer = torrentManager.getClientSession().get(socketChannel);

        if (!peer.isInterested()) return;

        int missingPieceIndex = torrentFile.getPieceManager().getIndexOfSearchedPiece(peer.getAvailablePieces());
        if (missingPieceIndex >= 0 && missingPieceIndex < torrentFile.getPieceHashes().size()) {
            Request request = new Request(missingPieceIndex, 0, (int) Math.min(torrentFile.getPieceLength(), torrentFile.getLength() - missingPieceIndex * torrentFile.getPieceLength()));
            Sender sender = new Sender(peer, request, torrentManager);
            torrentManager.executeMessage(sender);

            peer.getAvailablePieces().clear(missingPieceIndex);
        } else {
            NotInterested notInterested = new NotInterested();
            Sender sender = new Sender(peer, notInterested, torrentManager);
            torrentManager.executeMessage(sender);

            peer.setInterested(false);
        }
    }
    public void stop() {
        try {
            Thread.currentThread().interrupt();

            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
