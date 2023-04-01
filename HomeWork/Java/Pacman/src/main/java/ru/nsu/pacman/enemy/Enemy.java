package ru.nsu.pacman.enemy;

import ru.nsu.pacman.GameData;
import ru.nsu.pacman.generation.LevelData;

import static java.lang.Math.abs;
import static ru.nsu.pacman.Game.CELL_N;
import static ru.nsu.pacman.Game.CELL_SIZE;
import static ru.nsu.pacman.GameData.Coordinates;

public abstract class Enemy {
    protected LevelData data = null;
    protected GameData.Orientation curentOrientation = GameData.Orientation.NONE;
    protected GameData.Orientation nextOrientation = GameData.Orientation.NONE;
    protected double speed = 1;
    protected Coordinates position;
    protected Coordinates cellPosition;

    public Enemy(Coordinates startPosition, LevelData data) {
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
        return curentOrientation;
    }
    public void changeCurrentOrientation() {
        curentOrientation = nextOrientation;
        nextOrientation = GameData.Orientation.NONE;
    }
    public GameData.Orientation getNextOrientation() {
        return nextOrientation;
    }
    public void changeNextOrientation(GameData.Orientation newOrientation) {
        nextOrientation = newOrientation;
    }
    public boolean enemyInNewCell() {
        return ( abs(position.x - (cellPosition.x * CELL_SIZE)) >= CELL_SIZE ||
                abs(position.y - (cellPosition.y * CELL_SIZE)) >= CELL_SIZE);
    }
    public boolean enemyCanMove() {
        if ((curentOrientation == GameData.Orientation.UP) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == GameData.Orientation.LEFT) && (data.getValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == GameData.Orientation.RIGHT) && (data.getValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == GameData.Orientation.DOWN) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1)) != LevelData.Symbols.Wall)) {
            return true;
        }

        if (((cellPosition.x * CELL_SIZE - position.x) != 0) || ((cellPosition.y * CELL_SIZE - position.y) != 0)) {
            return true;
        }

        return false;
    }
    public boolean enemyCanRotate() {
        if ((nextOrientation == GameData.Orientation.UP) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == GameData.Orientation.LEFT) && (data.getValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == GameData.Orientation.RIGHT) && (data.getValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == GameData.Orientation.DOWN) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1)) != LevelData.Symbols.Wall)) {
            return true;
        } else {
            return false;
        }
    }
    public boolean enemyInBorder() {
        if ((curentOrientation == GameData.Orientation.LEFT) && ((int)cellPosition.x == 0)) {
            return true;
        } else if ((curentOrientation == GameData.Orientation.UP) && ((int)cellPosition.y == 0)) {
            return true;
        } else if ((curentOrientation == GameData.Orientation.RIGHT) && ((int)cellPosition.x == CELL_N - 1)) {
            return true;
        } else if ((curentOrientation == GameData.Orientation.DOWN) && ((int)cellPosition.y == CELL_N - 1)) {
            return true;
        }
        return false;
    }
    public void move() {
        if (enemyCanMove()) {
            if (curentOrientation == GameData.Orientation.UP) {
                position.y -= speed;
            } else if (curentOrientation == GameData.Orientation.RIGHT) {
                position.x += speed;
            } else if (curentOrientation == GameData.Orientation.DOWN) {
                position.y += speed;
            } else if (curentOrientation == GameData.Orientation.LEFT) {
                position.x -= speed;
            }
        } else {
            changeCurrentOrientation();
        }
    }
}
