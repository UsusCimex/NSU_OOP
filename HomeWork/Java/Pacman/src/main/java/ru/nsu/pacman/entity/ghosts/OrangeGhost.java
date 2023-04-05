package ru.nsu.pacman.entity.ghosts;

import ru.nsu.pacman.GameData;
import ru.nsu.pacman.entity.Entity;
import ru.nsu.pacman.entity.Pacman;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Random;

import static ru.nsu.pacman.GameData.Coordinates;
//Tries to get closer to Pacman, but keeps a long distance
public class OrangeGhost extends Entity {

    private final double shortSightedness = 250;
    public OrangeGhost(Coordinates startPosition, LevelData data) {
        super(startPosition, data);
        speed = 1.65;
    }

    @Override
    public GameData.Orientation getNextOrientation() {
        ArrayList<GameData.Orientation> availableOrientations = getAvailableOrientations();
        if (availableOrientations.size() == 0) {
            return GameData.Orientation.NONE;
        }

        if (getDistanceTo(data.getPacman().body) > shortSightedness) {
            Random random = new Random();
            return availableOrientations.get(random.nextInt(availableOrientations.size()));
        }

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
        Random random = new Random();
        if (priorityOrientations.size() != 0) {
            return priorityOrientations.get(random.nextInt(priorityOrientations.size()));
        } else {
            return availableOrientations.get(random.nextInt(availableOrientations.size()));
        }
    }
}
