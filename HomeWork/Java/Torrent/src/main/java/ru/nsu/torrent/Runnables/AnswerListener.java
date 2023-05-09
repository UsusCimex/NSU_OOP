package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.*;
import ru.nsu.torrent.Messages.Message;
import ru.nsu.torrent.Messages.Piece;
import ru.nsu.torrent.Messages.Request;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnswerListener implements Runnable {
    private final TorrentFile torrentFile;
    private Selector selector;
    private final List<Peer> session = new ArrayList<>();

    public AnswerListener(TorrentFile torrentFile) {
        this.torrentFile = torrentFile;
    }
    @Override
    public void run() {
        if (torrentFile == null) {
            System.err.println("[AnswerListener] Select file!");
            return;
        }
        if (TorrentClient.getTracker().getPeers().isEmpty()) {
            System.err.println("[AnswerListener] Peers not found!");
            return;
        }
        if (TorrentClient.getFile().getPieceHashes().size() == TorrentClient.getFile().getDownloadedPieces()) {
            System.err.println("[AnswerListener] File was download!");
            return;
        }

        System.err.println("[AnswerListener] Start download file: " + torrentFile.getName());
        try {
            this.selector = Selector.open();
            for (Peer peer : TorrentClient.getTracker().getPeers()) {
                SocketChannel socketChannel = peer.getSocketChannel();
                socketChannel.configureBlocking(false);
                socketChannel.connect(peer.getAddress());
                socketChannel.register(this.selector, SelectionKey.OP_CONNECT);
                session.add(peer);
            }

            boolean complete = false;
            while (!Thread.currentThread().isInterrupted() && !complete) {
                this.selector.selectNow();
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (!key.isValid()) {
                        continue;
                    } else if (key.isConnectable()) {
                        if (((SocketChannel)key.channel()).finishConnect()) {
                            connect(key);
                        }
                    } else if (key.isReadable()) {
                        read(key);
                    }
                }
                complete = sendRequest();
            }
            if (complete) System.err.println("[AnswerListener] File download complete: " + torrentFile.getName());
            stop();
        } catch (ClosedSelectorException e) {
            System.err.println("[AnswerListener] Selector closed!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void connect(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        Peer peer = null;
        for (Peer pr : session) {
            if (pr.getSocketChannel().equals(socketChannel)) {
                peer = pr;
                break;
            }
        }
        if (peer == null) {
            System.err.println("Peer not found!");
            return;
        }
        Handshake.sendHandshake(socketChannel, torrentFile.getInfoHash(), new byte[20]);
        key.interestOps(SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        Peer peer = null;
        for (Peer pr : session) {
            if (pr.getSocketChannel().equals(socketChannel)) {
                peer = pr;
                break;
            }
        }

        if (peer == null) {
            System.err.println("[AnswerListener] Peer not found... Socket exception!");
            socketChannel.close();
            key.cancel();
            return;
        }

        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        int numRead = socketChannel.read(lengthBuffer);
        if (numRead == -1) {
            System.err.println("[TorrentClient] Session closed: " + socketChannel.getRemoteAddress());
            this.session.remove(peer);
            socketChannel.close();
            key.cancel();
            return;
        }
        lengthBuffer.flip();
        int messageLength = lengthBuffer.getInt();

        ByteBuffer byteBuffer = ByteBuffer.allocate(messageLength + 4);
        lengthBuffer.flip();
        byteBuffer.put(lengthBuffer);
        while (byteBuffer.hasRemaining()) {
            numRead = socketChannel.read(byteBuffer);
            if (numRead == -1) {
                System.err.println("[AnswerListener] Session closed: " + socketChannel.getRemoteAddress());
                this.session.remove(peer);
                socketChannel.close();
                key.cancel();
                return;
            }
        }

        byte[] infoHash = peer.getInfoHash();
        Message message = Message.fromBytes(byteBuffer.flip().array());
        if (message.getType() == 7) {
            Piece piece = (Piece) message;
            Downloader downloader = new Downloader(socketChannel, piece, infoHash);
            peer.getActiveRequests().decrementAndGet();
            TorrentClient.executor.submit(downloader);
        }
    }
    private boolean sendRequest() {
        try {
            for (Peer pr : session) {
                if (!pr.getSocketChannel().finishConnect()) continue;
                if (pr.getActiveRequests().get() >= 5) {
                    continue;
                }
                int missingPieceIndex = torrentFile.getPieceManager().getNextRandomPiece();
                System.err.println("[AnswerListener] Request: " + missingPieceIndex + " piece, to " + pr.getSocketChannel().getRemoteAddress());
                if (missingPieceIndex >= 0 && missingPieceIndex < torrentFile.getPieceHashes().size()) {
                    Request request = new Request(missingPieceIndex, 0, (int) Math.min(torrentFile.getPieceLength(), torrentFile.getLength() - missingPieceIndex * torrentFile.getPieceLength()));
                    Requester requester = new Requester(pr.getSocketChannel(), request, pr.getInfoHash());
                    pr.getActiveRequests().incrementAndGet();
                    TorrentClient.executor.submit(requester);
                } else {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void stop() {
        try {
            Thread.currentThread().interrupt();

            for (Peer peer : session) {
                if (peer.getSocketChannel() != null) {
                    peer.getSocketChannel().close();
                }
            }
            session.clear();

            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
