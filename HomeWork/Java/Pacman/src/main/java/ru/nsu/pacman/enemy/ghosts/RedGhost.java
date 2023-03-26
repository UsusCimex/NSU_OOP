package ru.nsu.pacman.enemy.ghosts;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.PacmanGame;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;
import static ru.nsu.pacman.PacmanGame.CELL_SIZE;
import static ru.nsu.pacman.PacmanGame.CELL_N;
import static ru.nsu.pacman.PacmanGame.Coordinates;
import static ru.nsu.pacman.PacmanGame.Orientation;

public class RedGhost implements Enemy {
    private GridPane area = null;
    private LevelData data = null;
    private PacmanGame.Orientation curentOrientation = PacmanGame.Orientation.NONE;
    private PacmanGame.Orientation nextOrientation = PacmanGame.Orientation.NONE;
    private PacmanGame.Coordinates position;
    private PacmanGame.Coordinates cellPosition;
    private double speed = 1.5;
    @Override
    public void changeNextOrientation(PacmanGame.Orientation orientation) {
        nextOrientation = orientation;
    }
    public void changeCurrentOrientation() {
        curentOrientation = nextOrientation;
        nextOrientation = PacmanGame.Orientation.NONE;
    }
    @Override
    public PacmanGame.Orientation getCurrentOrientation() {
        return curentOrientation;
    }
    public PacmanGame.Orientation getNextOrientation() {
        return nextOrientation;
    }

    public RedGhost(PacmanGame.Coordinates startPosition, GridPane area, LevelData data) {
        this.cellPosition = startPosition;
        this.position = new Coordinates(startPosition.x * CELL_SIZE, startPosition.y * CELL_SIZE);

        this.area = area;
        this.data = data;
    }
    private boolean ghostInNewCell() {
        return ( abs(position.x - (cellPosition.x * CELL_SIZE)) >= CELL_SIZE ||
                abs(position.y - (cellPosition.y * CELL_SIZE)) >= CELL_SIZE);
    }

    private boolean ghostCanMove() {
        if ((curentOrientation == PacmanGame.Orientation.UP) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.LEFT) && (data.getValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.RIGHT) && (data.getValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.DOWN) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1)) != LevelData.Symbols.Wall)) {
            return true;
        }

        if (((cellPosition.x * CELL_SIZE - position.x) != 0) || ((cellPosition.y * CELL_SIZE - position.y) != 0)) {
            return true;
        }

        return false;
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
    public Coordinates getPosition() {
        return position;
    }

    public boolean ghostInBorder() {
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

    private void setPosition(Coordinates newPosition) {
        cellPosition = newPosition;
        position = new Coordinates(newPosition.x * CELL_SIZE, newPosition.y * CELL_SIZE);
    }

    @Override
    public void move() {
        if (curentOrientation == Orientation.NONE) {
            curentOrientation = getRandomOrientation();
        }

        if (ghostInNewCell()) {
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
//                if (nextOrientation == curentOrientation) {
//                    curentOrientation = getRandomOrientation();
//                }
            }

            if (!ghostCanMove()) {
                curentOrientation = randomOrientation;
            }
        }

        if (ghostInBorder()) {
            //Make after Animation go to for the board
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
