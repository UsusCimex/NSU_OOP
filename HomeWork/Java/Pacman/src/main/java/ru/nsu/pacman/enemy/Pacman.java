package ru.nsu.pacman.enemy;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import ru.nsu.pacman.PacmanGame;
import ru.nsu.pacman.generation.LevelData;

import static java.lang.Math.abs;
import static ru.nsu.pacman.PacmanGame.CELL_SIZE;
import static ru.nsu.pacman.PacmanGame.CELL_N;
import static ru.nsu.pacman.PacmanGame.Coordinates;
import static ru.nsu.pacman.PacmanGame.Orientation;

public class Pacman implements Enemy {
    private GridPane area = null;
    private LevelData data = null;
    private int foodEat = 0;
    private double distanceToEatFood = 12;
    private PacmanGame.Orientation curentOrientation = PacmanGame.Orientation.NONE;
    private PacmanGame.Orientation nextOrientation = PacmanGame.Orientation.NONE;
    private double speed = 2.5;
    private Coordinates position;
    private Coordinates cellPosition;
    public Pacman(PacmanGame.Coordinates startPosition, GridPane area, LevelData data) {
        this.cellPosition = startPosition;
        this.position = new Coordinates(startPosition.x * CELL_SIZE, startPosition.y * CELL_SIZE);

        this.area = area;
        this.data = data;
    }
    @Override
    public PacmanGame.Coordinates getPosition() {
        return position;
    }
    public void setPosition(PacmanGame.Coordinates newPosition) {
        cellPosition = newPosition;
        position = new Coordinates(newPosition.x * CELL_SIZE, newPosition.y * CELL_SIZE);
    }

    public int getFoodEat() {
        return foodEat;
    }
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

    public boolean pacmanInNewCell() {
        return ( abs(position.x - (cellPosition.x * CELL_SIZE)) >= CELL_SIZE ||
                abs(position.y - (cellPosition.y * CELL_SIZE)) >= CELL_SIZE);
    }

    public boolean pacmanCanRotate() {
        if ((nextOrientation == PacmanGame.Orientation.UP) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == PacmanGame.Orientation.LEFT) && (data.getValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == PacmanGame.Orientation.RIGHT) && (data.getValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y)) != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == PacmanGame.Orientation.DOWN) && (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1)) != LevelData.Symbols.Wall)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pacmanCanMove() {
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

    public boolean pacmanInBorder() {
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

    private void removeNodeFromArea(Coordinates cord) {
        for (Node node : area.getChildren()) {
            if (GridPane.getColumnIndex(node) == (int)cord.x && GridPane.getRowIndex(node) == (int)cord.y) {
                area.getChildren().remove(node);
                break;
            }
        }
    }

    private boolean pacmanCanEatFood() {
        if (pacmanInBorder()) return false;
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
        if (pacmanCanMove()) {
            if (curentOrientation == PacmanGame.Orientation.UP) {
                position.y -= speed;
            }
            else if (curentOrientation == PacmanGame.Orientation.RIGHT) {
                position.x += speed;
            }
            else if (curentOrientation == PacmanGame.Orientation.DOWN) {
                position.y += speed;
            }
            else if (curentOrientation == PacmanGame.Orientation.LEFT) {
                position.x -= speed;
            }
        } else {
            changeCurrentOrientation();
        }

        if (pacmanCanEatFood()) {
            System.out.println("I'm eat " + foodEat);
            if (getCurrentOrientation() == Orientation.LEFT) {
                removeNodeFromArea(new Coordinates(cellPosition.x - 1, cellPosition.y));
                data.setValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y), LevelData.Symbols.Empty);
            } else if (getCurrentOrientation() == Orientation.UP) {
                removeNodeFromArea(new Coordinates(cellPosition.x, cellPosition.y - 1));
                data.setValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1), LevelData.Symbols.Empty);
            } else if (getCurrentOrientation() == Orientation.RIGHT) {
                removeNodeFromArea(new Coordinates(cellPosition.x + 1, cellPosition.y));
                data.setValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y), LevelData.Symbols.Empty);
            } else if (getCurrentOrientation() == Orientation.DOWN) {
                removeNodeFromArea(new Coordinates(cellPosition.x, cellPosition.y + 1));
                data.setValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1), LevelData.Symbols.Empty);
            }
            foodEat += 1;
        }

        if (pacmanInNewCell() && getCurrentOrientation() != Orientation.NONE) {
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
            data.setValueLevelData(cellPosition, LevelData.Symbols.Empty);
            data.setValueLevelData(newPosition, LevelData.Symbols.Pacman);
            if (pacmanCanRotate()) {
                changeCurrentOrientation();
            }
        }

        if (getCurrentOrientation() == Orientation.UP && getNextOrientation() == Orientation.DOWN ||
            getCurrentOrientation() == Orientation.LEFT && getNextOrientation() == Orientation.RIGHT ||
            getCurrentOrientation() == Orientation.RIGHT && getNextOrientation() == Orientation.LEFT ||
            getCurrentOrientation() == Orientation.DOWN && getNextOrientation() == Orientation.UP) {
            changeCurrentOrientation();
        }

        if (pacmanInBorder()) {
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
    }
}
