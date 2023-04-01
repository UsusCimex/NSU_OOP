package ru.nsu.pacman.enemy.ghosts;

import ru.nsu.pacman.GameData;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Random;

import static ru.nsu.pacman.GameData.Coordinates;
import static ru.nsu.pacman.Game.CELL_N;

//Runs randomly, but he moves fast
public class PinkGhost extends Enemy {

    public PinkGhost(Coordinates startPosition, LevelData data) {
        super(startPosition, data);
        speed = 2.5;
    }
    private ArrayList<GameData.Orientation> getAvailableOrientations() {
        ArrayList<GameData.Orientation> availableOrientations = new ArrayList<>();
        if (data.getValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y)) != LevelData.Symbols.Wall) {
            availableOrientations.add(GameData.Orientation.LEFT);
        }
        if (data.getValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y)) != LevelData.Symbols.Wall) {
            availableOrientations.add(GameData.Orientation.RIGHT);
        }
        if (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1)) != LevelData.Symbols.Wall) {
            availableOrientations.add(GameData.Orientation.UP);
        }
        if (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1)) != LevelData.Symbols.Wall) {
            availableOrientations.add(GameData.Orientation.DOWN);
        }
        return availableOrientations;
    }
    private GameData.Orientation getRandomOrientation() {
        ArrayList<GameData.Orientation> availableOrientations = getAvailableOrientations();
        Random random = new Random();
        return availableOrientations.get(random.nextInt(availableOrientations.size()));
    }
    @Override
    public void move() {
        if (curentOrientation == GameData.Orientation.NONE) {
            curentOrientation = getRandomOrientation();
        }

        if (enemyInNewCell()) {
            Coordinates newPosition = null;
            if (getCurrentOrientation() == GameData.Orientation.UP) {
                newPosition = new Coordinates(cellPosition.x, cellPosition.y - 1);
            } else if (getCurrentOrientation() == GameData.Orientation.LEFT) {
                newPosition = new Coordinates(cellPosition.x - 1, cellPosition.y);
            } else if (getCurrentOrientation() == GameData.Orientation.RIGHT) {
                newPosition = new Coordinates(cellPosition.x + 1, cellPosition.y);
            } else if (getCurrentOrientation() == GameData.Orientation.DOWN) {
                newPosition = new Coordinates(cellPosition.x, cellPosition.y + 1);
            }
            setPosition(newPosition);

            GameData.Orientation randomOrientation = getRandomOrientation();
            if (getAvailableOrientations().size() != 2) {
                curentOrientation = randomOrientation;
            }

            if (!enemyCanMove()) {
                curentOrientation = randomOrientation;
            }
        }

        if (enemyInBorder()) {
            if (getCurrentOrientation() == GameData.Orientation.RIGHT) {
                setPosition(new Coordinates(0, cellPosition.y));
            } else if (getCurrentOrientation() == GameData.Orientation.LEFT) {
                setPosition(new Coordinates(CELL_N - 1, cellPosition.y));
            } else if (getCurrentOrientation() == GameData.Orientation.UP) {
                setPosition(new Coordinates(cellPosition.x, CELL_N - 1));
            } else if (getCurrentOrientation() == GameData.Orientation.DOWN) {
                setPosition(new Coordinates(cellPosition.x, 0));
            }
        }

        if (curentOrientation == GameData.Orientation.UP) {
            position.y -= speed;
        } else if (curentOrientation == GameData.Orientation.RIGHT) {
            position.x += speed;
        } else if (curentOrientation == GameData.Orientation.DOWN) {
            position.y += speed;
        } else if (curentOrientation == GameData.Orientation.LEFT) {
            position.x -= speed;
        }
    }
}
