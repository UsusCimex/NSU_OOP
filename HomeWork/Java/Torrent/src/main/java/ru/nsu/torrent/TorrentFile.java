package ru.nsu.torrent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TorrentFile {
    private String name;
    private String filePath;
    private int pieceSize;
    private List<byte[]> pieceHashes;
    private long totalSize;

    public TorrentFile(File file) {
        // Parsing .torrent file...

        filePath = null;
        name = file.getName();
        pieceSize = 20;
        pieceHashes = new ArrayList<>();
        totalSize = 50;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public List<byte[]> getPieceHashes() {
        return pieceHashes;
    }

    public long getTotalLength() {
        return totalSize;
    }

    public int getPieceSize() {
        return pieceSize;
    }

    public byte[] getInfoHash() {
        // get info hash .torrent file
        return null;
    }
}