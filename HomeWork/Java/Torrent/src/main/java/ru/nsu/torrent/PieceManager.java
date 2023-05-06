package ru.nsu.torrent;

import java.util.BitSet;

public class PieceManager {
    private final BitSet availablePieces;
    public PieceManager(int numberPieces) {
        this.availablePieces = new BitSet(numberPieces);
    }
    public synchronized int getNextPiece() {
        return availablePieces.nextClearBit(0);
    }
    public synchronized void markPieceAsDownloaded(int pieceIndex) {
        availablePieces.set(pieceIndex);
    }
    public int getNumberOfDownloadedPieces() { return availablePieces.cardinality(); }
}
