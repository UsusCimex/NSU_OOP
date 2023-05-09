package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.Piece;
import ru.nsu.torrent.Messages.Request;
import ru.nsu.torrent.TorrentClient;
import ru.nsu.torrent.TorrentFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Uploader implements Runnable {
    SocketChannel socketChannel;
    Request request;
    byte[] infoHash;

    public Uploader(SocketChannel socketChannel, Request request, byte[] infoHash) {
        this.socketChannel = socketChannel;
        this.request = request;
        this.infoHash = infoHash;
    }

    @Override
    public void run() {
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
            System.err.println("[Uploader] Uploaded: piece(" + index + "), to " + socketChannel.getRemoteAddress());
        } catch (IOException e) {
            System.err.println("[Uploader] Piece(" + index + ") not uploaded...");
        }
    }

    private byte[] readPieceData(int index, int offset, int length) {
        TorrentFile tFile = TorrentClient.getTorrentFileByInfoHash(infoHash);
        assert tFile != null;
        if (!tFile.getPieceManager().getPiece(index)) {
            return null;
        }
        File fileByInfoHash = TorrentClient.getDownloadFileByTorrent(tFile);
        try (RandomAccessFile file = new RandomAccessFile(fileByInfoHash, "r")) {
            file.seek((long) index * tFile.getPieceLength() + offset);

            byte[] data = new byte[length];
            file.read(data);

            return data;
        } catch (IOException e) {
            System.err.println("[Uploader] Mistake in file reader!");
        }
        return null;
    }
}