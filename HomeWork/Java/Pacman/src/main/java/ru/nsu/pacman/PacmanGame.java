package ru.nsu.pacman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.nsu.pacman.enemy.Pacman;
import ru.nsu.pacman.generation.LevelBuilder;
import ru.nsu.pacman.generation.LevelData;

import java.util.Objects;

import static java.lang.Math.abs;

public class PacmanGame extends Application {

    public enum WalkDir {
        UP,
        RIGHT,
        DOWN,
        LEFT,
        NONE
    };

    public static class Coordinates {
        public double x;
        public double y;

        public Coordinates(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private final Image pacmanRight = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/right.gif")));
    private final Image pacmanLeft = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/left.gif")));
    private final Image pacmanUp = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/up.gif")));
    private final Image pacmanDown = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/down.gif")));
    private LevelData data;
    private ImageView pacmanView;
    private final int pacmanSize = 32;
    private WalkDir pacmanDir = WalkDir.NONE;
    private final double areaSize = 32 * 21; //After we start calculate this value automatic

    Pacman pacman;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        data = new LevelData(getClass().getResourceAsStream("levels/1.txt"));
        LevelBuilder builder = new LevelBuilder();
        Pane root = new Pane(builder.buildLevel(data));
        Scene scene = new Scene(root);
        scene.setFill(Color.AQUA);

        pacman = new Pacman(data.getPacmanPosition().x * pacmanSize, data.getPacmanPosition().y * pacmanSize);

        pacmanView = new ImageView(pacmanRight);
        pacmanView.setFitWidth(pacmanSize);
        pacmanView.setFitHeight(pacmanSize);

        root.getChildren().add(pacmanView);
        pacmanView.setLayoutX(pacman.getPosition().x);
        pacmanView.setLayoutY(pacman.getPosition().y);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                pacmanDir = WalkDir.UP;
                pacmanView.setImage(pacmanUp);
            } else if (event.getCode() == KeyCode.DOWN) {
                pacmanDir = WalkDir.DOWN;
                pacmanView.setImage(pacmanDown);
            } else if (event.getCode() == KeyCode.LEFT) {
                pacmanDir = WalkDir.LEFT;
                pacmanView.setImage(pacmanLeft);
            } else if (event.getCode() == KeyCode.RIGHT) {
                pacmanDir = WalkDir.RIGHT;
                pacmanView.setImage(pacmanRight);
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void update() {
        System.out.println("========START UPDATE========");
        Coordinates curPos = data.getPacmanPosition();
        System.out.println("I'm here: " + curPos.x + " " + curPos.y);
        System.out.println("NEAR ME : L = " + data.getLevelData()[(int)curPos.x - 1][(int)curPos.y] +
                            ", R = " + data.getLevelData()[(int)curPos.x + 1][(int)curPos.y] +
                            ", U = " + data.getLevelData()[(int)curPos.x][(int)curPos.y - 1] +
                            ", D = " + data.getLevelData()[(int)curPos.x][(int)curPos.y + 1]);
        if ((pacmanDir == WalkDir.UP) && (data.getLevelData()[(int)curPos.x][(int)curPos.y - 1] == LevelData.SymbolWall)) {
            return;
        } else if ((pacmanDir == WalkDir.LEFT) && (data.getLevelData()[(int)curPos.x - 1][(int)curPos.y] == LevelData.SymbolWall)) {
            return;
        } else if ((pacmanDir == WalkDir.RIGHT) && (data.getLevelData()[(int)curPos.x + 1][(int)curPos.y] == LevelData.SymbolWall)) {
            return;
        } else if ((pacmanDir == WalkDir.DOWN) && (data.getLevelData()[(int)curPos.x][(int)curPos.y + 1] == LevelData.SymbolWall)) {
            return;
        }

        pacman.move(pacmanDir);

        pacmanView.setLayoutX(pacman.getPosition().x);
        pacmanView.setLayoutY(pacman.getPosition().y);

        System.out.println("Double: " + pacman.getPosition().x + " " + pacman.getPosition().y);
        System.out.println("Table: " + curPos.x + " " + curPos.y);
        System.out.println("Diff: " + abs(pacman.getPosition().x - (curPos.x * pacmanSize)));

        if (abs(pacman.getPosition().x - (curPos.x * pacmanSize)) >= pacmanSize) {
            data.getLevelData()[(int)curPos.x][(int)curPos.y] = LevelData.SymbolEmpty;
            if (pacmanDir == WalkDir.LEFT) {
                data.getLevelData()[(int)curPos.x - 1][(int)curPos.y] = LevelData.SymbolPacman;
                data.setPacmanPosition(new Coordinates(curPos.x - 1, curPos.y));
            } else if (pacmanDir == WalkDir.RIGHT) {
                data.getLevelData()[(int)curPos.x + 1][(int)curPos.y] = LevelData.SymbolPacman;
                data.setPacmanPosition(new Coordinates(curPos.x + 1, curPos.y));
            }
        }
        if (abs(pacman.getPosition().y - (curPos.y * pacmanSize)) >= pacmanSize) {
            data.getLevelData()[(int)curPos.x][(int)curPos.y] = LevelData.SymbolEmpty;
            if (pacmanDir == WalkDir.UP) {
                data.getLevelData()[(int)curPos.x][(int)curPos.y - 1] = LevelData.SymbolPacman;
                data.setPacmanPosition(new Coordinates(curPos.x, curPos.y - 1));
            } else if (pacmanDir == WalkDir.DOWN) {
                data.getLevelData()[(int)curPos.x][(int)curPos.y + 1] = LevelData.SymbolPacman;
                data.setPacmanPosition(new Coordinates(curPos.x, curPos.y + 1));
            }
        }
    }
}
