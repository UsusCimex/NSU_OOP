package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.RequestMessage;
import java.nio.channels.SocketChannel;

public class Uploader implements Runnable {
    SocketChannel socketChannel;
    RequestMessage requestMessage;
    byte[] infoHash;

    public Uploader(SocketChannel socketChannel, RequestMessage requestMessage, byte[] infoHash) {
        this.socketChannel = socketChannel;
        this.requestMessage = requestMessage;
        this.infoHash = infoHash;
    }

    @Override
    public void run() {

    }
}