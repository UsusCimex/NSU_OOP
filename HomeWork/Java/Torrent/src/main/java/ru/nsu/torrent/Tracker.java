package ru.nsu.torrent;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;

// По ТЗ не нужен, но пусть возвращает список пиров из файла
public class Tracker {
    private final List<SocketAddress> addresses = Collections.synchronizedList(new ArrayList<>());
    private final TorrentFile torrentFile;
    public final String TRACKERS_DIRECTORY = "trackersDir";
    public Tracker(TorrentFile torrentFile) {
        this.torrentFile = torrentFile;
    }
    public synchronized List<SocketAddress> getAddresses() {
        updateAddresses();
        return addresses;
    }
    private void updateAddresses() {
        synchronized (addresses) {
            addresses.clear();
            String trackerFile = getPathToTrackerFile();
            try (Scanner scanner = new Scanner(new File(TRACKERS_DIRECTORY + "/" + trackerFile))) {
                while (scanner.hasNextLine()) {
                    String[] peerData = scanner.nextLine().split(":");
                    String ipAddress = peerData[0].trim();
                    int port = Integer.parseInt(peerData[1].trim());
                    SocketAddress address = new InetSocketAddress(ipAddress, port);
                    addresses.add(address);
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Tracker not found. Path: " + TRACKERS_DIRECTORY + "/" + trackerFile);
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }
    private String getPathToTrackerFile() {
        int indexOfDot = torrentFile.getName().lastIndexOf('.');
        String trackerFile;
        if (indexOfDot != -1) {
            trackerFile = torrentFile.getName().substring(0, torrentFile.getName().lastIndexOf('.')) + ".txt";
        } else {
            trackerFile = torrentFile.getName() + ".txt";
        }
        return trackerFile;
    }
}

