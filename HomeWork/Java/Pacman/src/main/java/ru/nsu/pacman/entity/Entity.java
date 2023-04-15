package ru.nsu.pacman.entity;

import ru.nsu.pacman.data.GameData;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;

import static java.lang.Math.*;
import static ru.nsu.pacman.Game.CELL_N;
import static ru.nsu.pacman.Game.CELL_SIZE;
import static ru.nsu.pacman.data.GameData.Coordinates;

public abstract class Entity {
    protected LevelData data;
    protected GameData.Orientation currentOrientation = GameData.Orientation.NONE;
    protected GameData.Orientation nextOrientation = GameData.Orientation.NONE;
    protected double speed = 1;
    protected Coordinates position;
    protected Coordinates cellPosition;

    public Entity(Coordinates startPosition, LevelData data) {
        this.cellPosition = startPosition;
        this.position = new Coordinates(startPosition.x * CELL_SIZE, startPosition.y * CELL_SIZE);

        this.data = data;
    }

    public Coordinates getPosition() {
        return  position;
    }
    public void setPosition(Coordinates newPosition) {
        cellPosition = newPosition;
        position = new Coordinates(newPosition.x * CELL_SIZE, newPosition.y * CELL_SIZE);
    }
    public GameData.Orientation getCurrentOrientation() {
        return currentOrientation;
    }
    public void changeCurrentOrientation() {
        currentOrientation = nextOrientation;
        nextOrientation = GameData.Orientation.NONE;
    }
    public GameData.Orientation getNextOrientation() {
        return nextOrientation;
    }
    public void changeNextOrientation(GameData.Orientation newOrientation) {
        nextOrientation = newOrientation;
    }
    public boolean entityInNewCell() {
        return ( abs(position.x - (cellPosition.x * CELL_SIZE)) >= CELL_SIZE ||
                abs(position.y - (cellPosition.y * CELL_SIZE)) >= CELL_SIZE);
    }
    private boolean isStopBlock(LevelData.Symbols symbol) {
        if (symbol == LevelData.Symbols.Wall) return false;
        else return symbol != LevelData.Symbols.Barrier;
    }
    public boolean entityCanMove() {
        if ((currentOrientation == GameData.Orientation.UP) && isStopBlock(data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1)))) {
            return true;
        } else if ((currentOrientation == GameData.Orientation.LEFT) && isStopBlock(data.getValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y)))) {
            return true;
        } else if ((currentOrientation == GameData.Orientation.RIGHT) && isStopBlock(data.getValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y)))) {
            return true;
        } else if ((currentOrientation == GameData.Orientation.DOWN) && isStopBlock(data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1)))) {
            return true;
        }

        return ((cellPosition.x * CELL_SIZE - position.x) != 0) || ((cellPosition.y * CELL_SIZE - position.y) != 0);
    }
    public boolean entityCanRotate() {
        if ((nextOrientation == GameData.Orientation.UP) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == GameData.Orientation.LEFT) && (data.getValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == GameData.Orientation.RIGHT) && (data.getValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else return (nextOrientation == GameData.Orientation.DOWN) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1)) != LevelData.Symbols.Wall);
    }
    public boolean entityInBorder() {
        if ((currentOrientation == GameData.Orientation.LEFT) && ((int)cellPosition.x == 0)) {
            return true;
        } else if ((currentOrientation == GameData.Orientation.UP) && ((int)cellPosition.y == 0)) {
            return true;
        } else if ((currentOrientation == GameData.Orientation.RIGHT) && ((int)cellPosition.x == CELL_N - 1)) {
            return true;
        } else return (currentOrientation == GameData.Orientation.DOWN) && ((int) cellPosition.y == CELL_N - 1);
    }
    protected ArrayList<GameData.Orientation> getAvailableOrientations() {
        ArrayList<GameData.Orientation> availableOrientations = new ArrayList<>();
        if (isStopBlock(data.getValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y)))) {
            availableOrientations.add(GameData.Orientation.LEFT);
        }
        if (isStopBlock(data.getValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y)))) {
            availableOrientations.add(GameData.Orientation.RIGHT);
        }
        if (isStopBlock(data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1)))) {
            availableOrientations.add(GameData.Orientation.UP);
        }
        if (isStopBlock(data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1)))) {
            availableOrientations.add(GameData.Orientation.DOWN);
        }
        if (availableOrientations.size() == 0) {
            availableOrientations.add(GameData.Orientation.NONE);
        }
        return availableOrientations;
    }
    public double getDistanceTo(Entity entity) {
        return sqrt(pow(getPosition().x - entity.getPosition().x, 2) + pow(getPosition().y - entity.getPosition().y, 2));
    }
    public void move() {
        if (currentOrientation == GameData.Orientation.NONE) {
            currentOrientation = getNextOrientation();
        }

        if (entityInNewCell()) {
            if (getCurrentOrientation() == GameData.Orientation.UP) {
                setPosition(new Coordinates(cellPosition.x, cellPosition.y - 1));
            } else if (getCurrentOrientation() == GameData.Orientation.LEFT) {
                setPosition(new Coordinates(cellPosition.x - 1, cellPosition.y));
            } else if (getCurrentOrientation() == GameData.Orientation.RIGHT) {
                setPosition(new Coordinates(cellPosition.x + 1, cellPosition.y));
            } else if (getCurrentOrientation() == GameData.Orientation.DOWN) {
                setPosition(new Coordinates(cellPosition.x, cellPosition.y + 1));
            }
            currentOrientation = getNextOrientation();
        }

        if (entityInBorder()) {
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

        if (currentOrientation == GameData.Orientation.UP) {
            position.y -= speed;
        } else if (currentOrientation == GameData.Orientation.RIGHT) {
            position.x += speed;
        } else if (currentOrientation == GameData.Orientation.DOWN) {
            position.y += speed;
        } else if (currentOrientation == GameData.Orientation.LEFT) {
            position.x -= speed;
        }
    }
}
