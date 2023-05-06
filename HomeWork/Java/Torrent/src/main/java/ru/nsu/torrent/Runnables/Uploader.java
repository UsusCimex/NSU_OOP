package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.PieceMessage;
import ru.nsu.torrent.Messages.RequestMessage;
import ru.nsu.torrent.TorrentClient;
import ru.nsu.torrent.TorrentFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Uploader implements Runnable {
    SocketChannel socketChannel;
    RequestMessage requestMessage;
    byte[] infoHash;

    public Uploader(SocketChannel socketChannel, RequestMessage requestMessage, byte[] infoHash) {
        this.socketChannel = socketChannel;
        this.requestMessage = requestMessage;
        this.infoHash = infoHash;
    }

    @Override
    public void run() {
        try {
            System.err.println("[Uploader] Start upload!");
            int index = requestMessage.getIndex();
            int offset = requestMessage.getOffset();
            int length = requestMessage.getPieceLength();

            byte[] data = readPieceData(index, offset, length);
            if (data == null) return;
            PieceMessage pieceMessage = new PieceMessage(index, offset, data);
            ByteBuffer byteBuffer = ByteBuffer.wrap(pieceMessage.toBytes());
            while (byteBuffer.hasRemaining()) {
                socketChannel.write(byteBuffer);
            }
            System.err.println("[Uploader] Uploaded: " + index + " piece, to " + socketChannel.getRemoteAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readPieceData(int index, int offset, int length) throws IOException {
        TorrentFile tFile = TorrentClient.getTorrentFileByInfoHash(infoHash);
        assert tFile != null;
        if (!tFile.getPieceManager().getPiece(index)) {
            return null;
        }
        File fileByInfoHash = TorrentClient.getDownloadFileByTorrent(tFile);
        try (RandomAccessFile file = new RandomAccessFile(fileByInfoHash, "r")) {
            file.seek((long) index * length + offset);

            byte[] data = new byte[length];
            file.read(data);

            return data;
        }
    }
}