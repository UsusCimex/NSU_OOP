package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Handshake;
import ru.nsu.torrent.Messages.Bitfield;
import ru.nsu.torrent.Messages.Message;
import ru.nsu.torrent.Peer;
import ru.nsu.torrent.TorrentManager;

import java.io.IOException;
import java.net.InetSocketAddress;
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
            try {
                try {
                    this.selector.selectNow();
                } catch (IOException e) {
                    System.err.println("[TorrentServer] Selector destroyed.");
                    throw new RuntimeException(e);
                }
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (key.isAcceptable()) {
                        try {
                            accept(key);
                        } catch (IOException e) {
                            System.err.println("[TorrentServer] Accept failed.");
                            key.channel().close();
                        }
                    } else if (key.isReadable()) {
                        try {
                            read(key);
                        } catch (IOException e) {
                            System.err.println("[TorrentServer] Read failed.");
                            key.channel().close();
                        }
                    }
                }
            } catch (ClosedSelectorException | IOException e) {
                System.err.println("[TorrentServer] Selector closed!");
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

    public void stop() {
        try {
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }
            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}