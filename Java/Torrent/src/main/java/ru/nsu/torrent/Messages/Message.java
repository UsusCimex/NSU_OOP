package ru.nsu.torrent.Messages;

import java.nio.ByteBuffer;

public abstract class Message {
    protected int length;
    protected byte type;

    public int getLength() {
        return length;
    }
    public byte getType() {
        return type;
    }
    public abstract byte[] toBytes();

    public static Message fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int length = buffer.getInt();
        if (length == 0) return new KeepAlive();
        byte type = buffer.get();

        return switch (type) {
            case Choke.CHOKE -> Choke.fromByteBuffer(buffer, length);
            case Unchoke.UNCHOKE -> Unchoke.fromByteBuffer(buffer, length);
            case Interested.INTERESTED -> Interested.fromByteBuffer(buffer,length);
            case NotInterested.NOT_INTERESTED -> NotInterested.fromByteBuffer(buffer, length);
            case Have.HAVE -> Have.fromByteBuffer(buffer, length);
            case Bitfield.BITFIELD -> Bitfield.fromByteBuffer(buffer, length);
            case Request.REQUEST -> Request.fromByteBuffer(buffer, length);
            case Piece.PIECE -> Piece.fromByteBuffer(buffer, length);
            default -> throw new IllegalArgumentException("Unsupported message type: " + type);
        };
    }
}
