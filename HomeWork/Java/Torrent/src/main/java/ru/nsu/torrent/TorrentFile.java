package ru.nsu.torrent;

import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.BencodeInputStream;
import com.dampcake.bencode.Type;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class TorrentFile {
    private final long length;
    private final String name;
    private final long pieceLength;
    private final List<byte[]> pieceHashes;
    private final byte[] infoHash;
    private PieceManager pieceManager;
    private final Tracker tracker;

    public TorrentFile(File file) {
        try (
                FileInputStream fis = new FileInputStream(file);
                BencodeInputStream bin = new BencodeInputStream(fis, Charset.forName("ISO-8859-15"))
        ) {
            Type<?> type = bin.nextType();
            if (type != Type.DICTIONARY) {
                throw new RuntimeException("Type != DICTIONARY");
            }
            Map<String, Object> dict = bin.readDictionary();
            if (dict.get("info") instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> infoDict = (Map<String, Object>) dict.get("info");
                this.length = (Long) infoDict.get("length");
                this.name = (String) infoDict.get("name");
                this.pieceLength = (Long) infoDict.get("piece length");
                this.pieceHashes = extractPieceHashes(((String) infoDict.get("pieces")).getBytes("ISO-8859-15"));
                this.infoHash = calculateInfoHash(infoDict);
                this.pieceManager = generateExistingPieces();
            } else {
                throw new RuntimeException("Bencode failed");
            }
            this.tracker = new Tracker(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Tracker getTracker() {
        return tracker;
    }
    public TorrentFile updated() {
        this.pieceManager = generateExistingPieces();
        return this;
    }

    private List<byte[]> extractPieceHashes(byte[] pieces) {
        final int pieceHashLength = 20;
        int numberOfPieces = pieces.length / pieceHashLength;
        List<byte[]> pieceHashes = new ArrayList<>(numberOfPieces);

        for (int i = 0; i < numberOfPieces; i++) {
            byte[] pieceHash = new byte[pieceHashLength];
            System.arraycopy(pieces, i * pieceHashLength, pieceHash, 0, pieceHashLength);
            pieceHashes.add(pieceHash);
        }
        return pieceHashes;
    }

    private byte[] calculateInfoHash(Map<String, Object> infoDict) {
        Bencode bencode = new Bencode(Charset.forName("ISO-8859-15"));
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            return sha1.digest(bencode.encode(infoDict));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm SHA-1 not found...");
        }
    }

    private PieceManager generateExistingPieces() {
        PieceManager generatedPieceManager = new PieceManager(pieceHashes.size());
        File targetFile = new File(TorrentManager.DOWNLOADS_DIRECTORY + "/" + name);
        if (targetFile.exists() && targetFile.isFile()) {
            try (FileInputStream raf = new FileInputStream(targetFile)) {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                byte[] buffer = new byte[(int) pieceLength];

                for (int i = 0; i < pieceHashes.size(); i++) {
                    int bytesRead = raf.read(buffer);
                    if (bytesRead > 0) {
                        byte[] pieceData = Arrays.copyOf(buffer, bytesRead);
                        byte[] pieceHash = md.digest(pieceData);

                        if (Arrays.equals(pieceHash, pieceHashes.get(i))) {
                            generatedPieceManager.markPieceAsAvailable(i);
                        }
                    }
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
                System.err.println("[TorrentFile] Failed to update existing pieces");
            }
        }
        return generatedPieceManager;
    }

    public int getDownloadedPieces() {
        return pieceManager.getNumberOfAvailablePieces();
    }
    public synchronized void markPieceAsDownloaded(int index) {
        pieceManager.markPieceAsAvailable(index);
    }
    public PieceManager getPieceManager() {
        return pieceManager;
    }
    public String getName() {
        return name;
    }

    public List<byte[]> getPieceHashes() {
        return pieceHashes;
    }

    public long getLength() {
        return length;
    }

    public long getPieceLength() {
        return pieceLength;
    }

    public byte[] getInfoHash() {
        return infoHash;
    }
}
