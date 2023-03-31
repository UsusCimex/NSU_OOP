package ru.nsu.pacman.enemy.ghosts;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.generation.LevelData;

import static ru.nsu.pacman.Controller.Coordinates;
//Tries to get closer to Pacman, but keeps a short distance
public class BlueGhost extends Enemy {
    public BlueGhost(Coordinates startPosition, GridPane area, LevelData data) {
        super(startPosition, area, data);
    }
}
