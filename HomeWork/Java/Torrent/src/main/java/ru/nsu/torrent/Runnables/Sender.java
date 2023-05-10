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
                if ((!peer.isChoked()) || (message.getType() == Unchoke.UNCHOKE)) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(message.toBytes());
                    while (byteBuffer.hasRemaining()) {
                        int numWrite = peer.getSocketChannel().write(byteBuffer);
                        if (numWrite == -1) {
                            peer.getSocketChannel().close();
                            throw new RuntimeException("[Sender] Error socket write");
                        }
                    }
                    if (message instanceof Request) {
                        peer.getAvailablePieces().clear(((Request) message).getIndex());
                    }
                    printMessage(message, peer);
                }
            } else {
                for (Peer pr : Torrent.getTracker().getPeers()) {
                    if (pr.getSocketChannel().isConnected()) {
                        if (!pr.isInterested() && !pr.isChoked()) {
                            ByteBuffer byteBuffer = ByteBuffer.wrap(message.toBytes());
                            while (byteBuffer.hasRemaining()) {
                                int numWrite = pr.getSocketChannel().write(byteBuffer);
                                if (numWrite == -1) {
                                    pr.getSocketChannel().close();
                                    break;
                                }
                            }
                            printMessage(message, pr);
                        }
                    }
                }
            }
        } catch (IOException e){
            System.err.println("[Sender] Message(Type = " + message.getType() + ") not uploaded...");
        }
    }

    private void printMessage(Message message, Peer peer) throws IOException {
        if (message.getType() == Request.REQUEST) {
            System.err.println("[Sender] REQUEST piece: \"" + ((Request) message).getIndex() + "\" to: " + peer.getAddress());
        } else if (message.getType() == Piece.PIECE) {
            System.err.println("[SENDER] PIECE piece: \"" + ((Piece) message).getIndex() + "\" to: " + peer.getAddress());
        }
    }
}