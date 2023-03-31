package ru.nsu.pacman.enemy.ghosts;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.Controller;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Random;

import static ru.nsu.pacman.Game.CELL_N;
import static ru.nsu.pacman.Controller.Coordinates;
import static ru.nsu.pacman.Controller.Orientation;

//Always running after Pacman
public class RedGhost extends Enemy {
    public RedGhost(Coordinates startPosition, LevelData data) {
        super(startPosition, data);
        speed = 1.5;
    }
    @Override
    public void move() {
//        if (curentOrientation == Controller.Orientation.NONE) {
//            curentOrientation = getOrientationToPacman();
//        }
    }

//    private Orientation getOrientationToPacman() {
//
//    }
}
