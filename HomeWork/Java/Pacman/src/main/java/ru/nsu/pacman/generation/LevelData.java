package ru.nsu.pacman.generation;

import ru.nsu.pacman.GameData;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.enemy.EnemyFactory;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

import static ru.nsu.pacman.GameData.Coordinates;
import static ru.nsu.pacman.Game.CELL_N;

public class LevelData {
    private static final int ARRAY_SIZE = 21;
    public enum Symbols {
        Pacman,
        Empty,
        Wall,
        Food,
        Barrier,
        RedGhost,
        BlueGhost,
        PinkGhost,
        OrangeGhost
    }
    private int countFood = 0;
    private int eatedFood = 0;
    private ArrayList<GameData.EnemyData> allEnemies = null;
    private Symbols[][] levelData = new Symbols[ARRAY_SIZE][ARRAY_SIZE];
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
    public int getCountFood() {
        return countFood;
    }
    public void eatFood(Coordinates cord) {
        if (getValueLevelData(cord) == Symbols.Food) {
            setValueLevelData(cord, Symbols.Empty);
            eatedFood++;
        }
    }
    public int getEatedFood() { return eatedFood; }
    public static Symbols convertStringToSymbol(String symbol) throws Exception {
        return switch (symbol) {
            case ("P") -> Symbols.Pacman;
            case ("E") -> Symbols.Empty;
            case ("W") -> Symbols.Wall;
            case ("F") -> Symbols.Food;
            case ("B") -> Symbols.Barrier;
            case ("r") -> Symbols.RedGhost;
            case ("b") -> Symbols.BlueGhost;
            case ("p") -> Symbols.PinkGhost;
            case ("o") -> Symbols.OrangeGhost;
            default -> throw new Exception("Symbol not found");
        };
    }
    public ArrayList<GameData.EnemyData> getAllEnemies() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        if (allEnemies != null) return allEnemies;

        allEnemies = new ArrayList<GameData.EnemyData>();
        for (int col = 0; col < CELL_N; ++col) {
            for (int row = 0; row < CELL_N; ++row) {
                Enemy enemy = EnemyFactory.getInstance().createEnemy(getValueLevelData(new Coordinates(col, row)), new Coordinates(col, row), this);
                if (enemy != null) {
                    allEnemies.add(new GameData.EnemyData(enemy));
                }
            }
        }
        return allEnemies;
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
