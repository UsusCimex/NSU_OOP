package ru.nsu.torrent;

import java.util.BitSet;
import java.util.List;

public class Downloader {
    private TorrentFile torrentFile;
    private List<Peer> peers;
    private BitSet downloadedPieces;

    public Downloader(TorrentFile torrentFile, List<Peer> peers) {
        this.torrentFile = torrentFile;
        this.peers = peers;
        this.downloadedPieces = new BitSet(torrentFile.getPieceHashes().size());
    }

    public void download() {
        // Реализация скачивания кусочков от пиров...
    }
}
