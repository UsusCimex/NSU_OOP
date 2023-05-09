package ru.nsu.torrent;

import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class PieceManager {
    private final BitSet availablePieces;
    private final int numberPieces;
    public PieceManager(int numberPieces) {
        this.numberPieces = numberPieces;
        this.availablePieces = new BitSet(numberPieces);
    }
    public int getIndexOfSearchedPiece(BitSet accessBit) {
        BitSet neededPieces = new BitSet(numberPieces);
        neededPieces.or(accessBit);
        neededPieces.and(availablePieces);
        if (neededPieces.cardinality() != 0) {
            Random random = new Random();

            List<Integer> missingPieces = IntStream.range(0, numberPieces)
                    .filter(index -> !neededPieces.get(index))
                    .boxed()
                    .toList();

            if (!missingPieces.isEmpty()) {
                return random.nextInt(missingPieces.size());
            }
        }
        return -1;
    }
    public boolean getPiece(int index) { return availablePieces.get(index); }
    public synchronized void markPieceAsAvailable(int pieceIndex) {
        availablePieces.set(pieceIndex);
    }
    public int getNumberOfAvailablePieces() { return availablePieces.cardinality(); }
    public int getNumberPieces() { return numberPieces; }
    public BitSet getAvailablePieces() {
        return availablePieces;
    }
}
