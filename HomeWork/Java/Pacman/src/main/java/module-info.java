module ru.nsu.pacman {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.nsu.pacman to javafx.fxml;
    exports ru.nsu.pacman;
    exports ru.nsu.pacman.menu;
    opens ru.nsu.pacman.menu to javafx.fxml;
    exports ru.nsu.pacman.data;
    opens ru.nsu.pacman.data to javafx.fxml;
}