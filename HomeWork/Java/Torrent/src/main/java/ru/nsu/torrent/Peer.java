package ru.nsu.torrent;

import ru.nsu.torrent.Messages.Message;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

public class Peer {
    private final byte[] infoHash;
    private final SocketChannel socketChannel;
    private final InetSocketAddress address;
    private final AtomicInteger activeRequests = new AtomicInteger(0);
    private BitSet availablePieces;

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
    public AtomicInteger getActiveRequests() {
        return activeRequests;
    }
}
