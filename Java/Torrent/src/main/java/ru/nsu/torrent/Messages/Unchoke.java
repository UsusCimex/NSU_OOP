package ru.nsu.torrent.Messages;

import java.nio.ByteBuffer;

public class Unchoke extends Message {
    public static final byte UNCHOKE = 1;
    public Unchoke() {
        this.type = UNCHOKE;
        this.length = 1;
    }
    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + length);
        buffer.putInt(length);
        buffer.put(type);
        return buffer.array();
    }

    protected static Unchoke fromByteBuffer(ByteBuffer ignoredBuffer, int ignoredLength) {
        return new Unchoke();
    }
}
