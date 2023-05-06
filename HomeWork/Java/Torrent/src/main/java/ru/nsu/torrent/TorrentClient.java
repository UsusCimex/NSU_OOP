package ru.nsu.torrent;

import ru.nsu.torrent.Runnables.AnswerListener;
import ru.nsu.torrent.Runnables.RequestListener;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TorrentClient {
    public static ExecutorService executor = Executors.newFixedThreadPool(3);
    private static TorrentFile torrentFile = null;
    private static Tracker tracker;

    public static final String TORRENTS_DIRECTORY = "torrentsDir";
    public static final String DOWNLOADS_DIRECTORY = "downloadsDir";

    private final RequestListener requestListener;
    private AnswerListener answerListener;

    public TorrentClient(String host, int port) {
        requestListener = new RequestListener(host, port);
        Thread listenerThread = new Thread(requestListener);
        listenerThread.start();
    }
    public void stopTorrent() {
        if (requestListener != null) {
            requestListener.stop();
        }
        if (answerListener != null) {
            answerListener.stop();
        }
    }
    public void selectFile(TorrentFile file) {
        torrentFile = file;
        tracker = new Tracker(torrentFile);
    }

    public static TorrentFile getFile() {
        return torrentFile;
    }
    public static Tracker getTracker() {
        return tracker;
    }

    public static List<byte[]> getAvailableInfoHashes() {
        List<byte[]> infoHashes = new ArrayList<>();
        File torrentsDir = new File(TORRENTS_DIRECTORY);

        for (File file : Objects.requireNonNull(torrentsDir.listFiles())) {
            TorrentFile tFile = new TorrentFile(file);
            infoHashes.add(tFile.getInfoHash());
        }

        return infoHashes;
    }
    public static File getDownloadFileByTorrent(TorrentFile torrentFile) {
        return new File(DOWNLOADS_DIRECTORY, torrentFile.getName());
    }
    public static TorrentFile getTorrentFileByInfoHash(byte[] infoHash) {
        File torrentsDir = new File(TORRENTS_DIRECTORY);

        for (File torrentFile : Objects.requireNonNull(torrentsDir.listFiles())) {
            TorrentFile tFile = new TorrentFile(torrentFile);
            if (Arrays.equals(tFile.getInfoHash(), infoHash)) {
                return tFile;
            }
        }

        return null;
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void startDownload() {
        answerListener = new AnswerListener(torrentFile);
        Thread downloading = new Thread(answerListener);
        downloading.start();
    }
}