package ru.nsu.pacman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.nsu.pacman.generation.LevelBuilder;
import ru.nsu.pacman.generation.LevelData;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        LevelData levelData = new LevelData("src\\main\\resources\\ru\\nsu\\pacman\\levels\\1.txt");
        LevelBuilder levelBuilder = new LevelBuilder();
        GridPane levelGridPane = levelBuilder.buildLevel(levelData);
        Scene sc = new Scene(levelGridPane);
        sc.setFill(Color.AQUA);

        primaryStage.setScene(sc);
        primaryStage.setTitle("Pacman");
        primaryStage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}