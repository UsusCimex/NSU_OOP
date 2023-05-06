package ru.nsu.torrent.Messages;

import java.nio.ByteBuffer;

public class RequestMessage extends Message {
    public static final byte REQUEST = 6;
    private final int index;
    private final int offset;
    private final int pieceLength;

    public RequestMessage(int index, int offset, int pieceLength) {
        this.type = REQUEST;
        this.index = index;
        this.offset = offset;
        this.pieceLength = pieceLength;
        this.length = 13;
    }

    public int getIndex() {
        return index;
    }

    public int getOffset() {
        return offset;
    }

    public int getPieceLength() {
        return pieceLength;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + length);
        buffer.putInt(length);
        buffer.put(type);
        buffer.putInt(index);
        buffer.putInt(offset);
        buffer.putInt(pieceLength);
        return buffer.array();
    }
    public static RequestMessage fromByteBuffer(ByteBuffer buffer, int length) {
        int index = buffer.getInt();
        int offset = buffer.getInt();
        int pieceLength = buffer.getInt();
        return new RequestMessage(index, offset, pieceLength);
    }
}
