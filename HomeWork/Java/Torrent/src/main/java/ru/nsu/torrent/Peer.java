package ru.nsu.torrent;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.BitSet;

public class Peer {
    private final byte[] infoHash;
    private final SocketChannel socketChannel;
    private final InetSocketAddress address;
    private BitSet availablePieces = null;

    public Peer(String ipAddress, int port, SocketChannel socketChannel, byte[] infoHash) {
        address = new InetSocketAddress(ipAddress, port);
        this.socketChannel = socketChannel;
        this.infoHash = infoHash;
    }
    public void setAvailablePieces(BitSet availablePieces) {
        this.availablePieces = availablePieces;
    }
    public BitSet getAvailablePieces() {
        return availablePieces;
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
