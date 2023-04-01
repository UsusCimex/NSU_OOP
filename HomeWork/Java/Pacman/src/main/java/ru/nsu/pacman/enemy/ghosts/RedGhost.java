package ru.nsu.pacman.enemy.ghosts;

import ru.nsu.pacman.GameData;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.enemy.Pacman;
import ru.nsu.pacman.generation.LevelData;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

import static ru.nsu.pacman.Game.CELL_N;
import static ru.nsu.pacman.GameData.Coordinates;

//Always running after Pacman
public class RedGhost extends Enemy {
    public RedGhost(Coordinates startPosition, LevelData data) {
        super(startPosition, data);
        speed = 1.8;
    }
    @Override
    public void move() {
        if (curentOrientation == GameData.Orientation.NONE) {
            curentOrientation = getNextOrientation();
        }

        if (enemyInNewCell()) {
            Coordinates newPosition = null;
            if (getCurrentOrientation() == GameData.Orientation.UP) {
                newPosition = new Coordinates(cellPosition.x, cellPosition.y - 1);
            } else if (getCurrentOrientation() == GameData.Orientation.LEFT) {
                newPosition = new Coordinates(cellPosition.x - 1, cellPosition.y);
            } else if (getCurrentOrientation() == GameData.Orientation.RIGHT) {
                newPosition = new Coordinates(cellPosition.x + 1, cellPosition.y);
            } else if (getCurrentOrientation() == GameData.Orientation.DOWN) {
                newPosition = new Coordinates(cellPosition.x, cellPosition.y + 1);
            }
            setPosition(newPosition);

            curentOrientation = getNextOrientation();
        }

        if (enemyInBorder()) {
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

        if (curentOrientation == GameData.Orientation.UP) {
            position.y -= speed;
        } else if (curentOrientation == GameData.Orientation.RIGHT) {
            position.x += speed;
        } else if (curentOrientation == GameData.Orientation.DOWN) {
            position.y += speed;
        } else if (curentOrientation == GameData.Orientation.LEFT) {
            position.x -= speed;
        }
    }

    @Override
    public GameData.Orientation getNextOrientation() {
        ArrayList<GameData.Orientation> priority = getPriorityOrientationToPacman();
        Random random = new Random();
        return priority.get(random.nextInt(priority.size()));
    }

    private ArrayList<GameData.Orientation> getPriorityOrientationToPacman() {
        ArrayList<GameData.Orientation> availableOrientations = getAvailableOrientations();
        ArrayList<GameData.Orientation> priorityOrientations = new ArrayList<>();
        Pacman pacman = (Pacman) data.getPacman().body;
        if (pacman.getPosition().x - getPosition().x > 0) {
            priorityOrientations.add(GameData.Orientation.RIGHT);
        } else {
            priorityOrientations.add(GameData.Orientation.LEFT);
        }
        if (pacman.getPosition().y - getPosition().y > 0) {
            priorityOrientations.add(GameData.Orientation.DOWN);
        } else {
            priorityOrientations.add(GameData.Orientation.UP);
        }

        priorityOrientations.retainAll(availableOrientations);
        if (priorityOrientations.size() != 0) {
            return  priorityOrientations;
        } else {
            return  availableOrientations;
        }
    }
}
