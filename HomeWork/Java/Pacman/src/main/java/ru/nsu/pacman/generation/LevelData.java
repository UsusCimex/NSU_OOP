package ru.nsu.pacman.generation;

import ru.nsu.pacman.PacmanGame;
import ru.nsu.pacman.enemy.Pacman;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

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
    private Symbols[][] levelData = new Symbols[ARRAY_SIZE][ARRAY_SIZE];
    public LevelData(InputStream is) throws Exception { loadLevelDataFromFile(is); }
    public void setValueLevelData(PacmanGame.Coordinates cord, Symbols value) {
        levelData[(int)cord.x][(int)cord.y] = value;
    }
    public Symbols getValueLevelData(PacmanGame.Coordinates cord) {
        if (cord.x < 0 || cord.x >= ARRAY_SIZE || cord.y < 0 || cord.y >= ARRAY_SIZE) {
            return Symbols.Empty;
        }
        return levelData[(int)cord.x][(int)cord.y];
    }
    public int getCountFood() {
        return countFood;
    }
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
