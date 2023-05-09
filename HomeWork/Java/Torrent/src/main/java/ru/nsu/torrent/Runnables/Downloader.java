package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.Piece;
import ru.nsu.torrent.TorrentClient;
import ru.nsu.torrent.TorrentFile;

import java.io.RandomAccessFile;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Downloader implements Runnable {
    SocketChannel socketChannel;
    Piece piece;
    byte[] infoHash;

    public Downloader(SocketChannel socketChannel, Piece piece, byte[] infoHash) {
        this.socketChannel = socketChannel;
        this.piece = piece;
        this.infoHash = infoHash;
    }

    @Override
    public void run() {
        try {
            int index = piece.getIndex();
            int offset = piece.getOffset();
            byte[] data = piece.getData();

            TorrentFile torrentFile = TorrentClient.getTorrentFileByInfoHash(infoHash);
            if (torrentFile == null) {
                throw new IllegalStateException("[Downloader] File not found... Hash exception!");
            }
            if (torrentFile.getPieceManager().getPiece(index)) {
                return;
            }

            MessageDigest md;
            try {
                md = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                System.err.println("[Downloader] Algorithm SHA-1 not found!");
                throw new RuntimeException(e);
            }
            byte[] calculatedHash = md.digest(data);
            byte[] expectedHash = torrentFile.getPieceHashes().get(index);

            if (Arrays.equals(calculatedHash, expectedHash)) {
                try (RandomAccessFile raf = new RandomAccessFile(TorrentClient.getDownloadFileByTorrent(torrentFile), "rw")) {
                    raf.seek((long) index * torrentFile.getPieceLength() + offset);
                    raf.write(data);
                    TorrentClient.getFile().markPieceAsDownloaded(index);
                    System.err.println("[Downloader] Downloaded: piece(" + index + "), from " + socketChannel.getRemoteAddress());
                }
            } else {
                System.err.println("[Downloader] Hashes do not match for piece " + index);
            }
        } catch (Exception e) {
            System.err.println("[Downloader] File download error...");
        }
    }
}
