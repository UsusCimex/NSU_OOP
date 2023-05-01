package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.RequestMessage;
import java.net.Socket;

public class Uploader implements Runnable {
    Socket socket;
    RequestMessage requestMessage;

    public Uploader(Socket socket, RequestMessage requestMessage) {
        this.socket = socket;
        this.requestMessage = requestMessage;
    }

    @Override
    public void run() {

    }
}