package ru.nsu.torrent;

import ru.nsu.torrent.Runnables.TorrentListener;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TorrentClient {
    public static ExecutorService executor = Executors.newFixedThreadPool(3);
    private TorrentFile torrentFile = null;
    private Tracker tracker;
    private PieceManager pieceManager;

    public TorrentClient(String host, int port) {
        TorrentListener torrentListener = new TorrentListener(host, port);
        Thread listenerThread = new Thread(torrentListener);
        listenerThread.start();
    }

    public static Set<byte[]> getAvailableInfoHashes() {
        // Расспарсить все имеющиеся хэши торрентов
        // Хэш получить при помощи SHA-1 к "info" полю .torrent
        return null;
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
        // Начать установку файла (Запрашивать у пиров куски)!
    }
    public TorrentFile getFile() {
        return torrentFile;
    }

    public Tracker getTracker() {
        return tracker;
    }
}