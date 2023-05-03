module ru.nsu.torrent {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires bencode;

    opens ru.nsu.torrent to javafx.fxml;
    exports ru.nsu.torrent;
    exports ru.nsu.torrent.Messages;
    opens ru.nsu.torrent.Messages to javafx.fxml;
    exports ru.nsu.torrent.Runnables;
    opens ru.nsu.torrent.Runnables to javafx.fxml;
}