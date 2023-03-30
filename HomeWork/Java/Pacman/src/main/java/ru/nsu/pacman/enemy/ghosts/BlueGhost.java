package ru.nsu.pacman.enemy.ghosts;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.PacmanGame;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.generation.LevelData;

public class BlueGhost extends Enemy {
    public BlueGhost(PacmanGame.Coordinates startPosition, GridPane area, LevelData data) {
        super(startPosition, area, data);
    }
}
