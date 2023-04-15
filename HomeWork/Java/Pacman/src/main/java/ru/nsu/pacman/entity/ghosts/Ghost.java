package ru.nsu.pacman.entity.ghosts;

import ru.nsu.pacman.Game;
import ru.nsu.pacman.data.GameData;
import ru.nsu.pacman.entity.Entity;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Random;

public abstract class Ghost extends Entity {
    public Ghost(GameData.Coordinates startPosition, LevelData data) {
        super(startPosition, data);
    }
    public static void changeAllStates(ArrayList<GameData.EntityData> entities, GhostState newState) {
        for (GameData.EntityData entity : entities) {
            if (entity.body instanceof Ghost) {
                Ghost ghost = (Ghost) entity.body;
                ghost.setState(newState);
            }
        }
    }

    public enum GhostState {
        DEFAULT,
        SCARED,
        DEAD
    }
    protected GhostState state = GhostState.DEFAULT;
    public GhostState getState() {
        return state;
    }
    public void setState(GhostState newState) {
        state = newState;
    }
    protected ArrayList<GameData.Orientation> getAvailableOrientations() {
        ArrayList<GameData.Orientation> availableOrientations = new ArrayList<>();
        if (isStopBlock(data.getValueLevelData(new GameData.Coordinates(cellPosition.x - 1, cellPosition.y)))) {
            if (checkDeadlock(getCellPosition(), GameData.Orientation.LEFT)) {
                availableOrientations.add(GameData.Orientation.LEFT);
            }
        }
        if (isStopBlock(data.getValueLevelData(new GameData.Coordinates(cellPosition.x + 1, cellPosition.y)))) {
            if (checkDeadlock(getCellPosition(), GameData.Orientation.RIGHT)) {
                availableOrientations.add(GameData.Orientation.RIGHT);
            }
        }
        if (isStopBlock(data.getValueLevelData(new GameData.Coordinates(cellPosition.x, cellPosition.y - 1)))) {
            if (checkDeadlock(getCellPosition(), GameData.Orientation.UP)) {
                availableOrientations.add(GameData.Orientation.UP);
            }
        }
        if (isStopBlock(data.getValueLevelData(new GameData.Coordinates(cellPosition.x, cellPosition.y + 1)))) {
            if (checkDeadlock(getCellPosition(), GameData.Orientation.DOWN)) {
                availableOrientations.add(GameData.Orientation.DOWN);
            }
        }
        if (availableOrientations.size() == 0) {
            availableOrientations.add(GameData.Orientation.NONE);
        }
        return availableOrientations;
    }
    protected GameData.Orientation getRandomOrientation() {
        ArrayList<GameData.Orientation> availableOrientations = getAvailableOrientations();
        Random random = new Random();
        nextOrientation = availableOrientations.get(random.nextInt(availableOrientations.size()));

        if (nextOrientation == GameData.Orientation.UP && currentOrientation == GameData.Orientation.DOWN ||
                nextOrientation == GameData.Orientation.DOWN && currentOrientation == GameData.Orientation.UP ||
                nextOrientation == GameData.Orientation.LEFT && currentOrientation == GameData.Orientation.RIGHT ||
                nextOrientation == GameData.Orientation.RIGHT && currentOrientation == GameData.Orientation.LEFT) {
            return availableOrientations.get(random.nextInt(availableOrientations.size()));
        }

        return nextOrientation;
    }

    protected boolean checkDeadlock(GameData.Coordinates curCord, GameData.Orientation orientation) {
        boolean[][] area = new boolean[Game.CELL_N][Game.CELL_N];
        for (int y = 0; y < Game.CELL_N; ++y) {
            for (int x = 0; x < Game.CELL_N; ++x) {
                area[x][y] = isStopBlock(data.getValueLevelData(new GameData.Coordinates(x, y)));
            }
        }
        area[(int)curCord.x][(int)curCord.y] = false;
        if (orientation == GameData.Orientation.LEFT) {
            return findPath(area, (int)curCord.x - 1, (int)curCord.y, 0, 5);
        } else if (orientation == GameData.Orientation.RIGHT) {
            return findPath(area, (int)curCord.x + 1, (int)curCord.y, 0, 5);
        } else if (orientation == GameData.Orientation.UP) {
            return findPath(area, (int)curCord.x, (int)curCord.y - 1, 0, 5);
        } else {
            return findPath(area, (int)curCord.x, (int)curCord.y + 1, 0, 5);
        }
    }

    private static boolean findPath(boolean[][] maze, int x, int y, int steps, int maxSteps) {
        if (x < 0 || y < 0 || x >= maze.length || y >= maze[0].length || steps > maxSteps) {
            return true;
        }
        if (!maze[x][y]) {
            return false;
        }

        maze[x][y] = false;
        if (findPath(maze, x + 1, y, steps + 1, maxSteps) || findPath(maze, x - 1, y, steps + 1, maxSteps) || findPath(maze, x, y + 1, steps + 1, maxSteps) || findPath(maze, x, y - 1, steps + 1, maxSteps)) {
            return true;
        }

        maze[x][y] = true;
        return false;
    }
}
