package ru.nsu.pacman.generation;

import ru.nsu.pacman.PacmanGame;

import java.io.InputStream;
import java.util.Scanner;

public class LevelData {
    private static final int ARRAY_SIZE = 21;
    public static final char SymbolPacman = 'P';
    public static final char SymbolEmpty = 'E';
    public static final char SymbolWall = 'W';
    public static final char SymbolFood = 'F';
    public static final char SymbolBarrier = 'B';
    public static final char SymbolRedGhost = 'r';
    public static final char SymbolBlueGhost = 'b';
    public static final char SymbolPinkGhost = 'p';
    public static final char SymbolOrangeGhost = 'o';
    private PacmanGame.Coordinates pacmanCoord = null;
    private char[][] levelData = new char[ARRAY_SIZE][ARRAY_SIZE];
    public LevelData(InputStream is) { loadLevelDataFromFile(is); }
    public char[][] getLevelData() {
        return levelData;
    }
    public void setLevelData(PacmanGame.Coordinates cord, char value) {
        levelData[(int)cord.x][(int)cord.y] = value;
    }
    public PacmanGame.Coordinates getPacmanPosition() {
        if (pacmanCoord == null) return new PacmanGame.Coordinates(0,0);
        return pacmanCoord;
    }
    public void setPacmanPosition(PacmanGame.Coordinates coord) {
        pacmanCoord = coord;
    }
    private void loadLevelDataFromFile(InputStream is) {
        Scanner scanner = new Scanner(is);

        for (int row = 0; row < ARRAY_SIZE; ++row) {
            String line = scanner.nextLine();
            String[] symbols = line.split(" ");

            for (int col = 0; col < ARRAY_SIZE; ++col) {
                levelData[col][row] = symbols[col].charAt(0);
                if (levelData[col][row] == SymbolPacman) setPacmanPosition(new PacmanGame.Coordinates(col, row));
            }
        }

        scanner.close();
    }
}
