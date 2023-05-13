package ru.nsu.torrent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.BitSet;

public class Peer {
    private final byte[] infoHash;
    private final SocketChannel socketChannel;
    private final InetSocketAddress address;
    private BitSet availablePieces = null;
    private boolean interested = false;
    private boolean choked = false;

    public Peer(SocketChannel socketChannel, byte[] infoHash) throws IOException {
        this.socketChannel = socketChannel;
        this.address = (InetSocketAddress) socketChannel.getRemoteAddress();
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
    public boolean isInterested() {
        return interested;
    }
    public boolean isChoked() {
        return choked;
    }
    public void setInterested(boolean interested) {
        this.interested = interested;
    }
    public void setChoked(boolean choked) {
        this.choked = choked;
    }
}
