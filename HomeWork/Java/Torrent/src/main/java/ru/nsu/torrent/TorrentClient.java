package ru.nsu.torrent;

import com.dampcake.bencode.*;
import ru.nsu.torrent.Messages.PieceMessage;
import ru.nsu.torrent.Messages.RequestMessage;
import ru.nsu.torrent.Runnables.Downloader;
import ru.nsu.torrent.Runnables.TorrentListener;
import ru.nsu.torrent.Runnables.Uploader;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TorrentClient {
    public static ExecutorService executor = Executors.newFixedThreadPool(3);
    private TorrentFile torrentFile = null;
    private Tracker tracker;
    private PieceManager pieceManager;
    private List<Peer> availablePeers;

    public TorrentClient(String host, int port) {
        TorrentListener torrentListener = new TorrentListener(host, port);
        Thread listenerThread = new Thread(torrentListener);
        listenerThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                torrentListener.stop();
                listenerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }
    public void selectFile(TorrentFile file) {
        this.torrentFile = file;
        this.tracker = new Tracker(torrentFile);
        this.pieceManager = new PieceManager(torrentFile);
        start();
    }
    public void start(){
        if (torrentFile == null) {
            System.err.println("No file selected!");
            return;
        }

        for (Peer peer : tracker.getPeers()) {
            try {
                SocketChannel socketChannel = peer.getSocketChannel();
                socketChannel.configureBlocking(false);
                socketChannel.connect(peer.getAddress());
                boolean success = Handshake.sendHandshake(socketChannel, torrentFile.getInfoHash(), new byte[20]);
                if (success) {
                    availablePeers.add(peer);
                } else {
                    socketChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean complete = false;
        while(!complete) {
            for (Peer peer : availablePeers) {
                int missingPieceIndex = pieceManager.getNextPiece();
                if (missingPieceIndex >= 0 && missingPieceIndex < torrentFile.getPieceHashes().size()) {
                    RequestMessage requestMessage = new RequestMessage(missingPieceIndex, 0, torrentFile.getPieceSize());
                    Uploader uploader = new Uploader(peer.getSocketChannel(), requestMessage, peer.getInfoHash());
                    executor.submit(uploader);
                } else {
                    complete = true;
                    break;
                }
            }
        }

        for (Peer peer : availablePeers) {
            try {
                peer.getSocketChannel().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        availablePeers.clear();
        System.err.println("File downloaded!");
    }
    public TorrentFile getFile() {
        return torrentFile;
    }
    public Tracker getTracker() {
        return tracker;
    }

    public static Set<byte[]> getAvailableInfoHashes() {
        // Расспарсить все имеющиеся хэши торрентов
        // Хэш получить при помощи SHA-1 к "info" полю .torrent
        return null;
    }
    public static File getFileByInfoHash(byte[] infoHash) {
        // Реализация метода для поиска файла на основе infoHash
        return null;
    }
}