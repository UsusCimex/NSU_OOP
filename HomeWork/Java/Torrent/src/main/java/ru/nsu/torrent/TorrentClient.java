package ru.nsu.torrent;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TorrentClient {
    private ExecutorService executor = Executors.newFixedThreadPool (2);
    private TorrentFile torrentFile = null;
    private Tracker tracker;
    private PieceManager pieceManager;

    public TorrentClient() {
        Runnable uploaderTask = new Runnable () {
            @Override
            public void run () {

            }
        };
        executor.submit(uploaderTask);
    }
    public void selectFile(TorrentFile file) {
        this.torrentFile = file;
        this.tracker = new Tracker(torrentFile);
        this.pieceManager = new PieceManager(torrentFile);
    }
    public void start() {
        if (torrentFile != null) {
            return;
        }
        // Начать установку файла!
    }
    public TorrentFile getFile() {
        return torrentFile;
    }

    public Tracker getTracker() {
        return tracker;
    }
}