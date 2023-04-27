module ru.nsu.torrent {
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.nsu.torrent to javafx.fxml;
    exports ru.nsu.torrent;
}