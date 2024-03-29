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
    private final ExecutorService messagesExecutor = Executors.newFixedThreadPool(2);
    private final ExecutorService fileWriterExecutor = Executors.newFixedThreadPool(1);
    private final Map<String, TorrentFile> torrents = new HashMap<>();
    private final SocketAddress myAddress;

    private final Map<SocketAddress, Peer> ClientSession = new HashMap<>();
    private final Map<SocketAddress, Peer> ServerSession = new HashMap<>();

    public TorrentManager(SocketAddress address) {
        updateTorrents();
        myAddress = address;
    }
    public void updateClientSession(TorrentFile torrentFile, Selector selector) {
        List<SocketAddress> addresses = torrentFile.getTracker().getAddresses();
        Map<SocketAddress, Peer> clientSession = getClientSession();
        synchronized (addresses) {
            for (SocketAddress address : addresses) {
                try {
                    if (address == null || address.equals(myAddress)) continue;
                    boolean sessionExists = false;
                    for (Peer peer : clientSession.values()) {
                        if (peer.getAddress().equals(address)) {
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
                    e.printStackTrace();
                    System.err.println("[TorrentManager] ClientSession update exception...");
                }
            }
        }
    }
    public void updateTorrents() {
        torrents.clear();
        File torrentsDir = new File(TORRENTS_DIRECTORY);

        for (File file : Objects.requireNonNull(torrentsDir.listFiles())) {
            TorrentFile tFile = new TorrentFile(file);
            torrents.put(TorrentManager.bytesToHex(tFile.getInfoHash()), tFile);
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
        for (Peer peer : session.values()) {
            SocketChannel socketChannel = peer.getSocketChannel();
            if (socketChannel.isConnected()) {
                try {
                    socketChannel.close();
                    System.err.println("[TorrentManager] Session: " + socketChannel.getRemoteAddress() + " closed.");
                } catch (IOException e) {
                    System.err.println("[TorrentManager] Close socket exception!");
                }
            }
        }
        session.clear();
    }
    public List<String> getAvailableInfoHashes() {
        return new ArrayList<>(torrents.keySet());
    }
    public TorrentFile getTorrentFile(byte[] infoHash) {
        if (infoHash == null) {
            return null;
        }
        return torrents.get(TorrentManager.bytesToHex(infoHash));
    }
    public File getDownloadFileByTorrent(TorrentFile torrentFile) {
        return new File(DOWNLOADS_DIRECTORY, torrentFile.getName());
    }
    public static String bytesToHex(byte[] bytes) {
        return HexFormat.of().formatHex(bytes);
    }

    public void stop() {
        if (!messagesExecutor.isShutdown()) {
            messagesExecutor.shutdown();
        }
        if (!fileWriterExecutor.isShutdown()) {
            fileWriterExecutor.shutdown();
        }
    }
}
