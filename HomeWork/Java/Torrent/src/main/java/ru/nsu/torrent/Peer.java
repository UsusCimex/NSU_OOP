package ru.nsu.torrent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.BitSet;

public class Peer {
    private String ipAddress;
    private int port;
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    public Peer(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public boolean connect() {
        try {
            socket = new Socket(ipAddress, port);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean send(byte[] array) {
        if (outputStream == null) {
            return false;
        }

        try {
            outputStream.writeInt(array.length);
            outputStream.write(array);
            outputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] receive() {
        if (inputStream == null) {
            return null;
        }

        try {
            int length = inputStream.readInt();
            byte[] receivedData = new byte[length];
            inputStream.readFully(receivedData, 0, length);
            return receivedData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void disconnect() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
