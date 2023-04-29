package ru.nsu.torrent;

import java.io.IOException;
import java.util.List;

public class TorrentClient {
    private TorrentFile torrentFile = null;
    private Tracker tracker;
    private PieceManager pieceManager;
    private ConnectionManager connectionManager;
    private int totalPieces;
    private int downloadedPieces;
    public void selectFile(TorrentFile file) {
        try {
            this.torrentFile = file;
            this.tracker = new Tracker(torrentFile);
            this.pieceManager = new PieceManager(torrentFile);
            this.connectionManager = new ConnectionManager(tracker.getPeers());
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }
    public void start() {
        if (torrentFile != null) {
            return;
        }
        // Начать установку файла!
    }
    public int getTotalPieces() {
        return totalPieces;
    }

    public int getDownloadedPieces() {
        return downloadedPieces;
    }

    public double getDownloadSpeed() {
        return 0;
    }

    public double getUploadSpeed() {
        return 0;
    }

    public TorrentFile getFile() {
        return torrentFile;
    }

    public Tracker getTracker() {
        return tracker;
    }
}