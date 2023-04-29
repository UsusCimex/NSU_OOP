package ru.nsu.torrent;

import java.util.*;

// По ТЗ не нужен, но пусть возвращает список пиров из файла
public class Tracker {
    private List<Peer> peers;
    public Tracker(TorrentFile torrentFile) {
        peers = generatePeers(torrentFile);
    }
    public List<Peer> getPeers() {
        return peers;
    }
    private List<Peer> generatePeers(TorrentFile torrentFile) {
        String fileName = "Tracker/" + torrentFile.getName().replace(".torrent",".txt");
        List<Peer> peers = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(Objects.requireNonNull(getClass().getResourceAsStream(fileName)));
            while (scanner.hasNextLine()) {
                String[] peerData = scanner.nextLine().split(":");
                String ipAddress = peerData[0].trim();
                int port = Integer.parseInt(peerData[1].trim());
                peers.add(new Peer(ipAddress, port));
            }
            scanner.close();
        } catch (NullPointerException ex) {
            ex.getMessage();
        }
        return peers;
    }
}

