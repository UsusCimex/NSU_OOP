package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.Message;
import ru.nsu.torrent.Messages.Request;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Sender implements Runnable {
    SocketChannel socketChannel;
    Message message;
    byte[] infoHash;

    public Sender(SocketChannel socketChannel, Message message, byte[] infoHash) {
        this.socketChannel = socketChannel;
        this.message = message;
        this.infoHash = infoHash;
    }

    @Override
    public void run() {
        byte type = message.getType();
        switch (type) {
            case (Request.REQUEST) -> {
                Request request = (Request) message;
                ByteBuffer byteBuffer = ByteBuffer.wrap(request.toBytes());
                try {
                    while (byteBuffer.hasRemaining()) {
                        int numWrite = socketChannel.write(byteBuffer);
                        if (numWrite == -1) {
                            socketChannel.close();
                            throw new RuntimeException("Error socket write");
                        }
                    }
                    System.err.println("[Sender] Requested: piece(" + request.getIndex() + "), to " + socketChannel.getRemoteAddress());
                } catch (IOException e) {
                    System.err.println("[Sender] Request not uploaded...");
                }
            }
        }
    }
}