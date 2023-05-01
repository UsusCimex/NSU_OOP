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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TorrentListener implements Runnable {
    private InetSocketAddress address;
    private Set<Peer> session;
    private Selector selector;

    private byte[] infoHash = new byte[20];
    public TorrentListener(String host, int port) {
        this.address = new InetSocketAddress(host, port);
        this.session = new HashSet<>();
        System.err.println("Сервер запущен: " + address);
    }

    @Override
    public void run() {
        try {
            this.selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(address);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

            while (!Thread.currentThread().isInterrupted()) {
                this.selector.select();
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
            System.err.println("Сессия открыта: " + socketChannel.getLocalAddress());
        } else {
            socketChannel.close();
        }
    }


    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int numRead = socketChannel.read(byteBuffer);
        if (numRead == -1) {
            System.err.println("Сессия закрыта: " + socketChannel.getLocalAddress());
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
            System.err.println("Не найдена сессия для пира");
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
}