package ru.nsu.pacman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.nsu.pacman.enemy.Pacman;
import ru.nsu.pacman.generation.LevelBuilder;
import ru.nsu.pacman.generation.LevelData;

import java.util.Objects;

public class PacmanGame extends Application {

    public enum Orientation {
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

    //Images
    private final Image pacmanRight = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/right.gif")));
    private final Image pacmanLeft = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/left.gif")));
    private final Image pacmanUp = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/up.gif")));
    private final Image pacmanDown = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/down.gif")));
    private ImageView pacmanView;

    private LevelData data;
    private GridPane area = null;

    public static final int CELL_SIZE = 32;
    public static final int CELL_N = 21;
    private int currentLevel = 0;
    public static final int COUNT_LEVELS = 5;
    private int foodEat = 0;
    private int maxFood = 0;
    private boolean inGame = false;

    // Enemies
    Pacman pacman;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        data = generateNextLevel();
        LevelBuilder builder = new LevelBuilder();
        area = builder.buildLevel(data);
        Pane root = new Pane(area);
        Scene scene = new Scene(root);
        scene.setFill(Color.AQUA);

        pacman = new Pacman(new Coordinates(data.getPacmanPosition().x * CELL_SIZE, data.getPacmanPosition().y * CELL_SIZE), data);
        maxFood = data.getCountFood();

        pacmanView = new ImageView(pacmanRight);
        pacmanView.setFitWidth(CELL_SIZE);
        pacmanView.setFitHeight(CELL_SIZE);

        root.getChildren().add(pacmanView);
        pacmanView.setLayoutX(pacman.getPosition().x);
        pacmanView.setLayoutY(pacman.getPosition().y);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                pacman.changeNextOrientation(Orientation.UP);
            } else if (event.getCode() == KeyCode.DOWN) {
                pacman.changeNextOrientation(Orientation.DOWN);
            } else if (event.getCode() == KeyCode.LEFT) {
                pacman.changeNextOrientation(Orientation.LEFT);
            } else if (event.getCode() == KeyCode.RIGHT) {
                pacman.changeNextOrientation(Orientation.RIGHT);
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void removeNodeFromArea(int row, int col) {
        for (Node node : area.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                area.getChildren().remove(node);
                break;
            }
        }
    }

    private void update() {
        pacman.move();

        if (data.getValueLevelData(data.getPacmanPosition()) == LevelData.Symbols.Food) {
            removeNodeFromArea((int)data.getPacmanPosition().y, (int)data.getPacmanPosition().x);
            foodEat += 1;
        }
        data.setValueLevelData(data.getPacmanPosition(), LevelData.Symbols.Pacman);

        pacmanView.setLayoutX(pacman.getPosition().x);
        pacmanView.setLayoutY(pacman.getPosition().y);

        if (pacman.getCurrentOrientation() == Orientation.UP) {
            pacmanView.setImage(pacmanUp);
        } else if (pacman.getCurrentOrientation() == Orientation.LEFT) {
            pacmanView.setImage(pacmanLeft);
        } else if (pacman.getCurrentOrientation() == Orientation.RIGHT) {
            pacmanView.setImage(pacmanRight);
        } else if (pacman.getCurrentOrientation() == Orientation.DOWN) {
            pacmanView.setImage(pacmanDown);
        }
    }

    private LevelData generateNextLevel() {
        currentLevel += 1;
        try {
            if (currentLevel == 1) return new LevelData(getClass().getResourceAsStream("levels/1.txt"));
            else if (currentLevel == 2) return new LevelData(getClass().getResourceAsStream("levels/2.txt"));
            else if (currentLevel == 3) return new LevelData(getClass().getResourceAsStream("levels/3.txt"));
            else if (currentLevel == 4) return new LevelData(getClass().getResourceAsStream("levels/4.txt"));
            else if (currentLevel == 5) return new LevelData(getClass().getResourceAsStream("levels/5.txt"));
            else inGame = false;
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }
}
