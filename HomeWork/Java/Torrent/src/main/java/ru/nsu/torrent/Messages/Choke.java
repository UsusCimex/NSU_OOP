package ru.nsu.torrent.Messages;

import java.nio.ByteBuffer;

public class Choke extends Message {
    public static final byte CHOKE = 0;
    public Choke() {
        this.type = CHOKE;
        this.length = 1;
    }
    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + length);
        buffer.putInt(length);
        buffer.put(type);
        return buffer.array();
    }

    protected static Choke fromByteBuffer(ByteBuffer buffer, int length) {
        return new Choke();
    }
}
