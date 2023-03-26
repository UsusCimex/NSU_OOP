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
    private double positionX, positionY;
    public Pacman(PacmanGame.Coordinates startPosition, GridPane area, LevelData data) {
        this.positionX = startPosition.x;
        this.positionY = startPosition.y;

        this.area = area;
        this.data = data;
    }
    public PacmanGame.Coordinates getPosition() {
        return new PacmanGame.Coordinates(positionX, positionY);
    }
    public void setPosition(PacmanGame.Coordinates newPosition) {
        positionX = newPosition.x;
        positionY = newPosition.y;
    }

    public int getFoodEat() {
        return foodEat;
    }
    public void changeNextOrientation(PacmanGame.Orientation orientation) {
        nextOrientation = orientation;
    }
    public void changeCurrentOrientation() {
        curentOrientation = nextOrientation;
        nextOrientation = PacmanGame.Orientation.NONE;
    }
    public PacmanGame.Orientation getCurrentOrientation() {
        return curentOrientation;
    }
    public PacmanGame.Orientation getNextOrientation() {
        return nextOrientation;
    }

    public boolean pacmanInNewCell() {
        return ( abs(positionX - (data.getPacmanPosition().x * CELL_SIZE)) >= CELL_SIZE ||
                abs(positionY - (data.getPacmanPosition().y * CELL_SIZE)) >= CELL_SIZE);
    }

    public boolean pacmanCanRotate() {
        PacmanGame.Coordinates curPosition = data.getPacmanPosition();
        if ((nextOrientation == PacmanGame.Orientation.UP) && (data.getLevelData()[(int)curPosition.x][(int)curPosition.y - 1] != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == PacmanGame.Orientation.LEFT) && (data.getLevelData()[(int)curPosition.x - 1][(int)curPosition.y] != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == PacmanGame.Orientation.RIGHT) && (data.getLevelData()[(int)curPosition.x + 1][(int)curPosition.y] != LevelData.Symbols.Wall)) {
            return true;
        } else if ((nextOrientation == PacmanGame.Orientation.DOWN) && (data.getLevelData()[(int)curPosition.x][(int)curPosition.y + 1] != LevelData.Symbols.Wall)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pacmanCanMove() {
        PacmanGame.Coordinates curPosition = data.getPacmanPosition();
        if ((curentOrientation == PacmanGame.Orientation.UP) && (data.getLevelData()[(int)curPosition.x][(int)curPosition.y - 1] != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.LEFT) && (data.getLevelData()[(int)curPosition.x - 1][(int)curPosition.y] != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.RIGHT) && (data.getLevelData()[(int)curPosition.x + 1][(int)curPosition.y] != LevelData.Symbols.Wall)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.DOWN) && (data.getLevelData()[(int)curPosition.x][(int)curPosition.y + 1] != LevelData.Symbols.Wall)) {
            return true;
        }

        if (((curPosition.x * CELL_SIZE - getPosition().x) != 0) || ((curPosition.y * CELL_SIZE - getPosition().y) != 0)) {
            return true;
        }

        return false;
    }

    public boolean pacmanInBorder() {
        if ((curentOrientation == PacmanGame.Orientation.LEFT) && ((int)data.getPacmanPosition().x == 0)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.UP) && ((int)data.getPacmanPosition().y == 0)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.RIGHT) && ((int)data.getPacmanPosition().x == CELL_N - 1)) {
            return true;
        } else if ((curentOrientation == PacmanGame.Orientation.DOWN) && ((int)data.getPacmanPosition().y == CELL_N - 1)) {
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
        if (getCurrentOrientation() == Orientation.LEFT && ((getPosition().x - (data.getPacmanPosition().x - 1) * CELL_SIZE) <= distanceToEatFood)) {
            return data.getValueLevelData(new Coordinates(data.getPacmanPosition().x - 1, data.getPacmanPosition().y)) == LevelData.Symbols.Food;
        } else if (getCurrentOrientation() == Orientation.RIGHT && (((data.getPacmanPosition().x + 1) * CELL_SIZE - getPosition().x) <= distanceToEatFood)) {
            return data.getValueLevelData(new Coordinates(data.getPacmanPosition().x + 1, data.getPacmanPosition().y)) == LevelData.Symbols.Food;
        } else if (getCurrentOrientation() == Orientation.UP && ((getPosition().y - (data.getPacmanPosition().y - 1) * CELL_SIZE) <= distanceToEatFood)) {
            return data.getValueLevelData(new Coordinates(data.getPacmanPosition().x, data.getPacmanPosition().y - 1)) == LevelData.Symbols.Food;
        } else if (getCurrentOrientation() == Orientation.DOWN && (((data.getPacmanPosition().y + 1) * CELL_SIZE - getPosition().y) <= distanceToEatFood)) {
            return data.getValueLevelData(new Coordinates(data.getPacmanPosition().x, data.getPacmanPosition().y + 1)) == LevelData.Symbols.Food;
        } else {
            return false;
        }
    }
    @Override
    public void move() {
        if (pacmanCanMove()) {
            if (curentOrientation == PacmanGame.Orientation.UP) {
                positionY -= speed;
            }
            else if (curentOrientation == PacmanGame.Orientation.RIGHT) {
                positionX += speed;
            }
            else if (curentOrientation == PacmanGame.Orientation.DOWN) {
                positionY += speed;
            }
            else if (curentOrientation == PacmanGame.Orientation.LEFT) {
                positionX -= speed;
            }
        } else {
            changeCurrentOrientation();
        }

        if (pacmanCanEatFood()) {
            System.out.println("I'm eat " + foodEat);
            if (getCurrentOrientation() == Orientation.LEFT) {
                removeNodeFromArea(new Coordinates(data.getPacmanPosition().x - 1, data.getPacmanPosition().y));
                data.setValueLevelData(new Coordinates(data.getPacmanPosition().x - 1, data.getPacmanPosition().y), LevelData.Symbols.Empty);
            } else if (getCurrentOrientation() == Orientation.UP) {
                removeNodeFromArea(new Coordinates(data.getPacmanPosition().x, data.getPacmanPosition().y - 1));
                data.setValueLevelData(new Coordinates(data.getPacmanPosition().x, data.getPacmanPosition().y - 1), LevelData.Symbols.Empty);
            } else if (getCurrentOrientation() == Orientation.RIGHT) {
                removeNodeFromArea(new Coordinates(data.getPacmanPosition().x + 1, data.getPacmanPosition().y));
                data.setValueLevelData(new Coordinates(data.getPacmanPosition().x + 1, data.getPacmanPosition().y), LevelData.Symbols.Empty);
            } else if (getCurrentOrientation() == Orientation.DOWN) {
                removeNodeFromArea(new Coordinates(data.getPacmanPosition().x, data.getPacmanPosition().y + 1));
                data.setValueLevelData(new Coordinates(data.getPacmanPosition().x, data.getPacmanPosition().y + 1), LevelData.Symbols.Empty);
            }
            foodEat += 1;
        }

        if (pacmanInNewCell() && getCurrentOrientation() != Orientation.NONE) {
            Coordinates oldPosition = data.getPacmanPosition();
            Coordinates newPosition = null;
            if (getCurrentOrientation() == Orientation.UP) {
                newPosition = new Coordinates(oldPosition.x, oldPosition.y - 1);
            } else if (getCurrentOrientation() == Orientation.LEFT) {
                newPosition = new Coordinates(oldPosition.x - 1, oldPosition.y);
            } else if (getCurrentOrientation() == Orientation.RIGHT) {
                newPosition = new Coordinates(oldPosition.x + 1, oldPosition.y);
            } else if (getCurrentOrientation() == Orientation.DOWN) {
                newPosition = new Coordinates(oldPosition.x, oldPosition.y + 1);
            }

            setPosition(new Coordinates(newPosition.x * CELL_SIZE, newPosition.y * CELL_SIZE));
            data.setValueLevelData(oldPosition, LevelData.Symbols.Empty);
            data.setValueLevelData(newPosition, LevelData.Symbols.Pacman);
            data.setPacmanPosition(newPosition);
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
                setPosition(new Coordinates(0, getPosition().y));
                data.setPacmanPosition(new Coordinates(0, data.getPacmanPosition().y));
            } else if (getCurrentOrientation() == Orientation.LEFT) {
                setPosition(new Coordinates((CELL_N - 1) * CELL_SIZE, getPosition().y));
                data.setPacmanPosition(new Coordinates(CELL_N - 1, data.getPacmanPosition().y));
            } else if (getCurrentOrientation() == Orientation.UP) {
                setPosition(new Coordinates(getPosition().x, (CELL_N - 1) * CELL_SIZE));
                data.setPacmanPosition(new Coordinates(data.getPacmanPosition().x, CELL_N - 1));
            } else if (getCurrentOrientation() == Orientation.DOWN) {
                setPosition(new Coordinates(getPosition().x, 0));
                data.setPacmanPosition(new Coordinates(data.getPacmanPosition().x, 0));
            }
        }
    }
}
