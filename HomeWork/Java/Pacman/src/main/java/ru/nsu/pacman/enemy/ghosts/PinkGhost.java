package ru.nsu.pacman.enemy.ghosts;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.Controller;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Random;

import static ru.nsu.pacman.Controller.Coordinates;
import static ru.nsu.pacman.Game.CELL_N;

//Runs randomly, but he moves fast
public class PinkGhost extends Enemy {

    public PinkGhost(Coordinates startPosition, LevelData data) {
        super(startPosition, data);
        speed = 2.5;
    }
    private ArrayList<Controller.Orientation> getAvailableOrientations() {
        ArrayList<Controller.Orientation> availableOrientations = new ArrayList<>();
        if (data.getValueLevelData(new Coordinates(cellPosition.x - 1, cellPosition.y)) != LevelData.Symbols.Wall) {
            availableOrientations.add(Controller.Orientation.LEFT);
        }
        if (data.getValueLevelData(new Coordinates(cellPosition.x + 1, cellPosition.y)) != LevelData.Symbols.Wall) {
            availableOrientations.add(Controller.Orientation.RIGHT);
        }
        if (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y - 1)) != LevelData.Symbols.Wall) {
            availableOrientations.add(Controller.Orientation.UP);
        }
        if (data.getValueLevelData(new Coordinates(cellPosition.x, cellPosition.y + 1)) != LevelData.Symbols.Wall) {
            availableOrientations.add(Controller.Orientation.DOWN);
        }
        return availableOrientations;
    }
    private Controller.Orientation getRandomOrientation() {
        ArrayList<Controller.Orientation> availableOrientations = getAvailableOrientations();
        Random random = new Random();
        return availableOrientations.get(random.nextInt(availableOrientations.size()));
    }
    @Override
    public void move() {
        if (curentOrientation == Controller.Orientation.NONE) {
            curentOrientation = getRandomOrientation();
        }

        if (enemyInNewCell()) {
            Coordinates newPosition = null;
            if (getCurrentOrientation() == Controller.Orientation.UP) {
                newPosition = new Coordinates(cellPosition.x, cellPosition.y - 1);
            } else if (getCurrentOrientation() == Controller.Orientation.LEFT) {
                newPosition = new Coordinates(cellPosition.x - 1, cellPosition.y);
            } else if (getCurrentOrientation() == Controller.Orientation.RIGHT) {
                newPosition = new Coordinates(cellPosition.x + 1, cellPosition.y);
            } else if (getCurrentOrientation() == Controller.Orientation.DOWN) {
                newPosition = new Coordinates(cellPosition.x, cellPosition.y + 1);
            }
            setPosition(newPosition);

            Controller.Orientation randomOrientation = getRandomOrientation();
            if (getAvailableOrientations().size() != 2) {
                curentOrientation = randomOrientation;
            }

            if (!enemyCanMove()) {
                curentOrientation = randomOrientation;
            }
        }

        if (enemyInBorder()) {
            if (getCurrentOrientation() == Controller.Orientation.RIGHT) {
                setPosition(new Coordinates(0, cellPosition.y));
            } else if (getCurrentOrientation() == Controller.Orientation.LEFT) {
                setPosition(new Coordinates(CELL_N - 1, cellPosition.y));
            } else if (getCurrentOrientation() == Controller.Orientation.UP) {
                setPosition(new Coordinates(cellPosition.x, CELL_N - 1));
            } else if (getCurrentOrientation() == Controller.Orientation.DOWN) {
                setPosition(new Coordinates(cellPosition.x, 0));
            }
        }

        if (curentOrientation == Controller.Orientation.UP) {
            position.y -= speed;
        } else if (curentOrientation == Controller.Orientation.RIGHT) {
            position.x += speed;
        } else if (curentOrientation == Controller.Orientation.DOWN) {
            position.y += speed;
        } else if (curentOrientation == Controller.Orientation.LEFT) {
            position.x -= speed;
        }
    }
}
