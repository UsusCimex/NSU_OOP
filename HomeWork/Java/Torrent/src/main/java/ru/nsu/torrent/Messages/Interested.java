package ru.nsu.torrent.Messages;

import java.nio.ByteBuffer;

public class Interested extends Message{
    public static final byte INTERESTED = 2;
    public Interested() {
        this.type = INTERESTED;
        this.length = 1;
    }
    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + length);
        buffer.putInt(length);
        buffer.put(type);
        return buffer.array();
    }

    protected static Interested fromByteBuffer(ByteBuffer ignoredBuffer, int ignoredLength) {
        return new Interested();
    }
}
