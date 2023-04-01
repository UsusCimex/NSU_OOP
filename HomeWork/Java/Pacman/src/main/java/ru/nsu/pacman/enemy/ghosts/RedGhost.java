package ru.nsu.pacman.enemy.ghosts;

import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.generation.LevelData;

import static ru.nsu.pacman.GameData.Coordinates;

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
