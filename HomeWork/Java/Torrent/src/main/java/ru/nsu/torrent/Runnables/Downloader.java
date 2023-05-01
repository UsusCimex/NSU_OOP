package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.PieceMessage;
import java.nio.channels.SocketChannel;

public class Downloader implements Runnable {
    SocketChannel socketChannel;
    PieceMessage pieceMessage;
    byte[] infoHash;

    public Downloader(SocketChannel socketChannel, PieceMessage pieceMessage, byte[] infoHash) {
        this.socketChannel = socketChannel;
        this.pieceMessage = pieceMessage;
        this.infoHash = infoHash;
    }
    @Override
    public void run() {

    }
}
