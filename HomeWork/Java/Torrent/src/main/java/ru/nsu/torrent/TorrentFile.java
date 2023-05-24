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
    private PieceManager pieceManager = null;
    private Tracker tracker = null;

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
                this.infoHash = calculateInfoHash(file);
            } else {
                throw new RuntimeException("Bencode failed");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Tracker getTracker() {
        if (tracker == null) {
            this.tracker = new Tracker(this);
        }
        return tracker;
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

    public static byte[] calculateInfoHash(File file) {
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
                Bencode bencode = new Bencode( Charset.forName("ISO-8859-15"));

                MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
                return sha1.digest(bencode.encode(infoDict));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm SHA-1 not found...");
        }
        return null;
    }

    public int getDownloadedPieces() {
        if (pieceManager == null) {
            this.pieceManager = new PieceManager(this);
        }
        return pieceManager.getNumberOfAvailablePieces();
    }
    public synchronized void markPieceAsDownloaded(int index) {
        if (pieceManager == null) {
            this.pieceManager = new PieceManager(this);
        }
        pieceManager.markPieceAsAvailable(index);
    }
    public PieceManager getPieceManager() {
        if (pieceManager == null) {
            this.pieceManager = new PieceManager(this);
        }
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
