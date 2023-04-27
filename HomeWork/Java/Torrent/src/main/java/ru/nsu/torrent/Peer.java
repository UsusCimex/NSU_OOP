package ru.nsu.torrent;

import java.util.BitSet;

public class Peer {
    private String ipAddress;
    private int port;
    private byte[] peerId;
    private BitSet pieces;
    private boolean[] interested;
    private boolean[] choked;

    // Геттеры, сеттеры и другие методы...
}

