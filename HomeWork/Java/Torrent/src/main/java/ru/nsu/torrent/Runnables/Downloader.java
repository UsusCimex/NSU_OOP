package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.PieceMessage;
import ru.nsu.torrent.TorrentClient;
import ru.nsu.torrent.TorrentFile;

import java.io.RandomAccessFile;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Downloader implements Runnable {
    SocketChannel socketChannel;
    PieceMessage pieceMessage;
    byte[] infoHash;

    public Downloader(SocketChannel socketChannel, PieceMessage pieceMessage, byte[] infoHash) {
        this.socketChannel = socketChannel;
        this.pieceMessage = pieceMessage;
        this.infoHash = infoHash;
    }

    @Override
    public void run() {
        try {
            int index = pieceMessage.getIndex();
            int offset = pieceMessage.getOffset();
            byte[] data = pieceMessage.getData();

            TorrentFile torrentFile = TorrentClient.getTorrentFileByInfoHash(infoHash);
            if (torrentFile == null) {
                throw new IllegalStateException("Не удалось найти файл для данного infoHash");
            }

            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            assert md != null;
            byte[] calculatedHash = md.digest(data);

            byte[] expectedHash = torrentFile.getPieceHashes().get(index);

            if (Arrays.equals(calculatedHash, expectedHash)) {
                try (RandomAccessFile raf = new RandomAccessFile(TorrentClient.getDownloadFileByTorrent(torrentFile), "rw")) {
                    raf.seek((long) index * pieceMessage.getData().length + offset);
                    raf.write(data);
                    TorrentClient.markPieceAsDownloaded(index);
                }
            } else {
                System.err.println("Hashes do not match for piece " + index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
