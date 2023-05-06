package ru.nsu.torrent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public abstract class Handshake {
    private static final String PROTOCOL_STRING = "BitTorrent protocol";
    private static final int RESERVED_BYTES_LENGTH = 8;
    private static final int HANDSHAKE_LENGTH = 1 + PROTOCOL_STRING.length() + RESERVED_BYTES_LENGTH + 20 + 20;

    public static void sendHandshake(SocketChannel socketChannel, byte[] infoHash, byte[] peerId) throws IOException {
        ByteBuffer handshakeBuffer = ByteBuffer.allocate(HANDSHAKE_LENGTH);

        System.err.println("[Handshake] Start sending(" + TorrentClient.bytesToHex(infoHash) + ") to " + socketChannel.getRemoteAddress());

        handshakeBuffer.put((byte) PROTOCOL_STRING.length());
        handshakeBuffer.put(PROTOCOL_STRING.getBytes(Charset.forName("ISO-8859-15")));
        handshakeBuffer.put(new byte[8]);
        handshakeBuffer.put(infoHash);
        handshakeBuffer.put(peerId);
        handshakeBuffer.flip();

        while (handshakeBuffer.hasRemaining()) {
            socketChannel.write(handshakeBuffer);
        }
    }

    public static byte[] receiveHandshake(SocketChannel socketChannel) throws IOException {
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
            System.err.println("[Handshake] Received : " + TorrentClient.bytesToHex(receivedInfoHash) + ", from " + socketChannel.getRemoteAddress());
            return receivedInfoHash;
        } else {
            return null;
        }
    }
}
