package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.PieceMessage;
import ru.nsu.torrent.Messages.RequestMessage;

import java.net.Socket;

public class Downloader implements Runnable {
    Socket socket;
    PieceMessage pieceMessage;
    public Downloader(Socket socket, PieceMessage pieceMessage) {
        this.socket = socket;
        this.pieceMessage = pieceMessage;
    }
    @Override
    public void run() {

    }
}
