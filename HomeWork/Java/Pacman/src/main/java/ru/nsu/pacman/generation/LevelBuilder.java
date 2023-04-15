package ru.nsu.pacman.generation;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import static ru.nsu.pacman.data.GameData.Coordinates;

public class LevelBuilder {
    public static final int CELL_N = 21;
    public static final int CELL_SIZE = 32;
    public static final Color WALL_COLOR = Color.BLUE;
    public static final Color FOOD_COLOR = Color.WHITE;
    public static final Color BARRIER_COLOR = Color.BROWN;
    public static final Color CHERRY_COLOR = Color.RED;

    public LevelBuilder() {}

    public GridPane buildLevel(LevelData levelData) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setVgap(0);

        for (int row = 0; row < CELL_N; row++) {
            for (int col = 0; col < CELL_N; col++) {
                LevelData.Symbols status = levelData.getValueLevelData(new Coordinates(row, col));
                Shape elem = null;

                if (status == LevelData.Symbols.Wall) {
                    elem = new Rectangle(CELL_SIZE, CELL_SIZE);
                    elem.setFill(WALL_COLOR);
                } else if (status == LevelData.Symbols.Food) {
                    elem = new Circle(CELL_SIZE, CELL_SIZE, 4);
                    elem.setFill(FOOD_COLOR);
                } else if (status == LevelData.Symbols.MegaFood) {
                    elem = new Circle(CELL_SIZE, CELL_SIZE, 9);
                    elem.setFill(FOOD_COLOR);
                } else if (status == LevelData.Symbols.Barrier) {
                    elem = new Rectangle(CELL_SIZE - 5, CELL_SIZE - 5);
                    elem.setFill(BARRIER_COLOR);
                } else if (status == LevelData.Symbols.Cherry) {
                    elem = new Circle(CELL_SIZE, CELL_SIZE, 6);
                    elem.setFill(CHERRY_COLOR);
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
