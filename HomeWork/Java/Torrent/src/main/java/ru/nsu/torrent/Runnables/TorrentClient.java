package ru.nsu.torrent.Runnables;

import ru.nsu.torrent.*;
import ru.nsu.torrent.Messages.Message;
import ru.nsu.torrent.Messages.Request;
import ru.nsu.torrent.Torrent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class TorrentClient implements Runnable {
    private final TorrentFile torrentFile;
    private Selector selector;
    private final Map<SocketChannel, Peer> session = new HashMap<>();

    public TorrentClient(TorrentFile torrentFile) {
        this.torrentFile = torrentFile;
    }
    @Override
    public void run() {
        if (torrentFile == null) {
            System.err.println("[TorrentClient] Select file!");
            return;
        }
        if (Torrent.getTracker().getPeers().isEmpty()) {
            System.err.println("[TorrentClient] Peers not found!");
            return;
        }
        if (Torrent.getFile().getPieceHashes().size() == Torrent.getFile().getDownloadedPieces()) {
            System.err.println("[TorrentClient] File was download!");
            return;
        }

        System.err.println("[TorrentClient] Start download file: " + torrentFile.getName());
        try {
            this.selector = Selector.open();
            for (Peer peer : Torrent.getTracker().getPeers()) {
                SocketChannel socketChannel = peer.getSocketChannel();
                socketChannel.configureBlocking(false);
                socketChannel.connect(peer.getAddress());
                socketChannel.register(this.selector, SelectionKey.OP_CONNECT);
                session.put(socketChannel, peer);
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
            if (complete) System.err.println("[TorrentClient] File download complete: " + torrentFile.getName());
            stop();
        } catch (ClosedSelectorException e) {
            System.err.println("[TorrentClient] Selector closed!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void connect(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        Peer peer = session.get(socketChannel);
        if (peer == null) {
            System.err.println("Peer not found!");
            return;
        }
        Handshake.sendHandshake(socketChannel, torrentFile.getInfoHash(), new byte[20]);
        key.interestOps(SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        Peer peer = session.get(socketChannel);

        if (peer == null) {
            System.err.println("[TorrentClient] Peer not found... Socket exception!");
            socketChannel.close();
            key.cancel();
            return;
        }

        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        int numRead = socketChannel.read(lengthBuffer);
        if (numRead == -1) {
            System.err.println("[Torrent] Session closed: " + socketChannel.getRemoteAddress());
            this.session.remove(socketChannel);
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
                System.err.println("[TorrentClient] Session closed: " + socketChannel.getRemoteAddress());
                this.session.remove(socketChannel);
                socketChannel.close();
                key.cancel();
                return;
            }
        }

        Message message = Message.fromBytes(byteBuffer.flip().array());
        Handler handler = new Handler(peer, message);
        Torrent.executor.submit(handler);
    }
    private boolean sendRequest() {
        try {
            for (Map.Entry<SocketChannel, Peer> entry : session.entrySet()) {
                Peer peer = entry.getValue();
                if (!peer.getSocketChannel().finishConnect()) continue;
                int missingPieceIndex = torrentFile.getPieceManager().getIndexOfSearchedPiece(peer.getAvailablePieces());
                if (missingPieceIndex >= 0 && missingPieceIndex < torrentFile.getPieceHashes().size()) {
                    System.err.println("[TorrentClient] Request: " + missingPieceIndex + " piece, to " + peer.getSocketChannel().getRemoteAddress());
                    Request request = new Request(missingPieceIndex, 0, (int) Math.min(torrentFile.getPieceLength(), torrentFile.getLength() - missingPieceIndex * torrentFile.getPieceLength()));
                    Sender sender = new Sender(peer, request);
                    Torrent.executor.submit(sender);
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

            Iterator<Map.Entry<SocketChannel, Peer>> iterator = session.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SocketChannel, Peer> entry = iterator.next();
                SocketChannel socketChannel = entry.getKey();
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iterator.remove();
            }

            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
