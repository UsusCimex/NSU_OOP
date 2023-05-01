package ru.nsu.torrent;

import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.Type;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TorrentFile {
    private final long totalSize;
    private final String name;
    private final long pieceSize;
    private final List<byte[]> pieceHashes;
    private final byte[] infoHash;

    public TorrentFile(File file) {
        Bencode bencode = new Bencode();
        try (FileInputStream fis = new FileInputStream(file)) {
            Map<String, Object> torrentData = bencode.decode(fis.readAllBytes(), Type.DICTIONARY);
            Map<String, Object> infoDict = (Map<String, Object>) torrentData.get("info");
            this.name = (String) infoDict.get("name");
            this.pieceSize = (Long) infoDict.get("piece length");
            this.totalSize = (Long) infoDict.get("length");
            this.pieceHashes = extractPieceHashes((String) infoDict.get("pieces"));
            this.infoHash = calculateInfoHash(bencode, infoDict);
        } catch (IOException e) {
            throw new RuntimeException("Error decoding torrent file", e);
        }
    }

    private List<byte[]> extractPieceHashes(String pieces) {
        int pieceHashLength = 20;
        int numberOfPieces = pieces.length() / pieceHashLength;
        List<byte[]> pieceHashList = new ArrayList<>(numberOfPieces);

        for (int i = 0; i < numberOfPieces; i++) {
            byte[] pieceHash = new byte[pieceHashLength];
            System.arraycopy(pieces.getBytes(), i * pieceHashLength, pieceHash, 0, pieceHashLength);
            pieceHashList.add(pieceHash);
        }

        return pieceHashList;
    }

    private byte[] calculateInfoHash(Bencode bencode, Map<String, Object> infoDict) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            return sha1.digest(bencode.encode(infoDict));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm SHA-1 not found...");
        }
    }

    public String getName() {
        return name;
    }

    public List<byte[]> getPieceHashes() {
        return pieceHashes;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public long getPieceSize() {
        return pieceSize;
    }

    public byte[] getInfoHash() {
        return infoHash;
    }
}
