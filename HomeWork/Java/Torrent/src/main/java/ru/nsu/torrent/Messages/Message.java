package ru.nsu.torrent.Messages;

import java.nio.ByteBuffer;

public abstract class Message {
    protected int length;
    protected byte type;

    public byte getType() {
        return type;
    }
    public abstract byte[] toBytes();

    public static Message fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int length = buffer.getInt();
        byte type = buffer.get();

        return switch (type) {
            case Have.HAVE -> Have.fromByteBuffer(buffer, length);
            case Bitfield.BITFIELD -> Bitfield.fromByteBuffer(buffer, length);
            case Request.REQUEST -> Request.fromByteBuffer(buffer, length);
            case Piece.PIECE -> Piece.fromByteBuffer(buffer, length);
            default -> throw new IllegalArgumentException("Unsupported message type: " + type);
        };
    }
}
