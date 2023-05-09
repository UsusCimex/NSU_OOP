package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.Message;
import ru.nsu.torrent.Messages.Piece;
import ru.nsu.torrent.Messages.Request;
import ru.nsu.torrent.Torrent;
import ru.nsu.torrent.TorrentFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Handler implements Runnable {
    SocketChannel socketChannel;
    Message message;
    byte[] infoHash;

    public Handler(SocketChannel socketChannel, Message message, byte[] infoHash) {
        this.socketChannel = socketChannel;
        this.message = message;
        this.infoHash = infoHash;
    }

    @Override
    public void run() {
        byte type = message.getType();
        switch (type) {
            case (Request.REQUEST) -> {
                Request request = (Request) message;
                int index = request.getIndex();
                int offset = request.getOffset();
                int length = request.getPieceLength();

                byte[] data = readPieceData(index, offset, length);
                if (data == null) return;
                Piece piece = new Piece(index, offset, data);
                ByteBuffer byteBuffer = ByteBuffer.wrap(piece.toBytes());
                try {
                    while (byteBuffer.hasRemaining()) {
                        int numWrite = socketChannel.write(byteBuffer);
                        if (numWrite == -1) {
                            socketChannel.close();
                            throw new RuntimeException("Error socket write");
                        }
                    }
                    System.err.println("[Handler] Uploaded: piece(" + index + "), to " + socketChannel.getRemoteAddress());
                } catch (IOException e) {
                    System.err.println("[Handler] Piece(" + index + ") not uploaded...");
                }
            }
            case (Piece.PIECE) -> {
                Piece piece = (Piece) message;
                int index = piece.getIndex();
                int offset = piece.getOffset();
                byte[] data = piece.getData();

                TorrentFile torrentFile = Torrent.getTorrentFileByInfoHash(infoHash);
                if (torrentFile == null) {
                    throw new IllegalStateException("[Handler] File not found... Hash exception!");
                }
                if (torrentFile.getPieceManager().getPiece(index)) {
                    return;
                }

                MessageDigest md;
                try {
                    md = MessageDigest.getInstance("SHA-1");
                } catch (NoSuchAlgorithmException e) {
                    System.err.println("[Handler] Algorithm SHA-1 not found!");
                    throw new RuntimeException(e);
                }
                byte[] calculatedHash = md.digest(data);
                byte[] expectedHash = torrentFile.getPieceHashes().get(index);

                if (Arrays.equals(calculatedHash, expectedHash)) {
                    try (RandomAccessFile raf = new RandomAccessFile(Torrent.getDownloadFileByTorrent(torrentFile), "rw")) {
                        raf.seek((long) index * torrentFile.getPieceLength() + offset);
                        raf.write(data);
                        Torrent.getFile().markPieceAsDownloaded(index);
                        System.err.println("[Handler] Downloaded: piece(" + index + "), from " + socketChannel.getRemoteAddress());
                    } catch (IOException e) {
                        throw new RuntimeException("[Handler] File not found!", e);
                    }
                } else {
                    System.err.println("[Handler] Hashes do not match for piece " + index);
                }
            }
        }
    }

    private byte[] readPieceData(int index, int offset, int length) {
        TorrentFile tFile = Torrent.getTorrentFileByInfoHash(infoHash);
        assert tFile != null;
        if (!tFile.getPieceManager().getPiece(index)) {
            return null;
        }
        File fileByInfoHash = Torrent.getDownloadFileByTorrent(tFile);
        try (RandomAccessFile file = new RandomAccessFile(fileByInfoHash, "r")) {
            file.seek((long) index * tFile.getPieceLength() + offset);

            byte[] data = new byte[length];
            file.read(data);

            return data;
        } catch (IOException e) {
            System.err.println("[Handler] Mistake in file reader!");
        }
        return null;
    }
}
