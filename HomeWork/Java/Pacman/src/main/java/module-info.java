module ru.nsu.pacman {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.nsu.pacman to javafx.fxml;
    exports ru.nsu.pacman;
}