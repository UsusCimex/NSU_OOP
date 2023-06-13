package ru.nsu.torrent.Messages;

import java.nio.ByteBuffer;

public class NotInterested extends Message{
    public static final byte NOT_INTERESTED = 3;
    public NotInterested() {
        this.type = NOT_INTERESTED;
        this.length = 1;
    }
    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + length);
        buffer.putInt(length);
        buffer.put(type);
        return buffer.array();
    }

    protected static NotInterested fromByteBuffer(ByteBuffer ignoredBuffer, int ignoredLength) {
        return new NotInterested();
    }
}
