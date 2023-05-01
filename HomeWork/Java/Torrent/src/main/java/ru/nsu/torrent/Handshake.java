package ru.nsu.torrent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public abstract class Handshake {
    private static final String PROTOCOL_STRING = "BitTorrent protocol";
    private static final int RESERVED_BYTES_LENGTH = 8;
    private static final int HANDSHAKE_LENGTH = 1 + PROTOCOL_STRING.length() + RESERVED_BYTES_LENGTH + 20 + 20;

    public static boolean sendHandshake(SocketChannel socketChannel, byte[] infoHash, byte[] peerId) throws IOException {
        if (!socketChannel.isConnected()) {
            return false;
        }
        ByteBuffer handshakeBuffer = ByteBuffer.allocate(HANDSHAKE_LENGTH);

        handshakeBuffer.put((byte) PROTOCOL_STRING.length());
        handshakeBuffer.put(PROTOCOL_STRING.getBytes());
        handshakeBuffer.put(new byte[RESERVED_BYTES_LENGTH]);
        handshakeBuffer.put(infoHash);
        handshakeBuffer.put(peerId);

        handshakeBuffer.flip();

        int bytesWritten = 0;
        while (handshakeBuffer.hasRemaining()) {
            bytesWritten += socketChannel.write(handshakeBuffer);
        }

        return bytesWritten == HANDSHAKE_LENGTH;
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
        String receivedProtocolString = new String(protocolStringBytes);
        if (protocolStringLength == PROTOCOL_STRING.length() && receivedProtocolString.equals(PROTOCOL_STRING)) {
            byte[] receivedInfoHash = new byte[20];
            handshakeBuffer.get(receivedInfoHash);
            return receivedInfoHash;
        } else {
            return null;
        }
    }
}
