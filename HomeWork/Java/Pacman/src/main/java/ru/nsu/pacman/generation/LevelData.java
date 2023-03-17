package ru.nsu.pacman.generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class LevelData {
    private static final int ARRAY_SIZE = 21;
    private char[][] levelData = new char[ARRAY_SIZE][ARRAY_SIZE];
    public LevelData(InputStream is) { loadLevelDataFromFile(is); }
    public char[][] getLevelData() {
        return levelData;
    }
    private void loadLevelDataFromFile(InputStream is) {
        Scanner scanner = new Scanner(is);

        for (int row = 0; row < ARRAY_SIZE; row++) {
            String line = scanner.nextLine();
            String[] symbols = line.split(" ");

            for (int col = 0; col < ARRAY_SIZE; col++) {
                levelData[row][col] = symbols[col].charAt(0);
            }
        }

        scanner.close();
    }
}
