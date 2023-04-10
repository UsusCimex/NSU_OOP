package ru.nsu.pacman.generation;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import static ru.nsu.pacman.GameData.Coordinates;

public class LevelBuilder {
    private static final int GRID_SIZE = 21;
    private static final int BLOCK_SIZE = 32;
    private static final Color WALL_COLOR = Color.BLUE;
    private static final Color COIN_COLOR = Color.WHITE;
    private static final Color BARRIER_COLOR = Color.BROWN;

    public LevelBuilder() {}

    public GridPane buildLevel(LevelData levelData) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setVgap(0);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                LevelData.Symbols status = levelData.getValueLevelData(new Coordinates(row, col));
                Shape elem = null;

                if (status == LevelData.Symbols.Wall) {
                    elem = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
                    elem.setFill(WALL_COLOR);
                } else if (status == LevelData.Symbols.Food) {
                    elem = new Circle(BLOCK_SIZE, BLOCK_SIZE, 6);
                    elem.setFill(COIN_COLOR);
                } else if (status == LevelData.Symbols.Barrier) {
                    elem = new Rectangle(BLOCK_SIZE - 5, BLOCK_SIZE - 5);
                    elem.setFill(BARRIER_COLOR);
                }
                if (elem != null) {
                    gridPane.add(elem, row, col);
                    GridPane.setHalignment(elem, HPos.CENTER);
                    GridPane.setValignment(elem, VPos.CENTER);
                }
            }
        }
        return gridPane;
    }
}
