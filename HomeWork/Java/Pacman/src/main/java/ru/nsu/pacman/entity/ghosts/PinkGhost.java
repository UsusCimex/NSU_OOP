package ru.nsu.pacman.entity.ghosts;

import ru.nsu.pacman.data.GameData;
import ru.nsu.pacman.entity.Entity;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Random;

import static ru.nsu.pacman.data.GameData.Coordinates;

//Runs randomly, but he moves fast
public class PinkGhost extends Ghost {

    public PinkGhost(Coordinates startPosition, LevelData data) {
        super(startPosition, data);
        speed = 2.2;
    }
    @Override
    public GameData.Orientation getNextOrientation() {
        if (getAvailableOrientations().size() == 2 && currentOrientation != GameData.Orientation.NONE && entityCanMove()) {
            return currentOrientation;
        }
        return getRandomOrientation();
    }
}
