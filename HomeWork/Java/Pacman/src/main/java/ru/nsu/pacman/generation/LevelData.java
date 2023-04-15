package ru.nsu.pacman.generation;

import ru.nsu.pacman.data.GameData;
import ru.nsu.pacman.entity.Entity;
import ru.nsu.pacman.entity.EntityFactory;
import ru.nsu.pacman.entity.Pacman;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

import static ru.nsu.pacman.Game.signalChangeMode;
import static ru.nsu.pacman.data.GameData.Coordinates;
import static ru.nsu.pacman.Game.CELL_N;

public class LevelData {
    private static final int ARRAY_SIZE = 21;

    public enum Symbols {
        Pacman,
        Empty,
        Wall,
        Food,
        MegaFood,
        Barrier,
        RedGhost,
        BlueGhost,
        PinkGhost,
        GreenGhost
    }
    private int countFood = 0;
    private int eatedFood = 0;
    private ArrayList<GameData.EntityData> allEntities = null;
    private GameData.EntityData pacman = null;
    private final Symbols[][] levelData = new Symbols[ARRAY_SIZE][ARRAY_SIZE];
    public LevelData(InputStream is) throws Exception { loadLevelDataFromFile(is); }
    public void setValueLevelData(Coordinates cord, Symbols value) {
        levelData[(int)cord.x][(int)cord.y] = value;
    }
    public Symbols getValueLevelData(Coordinates cord) {
        if (cord.x < 0 || cord.x >= ARRAY_SIZE || cord.y < 0 || cord.y >= ARRAY_SIZE) {
            return Symbols.Empty;
        }
        return levelData[(int)cord.x][(int)cord.y];
    }
    public void removeAllBarriers() {
        for (int col = 0; col < CELL_N; ++col) {
            for (int row = 0; row < CELL_N; ++row) {
                Coordinates coordinates = new Coordinates(col, row);
                if (getValueLevelData(coordinates) == Symbols.Barrier) {
                    setValueLevelData(coordinates, Symbols.Empty);
                }
            }
        }
    }
    public int getCountFood() {
        return countFood;
    }
    public void eatFood(Coordinates cord) {
        if (getValueLevelData(cord) == Symbols.Food) {
            setValueLevelData(cord, Symbols.Empty);
            eatedFood++;
        } else if (getValueLevelData(cord) == Symbols.MegaFood) {
            setValueLevelData(cord, Symbols.Empty);
            signalChangeMode();
        }
    }
    public int getEatedFood() { return eatedFood; }
    public static Symbols convertStringToSymbol(String symbol) throws Exception {
        return switch (symbol) {
            case ("P") -> Symbols.Pacman;
            case ("E") -> Symbols.Empty;
            case ("W") -> Symbols.Wall;
            case ("F") -> Symbols.Food;
            case ("M") -> Symbols.MegaFood;
            case ("B") -> Symbols.Barrier;
            case ("r") -> Symbols.RedGhost;
            case ("b") -> Symbols.BlueGhost;
            case ("p") -> Symbols.PinkGhost;
            case ("g") -> Symbols.GreenGhost;
            default -> throw new Exception("Symbol not found");
        };
    }
    public ArrayList<GameData.EntityData> getAllEntities() {
        if (allEntities != null) return allEntities;

        try {
            allEntities = new ArrayList<>();
            for (int col = 0; col < CELL_N; ++col) {
                for (int row = 0; row < CELL_N; ++row) {
                    Entity entity = EntityFactory.getInstance().createEnemy(getValueLevelData(new Coordinates(col, row)), new Coordinates(col, row), this);
                    if (entity != null) {
                        allEntities.add(new GameData.EntityData(entity));
                    }
                }
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException ex) {
            ex.getStackTrace();
        }
        return allEntities;
    }
    public void resetAllEntities() {
        allEntities = null;
        pacman = null;
    }
    public GameData.EntityData getPacman() {
        if (allEntities == null) {
            allEntities = getAllEntities();
        }
        if (pacman != null) {
            return pacman;
        }
        for (GameData.EntityData enemy : allEntities) {
            if (enemy.body.getClass().equals(Pacman.class)) {
                pacman = enemy;
                break;
            }
        }
        return pacman;
    }
    private void loadLevelDataFromFile(InputStream is) throws Exception {
        Scanner scanner = new Scanner(is);

        countFood = 0;
        for (int row = 0; row < ARRAY_SIZE; ++row) {
            String line = scanner.nextLine();
            String[] symbols = line.split(" ");

            for (int col = 0; col < ARRAY_SIZE; ++col) {
                String symbol = symbols[col];
                levelData[col][row] = convertStringToSymbol(symbol);
                if (levelData[col][row] == Symbols.Food) countFood += 1;
            }
        }
        scanner.close();
    }
}
