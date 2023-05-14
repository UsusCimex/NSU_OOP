package ru.nsu.torrent;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.util.*;

// По ТЗ не нужен, но пусть возвращает список пиров из файла
public class Tracker {
    private final List<InetSocketAddress> addresses;
    public final String TRACKERS_DIRECTORY = "trackersDir";
    public Tracker(TorrentFile torrentFile) {
        addresses = new ArrayList<>();
        generateAddresses(torrentFile);
    }
    public List<InetSocketAddress> getAddresses() {
        return addresses;
    }
    private void generateAddresses(TorrentFile torrentFile) {
        int indexOfDot = torrentFile.getName().lastIndexOf('.');
        String fileName;
        if (indexOfDot != -1) {
            fileName = torrentFile.getName().substring(0, torrentFile.getName().lastIndexOf('.')) + ".txt";
        } else {
            fileName = torrentFile.getName() + ".txt";
        }
        try (Scanner scanner = new Scanner(new File(TRACKERS_DIRECTORY + "/" + fileName))) {
            while (scanner.hasNextLine()) {
                String[] peerData = scanner.nextLine().split(":");
                String ipAddress = peerData[0].trim();
                int port = Integer.parseInt(peerData[1].trim());
                addresses.add(new InetSocketAddress(ipAddress, port));
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Tracker not found. Path: " + TRACKERS_DIRECTORY + "/" + fileName);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}

