package ru.nsu.torrent;

import java.util.List;

public class Uploader {
    private TorrentFile torrentFile;
    private List<Peer> peers;

    public Uploader(TorrentFile torrentFile, List<Peer> peers) {
        this.torrentFile = torrentFile;
        this.peers = peers;
    }

    public void upload() {
        // Реализация раздачи кусочков другим пирам...
    }
}
