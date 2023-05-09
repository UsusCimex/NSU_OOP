package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Handshake;
import ru.nsu.torrent.Messages.Message;
import ru.nsu.torrent.Messages.Request;
import ru.nsu.torrent.Peer;
import ru.nsu.torrent.TorrentClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class RequestListener implements Runnable {
    private final InetSocketAddress address;
    private final Set<Peer> session;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public RequestListener(String host, int port) {
        this.address = new InetSocketAddress(host, port);
        this.session = new HashSet<>();
        System.err.println("[RequestListener] Your address: " + address);
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
            System.err.println("[RequestListener] Selector closed after exit program!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        byte[] receivedInfoHash = Handshake.receiveHandshake(socketChannel);
        List<byte[]> availableInfoHashes = TorrentClient.getAvailableInfoHashes();

        byte[] validInfoHash = null;
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
            System.err.println("[RequestListener] Session opened: " + rmAddress);
        } else {
            socketChannel.close();
        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        Peer peer = null;
        for (Peer pr : session) {
            if (pr.getSocketChannel().equals(socketChannel)) {
                peer = pr;
                break;
            }
        }

        if (peer == null) {
            System.err.println("[RequestListener] Peer not found... Socket exception!");
            socketChannel.close();
            key.cancel();
            return;
        }

        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        while (lengthBuffer.hasRemaining()) {
            int numRead = socketChannel.read(lengthBuffer);
            if (numRead == -1) {
                System.err.println("[RequestListener] Session closed: " + socketChannel.getRemoteAddress());
                this.session.remove(peer);
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
                System.err.println("[RequestListener] Session closed: " + socketChannel.getRemoteAddress());
                this.session.remove(peer);
                socketChannel.close();
                key.cancel();
                return;
            }
        }

        byte[] infoHash = peer.getInfoHash();
        Message message = Message.fromBytes(byteBuffer.flip().array());
        if (message.getType() == 6) {
            Request request = (Request) message;
            Uploader uploader = new Uploader(socketChannel, request, infoHash);
            TorrentClient.executor.submit(uploader);
        }
    }

    public void stop() {
        try {
            Thread.currentThread().interrupt();

            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }

            for (Peer peer : session) {
                if (peer.getSocketChannel() != null) {
                    peer.getSocketChannel().close();
                }
            }
            session.clear();

            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}