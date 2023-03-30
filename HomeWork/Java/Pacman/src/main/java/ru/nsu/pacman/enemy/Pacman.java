package ru.nsu.pacman.enemy;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import ru.nsu.pacman.generation.LevelData;

import static ru.nsu.pacman.Game.CELL_SIZE;
import static ru.nsu.pacman.Game.CELL_N;
import static ru.nsu.pacman.Controller.Coordinates;
import static ru.nsu.pacman.Controller.Orientation;

public class Pacman extends Enemy {
    private double distanceToEatFood = 12;

    public Pacman(Coordinates startPosition, GridPane area, LevelData data) {
        super(startPosition, area, data);
        speed = 2.5;
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
        if (enemyInBorder()) {
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
        if (enemyCanMove()) {
            if (curentOrientation == Orientation.UP) {
                position.y -= speed;
            }
            else if (curentOrientation == Orientation.RIGHT) {
                position.x += speed;
            }
            else if (curentOrientation == Orientation.DOWN) {
                position.y += speed;
            }
            else if (curentOrientation == Orientation.LEFT) {
                position.x -= speed;
            }
        } else {
            changeCurrentOrientation();
        }

        if (pacmanCanEatFood()) {
            if (getCurrentOrientation() == Orientation.LEFT) {
                removeNodeFromArea(new Coordinates(cellPosition.x - 1, cellPosition.y));
                data.eatFood(new Coordinates(cellPosition.x - 1, cellPosition.y));
            } else if (getCurrentOrientation() == Orientation.UP) {
                removeNodeFromArea(new Coordinates(cellPosition.x, cellPosition.y - 1));
                data.eatFood(new Coordinates(cellPosition.x, cellPosition.y - 1));
            } else if (getCurrentOrientation() == Orientation.RIGHT) {
                removeNodeFromArea(new Coordinates(cellPosition.x + 1, cellPosition.y));
                data.eatFood(new Coordinates(cellPosition.x + 1, cellPosition.y));
            } else if (getCurrentOrientation() == Orientation.DOWN) {
                removeNodeFromArea(new Coordinates(cellPosition.x, cellPosition.y + 1));
                data.eatFood(new Coordinates(cellPosition.x, cellPosition.y + 1));
            }
            System.out.println("FOOD: " + data.getEatedFood() + "/" + data.getCountFood());
        }

        if (enemyInNewCell() && getCurrentOrientation() != Orientation.NONE) {
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
            if (enemyCanRotate()) {
                changeCurrentOrientation();
            }
        }

        if (getCurrentOrientation() == Orientation.UP && getNextOrientation() == Orientation.DOWN ||
            getCurrentOrientation() == Orientation.LEFT && getNextOrientation() == Orientation.RIGHT ||
            getCurrentOrientation() == Orientation.RIGHT && getNextOrientation() == Orientation.LEFT ||
            getCurrentOrientation() == Orientation.DOWN && getNextOrientation() == Orientation.UP) {
            changeCurrentOrientation();
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
    }
}
