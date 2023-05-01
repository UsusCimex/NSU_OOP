package ru.nsu.torrent;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Peer {
    private final byte[] infoHash;
    private final SocketChannel socketChannel;
    private final InetSocketAddress address;

    public Peer(String ipAddress, int port, SocketChannel socketChannel, byte[] infoHash) {
        address = new InetSocketAddress(ipAddress, port);
        this.socketChannel = socketChannel;
        this.infoHash = infoHash;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
    public byte[] getInfoHash() {
        return infoHash;
    }
    public InetSocketAddress getAddress() {
        return address;
    }
}
