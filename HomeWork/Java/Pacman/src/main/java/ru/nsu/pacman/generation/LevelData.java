package ru.nsu.pacman.generation;

import ru.nsu.pacman.PacmanGame;
import ru.nsu.pacman.enemy.Pacman;

import java.io.InputStream;
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
    private PacmanGame.Coordinates pacmanCoord = null;
    private int countFood = 0;
    private Symbols[][] levelData = new Symbols[ARRAY_SIZE][ARRAY_SIZE];
    public LevelData(InputStream is) throws Exception { loadLevelDataFromFile(is); }
    public Symbols[][] getLevelData() {
        return levelData;
    }
    public void setValueLevelData(PacmanGame.Coordinates cord, Symbols value) {
        levelData[(int)cord.x][(int)cord.y] = value;
    }
    public Symbols getValueLevelData(PacmanGame.Coordinates cord) {
        return levelData[(int)cord.x][(int)cord.y];
    }
    public PacmanGame.Coordinates getPacmanPosition() {
        if (pacmanCoord == null) return new PacmanGame.Coordinates(0,0);
        return pacmanCoord;
    }
    public void setPacmanPosition(PacmanGame.Coordinates coord) {
        pacmanCoord = coord;
    }
    public int getCountFood() {
        return countFood;
    }
    private void loadLevelDataFromFile(InputStream is) throws Exception {
        Scanner scanner = new Scanner(is);

        countFood = 0;
        for (int row = 0; row < ARRAY_SIZE; ++row) {
            String line = scanner.nextLine();
            String[] symbols = line.split(" ");

            for (int col = 0; col < ARRAY_SIZE; ++col) {
                char symbol = symbols[col].charAt(0);
                switch (symbol) {
                    case ('P') -> levelData[col][row] = Symbols.Pacman;
                    case ('E') -> levelData[col][row] = Symbols.Empty;
                    case ('W') -> levelData[col][row] = Symbols.Wall;
                    case ('F') -> levelData[col][row] = Symbols.Food;
                    case ('B') -> levelData[col][row] = Symbols.Barrier;
                    case ('r') -> levelData[col][row] = Symbols.RedGhost;
                    case ('b') -> levelData[col][row] = Symbols.BlueGhost;
                    case ('p') -> levelData[col][row] = Symbols.PinkGhost;
                    case ('o') -> levelData[col][row] = Symbols.OrangeGhost;
                    default -> throw new Exception("Symbol not found");
                }
                if (levelData[col][row] == Symbols.Pacman) setPacmanPosition(new PacmanGame.Coordinates(col, row));
                if (levelData[col][row] == Symbols.Food) countFood += 1;
            }
        }

        scanner.close();
    }
}
