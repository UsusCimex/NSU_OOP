package ru.nsu.torrent;

import ru.nsu.torrent.Messages.RequestMessage;
import ru.nsu.torrent.Runnables.TorrentListener;
import ru.nsu.torrent.Runnables.Uploader;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TorrentClient {
    public static ExecutorService executor = Executors.newFixedThreadPool(3);
    private TorrentFile torrentFile = null;
    private Tracker tracker;
    private static PieceManager pieceManager;
    private final List<Peer> availablePeers = new ArrayList<>();

    public static final String TORRENTS_DIRECTORY = "torrentsDir";
    public static final String DOWNLOADS_DIRECTORY = "downloadsDir";

    private final TorrentListener torrentListener;

    public TorrentClient(String host, int port) {
        torrentListener = new TorrentListener(host, port);
        Thread listenerThread = new Thread(torrentListener);
        listenerThread.start();
    }
    public void stopTorrentListener() {
        if (torrentListener != null) {
            torrentListener.stop();
        }
    }
    public void selectFile(TorrentFile file) {
        this.torrentFile = file;
        this.tracker = new Tracker(torrentFile);
        pieceManager = new PieceManager(torrentFile);
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
                    RequestMessage requestMessage = new RequestMessage(missingPieceIndex, 0, (int) torrentFile.getPieceLength());
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

        for (File torrentFile : Objects.requireNonNull(torrentsDir.listFiles())) {
            TorrentFile tFile = new TorrentFile(torrentFile);
            infoHashes.add(tFile.getInfoHash());
        }

        return infoHashes;
    }
    public static File getDownloadFileByTorrent(TorrentFile torrentFile) {
        return new File(DOWNLOADS_DIRECTORY, torrentFile.getName());
    }
    public static TorrentFile getTorrentFileByInfoHash(byte[] infoHash) {
        File torrentsDir = new File(TORRENTS_DIRECTORY);

        for (File torrentFile : Objects.requireNonNull(torrentsDir.listFiles())) {
            TorrentFile tFile = new TorrentFile(torrentFile);
            if (Arrays.equals(tFile.getInfoHash(), infoHash)) {
                return tFile;
            }
        }

        return null;
    }

    public int getTotalPieces() {
        return torrentFile.getPieceHashes().size();
    }

    public int getDownloadedPieces() {
        return pieceManager.getNumberOfDownloadedPieces();
    }
    public static synchronized void markPieceAsDownloaded(int index) {
        pieceManager.markPieceAsDownloaded(index);
    }

    public long getTotalLength() {
        return torrentFile.getLength();
    }

    public long getPieceLength() {
        return torrentFile.getPieceLength();
    }
}