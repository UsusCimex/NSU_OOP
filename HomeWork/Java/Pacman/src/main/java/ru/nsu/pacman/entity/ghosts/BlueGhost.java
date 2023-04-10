package ru.nsu.pacman.entity.ghosts;

import ru.nsu.pacman.GameData;
import ru.nsu.pacman.entity.Entity;
import ru.nsu.pacman.entity.Pacman;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Random;

import static ru.nsu.pacman.GameData.Coordinates;
//Tries to get closer to Pacman, but keeps a long distance
public class BlueGhost extends Entity {
    private final double longSightedness = 160;
    public BlueGhost(Coordinates startPosition, LevelData data) {
        super(startPosition, data);
        speed = 1.94;
    }
    @Override
    public GameData.Orientation getNextOrientation() {
        if (getAvailableOrientations().size() == 2 && currentOrientation != GameData.Orientation.NONE && entityCanMove()) {
            return currentOrientation;
        }

        ArrayList<GameData.Orientation> availableOrientations = getAvailableOrientations();
        if (availableOrientations.size() == 0) {
            return GameData.Orientation.NONE;
        }

        if (getDistanceTo(data.getPacman().body) < longSightedness) {
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

        ArrayList<GameData.Orientation> priorityOrientations = new ArrayList<>();
        Pacman pacman = (Pacman) data.getPacman().body;
        if (pacman.getPosition().x - getPosition().x > 0) {
            priorityOrientations.add(GameData.Orientation.RIGHT);
        } else {
            priorityOrientations.add(GameData.Orientation.LEFT);
        }
        if (pacman.getPosition().y - getPosition().y > 0) {
            priorityOrientations.add(GameData.Orientation.DOWN);
        } else {
            priorityOrientations.add(GameData.Orientation.UP);
        }

        priorityOrientations.retainAll(availableOrientations);
        Random random = new Random();
        if (priorityOrientations.size() != 0) {
            return priorityOrientations.get(random.nextInt(priorityOrientations.size()));
        } else {
            return availableOrientations.get(random.nextInt(availableOrientations.size()));
        }
    }
}
