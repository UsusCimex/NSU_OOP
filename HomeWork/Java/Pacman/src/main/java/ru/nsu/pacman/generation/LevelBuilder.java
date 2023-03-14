package ru.nsu.pacman.generation;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LevelBuilder {

    private static final int GRID_SIZE = 21;
    private static final int BLOCK_SIZE = 32;
    private static final Color WALL_COLOR = Color.BLUE;
    private static final Color COIN_COLOR = Color.YELLOW;

    private int[][] levelData;

    public LevelBuilder(String path) {
        this.levelData = loadLevelDataFromFile(path);
    }

    public GridPane buildLevel() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setVgap(0);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Rectangle block = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);

                if (levelData[row][col] == 1) {
                    block.setFill(WALL_COLOR);
                } else {
                    block.setFill(COIN_COLOR);
                }

                gridPane.add(block, col, row);
            }
        }

        return gridPane;
    }

    private int[][] loadLevelDataFromFile(String fileName) {
        int[][] levelData = new int[GRID_SIZE][GRID_SIZE];

        try {
            Scanner scanner = new Scanner(new File(fileName));

            for (int row = 0; row < GRID_SIZE; row++) {
                String line = scanner.nextLine();
                String[] symbols = line.split(" ");

                for (int col = 0; col < GRID_SIZE; col++) {
                    char symbol = symbols[col].charAt(0);

                    if (symbol == 'W') { //W = wall, later we use Fabric....
                        levelData[row][col] = 1;
                    } else {
                        levelData[row][col] = 0;
                    }
                }
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return levelData;
    }
}
