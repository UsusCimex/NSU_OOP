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

public class UploaderRequest implements Runnable {
    SocketChannel socketChannel;
    RequestMessage requestMessage;
    byte[] infoHash;

    public UploaderRequest(SocketChannel socketChannel, RequestMessage requestMessage, byte[] infoHash) {
        this.socketChannel = socketChannel;
        this.requestMessage = requestMessage;
        this.infoHash = infoHash;
    }

    @Override
    public void run() {
        try {
            System.err.println("[UploaderRequest] Start upload request!");
            ByteBuffer byteBuffer = ByteBuffer.wrap(requestMessage.toBytes());
            while (byteBuffer.hasRemaining()) {
                socketChannel.write(byteBuffer);
            }
            System.err.println("[UploaderRequest] Requested: " + requestMessage.getIndex() + ", to " + socketChannel.getRemoteAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}