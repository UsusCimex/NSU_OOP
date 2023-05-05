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
            int pieceIndex = requestMessage.getIndex();
            int offset = requestMessage.getOffset();
            int length = requestMessage.getPieceLength();

            byte[] data = readPieceData(pieceIndex, offset, length);
            PieceMessage pieceMessage = new PieceMessage(pieceIndex, offset, data);

            ByteBuffer byteBuffer = ByteBuffer.wrap(pieceMessage.toBytes());
            while (byteBuffer.hasRemaining()) {
                socketChannel.write(byteBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readPieceData(int pieceIndex, int offset, int length) throws IOException {
        TorrentFile tFile = TorrentClient.getTorrentFileByInfoHash(infoHash);
        assert tFile != null;
        File fileByInfoHash = TorrentClient.getDownloadFileByTorrent(tFile);
        try (RandomAccessFile file = new RandomAccessFile(fileByInfoHash, "r")) {
            file.seek((long) pieceIndex * length + offset);

            byte[] data = new byte[length];
            file.read(data);

            return data;
        }
    }
}