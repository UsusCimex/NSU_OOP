package ru.nsu.torrent;

import java.nio.channels.SocketChannel;

public class Peer {
    private byte[] infoHash;
    private SocketChannel socketChannel;

    public Peer(SocketChannel socketChannel, byte[] infoHash) {
        this.socketChannel = socketChannel;
        this.infoHash = infoHash;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public byte[] getInfoHash() {
        return infoHash;
    }
}
