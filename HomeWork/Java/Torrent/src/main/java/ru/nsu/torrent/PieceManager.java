package ru.nsu.torrent;

import java.util.BitSet;
import java.util.List;

public class PieceManager {
    private TorrentFile torrentFile;
    private List<Peer> peers;
    private BitSet availablePieces;

    public PieceManager(TorrentFile torrentFile, List<Peer> peers) {
        this.torrentFile = torrentFile;
        this.peers = peers;
        this.availablePieces = new BitSet(torrentFile.getPieceHashes().size());
    }

    public int getNextPiece() {
        // Реализация выбора следующего кусочка для скачивания...
        return 0;
    }
}
