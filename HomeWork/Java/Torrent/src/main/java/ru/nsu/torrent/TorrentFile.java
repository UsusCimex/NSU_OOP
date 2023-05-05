package ru.nsu.torrent;

import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.BencodeInputStream;
import com.dampcake.bencode.Type;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class TorrentFile {
    private final long length;
    private final String name;
    private final long pieceLength;
    private final List<byte[]> pieceHashes;
    private final byte[] infoHash;

    public TorrentFile(File file) {
        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-15"));
                ByteArrayInputStream in = new ByteArrayInputStream(bufferedReader.readLine().getBytes());
                BencodeInputStream bin = new BencodeInputStream(in);
        ) {
            Type type = bin.nextType();
            if (type != Type.DICTIONARY) {
                throw new RuntimeException("Type != DICTIONARY");
            }
            Map<String, Object> dict = bin.readDictionary();
            Map<String, Object> infoDict = (Map<String, Object>) dict.get("info");
            this.length = (Long) infoDict.get("length");
            this.name = (String) infoDict.get("name");
            this.pieceLength = (Long) infoDict.get("piece length");
            this.pieceHashes = extractPieceHashes(((String) infoDict.get("pieces")).getBytes("ISO-8859-15"));
            this.infoHash = calculateInfoHash(infoDict);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        Bencode bencode = new Bencode();
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
