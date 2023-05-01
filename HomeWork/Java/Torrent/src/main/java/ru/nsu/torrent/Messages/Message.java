package ru.nsu.torrent.Messages;

import java.nio.ByteBuffer;

public abstract class Message {
    protected int length;
    protected byte type;

    public byte getType() {
        return type;
    }
    // Get piece size
    public int getLength() { return length; }
    public abstract byte[] toBytes();

    public static Message fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int length = buffer.getInt();
        byte type = buffer.get();

        return switch (type) {
            case RequestMessage.REQUEST -> RequestMessage.fromByteBuffer(buffer, length);
            case PieceMessage.PIECE -> PieceMessage.fromByteBuffer(buffer, length);
            default -> throw new IllegalArgumentException("Unsupported message type: " + type);
        };
    }
}
