package ru.nsu.torrent.Messages;

import java.nio.ByteBuffer;
import java.util.BitSet;

public class Bitfield extends Message {
    public static final byte BITFIELD = 5;
    private final BitSet bitSet;
    public Bitfield(BitSet bitSet) {
        this.type = BITFIELD;
        this.bitSet = bitSet;
        this.length = 1 + (bitSet.length() + 7) / 8;
    }
    public BitSet getBitSet() {
        return bitSet;
    }
    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + length);
        buffer.putInt(length);
        buffer.put(type);
        buffer.put(bitSet.toByteArray());
        return buffer.array();
    }
    protected static Bitfield fromByteBuffer(ByteBuffer buffer, int length) {
        byte[] data = new byte[length - 1];
        buffer.get(data);
        return new Bitfield(BitSet.valueOf(data));
    }
}
