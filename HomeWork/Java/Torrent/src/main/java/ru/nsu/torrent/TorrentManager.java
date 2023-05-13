package ru.nsu.torrent;

import ru.nsu.torrent.Messages.Message;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TorrentManager {
    public static final String TORRENTS_DIRECTORY = "torrentsDir";
    public static final String DOWNLOADS_DIRECTORY = "downloadsDir";
    private final ExecutorService messagesExecutor = Executors.newFixedThreadPool(3);
    private final ExecutorService fileWriterExecutor = Executors.newFixedThreadPool(1);
    private final Map<File, TorrentFile> torrents = new HashMap<>();

    private final Map<SocketChannel, Peer> ClientSession = new HashMap<>();
    private final Map<SocketChannel, Peer> ServerSession = new HashMap<>();

    public TorrentManager() {
        updateTorrents();
    }
    public void updateTorrents() {
        torrents.clear();
        File torrentsDir = new File(TORRENTS_DIRECTORY);

        for (File file : Objects.requireNonNull(torrentsDir.listFiles())) {
            TorrentFile tFile = new TorrentFile(file);
            torrents.put(file.getAbsoluteFile(), tFile);
        }
    }
    public void executeMessage(Runnable runnable) {
        messagesExecutor.submit(runnable);
    }
    public void executeFileWrite(Runnable runnable) {
        fileWriterExecutor.submit(runnable);
    }

    public Map<SocketChannel, Peer> getClientSession() {
        return ClientSession;
    }
    public Map<SocketChannel, Peer> getServerSession() {
        return ServerSession;
    }
    public void stopSession(Map<SocketChannel, Peer> session) {
        Iterator<Map.Entry<SocketChannel, Peer>> iterator = session.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<SocketChannel, Peer> entry = iterator.next();
            SocketChannel socketChannel = entry.getKey();
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            iterator.remove();
        }
        session.clear();
    }
    public List<byte[]> getAvailableInfoHashes() {
        List<byte[]> infoHashes = new ArrayList<>();

        for (Map.Entry<File, TorrentFile> entry : torrents.entrySet()) {
            TorrentFile tFile = entry.getValue();
            infoHashes.add(tFile.getInfoHash());
        }

        return infoHashes;
    }
    public TorrentFile getTorrentFile(File file) {
        if (file == null) return null;
        return torrents.get(file.getAbsoluteFile());
    }
    public TorrentFile getTorrentFile(byte[] infoHash) {
        for (Map.Entry<File, TorrentFile> entry : torrents.entrySet()) {
            TorrentFile tFile = entry.getValue();
            if (Arrays.equals(tFile.getInfoHash(), infoHash)) {
                return tFile;
            }
        }
        return null;
    }
    public File getDownloadFileByTorrent(TorrentFile torrentFile) {
        return new File(DOWNLOADS_DIRECTORY, torrentFile.getName());
    }
    public String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void stop() {
        if (!messagesExecutor.isShutdown()) {
            messagesExecutor.shutdown();
        }
        if (!fileWriterExecutor.isShutdown()) {
            fileWriterExecutor.shutdown();
        }
        stopSession(ClientSession);
        stopSession(ServerSession);
    }
}
