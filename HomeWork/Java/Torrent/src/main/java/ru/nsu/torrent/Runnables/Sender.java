package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.*;
import ru.nsu.torrent.Peer;
import ru.nsu.torrent.Torrent;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Sender implements Runnable {
    private final Peer peer;
    private final Message message;

    public Sender(Peer peer, Message message) {
        this.peer = peer;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            if (peer != null) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(message.toBytes());
                while (byteBuffer.hasRemaining()) {
                    int numWrite = peer.getSocketChannel().write(byteBuffer);
                    if (numWrite == -1) {
                        peer.getSocketChannel().close();
                        throw new RuntimeException("Error socket write");
                    }
                }
                System.err.println("[Sender] Message(Type = " + message.getType() + "), send to " + peer.getSocketChannel().getRemoteAddress());
            } else {
                for (Peer pr : Torrent.getTracker().getPeers()) {
                    if (pr.getSocketChannel().isConnected()) {
                        ByteBuffer byteBuffer = ByteBuffer.wrap(message.toBytes());
                        while (byteBuffer.hasRemaining()) {
                            int numWrite = pr.getSocketChannel().write(byteBuffer);
                            if (numWrite == -1) {
                                pr.getSocketChannel().close();
                                break;
                            }
                        }
                        System.err.println("[Sender] Message(Type = " + message.getType() + "), send to " + peer.getSocketChannel().getRemoteAddress());
                    }
                }
            }
        } catch (IOException e){
            System.err.println("[Sender] Message(Type = " + message.getType() + ") not uploaded...");
        }
    }
}