package ru.nsu.torrent;

import ru.nsu.torrent.Runnables.TorrentClient;
import ru.nsu.torrent.Runnables.TorrentServer;

import java.io.File;
import java.io.IOException;

public class Torrent {
    private final TorrentServer torrentServer;
    private final TorrentClient torrentClient;
    private final Thread serverThread;
    private final Thread clientThread;
    private final TorrentManager torrentManager = new TorrentManager();
    private File selectedFile;
    public Torrent(String host, int port) {
        try {
            this.torrentServer = new TorrentServer(host, port, torrentManager);
            this.serverThread = new Thread(torrentServer);
            this.serverThread.start();

            this.torrentClient = new TorrentClient(torrentManager);
            this.clientThread = new Thread(torrentClient);
            this.clientThread.start();
        } catch (IOException e) {
            System.err.println("[Torrent] Server/Client destroyed!");
            throw new RuntimeException(e);
        }
    }
    public void stopTorrent() {
        if (torrentServer != null) {
            serverThread.interrupt();
            torrentServer.stop();
        }
        if (torrentClient != null) {
            clientThread.interrupt();
            torrentClient.stop();
        }
        torrentManager.stop();
    }
    public void selectFile(File file) {
        this.selectedFile = file;
        torrentManager.stopSession(torrentManager.getClientSession());
        torrentManager.updateTorrents();
        TorrentFile torrentFile = torrentManager.getTorrentFile(selectedFile);
        torrentClient.changeFile(torrentFile);
    }

    public TorrentFile getTorrentFile() {
        return torrentManager.getTorrentFile(selectedFile);
    }
}