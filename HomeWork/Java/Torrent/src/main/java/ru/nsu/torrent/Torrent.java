package ru.nsu.torrent;

import ru.nsu.torrent.Runnables.TorrentClient;
import ru.nsu.torrent.Runnables.TorrentServer;

import java.io.File;
import java.io.IOException;

public class Torrent {
    private final TorrentServer torrentServer;
    private final TorrentClient torrentClient;
    private final TorrentManager torrentManager = new TorrentManager();
    public Torrent(String host, int port) {
        try {
            torrentServer = new TorrentServer(host, port, torrentManager);
            Thread listenerThread = new Thread(torrentServer);
            listenerThread.start();

            torrentClient = new TorrentClient(torrentManager);
            Thread downloader = new Thread(torrentClient);
            downloader.start();
        } catch (IOException e) {
            System.err.println("[Torrent] Server/Client destroyed!");
            throw new RuntimeException(e);
        }
    }
    public void stopTorrent() {
        if (torrentServer != null) {
            torrentServer.stop();
        }
        if (torrentClient != null) {
            torrentClient.stop();
        }
        torrentManager.stop();
    }
    public void selectFile(File file) {
        TorrentFile torrentFile = new TorrentFile(file);
        torrentClient.changeFile(torrentFile);
    }

    public TorrentFile getTorrentFile() {
        return torrentClient.getTorrentFile();
    }
}