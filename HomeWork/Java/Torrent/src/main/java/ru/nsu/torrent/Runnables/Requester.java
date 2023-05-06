package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.RequestMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Requester implements Runnable {
    SocketChannel socketChannel;
    RequestMessage requestMessage;
    byte[] infoHash;

    public Requester(SocketChannel socketChannel, RequestMessage requestMessage, byte[] infoHash) {
        this.socketChannel = socketChannel;
        this.requestMessage = requestMessage;
        this.infoHash = infoHash;
    }

    @Override
    public void run() {
        try {
            System.err.println("[Requester] Start upload request!");
            ByteBuffer byteBuffer = ByteBuffer.wrap(requestMessage.toBytes());
            while (byteBuffer.hasRemaining()) {
                socketChannel.write(byteBuffer);
            }
            System.err.println("[Requester] Requested: " + requestMessage.getIndex() + ", to " + socketChannel.getRemoteAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}