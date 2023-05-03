package ru.nsu.torrent;

import ru.nsu.torrent.Messages.RequestMessage;
import ru.nsu.torrent.Runnables.TorrentListener;
import ru.nsu.torrent.Runnables.Uploader;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TorrentClient {
    public static ExecutorService executor = Executors.newFixedThreadPool(3);
    private TorrentFile torrentFile = null;
    private Tracker tracker;
    private PieceManager pieceManager;
    private List<Peer> availablePeers = new ArrayList<>();

    private static final String TORRENTS_DIRECTORY = "torrentsDir";
    private static final String DOWNLOADS_DIRECTORY = "downloadsDir";

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

                if (!socketChannel.isConnected()) {
                    continue;
                }
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
        while(!complete && !availablePeers.isEmpty()) {
            for (Peer peer : availablePeers) {
                int missingPieceIndex = pieceManager.getNextPiece();
                if (missingPieceIndex >= 0 && missingPieceIndex < torrentFile.getPieceHashes().size()) {
                    RequestMessage requestMessage = new RequestMessage(missingPieceIndex, 0, (int) torrentFile.getPieceSize());
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
    }
    public TorrentFile getFile() {
        return torrentFile;
    }
    public Tracker getTracker() {
        return tracker;
    }

    public static Set<byte[]> getAvailableInfoHashes() {
        Set<byte[]> infoHashes = new HashSet<>();
        File torrentsDir = new File(TORRENTS_DIRECTORY);

        for (File torrentFile : torrentsDir.listFiles()) {
            TorrentFile tFile = new TorrentFile(torrentFile);
            infoHashes.add(tFile.getInfoHash());
        }

        return infoHashes;
    }
    public static File getFileByInfoHash(byte[] infoHash) {
        File torrentsDir = new File(TORRENTS_DIRECTORY);

        for (File torrentFile : torrentsDir.listFiles()) {
            TorrentFile tFile = new TorrentFile(torrentFile);
            if (Arrays.equals(tFile.getInfoHash(), infoHash)) {
                return new File(DOWNLOADS_DIRECTORY, tFile.getName());
            }
        }

        return null;
    }
}