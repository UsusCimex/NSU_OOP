package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.Messages.*;
import ru.nsu.torrent.Peer;
import ru.nsu.torrent.Torrent;
import ru.nsu.torrent.TorrentFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;

public class Handler implements Runnable {
    private final Peer peer;
    private final Message message;

    public Handler(Peer peer, Message message) {
        this.peer = peer;
        this.message = message;
    }

    @Override
    public void run() {
        byte type = message.getType();
        switch (type) {
            case (Request.REQUEST) -> {
                Request request = (Request) message;
                int index = request.getIndex();
                int offset = request.getOffset();
                int length = request.getPieceLength();

                byte[] data = readPieceData(index, offset, length);
                if (data == null) return;
                Piece piece = new Piece(index, offset, data);
                Sender sender = new Sender(peer, piece);
                Torrent.executor.submit(sender);
            }
            case (Piece.PIECE) -> {
                Piece piece = (Piece) message;
                int index = piece.getIndex();
                int offset = piece.getOffset();
                byte[] data = piece.getData();

                TorrentFile torrentFile = Torrent.getTorrentFileByInfoHash(peer.getInfoHash());
                if (torrentFile == null) {
                    throw new IllegalStateException("[Handler] File not found... Hash exception!");
                }
                if (torrentFile.getPieceManager().getPiece(index)) {
                    return;
                }

                MessageDigest md;
                try {
                    md = MessageDigest.getInstance("SHA-1");
                } catch (NoSuchAlgorithmException e) {
                    System.err.println("[Handler] Algorithm SHA-1 not found!");
                    throw new RuntimeException(e);
                }
                byte[] calculatedHash = md.digest(data);
                byte[] expectedHash = torrentFile.getPieceHashes().get(index);

                if (Arrays.equals(calculatedHash, expectedHash)) {
                    try (RandomAccessFile raf = new RandomAccessFile(Torrent.getDownloadFileByTorrent(torrentFile), "rw")) {
                        raf.seek((long) index * torrentFile.getPieceLength() + offset);
                        raf.write(data);
                        Torrent.getFile().markPieceAsDownloaded(index);
                    } catch (IOException e) {
                        throw new RuntimeException("[Handler] File not found!", e);
                    }

                    Have have = new Have(index);
                    Sender sender = new Sender(peer, have);
                    Torrent.executor.submit(sender);
                } else {
                    System.err.println("[Handler] Hashes do not match for piece " + index);
                }
            }
            case (Have.HAVE) -> {
                Have have = (Have) message;
                int index = have.getIndex();
                peer.getAvailablePieces().set(index);

                TorrentFile tFile = Torrent.getTorrentFileByInfoHash(peer.getInfoHash());
                BitSet availablePieces = new BitSet(tFile.getPieceManager().getNumberPieces());
                availablePieces.or(tFile.getPieceManager().getAvailablePieces());
                if (!availablePieces.get(index)) {
                    Sender sender = new Sender(peer, new Interested());
                    Torrent.executor.submit(sender);
                }
            }
            case (Bitfield.BITFIELD) -> {
                Bitfield bitfield = (Bitfield) message;
                peer.setAvailablePieces(bitfield.getBitSet());

                TorrentFile tFile = Torrent.getTorrentFileByInfoHash(peer.getInfoHash());
                BitSet availablePieces = new BitSet(tFile.getPieceManager().getNumberPieces());
                availablePieces.or(tFile.getPieceManager().getAvailablePieces());
                BitSet peerPieces = peer.getAvailablePieces();

                availablePieces.flip(0, tFile.getPieceManager().getNumberPieces());
                availablePieces.and(peerPieces);
                Sender sender;
                if (availablePieces.cardinality() != 0) {
                    peer.setInterested(true);
                    sender = new Sender(peer, new Interested());
                } else {
                    peer.setInterested(false);
                    sender = new Sender(peer, new NotInterested());
                }
                Torrent.executor.submit(sender);
            }
            case (Choke.CHOKE) -> {
                peer.setChoked(true);
            }
            case (Unchoke.UNCHOKE) -> {
                peer.setChoked(false);
            }
            case (Interested.INTERESTED) -> {
                peer.setInterested(true);
            }
            case (NotInterested.NOT_INTERESTED) -> {
                peer.setInterested(false);
            }
        }
        try {
            printMessage(message, peer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readPieceData(int index, int offset, int length) {
        TorrentFile tFile = Torrent.getTorrentFileByInfoHash(peer.getInfoHash());
        assert tFile != null;
        if (!tFile.getPieceManager().getPiece(index)) {
            return null;
        }
        File fileByInfoHash = Torrent.getDownloadFileByTorrent(tFile);
        try (RandomAccessFile file = new RandomAccessFile(fileByInfoHash, "r")) {
            file.seek((long) index * tFile.getPieceLength() + offset);

            byte[] data = new byte[length];
            file.read(data);

            return data;
        } catch (IOException e) {
            System.err.println("[Handler] Mistake in file reader!");
        }
        return null;
    }
    private void printMessage(Message message, Peer peer) throws IOException {
        if (message.getType() == Request.REQUEST) {
            System.err.println("[Handler] REQUEST piece: \"" + ((Request) message).getIndex() + "\". From: " + peer.getAddress());
        } else if (message.getType() == Piece.PIECE) {
            System.err.println("[Handler] PIECE piece: \"" + ((Piece) message).getIndex() + "\". From: " + peer.getAddress());
        } else if (message.getType() == Bitfield.BITFIELD) {
            System.err.println("[Handler] BITFIELD from: " + peer.getAddress());
        } else if (message.getType() == Interested.INTERESTED) {
            System.err.println("[Handler] INTERESTED from: " + peer.getAddress());
        } else if (message.getType() == NotInterested.NOT_INTERESTED) {
            System.err.println("[Handler] NOT_INTERESTED from: " + peer.getAddress());
        } else if (message.getType() == Have.HAVE) {
            System.err.println("[Handler] HAVE piece: \"" + ((Have) message).getIndex() + "\". From: " + peer.getAddress());
        } else if (message.getType() == Choke.CHOKE) {
            System.err.println("[Handler] CHOKE from: " + peer.getAddress());
        } else if (message.getType() == Unchoke.UNCHOKE) {
            System.err.println("[Handler] UNCHOKE from: " + peer.getAddress());
        }
    }
}