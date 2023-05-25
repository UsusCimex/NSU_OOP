package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.*;
import ru.nsu.torrent.Messages.Bitfield;
import ru.nsu.torrent.Messages.Message;
import ru.nsu.torrent.Messages.NotInterested;
import ru.nsu.torrent.Messages.Request;

import java.io.IOException;
import java.net.Socket;
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
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (torrentFile == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                continue;
            }
            if (torrentFile.getPieceHashes().size() == torrentFile.getDownloadedPieces()) {
                System.err.println("[TorrentClient] File was downloaded!");
                torrentFile = null;
                continue;
            }
            if (torrentFile.getTracker().getAddresses().isEmpty()) {
                System.err.println("[TorrentClient] Peers not found!");
                torrentFile = null;
                continue;
            }

            System.err.println("[TorrentClient] Start download file: " + torrentFile.getName());
            boolean complete = false;
            while (!complete && !Thread.currentThread().isInterrupted()) {
                torrentManager.updateClientSession(torrentFile, selector);
                Iterator<SelectionKey> keys;
                try {
                    this.selector.select(100);
                    keys = this.selector.selectedKeys().iterator();
                } catch (IOException | ClosedSelectorException e) {
                    System.err.println("[TorrentClient] Selector destroyed!");
                    break;
                }
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (key.isConnectable()) {
                        try {
                            if (((SocketChannel) key.channel()).finishConnect()) {
                                connect(key);
                            }
                        } catch (IOException e) {
                            try {
                                SocketChannel socketChannel = (SocketChannel) (key.channel());
                                Socket socket = socketChannel.socket();
                                System.err.println("[TorrentClient] Connection failed: " + socket.getRemoteSocketAddress());
                                torrentManager.getClientSession().remove(socket.getRemoteSocketAddress());
                                socketChannel.close();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    } else if (key.isReadable()) {
                        try {
                            read(key);
                        } catch (IOException e) {
                            try {
                                SocketChannel socketChannel = (SocketChannel) (key.channel());
                                Socket socket = socketChannel.socket();
                                System.err.println("[TorrentClient] Read failed: " + socket.getRemoteSocketAddress());
                                torrentManager.getClientSession().remove(socket.getRemoteSocketAddress());
                                socketChannel.close();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                    sendRequest(key);
                }
                if (torrentFile == null) break;
                complete = torrentFile.getPieceManager().getNumberOfAvailablePieces() == torrentFile.getPieceManager().getNumberPieces();
            }
            if (complete) {
                System.err.println("[TorrentClient] File download complete: " + torrentFile.getName());
                for (Peer pr : torrentManager.getServerSession().values()) {
                    if (pr.getSocketChannel().isConnected()) {
                        if (pr.getAvailablePieces().cardinality() != torrentFile.getDownloadedPieces()) {
                            Sender sender = new Sender(pr, new Bitfield(torrentFile.getPieceManager().getAvailablePieces()), torrentManager);
                            torrentManager.executeMessage(sender);
                        }
                    }
                }
            }
            torrentFile = null;
        }
        try {
            selector.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void connect(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        Peer peer = torrentManager.getClientSession().get(socketChannel.getRemoteAddress());
        if (peer == null) {
            System.err.println("[TorrentClient] Peer not found!");
            return;
        }
        handshake.sendHandshake(socketChannel, torrentFile.getInfoHash(), new byte[20]);
        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        Peer peer = torrentManager.getClientSession().get(socketChannel.getRemoteAddress());

        if (peer == null) {
            System.err.println("[TorrentClient] Peer not found... Socket exception!");
            socketChannel.close();
            key.cancel();
            return;
        }

        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        int numRead = socketChannel.read(lengthBuffer);
        if (numRead == -1) {
            System.err.println("[TorrentClient] Session closed: " + socketChannel.getRemoteAddress());
            torrentManager.getClientSession().remove(socketChannel.getRemoteAddress());
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
                torrentManager.getClientSession().remove(socketChannel.getRemoteAddress());
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
        Socket socket = socketChannel.socket();
        Peer peer = torrentManager.getClientSession().get(socket.getRemoteSocketAddress());
        if (peer == null) return;
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
}
