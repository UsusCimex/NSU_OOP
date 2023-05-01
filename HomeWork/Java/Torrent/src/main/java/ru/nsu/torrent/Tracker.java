package ru.nsu.torrent;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.*;

// По ТЗ не нужен, но пусть возвращает список пиров из файла
public class Tracker {
    private final List<Peer> peers;
    public Tracker(TorrentFile torrentFile) {
        peers = generatePeers(torrentFile);
    }
    public List<Peer> getPeers() {
        return peers;
    }
    private List<Peer> generatePeers(TorrentFile torrentFile) {
        String fileName = "Tracker/" + torrentFile.getName() + ".txt";
        List<Peer> peers = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(Objects.requireNonNull(getClass().getResourceAsStream(fileName)));
            while (scanner.hasNextLine()) {
                String[] peerData = scanner.nextLine().split(":");
                String ipAddress = peerData[0].trim();
                int port = Integer.parseInt(peerData[1].trim());

                try {
                    SocketChannel socketChannel = SocketChannel.open();
                    socketChannel.configureBlocking(false);

                    // Создаем объект Peer с данным SocketChannel и хэшем торрента.
                    Peer peer = new Peer(ipAddress, port, socketChannel, torrentFile.getInfoHash());
                    peers.add(peer);
                } catch (IOException e) {
                    System.err.println("Ошибка при создании SocketChannel для пира: " + ipAddress + ":" + port);
                }
            }
            scanner.close();
        } catch (NullPointerException ex) {
            ex.getMessage();
        }
        return peers;
    }
}

