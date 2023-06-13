package ru.nsu.torrent.Messages;

import java.nio.ByteBuffer;

public class Have extends Message {
    public static final byte HAVE = 4;
    private final int index;
    public Have(int index) {
        this.type = HAVE;
        this.index = index;
        this.length = 5;
    }
    public int getIndex() {
        return index;
    }
    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + length);
        buffer.putInt(length);
        buffer.put(type);
        buffer.putInt(index);
        return buffer.array();
    }
    protected static Have fromByteBuffer(ByteBuffer buffer, int ignoredLength) {
        int index = buffer.getInt();
        return new Have(index);
    }
}
