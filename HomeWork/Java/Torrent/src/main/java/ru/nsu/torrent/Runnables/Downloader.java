package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.PieceMessage;
import ru.nsu.torrent.TorrentClient;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.SocketChannel;

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

            // Найти файл на основе infoHash
            File file = TorrentClient.getFileByInfoHash(infoHash);
            if (file == null) {
                throw new IllegalStateException("Не удалось найти файл для данного infoHash");
            }

            // Записать кусок данных в файл в соответствующем смещении
            try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                raf.seek((long) index * pieceMessage.getData().length + offset);
                raf.write(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
