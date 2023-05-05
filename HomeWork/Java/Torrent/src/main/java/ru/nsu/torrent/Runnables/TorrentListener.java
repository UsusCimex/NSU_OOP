package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Handshake;
import ru.nsu.torrent.Messages.Message;
import ru.nsu.torrent.Messages.PieceMessage;
import ru.nsu.torrent.Messages.RequestMessage;
import ru.nsu.torrent.Peer;
import ru.nsu.torrent.TorrentClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TorrentListener implements Runnable {
    private final InetSocketAddress address;
    private final Set<Peer> session;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public TorrentListener(String host, int port) {
        this.address = new InetSocketAddress(host, port);
        this.session = new HashSet<>();
        System.err.println("Server started: " + address);
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
                this.selector.select(100);
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
            System.err.println("Selector closed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        byte[] receivedInfoHash = Handshake.receiveHandshake(socketChannel);
        Set<byte[]> availableInfoHashes = TorrentClient.getAvailableInfoHashes();
        byte[] validInfoHash = null;
        assert availableInfoHashes != null;
        for (byte[] infoHash : availableInfoHashes) {
            if (receivedInfoHash != null && Arrays.equals(infoHash, receivedInfoHash)) {
                validInfoHash = infoHash;
                break;
            }
        }

        if (validInfoHash != null) {
            socketChannel.register(this.selector, SelectionKey.OP_READ);
            InetSocketAddress rmAddress = ((InetSocketAddress)socketChannel.getRemoteAddress());
            this.session.add(new Peer(rmAddress.getAddress().getHostAddress(), rmAddress.getPort() ,socketChannel, validInfoHash));
            System.err.println("Session opened: " + socketChannel.getLocalAddress());
        } else {
            socketChannel.close();
        }
    }


    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int numRead = socketChannel.read(byteBuffer);
        if (numRead == -1) {
            System.err.println("Session closed: " + socketChannel.getLocalAddress());
            this.session.removeIf(session -> session.getSocketChannel().equals(socketChannel));
            socketChannel.close();
            key.cancel();
            return;
        }

        Peer peer = this.session.stream()
                .filter(session -> session.getSocketChannel().equals(socketChannel))
                .findFirst()
                .orElse(null);

        if (peer == null) {
            System.err.println("less not found...");
            return;
        }

        byte[] infoHash = peer.getInfoHash();

        Message message = Message.fromBytes(byteBuffer.flip().toString().getBytes());
        if (message.getType() == 6) {
            RequestMessage requestMessage = (RequestMessage) message;
            Uploader uploader = new Uploader(socketChannel, requestMessage, infoHash);
            TorrentClient.executor.submit(uploader);
        } else if (message.getType() == 7) {
            PieceMessage pieceMessage = (PieceMessage) message;
            Downloader downloader = new Downloader(socketChannel, pieceMessage, infoHash);
            TorrentClient.executor.submit(downloader);
        }
    }

    public void stop() {
        try {
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }

            for (Peer peer : session) {
                if (peer.getSocketChannel() != null) {
                    peer.getSocketChannel().close();
                }
            }

            if (selector != null) {
                selector.close();
            }

            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}