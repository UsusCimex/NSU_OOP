package ru.nsu.torrent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class PieceManager {
    private final BitSet availablePieces;
    private final int numberPieces;
    private final TorrentFile torrentFile;
    public PieceManager(TorrentFile torrentFile) {
        this.torrentFile = torrentFile;
        this.numberPieces = torrentFile.getPieceHashes().size();
        this.availablePieces = new BitSet(numberPieces);
        this.generateExistingPieces();
    }
    public int getIndexOfSearchedPiece(BitSet accessBit) {
        BitSet neededPieces = new BitSet(numberPieces);
        neededPieces.or(availablePieces);
        neededPieces.flip(0, numberPieces);
        neededPieces.and(accessBit);
        if (neededPieces.cardinality() != 0) {
            Random random = new Random();

            List<Integer> missingPieces = IntStream.range(0, numberPieces)
                    .filter(neededPieces::get)
                    .boxed()
                    .toList();

            if (!missingPieces.isEmpty()) {
                return missingPieces.get(random.nextInt(missingPieces.size()));
            }
        }
        return -1;
    }
    private void generateExistingPieces() {
        File targetFile = new File(TorrentManager.DOWNLOADS_DIRECTORY + "/" + torrentFile.getName());
        if (targetFile.exists() && targetFile.isFile()) {
            try (FileInputStream raf = new FileInputStream(targetFile)) {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                byte[] buffer = new byte[(int) torrentFile.getPieceLength()];

                for (int i = 0; i < numberPieces; i++) {
                    int bytesRead = raf.read(buffer);
                    if (bytesRead > 0) {
                        byte[] pieceData = Arrays.copyOf(buffer, bytesRead);
                        byte[] pieceHash = md.digest(pieceData);

                        if (Arrays.equals(pieceHash, torrentFile.getPieceHashes().get(i))) {
                            this.markPieceAsAvailable(i);
                        }
                    }
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
                System.err.println("[PieceManager] Failed to update existing pieces");
            }
        }
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
