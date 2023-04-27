package ru.nsu.torrent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TorrentFile {
    private String name;
    private String filePath;
    private long pieceLength;
    private List<byte[]> pieceHashes;
    private long totalLength;

    public TorrentFile(File file) {
        // Parsing .torrent file...

        filePath = null;
        name = file.getName();
        pieceLength = 20;
        pieceHashes = new ArrayList<>();
        totalLength = 50;
    }

    public String getName() {
        return name;
    }

    public List<byte[]> getPieceHashes() {
        return pieceHashes;
    }
}
