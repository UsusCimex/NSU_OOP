package ru.nsu.pacman.enemy;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.PacmanGame;
import ru.nsu.pacman.generation.LevelData;

public interface Enemy {
    PacmanGame.Coordinates getPosition();
    PacmanGame.Orientation getCurrentOrientation();
    void changeNextOrientation(PacmanGame.Orientation newOrientation);
    void move();
}
