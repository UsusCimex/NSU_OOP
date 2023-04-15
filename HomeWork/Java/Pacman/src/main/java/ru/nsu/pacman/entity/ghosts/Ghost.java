package ru.nsu.pacman.entity.ghosts;

import ru.nsu.pacman.data.GameData;
import ru.nsu.pacman.entity.Entity;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Random;

public abstract class Ghost extends Entity {
    public Ghost(GameData.Coordinates startPosition, LevelData data) {
        super(startPosition, data);
    }
    public static void changeAllStates(ArrayList<GameData.EntityData> entities, GhostState newState) {
        for (GameData.EntityData entity : entities) {
            if (entity.body instanceof Ghost) {
                Ghost ghost = (Ghost) entity.body;
                ghost.setState(newState);
            }
        }
    }

    public enum GhostState {
        DEFAULT,
        SCARED,
        DEAD
    }
    protected GhostState state = GhostState.DEFAULT;
    public GhostState getState() {
        return state;
    }
    public void setState(GhostState newState) {
        state = newState;
    }
    protected ArrayList<GameData.Orientation> getAvailableOrientations() {
        ArrayList<GameData.Orientation> availableOrientations = new ArrayList<>();
        if (isStopBlock(data.getValueLevelData(new GameData.Coordinates(cellPosition.x - 1, cellPosition.y)))) {
            availableOrientations.add(GameData.Orientation.LEFT);
        }
        if (isStopBlock(data.getValueLevelData(new GameData.Coordinates(cellPosition.x + 1, cellPosition.y)))) {
            availableOrientations.add(GameData.Orientation.RIGHT);
        }
        if (isStopBlock(data.getValueLevelData(new GameData.Coordinates(cellPosition.x, cellPosition.y - 1)))) {
            availableOrientations.add(GameData.Orientation.UP);
        }
        if (isStopBlock(data.getValueLevelData(new GameData.Coordinates(cellPosition.x, cellPosition.y + 1)))) {
            availableOrientations.add(GameData.Orientation.DOWN);
        }
        if (availableOrientations.size() == 0) {
            availableOrientations.add(GameData.Orientation.NONE);
        }
        return availableOrientations;
    }
    protected GameData.Orientation getRandomOrientation() {
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
