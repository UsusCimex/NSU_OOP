package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Handshake;
import ru.nsu.torrent.Messages.Bitfield;
import ru.nsu.torrent.Messages.Message;
import ru.nsu.torrent.Peer;
import ru.nsu.torrent.TorrentManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class TorrentServer implements Runnable {
    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;
    private final TorrentManager torrentManager;
    private final Handshake handshake;
    public TorrentServer(String host, int port, TorrentManager torrentManager) throws IOException {
        InetSocketAddress address = new InetSocketAddress(host, port);
        this.torrentManager = torrentManager;
        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.bind(address);
        this.serverSocketChannel.configureBlocking(false);
        this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        this.handshake = new Handshake(torrentManager);
        System.err.println("[TorrentServer] Your address: " + address);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Iterator<SelectionKey> keys;
            try {
                this.selector.select(100);
                keys = this.selector.selectedKeys().iterator();
            } catch (IOException | ClosedSelectorException e) {
                System.err.println("[TorrentServer] Selector destroyed!");
                break;
            }
            while (keys.hasNext()) {
                try {
                    SelectionKey key = keys.next();
                    keys.remove();
                    try {
                        if (key.isAcceptable()) {
                            accept(key);
                        } else if (key.isReadable()) {
                            read(key);
                        }
                    } catch (IOException e) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        Socket socket = socketChannel.socket();
                        System.err.println("[TorrentServer] Session: " + socket.getRemoteSocketAddress() + " closed");
                        torrentManager.getServerSession().remove(socket.getRemoteSocketAddress());
                        socketChannel.close();
                    }
                } catch (IOException e) {
                    System.err.println("[TorrentServer] Socket close exception!");
                }
            }
        }
        try {
            torrentManager.stopSession(torrentManager.getServerSession());
            selector.close();
        } catch (IOException e) {
            System.err.println("[TorrentServer] Selector close exception!");
            throw new RuntimeException(e);
        } finally {
            try {
                serverSocketChannel.close();
            } catch (IOException e) {
                System.err.println("[TorrentServer] ServerSocket channel closed exception!");
                throw new RuntimeException(e);
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.err.println("[TorrentServer] " + socketChannel.getRemoteAddress() + " try to connect.");
        socketChannel.configureBlocking(false);

        String receivedInfoHash = TorrentManager.bytesToHex(handshake.receiveHandshake(socketChannel));
        List<String> availableInfoHashes = torrentManager.getAvailableInfoHashes();

        byte[] validInfoHash = null;
        for (String infoHash : availableInfoHashes) {
            if (infoHash.equals(receivedInfoHash)) {
                validInfoHash = HexFormat.of().parseHex(infoHash);
                break;
            }
        }

        if (validInfoHash != null) {
            Peer peer = new Peer(socketChannel, validInfoHash);
            peer.setAvailablePieces(Objects.requireNonNull(torrentManager.getTorrentFile(validInfoHash)).getPieceManager().getAvailablePieces());

            Bitfield bitfield = new Bitfield(peer.getAvailablePieces());
            Sender sender = new Sender(peer, bitfield, torrentManager);
            torrentManager.executeMessage(sender);

            socketChannel.register(this.selector, SelectionKey.OP_READ);
            torrentManager.getServerSession().put(socketChannel.getRemoteAddress(), peer);
            System.err.println("[TorrentServer] Session opened: " + peer.getAddress());
        } else {
            socketChannel.close();
        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        Peer peer = torrentManager.getServerSession().get(socketChannel.getRemoteAddress());
        if (peer == null) {
            System.err.println("[TorrentServer] Peer not found... Socket exception!");
            socketChannel.close();
            key.cancel();
            return;
        }

        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        while (lengthBuffer.hasRemaining()) {
            int numRead = socketChannel.read(lengthBuffer);
            if (numRead == -1) {
                System.err.println("[TorrentServer] Session closed: " + socketChannel.getRemoteAddress());
                torrentManager.getServerSession().remove(socketChannel.getRemoteAddress());
                socketChannel.close();
                key.cancel();
                return;
            }
        }
        lengthBuffer.flip();
        int messageLength = lengthBuffer.getInt();
        ByteBuffer byteBuffer = ByteBuffer.allocate(messageLength + 4);
        lengthBuffer.flip();
        byteBuffer.put(lengthBuffer);
        while(byteBuffer.hasRemaining()) {
            int numRead = socketChannel.read(byteBuffer);
            if (numRead == -1) {
                System.err.println("[TorrentServer] Session closed: " + socketChannel.getRemoteAddress());
                torrentManager.getServerSession().remove(socketChannel.getRemoteAddress());
                socketChannel.close();
                key.cancel();
                return;
            }
        }
        Message message = Message.fromBytes(byteBuffer.flip().array());
        Handler sender = new Handler(peer, message, torrentManager);
        torrentManager.executeMessage(sender);
    }
}