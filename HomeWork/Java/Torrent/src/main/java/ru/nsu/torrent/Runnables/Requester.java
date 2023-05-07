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
        ByteBuffer byteBuffer = ByteBuffer.wrap(requestMessage.toBytes());
        try {
            while (byteBuffer.hasRemaining()) {
                int numWrite = socketChannel.write(byteBuffer);
                if (numWrite == -1) {
                    socketChannel.close();
                    throw new RuntimeException("Error socket write");
                }
            }
            System.err.println("[Requester] Requested: piece(" + requestMessage.getIndex() + "), to " + socketChannel.getRemoteAddress());
        } catch (IOException e) {
            System.err.println("[Requester] Request not uploaded...");
        }
    }
}