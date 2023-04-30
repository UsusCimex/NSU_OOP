package ru.nsu.torrent.Messages;

import java.nio.ByteBuffer;

public class RequestMessage extends Message {
    public static final byte REQUEST = 6;
    private int index;
    private int offset;
    private int pieceLength;

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

    public static PieceMessage fromByteBuffer(ByteBuffer buffer, int length) {
        int index = buffer.getInt();
        int offset = buffer.getInt();
        byte[] data = new byte[length - 9];
        buffer.get(data);
        return new PieceMessage(index, offset, data);
    }

}
