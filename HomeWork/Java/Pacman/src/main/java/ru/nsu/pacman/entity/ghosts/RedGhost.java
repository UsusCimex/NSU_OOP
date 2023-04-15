package ru.nsu.pacman.entity.ghosts;

import ru.nsu.pacman.data.GameData;
import ru.nsu.pacman.entity.Pacman;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Random;

import static ru.nsu.pacman.data.GameData.Coordinates;

//Always running after Pacman
public class RedGhost extends Ghost {
    public RedGhost(Coordinates startPosition, LevelData data) {
        super(startPosition, data);
        speed = 1.8;
    }
    @Override
    public GameData.Orientation getNextOrientation() {
        if (getState() == GhostState.DEFAULT) {
            return defaultAction();
        } else if (getState() == GhostState.SCARED) {
            return scaredAction();
        } else {
            return deadAction();
        }
    }

    private GameData.Orientation deadAction() {
        return defaultAction();
    }

    private GameData.Orientation scaredAction() {
        ArrayList<GameData.Orientation> availableOrientations = getAvailableOrientations();
        if (availableOrientations.size() == 0) {
            return GameData.Orientation.NONE;
        }
        ArrayList<GameData.Orientation> priorityOrientations = new ArrayList<>();
        Pacman pacman = (Pacman) data.getPacman().body;
        if (pacman.getPosition().x - getPosition().x > 0) {
            priorityOrientations.add(GameData.Orientation.LEFT);
        } else {
            priorityOrientations.add(GameData.Orientation.RIGHT);
        }
        if (pacman.getPosition().y - getPosition().y > 0) {
            priorityOrientations.add(GameData.Orientation.UP);
        } else {
            priorityOrientations.add(GameData.Orientation.DOWN);
        }

        priorityOrientations.retainAll(availableOrientations);
        Random random = new Random();
        if (priorityOrientations.size() != 0) {
            return priorityOrientations.get(random.nextInt(priorityOrientations.size()));
        } else {
            return availableOrientations.get(random.nextInt(availableOrientations.size()));
        }
    }

    private GameData.Orientation defaultAction() {
        ArrayList<GameData.Orientation> availableOrientations = getAvailableOrientations();
        if (availableOrientations.size() == 0) {
            return GameData.Orientation.NONE;
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
