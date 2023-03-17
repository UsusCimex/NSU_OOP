package ru.nsu.pacman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.nsu.pacman.generation.LevelBuilder;
import ru.nsu.pacman.generation.LevelData;

public class PacmanGame extends Application {

    private ImageView characterView;
    private int pacmanSize = 32;
    private double posX = 0, posY = 0;
    private double dX, dY;
    private double pacmanSpeed = 2.5;
    private double areaSize;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LevelData data = new LevelData(getClass().getResourceAsStream("levels/1.txt"));
        LevelBuilder builder = new LevelBuilder();
        Pane root = new Pane(builder.buildLevel(data));
        Scene scene = new Scene(root);
        scene.setFill(Color.AQUA);

        areaSize = 32 * 21; //temp//

        Image characterImage = new Image(getClass().getResourceAsStream("sprites/pacman/pRight.gif"));
        characterView = new ImageView(characterImage);
        characterView.setFitWidth(pacmanSize);
        characterView.setFitHeight(pacmanSize);

        root.getChildren().add(characterView);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                dX = 0;
                dY = -pacmanSpeed;
            } else if (event.getCode() == KeyCode.DOWN) {
                dX = 0;
                dY = pacmanSpeed;
            } else if (event.getCode() == KeyCode.LEFT) {
                dX = -pacmanSpeed;
                dY = 0;
            } else if (event.getCode() == KeyCode.RIGHT) {
                dX = pacmanSpeed;
                dY = 0;
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void update() {
        posX += dX;
        posY += dY;

        if (posX < 0) posX = 0;
        if (posY < 0) posY = 0;
        if (posX > areaSize - pacmanSize) posX = areaSize - pacmanSize;
        if (posY > areaSize - pacmanSize) posY = areaSize - pacmanSize;

        characterView.setLayoutX(posX);
        characterView.setLayoutY(posY);
    }
}
