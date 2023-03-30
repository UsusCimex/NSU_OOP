package ru.nsu.pacman.enemy.ghosts;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.PacmanGame;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Random;

import static ru.nsu.pacman.PacmanGame.CELL_N;
import static ru.nsu.pacman.PacmanGame.Coordinates;
import static ru.nsu.pacman.PacmanGame.Orientation;

public class RedGhost extends Enemy {
    public RedGhost(Coordinates startPosition, GridPane area, LevelData data) {
        super(startPosition, area, data);
        speed = 1.5;
    }

    private ArrayList<Orientation> getAvailableOrientations() {
        ArrayList<Orientation> availableOrientations = new ArrayList<>();
        if (data.getValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y)) != LevelData.Symbols.Wall) {
            availableOrientations.add(Orientation.LEFT);
        }
        if (data.getValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y)) != LevelData.Symbols.Wall) {
            availableOrientations.add(Orientation.RIGHT);
        }
        if (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1)) != LevelData.Symbols.Wall) {
            availableOrientations.add(Orientation.UP);
        }
        if (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1)) != LevelData.Symbols.Wall) {
            availableOrientations.add(Orientation.DOWN);
        }
        return availableOrientations;
    }
    private Orientation getRandomOrientation() {
        ArrayList<Orientation> availableOrientations = getAvailableOrientations();
        Random random = new Random();
        return availableOrientations.get(random.nextInt(availableOrientations.size()));
    }

    @Override
    public void move() {
        if (curentOrientation == Orientation.NONE) {
            curentOrientation = getRandomOrientation();
        }

        if (enemyInNewCell()) {
            Coordinates newPosition = null;
            if (getCurrentOrientation() == Orientation.UP) {
                newPosition = new Coordinates(cellPosition.x, cellPosition.y - 1);
            } else if (getCurrentOrientation() == Orientation.LEFT) {
                newPosition = new Coordinates(cellPosition.x - 1, cellPosition.y);
            } else if (getCurrentOrientation() == Orientation.RIGHT) {
                newPosition = new Coordinates(cellPosition.x + 1, cellPosition.y);
            } else if (getCurrentOrientation() == Orientation.DOWN) {
                newPosition = new Coordinates(cellPosition.x, cellPosition.y + 1);
            }
            setPosition(newPosition);

            Orientation randomOrientation = getRandomOrientation();
            if (getAvailableOrientations().size() != 2) {
                curentOrientation = randomOrientation;
            }

            if (!enemyCanMove()) {
                curentOrientation = randomOrientation;
            }
        }

        if (enemyInBorder()) {
            if (getCurrentOrientation() == Orientation.RIGHT) {
                setPosition(new Coordinates(0, cellPosition.y));
            } else if (getCurrentOrientation() == Orientation.LEFT) {
                setPosition(new Coordinates(CELL_N - 1, cellPosition.y));
            } else if (getCurrentOrientation() == Orientation.UP) {
                setPosition(new Coordinates(cellPosition.x, CELL_N - 1));
            } else if (getCurrentOrientation() == Orientation.DOWN) {
                setPosition(new Coordinates(cellPosition.x, 0));
            }
        }

        if (curentOrientation == PacmanGame.Orientation.UP) {
            position.y -= speed;
        } else if (curentOrientation == PacmanGame.Orientation.RIGHT) {
            position.x += speed;
        } else if (curentOrientation == PacmanGame.Orientation.DOWN) {
            position.y += speed;
        } else if (curentOrientation == PacmanGame.Orientation.LEFT) {
            position.x -= speed;
        }
    }
}
