package ru.nsu.torrent.Messages;

import java.nio.ByteBuffer;

public class PieceMessage extends Message {
    public static final byte PIECE = 7;
    private final int index;
    private final int offset;
    private final byte[] data;

    public PieceMessage(int index, int offset, byte[] data) {
        this.type = PIECE;
        this.index = index;
        this.offset = offset;
        this.data = data;
        this.length = 9 + data.length;
    }

    public int getIndex() {
        return index;
    }

    public int getOffset() {
        return offset;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + length);
        buffer.putInt(length);
        buffer.put(type);
        buffer.putInt(index);
        buffer.putInt(offset);
        buffer.put(data);
        return buffer.array();
    }
    public static PieceMessage fromByteBuffer(ByteBuffer buffer, int length) {
        int index = buffer.getInt();
        int offset = buffer.getInt();
        byte[] data = new byte[length - 9];
        buffer.get(data);
        return new PieceMessage(index, offset, data);
    }
}

