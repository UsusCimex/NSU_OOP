package ru.nsu.torrent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Handshake {
    private static final String PROTOCOL_STRING = "BitTorrent protocol";
    private static final int RESERVED_BYTES_LENGTH = 8;
    private static final int HANDSHAKE_LENGTH = 1 + PROTOCOL_STRING.length() + RESERVED_BYTES_LENGTH + 20 + 20;
    private final TorrentManager torrentManager;
    public Handshake(TorrentManager torrentManager) {
        this.torrentManager = torrentManager;
    }

    public void sendHandshake(SocketChannel socketChannel, byte[] infoHash, byte[] peerId) throws IOException {
        ByteBuffer handshakeBuffer = ByteBuffer.allocate(HANDSHAKE_LENGTH);

        System.err.println("[Handshake] Start sending(" + torrentManager.bytesToHex(infoHash) + ") to " + socketChannel.getRemoteAddress());

        handshakeBuffer.put((byte) PROTOCOL_STRING.length());
        handshakeBuffer.put(PROTOCOL_STRING.getBytes(Charset.forName("ISO-8859-15")));
        handshakeBuffer.put(new byte[8]);
        handshakeBuffer.put(infoHash);
        handshakeBuffer.put(peerId);
        handshakeBuffer.flip();

        try {
            while (handshakeBuffer.hasRemaining()) {
                int numWrite = socketChannel.write(handshakeBuffer);
                if (numWrite == -1) {
                    socketChannel.close();
                }
            }
        } catch (IOException e) {
            System.err.println("[Handshake] Handshake not sending (" + socketChannel.getRemoteAddress() + ")");
            throw new IOException();
        }
    }

    public byte[] receiveHandshake(SocketChannel socketChannel) throws IOException {
        ByteBuffer handshakeBuffer = ByteBuffer.allocate(HANDSHAKE_LENGTH);
        while (handshakeBuffer.hasRemaining()) {
            socketChannel.read(handshakeBuffer);
        }
        handshakeBuffer.flip();

        int protocolStringLength = handshakeBuffer.get();
        byte[] protocolStringBytes = new byte[protocolStringLength];
        handshakeBuffer.get(protocolStringBytes);
        String receivedProtocolString = new String(protocolStringBytes, Charset.forName("ISO-8859-15"));
        if (protocolStringLength == PROTOCOL_STRING.length() && receivedProtocolString.equals(PROTOCOL_STRING)) {
            byte[] reserved = new byte[8];
            byte[] receivedInfoHash = new byte[20];
            byte[] peerID = new byte[20];
            handshakeBuffer.get(reserved);
            handshakeBuffer.get(receivedInfoHash);
            handshakeBuffer.get(peerID);
            System.err.println("[Handshake] Received : " + torrentManager.bytesToHex(receivedInfoHash) + ", from " + socketChannel.getRemoteAddress());
            return receivedInfoHash;
        } else {
            return null;
        }
    }
}
