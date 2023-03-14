package ru.nsu.pacman;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.nsu.pacman.generation.LevelBuilder;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        LevelBuilder levelBuilder = new LevelBuilder("src\\main\\resources\\ru\\nsu\\pacman\\levels\\1.txt");
        GridPane levelGridPane = levelBuilder.buildLevel();
        Scene sc = new Scene(levelGridPane);

        primaryStage.setScene(sc);
        primaryStage.setTitle("Pacman");
        primaryStage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}