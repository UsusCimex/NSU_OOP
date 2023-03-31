package ru.nsu.pacman.enemy.ghosts;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.generation.LevelData;

import static ru.nsu.pacman.Controller.Coordinates;
//trying to get to the place where the pacman is moving
public class OrangeGhost extends Enemy {

    public OrangeGhost(Coordinates startPosition, LevelData data) {
        super(startPosition, data);
    }
}
