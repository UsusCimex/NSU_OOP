package ru.nsu.pacman.enemy;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.Controller;
import ru.nsu.pacman.generation.LevelData;

import static java.lang.Math.abs;
import static ru.nsu.pacman.Game.CELL_N;
import static ru.nsu.pacman.Game.CELL_SIZE;
import static ru.nsu.pacman.Controller.Coordinates;

public abstract class Enemy {
    protected LevelData data = null;
    protected Controller.Orientation curentOrientation = Controller.Orientation.NONE;
    protected Controller.Orientation nextOrientation = Controller.Orientation.NONE;
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
    public Controller.Orientation getCurrentOrientation() {
        return curentOrientation;
    }
    public void changeCurrentOrientation() {
        curentOrientation = nextOrientation;
        nextOrientation = Controller.Orientation.NONE;
    }
    public Controller.Orientation getNextOrientation() {
        return nextOrientation;
    }
    public void changeNextOrientation(Controller.Orientation newOrientation) {
        nextOrientation = newOrientation;
    }
    public boolean enemyInNewCell() {
        return ( abs(position.x - (cellPosition.x * CELL_SIZE)) >= CELL_SIZE ||
                abs(position.y - (cellPosition.y * CELL_SIZE)) >= CELL_SIZE);
    }
    public boolean enemyCanMove() {
        if ((curentOrientation == Controller.Orientation.UP) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == Controller.Orientation.LEFT) && (data.getValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == Controller.Orientation.RIGHT) && (data.getValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == Controller.Orientation.DOWN) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1)) != LevelData.Symbols.Wall)) {
            return true;
        }

        if (((cellPosition.x * CELL_SIZE - position.x) != 0) || ((cellPosition.y * CELL_SIZE - position.y) != 0)) {
            return true;
        }

        return false;
    }
    public boolean enemyCanRotate() {
        if ((nextOrientation == Controller.Orientation.UP) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == Controller.Orientation.LEFT) && (data.getValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == Controller.Orientation.RIGHT) && (data.getValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == Controller.Orientation.DOWN) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1)) != LevelData.Symbols.Wall)) {
            return true;
        } else {
            return false;
        }
    }
    public boolean enemyInBorder() {
        if ((curentOrientation == Controller.Orientation.LEFT) && ((int)cellPosition.x == 0)) {
            return true;
        } else if ((curentOrientation == Controller.Orientation.UP) && ((int)cellPosition.y == 0)) {
            return true;
        } else if ((curentOrientation == Controller.Orientation.RIGHT) && ((int)cellPosition.x == CELL_N - 1)) {
            return true;
        } else if ((curentOrientation == Controller.Orientation.DOWN) && ((int)cellPosition.y == CELL_N - 1)) {
            return true;
        }
        return false;
    }
    public void move() {
        if (enemyCanMove()) {
            if (curentOrientation == Controller.Orientation.UP) {
                position.y -= speed;
            } else if (curentOrientation == Controller.Orientation.RIGHT) {
                position.x += speed;
            } else if (curentOrientation == Controller.Orientation.DOWN) {
                position.y += speed;
            } else if (curentOrientation == Controller.Orientation.LEFT) {
                position.x -= speed;
            }
        } else {
            changeCurrentOrientation();
        }
    }
}
