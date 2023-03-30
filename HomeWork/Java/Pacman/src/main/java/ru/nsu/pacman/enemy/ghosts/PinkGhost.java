package ru.nsu.pacman.enemy.ghosts;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.generation.LevelData;

import static ru.nsu.pacman.Controller.Coordinates;
public class PinkGhost extends Enemy {

    public PinkGhost(Coordinates startPosition, GridPane area, LevelData data) {
        super(startPosition, area, data);
    }
}
