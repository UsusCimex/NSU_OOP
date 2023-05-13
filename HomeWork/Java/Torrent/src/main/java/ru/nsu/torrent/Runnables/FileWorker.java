package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.Have;
import ru.nsu.torrent.Messages.Message;
import ru.nsu.torrent.Messages.Piece;
import ru.nsu.torrent.Peer;
import ru.nsu.torrent.TorrentFile;
import ru.nsu.torrent.TorrentManager;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileWorker implements Runnable {
    private final Peer peer;
    private final Message message;
    private final TorrentManager torrentManager;
    public FileWorker(Peer peer, Message message, TorrentManager torrentManager) {
        this.peer = peer;
        this.message = message;
        this.torrentManager = torrentManager;
    }
    @Override
    public void run() {
        if (message.getType() == Piece.PIECE) {
            Piece piece = (Piece) message;
            int index = piece.getIndex();
            int offset = piece.getOffset();
            byte[] data = piece.getData();
            TorrentFile torrentFile = torrentManager.getTorrentFile(peer.getInfoHash());

            try (RandomAccessFile raf = new RandomAccessFile(torrentManager.getDownloadFileByTorrent(torrentFile), "rw")) {
                raf.seek((long) index * torrentFile.getPieceLength() + offset);
                raf.write(data);
                torrentFile.markPieceAsDownloaded(index);
            } catch (IOException e) {
                throw new RuntimeException("[FileWorker] File not found!", e);
            }

            Have have = new Have(index);
            Sender sender = new Sender(peer, have, torrentManager);
            torrentManager.executeMessage(sender);
        } else {
            System.err.println("[FileWorker] Please use this class only by Piece message");
            throw new RuntimeException("Use only Piece message!");
        }
    }
}
