package ru.nsu.torrent.Messages;

import java.nio.ByteBuffer;

public class KeepAlive extends Message {
    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(0);
        return buffer.array();
    }


}
