package ru.nsu.pacman.entity;

import ru.nsu.pacman.generation.LevelData;

import static ru.nsu.pacman.Game.CELL_SIZE;
import static ru.nsu.pacman.Game.CELL_N;
import static ru.nsu.pacman.data.GameData.Coordinates;
import static ru.nsu.pacman.data.GameData.Orientation;

public class Pacman extends Entity {
    private final double distanceToEatFood = 12;

    public Pacman(Coordinates startPosition, LevelData data) {
        super(startPosition, data);
        speed = 2.5;
    }

    private boolean pacmanCanEatFood() {
        if (entityInBorder()) {
            return false;
        }
        if (getCurrentOrientation() == Orientation.LEFT && ((position.x - (cellPosition.x - 1) * CELL_SIZE) <= distanceToEatFood)) {
            return data.getValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y)) == LevelData.Symbols.Food;
        } else if (getCurrentOrientation() == Orientation.RIGHT && (((cellPosition.x + 1) * CELL_SIZE - position.x) <= distanceToEatFood)) {
            return data.getValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y)) == LevelData.Symbols.Food;
        } else if (getCurrentOrientation() == Orientation.UP && ((position.y - (cellPosition.y - 1) * CELL_SIZE) <= distanceToEatFood)) {
            return data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1)) == LevelData.Symbols.Food;
        } else if (getCurrentOrientation() == Orientation.DOWN && (((cellPosition.y + 1) * CELL_SIZE - position.y) <= distanceToEatFood)) {
            return data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1)) == LevelData.Symbols.Food;
        } else {
            return false;
        }
    }
    @Override
    public void move() {
        if (entityCanMove()) {
            if (currentOrientation == Orientation.UP) {
                position.y -= speed;
            }
            else if (currentOrientation == Orientation.RIGHT) {
                position.x += speed;
            }
            else if (currentOrientation == Orientation.DOWN) {
                position.y += speed;
            }
            else if (currentOrientation == Orientation.LEFT) {
                position.x -= speed;
            }
        } else {
            changeCurrentOrientation();
            if (!entityCanMove()) {
                currentOrientation = Orientation.NONE;
            }
        }

        if (pacmanCanEatFood()) {
            if (getCurrentOrientation() == Orientation.LEFT) {
                data.eatFood(new Coordinates(cellPosition.x - 1, cellPosition.y));
            } else if (getCurrentOrientation() == Orientation.UP) {
                data.eatFood(new Coordinates(cellPosition.x, cellPosition.y - 1));
            } else if (getCurrentOrientation() == Orientation.RIGHT) {
                data.eatFood(new Coordinates(cellPosition.x + 1, cellPosition.y));
            } else if (getCurrentOrientation() == Orientation.DOWN) {
                data.eatFood(new Coordinates(cellPosition.x, cellPosition.y + 1));
            }
        }

        if (entityInNewCell() && getCurrentOrientation() != Orientation.NONE) {
            if (getCurrentOrientation() == Orientation.UP) {
                setPosition(new Coordinates(cellPosition.x, cellPosition.y - 1));
            } else if (getCurrentOrientation() == Orientation.LEFT) {
                setPosition(new Coordinates(cellPosition.x - 1, cellPosition.y));
            } else if (getCurrentOrientation() == Orientation.RIGHT) {
                setPosition(new Coordinates(cellPosition.x + 1, cellPosition.y));
            } else if (getCurrentOrientation() == Orientation.DOWN) {
                setPosition(new Coordinates(cellPosition.x, cellPosition.y + 1));
            }

            if (entityCanRotate()) {
                changeCurrentOrientation();
            }
        }

        if (getCurrentOrientation() == Orientation.UP && getNextOrientation() == Orientation.DOWN ||
            getCurrentOrientation() == Orientation.LEFT && getNextOrientation() == Orientation.RIGHT ||
            getCurrentOrientation() == Orientation.RIGHT && getNextOrientation() == Orientation.LEFT ||
            getCurrentOrientation() == Orientation.DOWN && getNextOrientation() == Orientation.UP) {
            changeCurrentOrientation();
        }

        if (entityInBorder()) {
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
    }
}
