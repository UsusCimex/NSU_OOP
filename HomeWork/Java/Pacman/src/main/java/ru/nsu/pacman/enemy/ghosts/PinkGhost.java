package ru.nsu.pacman.enemy.ghosts;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.PacmanGame;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.generation.LevelData;

public class PinkGhost extends Enemy {

    public PinkGhost(PacmanGame.Coordinates startPosition, GridPane area, LevelData data) {
        super(startPosition, area, data);
    }
}
