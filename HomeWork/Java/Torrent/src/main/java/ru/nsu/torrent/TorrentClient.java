package ru.nsu.torrent;

import java.io.IOException;
import java.util.List;

public class TorrentClient {
    private TorrentFile torrentFile;
    private Downloader downloader;
    private Uploader uploader;
    private PieceManager pieceManager;
    private ConnectionManager connectionManager;
    private int totalPieces;
    private int downloadedPieces;
    public TorrentClient(TorrentFile file) {
        try {
            this.torrentFile = file;
            Tracker tracker = new Tracker();
            List<Peer> peers = tracker.getPeers(torrentFile);
            this.downloader = new Downloader(torrentFile, peers);
            this.uploader = new Uploader(torrentFile, peers);
            this.pieceManager = new PieceManager(torrentFile, peers);
            this.connectionManager = new ConnectionManager(peers);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void start() {
        // Запуск управления соединениями
        connectionManager.manageConnections();

        // Запуск скачивания и раздачи кусочков
        new Thread(() -> downloader.download()).start();
        new Thread(() -> uploader.upload()).start();
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

    public String getFileName() {
        return torrentFile.getName();
    }
}