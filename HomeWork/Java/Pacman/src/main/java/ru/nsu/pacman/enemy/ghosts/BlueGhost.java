package ru.nsu.pacman.enemy.ghosts;

import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.generation.LevelData;

import static ru.nsu.pacman.GameData.Coordinates;
//Tries to get closer to Pacman, but keeps a short distance
public class BlueGhost extends Enemy {
    public BlueGhost(Coordinates startPosition, LevelData data) {
        super(startPosition, data);
    }
}
