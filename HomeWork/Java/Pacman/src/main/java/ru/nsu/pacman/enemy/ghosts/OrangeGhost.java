package ru.nsu.pacman.enemy.ghosts;

import ru.nsu.pacman.GameData;
import ru.nsu.pacman.enemy.Entity;
import ru.nsu.pacman.enemy.Pacman;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Random;

import static ru.nsu.pacman.Game.CELL_N;
import static ru.nsu.pacman.GameData.Coordinates;
//Tries to get closer to Pacman, but keeps a long distance
public class OrangeGhost extends Entity {

    private double distanceToPacman = 250;
    public OrangeGhost(Coordinates startPosition, LevelData data) {
        super(startPosition, data);
        speed = 1.5;
    }
    private ArrayList<GameData.Orientation> getPriorityOrientationToPacman() {
        if (getDistanceTo(data.getPacman().body) > distanceToPacman) {
            return getAvailableOrientations();
        }

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

    @Override
    public void move() {
        if (currentOrientation == GameData.Orientation.NONE) {
            currentOrientation = getNextOrientation();
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

            nextOrientation = getNextOrientation();
            if (getAvailableOrientations().size() != 2) {
                currentOrientation = nextOrientation;
            }
            if (!enemyCanMove()) {
                currentOrientation = nextOrientation;
            }
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

    @Override
    public GameData.Orientation getNextOrientation() {
        ArrayList<GameData.Orientation> priority = getPriorityOrientationToPacman();
        Random random = new Random();
        return priority.get(random.nextInt(priority.size()));
    }
}
