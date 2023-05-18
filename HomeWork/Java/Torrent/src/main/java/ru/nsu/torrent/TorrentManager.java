package ru.nsu.torrent;

import java.io.File;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
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

    private final Map<SocketAddress, Peer> ClientSession = new HashMap<>();
    private final Map<SocketAddress, Peer> ServerSession = new HashMap<>();

    public TorrentManager() {
        updateTorrents();
    }
    public void updateClientSession(TorrentFile torrentFile, Selector selector) {
        List<SocketAddress> addresses = torrentFile.getTracker().getAddresses();
        Map<SocketAddress, Peer> clientSession = getClientSession();

        for (SocketAddress address : addresses) {
            try {
                boolean sessionExists = false;
                for (Peer peer : clientSession.values()) {
                    if (peer.getSocketChannel().getRemoteAddress().equals(address)) {
                        sessionExists = true;
                        break;
                    }
                }

                if (!sessionExists) {
                    SocketChannel socketChannel = SocketChannel.open();
                    socketChannel.configureBlocking(false);
                    System.err.println("[TorrentManager] connecting to: " + address);
                    socketChannel.connect(address);
                    socketChannel.register(selector, SelectionKey.OP_CONNECT);
                    Peer peer = new Peer(socketChannel, torrentFile.getInfoHash());
                    clientSession.put(address, peer);
                }
            } catch (IOException e) {
                System.err.println("[TorrentManager] ClientSession update exception...");
            }
        }
    }
    private void updateTorrents() {
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

    public Map<SocketAddress, Peer> getClientSession() {
        return ClientSession;
    }
    public Map<SocketAddress, Peer> getServerSession() {
        return ServerSession;
    }
    public void stopSession(Map<SocketAddress, Peer> session) {
        Iterator<Map.Entry<SocketAddress, Peer>> iterator = session.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<SocketAddress, Peer> entry = iterator.next();
            SocketChannel socketChannel = entry.getValue().getSocketChannel();
            if (socketChannel.isConnected()) {
                try {
                    System.err.println("[TorrentManager] Session: " + socketChannel.getRemoteAddress() + " closed.");
                    socketChannel.close();
                } catch (IOException e) {
                    System.err.println("[TorrentManager] Close socket exception!");
                }
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
        TorrentFile torrentFile = torrents.get(file.getAbsoluteFile());
        if (torrentFile == null) {
            torrents.put(file.getAbsoluteFile(), new TorrentFile(file));
            torrentFile = torrents.get(file.getAbsoluteFile());
        }
        return torrentFile.updated();
    }
    public TorrentFile getTorrentFile(byte[] infoHash) {
        for (Map.Entry<File, TorrentFile> entry : torrents.entrySet()) {
            TorrentFile tFile = entry.getValue();
            if (Arrays.equals(tFile.getInfoHash(), infoHash)) {
                return tFile.updated();
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
