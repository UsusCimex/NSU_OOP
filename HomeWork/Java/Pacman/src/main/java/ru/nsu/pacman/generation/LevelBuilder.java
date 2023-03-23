package ru.nsu.pacman.generation;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class LevelBuilder {
    private static final int GRID_SIZE = 21;
    private static final int BLOCK_SIZE = 32;
    private static final Color WALL_COLOR = Color.BLUE;
    private static final Color COIN_COLOR = Color.WHITE;
    public LevelBuilder() {}

    public GridPane buildLevel(LevelData levelData) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setVgap(0);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                LevelData.Symbols status = levelData.getLevelData()[row][col];
                if (status == LevelData.Symbols.Wall) { // after we make factory
                    Rectangle block = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
                    block.setFill(WALL_COLOR);
                    gridPane.add(block, row, col);
                } else if (status == LevelData.Symbols.Food) {
                    Circle circ = new Circle(BLOCK_SIZE, BLOCK_SIZE, 6);
                    circ.setFill(COIN_COLOR);
                    gridPane.add(circ, row, col);

                    gridPane.setHalignment(circ, HPos.CENTER);
                    gridPane.setValignment(circ, VPos.CENTER);
                }
            }
        }
        return gridPane;
    }
}
