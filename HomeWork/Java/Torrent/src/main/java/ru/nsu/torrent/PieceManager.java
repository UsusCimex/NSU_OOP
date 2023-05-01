package ru.nsu.torrent;

import java.util.BitSet;

public class PieceManager {
    private final TorrentFile torrentFile;
    private final BitSet availablePieces;

    public PieceManager(TorrentFile torrentFile) {
        this.torrentFile = torrentFile;
        this.availablePieces = new BitSet(torrentFile.getPieceHashes().size());
        this.availablePieces.set(0, torrentFile.getPieceHashes().size());
    }
    public synchronized int getNextPiece() {
        int nextClearBit = availablePieces.nextClearBit(0);
        if (nextClearBit < torrentFile.getPieceHashes().size()) {
            availablePieces.clear(nextClearBit);
            return nextClearBit;
        } else {
            return -1;
        }
    }
    public synchronized void markPieceAsAvailable(int pieceIndex) {
        availablePieces.set(pieceIndex);
    }
    public int getNumberOfPieces() {
        return torrentFile.getPieceHashes().size();
    }
}
