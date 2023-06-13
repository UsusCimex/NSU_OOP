package ru.nsu.torrent;

import ru.nsu.torrent.Runnables.TorrentClient;
import ru.nsu.torrent.Runnables.TorrentServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Torrent {
    private final TorrentServer torrentServer;
    private final TorrentClient torrentClient;
    private final Thread serverThread;
    private final Thread clientThread;
    private final TorrentManager torrentManager;
    private final SocketAddress myAddress;
    private byte[] selectedFileHash;
    public Torrent(String host, int port) {
        try {
            this.myAddress = new InetSocketAddress(host, port);
            this.torrentManager = new TorrentManager(myAddress);

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
        if (!serverThread.isInterrupted()) {
            serverThread.interrupt();
        }
        if (!clientThread.isInterrupted()) {
            clientThread.interrupt();
        }
        torrentManager.stop();
    }
    public void selectFile(File file) {
        torrentManager.stopSession(torrentManager.getClientSession());
        if (file == null) {
            this.selectedFileHash = null;
            torrentClient.changeFile(null);
        } else {
            this.selectedFileHash = TorrentFile.calculateInfoHash(file);
            TorrentFile torrentFile = torrentManager.getTorrentFile(selectedFileHash);
            torrentClient.changeFile(torrentFile);
        }
    }
    public void uploadTorrents() {
        torrentManager.updateTorrents();
    }

    public TorrentFile getTorrentFile() {
        return torrentManager.getTorrentFile(selectedFileHash);
    }

    public SocketAddress getMyIP() {
        return myAddress;
    }
}