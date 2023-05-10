package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Handshake;
import ru.nsu.torrent.Messages.Bitfield;
import ru.nsu.torrent.Messages.Message;
import ru.nsu.torrent.Peer;
import ru.nsu.torrent.Torrent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class TorrentServer implements Runnable {
    private final InetSocketAddress address;
    private final Map<SocketChannel, Peer> session;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public TorrentServer(String host, int port) {
        this.address = new InetSocketAddress(host, port);
        this.session = new HashMap<>();
        System.err.println("[TorrentServer] Your address: " + address);
    }

    @Override
    public void run() {
        try {
            this.selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(address);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

            while (!Thread.currentThread().isInterrupted()) {
                this.selector.selectNow();
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (!key.isValid()) {
                        continue;
                    } else if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        read(key);
                    }
                }
            }
        } catch (ClosedSelectorException e) {
            System.err.println("[TorrentServer] Selector closed after exit program!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        byte[] receivedInfoHash = Handshake.receiveHandshake(socketChannel);
        List<byte[]> availableInfoHashes = Torrent.getAvailableInfoHashes();

        byte[] validInfoHash = null;
        for (byte[] infoHash : availableInfoHashes) {
            if (receivedInfoHash != null && Arrays.equals(infoHash, receivedInfoHash)) {
                validInfoHash = infoHash;
                break;
            }
        }

        if (validInfoHash != null) {
            Peer peer = new Peer(socketChannel, validInfoHash);
            peer.setAvailablePieces(Torrent.getTorrentFileByInfoHash(validInfoHash).getPieceManager().getAvailablePieces());

            Bitfield bitfield = new Bitfield(peer.getAvailablePieces());
            Sender sender = new Sender(peer, bitfield);
            Torrent.executor.submit(sender);

            socketChannel.register(this.selector, SelectionKey.OP_READ);
            this.session.put(socketChannel, peer);
            System.err.println("[TorrentServer] Session opened: " + peer.getAddress());
        } else {
            socketChannel.close();
        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        Peer peer = session.get(socketChannel);

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
                this.session.remove(socketChannel);
                socketChannel.close();
                key.cancel();
                return;
            }
        }
        lengthBuffer.flip();
        int messageLength = lengthBuffer.getInt();

        ByteBuffer byteBuffer = ByteBuffer.allocate(messageLength + 4);
        lengthBuffer.flip();
        while(byteBuffer.hasRemaining()) {
            byteBuffer.put(lengthBuffer);
            int numRead = socketChannel.read(byteBuffer);
            if (numRead == -1) {
                System.err.println("[TorrentServer] Session closed: " + socketChannel.getRemoteAddress());
                this.session.remove(socketChannel);
                socketChannel.close();
                key.cancel();
                return;
            }
        }

        Message message = Message.fromBytes(byteBuffer.flip().array());
        Handler sender = new Handler(peer, message);
        Torrent.executor.submit(sender);
    }

    public void stop() {
        try {
            Thread.currentThread().interrupt();

            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }

            Iterator<Map.Entry<SocketChannel, Peer>> iterator = session.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SocketChannel, Peer> entry = iterator.next();
                SocketChannel socketChannel = entry.getKey();
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iterator.remove();
            }

            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}