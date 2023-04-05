package ru.nsu.pacman.entity.ghosts;

import ru.nsu.pacman.GameData;
import ru.nsu.pacman.entity.Entity;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Random;

import static ru.nsu.pacman.GameData.Coordinates;

//Runs randomly, but he moves fast
public class PinkGhost extends Entity {

    public PinkGhost(Coordinates startPosition, LevelData data) {
        super(startPosition, data);
        speed = 2.2;
    }
    @Override
    public GameData.Orientation getNextOrientation() {
        if (getAvailableOrientations().size() == 2 && currentOrientation != GameData.Orientation.NONE && entityCanMove()) {
            return currentOrientation;
        }

        ArrayList<GameData.Orientation> availableOrientations = getAvailableOrientations();
        Random random = new Random();
        nextOrientation = availableOrientations.get(random.nextInt(availableOrientations.size()));

        if (nextOrientation == GameData.Orientation.UP && currentOrientation == GameData.Orientation.DOWN ||
            nextOrientation == GameData.Orientation.DOWN && currentOrientation == GameData.Orientation.UP ||
            nextOrientation == GameData.Orientation.LEFT && currentOrientation == GameData.Orientation.RIGHT ||
            nextOrientation == GameData.Orientation.RIGHT && currentOrientation == GameData.Orientation.LEFT) {
            return availableOrientations.get(random.nextInt(availableOrientations.size()));
        }

        return nextOrientation;
    }
}
