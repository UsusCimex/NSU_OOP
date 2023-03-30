package ru.nsu.pacman.enemy;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.PacmanGame;
import ru.nsu.pacman.generation.LevelData;

import static java.lang.Math.abs;
import static ru.nsu.pacman.PacmanGame.CELL_N;
import static ru.nsu.pacman.PacmanGame.CELL_SIZE;

public abstract class Enemy {
    protected GridPane area = null;
    protected LevelData data = null;
    protected PacmanGame.Orientation curentOrientation = PacmanGame.Orientation.NONE;
    protected PacmanGame.Orientation nextOrientation = PacmanGame.Orientation.NONE;
    protected double speed = 1;
    protected PacmanGame.Coordinates position;
    protected PacmanGame.Coordinates cellPosition;

    public Enemy(PacmanGame.Coordinates startPosition, GridPane area, LevelData data) {
        this.cellPosition = startPosition;
        this.position = new PacmanGame.Coordinates(startPosition.x * CELL_SIZE, startPosition.y * CELL_SIZE);

        this.area = area;
        this.data = data;
    }

    public PacmanGame.Coordinates getPosition() {
        return  position;
    }
    public void setPosition(PacmanGame.Coordinates newPosition) {
        cellPosition = newPosition;
        position = new PacmanGame.Coordinates(newPosition.x * CELL_SIZE, newPosition.y * CELL_SIZE);
    }
    public PacmanGame.Orientation getCurrentOrientation() {
        return curentOrientation;
    }
    public void changeCurrentOrientation() {
        curentOrientation = nextOrientation;
        nextOrientation = PacmanGame.Orientation.NONE;
    }
    public PacmanGame.Orientation getNextOrientation() {
        return nextOrientation;
    }
    public void changeNextOrientation(PacmanGame.Orientation newOrientation) {
        nextOrientation = newOrientation;
    }
    public boolean enemyInNewCell() {
        return ( abs(position.x - (cellPosition.x * CELL_SIZE)) >= CELL_SIZE ||
                abs(position.y - (cellPosition.y * CELL_SIZE)) >= CELL_SIZE);
    }
    public boolean enemyCanMove() {
        if ((curentOrientation == PacmanGame.Orientation.UP) && (data.getValueLevelData(new PacmanGame.Coordinates(cellPosition.x, cellPosition.y - 1)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.LEFT) && (data.getValueLevelData(new PacmanGame.Coordinates(cellPosition.x - 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.RIGHT) && (data.getValueLevelData(new PacmanGame.Coordinates(cellPosition.x + 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.DOWN) && (data.getValueLevelData(new PacmanGame.Coordinates(cellPosition.x, cellPosition.y + 1)) != LevelData.Symbols.Wall)) {
            return true;
        }

        if (((cellPosition.x * CELL_SIZE - position.x) != 0) || ((cellPosition.y * CELL_SIZE - position.y) != 0)) {
            return true;
        }

        return false;
    }
    public boolean enemyCanRotate() {
        if ((nextOrientation == PacmanGame.Orientation.UP) && (data.getValueLevelData(new PacmanGame.Coordinates(cellPosition.x, cellPosition.y - 1)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == PacmanGame.Orientation.LEFT) && (data.getValueLevelData(new PacmanGame.Coordinates(cellPosition.x - 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == PacmanGame.Orientation.RIGHT) && (data.getValueLevelData(new PacmanGame.Coordinates(cellPosition.x + 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == PacmanGame.Orientation.DOWN) && (data.getValueLevelData(new PacmanGame.Coordinates(cellPosition.x, cellPosition.y + 1)) != LevelData.Symbols.Wall)) {
            return true;
        } else {
            return false;
        }
    }
    public boolean enemyInBorder() {
        if ((curentOrientation == PacmanGame.Orientation.LEFT) && ((int)cellPosition.x == 0)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.UP) && ((int)cellPosition.y == 0)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.RIGHT) && ((int)cellPosition.x == CELL_N - 1)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.DOWN) && ((int)cellPosition.y == CELL_N - 1)) {
            return true;
        }
        return false;
    }
    public void move() {
        if (enemyCanMove()) {
            if (curentOrientation == PacmanGame.Orientation.UP) {
                position.y -= speed;
            } else if (curentOrientation == PacmanGame.Orientation.RIGHT) {
                position.x += speed;
            } else if (curentOrientation == PacmanGame.Orientation.DOWN) {
                position.y += speed;
            } else if (curentOrientation == PacmanGame.Orientation.LEFT) {
                position.x -= speed;
            }
        } else {
            changeCurrentOrientation();
        }
    }
}
