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

import static java.lang.Math.abs;

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
    LevelBuilder builder;
    private GridPane area = null;

    private final int CELL_SIZE = 32;
    private final int CELL_N = 21;
    private int currentLevel = 0;
    private final int COUNT_LEVELS = 5;
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
        builder = new LevelBuilder();
        area = builder.buildLevel(data);
        Pane root = new Pane(area);
        Scene scene = new Scene(root);
        scene.setFill(Color.AQUA);

        pacman = new Pacman(new Coordinates(data.getPacmanPosition().x * CELL_SIZE, data.getPacmanPosition().y * CELL_SIZE));
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

    private boolean PacmanInCenterCell() {
        return ( abs(pacman.getPosition().x - (data.getPacmanPosition().x * CELL_SIZE)) >= CELL_SIZE ||
                abs(pacman.getPosition().y - (data.getPacmanPosition().y * CELL_SIZE)) >= CELL_SIZE );
    }

    private boolean PacmanCanRotate() {
        Coordinates curPosition = data.getPacmanPosition();
        if ((pacman.getNextOrientation() == Orientation.UP) && (data.getLevelData()[(int)curPosition.x][(int)curPosition.y - 1] != LevelData.Symbols.Wall)) {
            return true;
        } else if ((pacman.getNextOrientation() == Orientation.LEFT) && (data.getLevelData()[(int)curPosition.x - 1][(int)curPosition.y] != LevelData.Symbols.Wall)) {
            return true;
        } else if ((pacman.getNextOrientation() == Orientation.RIGHT) && (data.getLevelData()[(int)curPosition.x + 1][(int)curPosition.y] != LevelData.Symbols.Wall)) {
            return true;
        } else if ((pacman.getNextOrientation() == Orientation.DOWN) && (data.getLevelData()[(int)curPosition.x][(int)curPosition.y + 1] != LevelData.Symbols.Wall)) {
            return true;
        } else {
            return false;
        }
    }
    private boolean PacmanCanMove() {
        Coordinates curPosition = data.getPacmanPosition();
        if ((pacman.getCurrentOrientation() == Orientation.UP) && (data.getLevelData()[(int)curPosition.x][(int)curPosition.y - 1] != LevelData.Symbols.Wall)) {
            return true;
        } else if ((pacman.getCurrentOrientation() == Orientation.LEFT) && (data.getLevelData()[(int)curPosition.x - 1][(int)curPosition.y] != LevelData.Symbols.Wall)) {
            return true;
        } else if ((pacman.getCurrentOrientation() == Orientation.RIGHT) && (data.getLevelData()[(int)curPosition.x + 1][(int)curPosition.y] != LevelData.Symbols.Wall)) {
            return true;
        } else if ((pacman.getCurrentOrientation() == Orientation.DOWN) && (data.getLevelData()[(int)curPosition.x][(int)curPosition.y + 1] != LevelData.Symbols.Wall)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean PacmanInBorder() {
        if ((pacman.getCurrentOrientation() == Orientation.LEFT) && ((int)data.getPacmanPosition().x == 0)) {
            return true;
        } else if ((pacman.getCurrentOrientation() == Orientation.UP) && ((int)data.getPacmanPosition().y == 0)) {
            return true;
        } else if ((pacman.getCurrentOrientation() == Orientation.RIGHT) && ((int)data.getPacmanPosition().x == CELL_N - 1)) {
            return true;
        } else if ((pacman.getCurrentOrientation() == Orientation.DOWN) && ((int)data.getPacmanPosition().y == CELL_N - 1)) {
            return true;
        }
        return false;
    }

    private void removeNodeFromGridPane(GridPane gridPane, int row, int col) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                gridPane.getChildren().remove(node);
                break;
            }
        }
    }

    private void update() {
        if (PacmanInCenterCell() && pacman.getCurrentOrientation() != Orientation.NONE) {
            Coordinates oldPosition = data.getPacmanPosition();
            Coordinates newPosition = null;
            if (pacman.getCurrentOrientation() == Orientation.UP) {
                newPosition = new Coordinates(oldPosition.x, oldPosition.y - 1);
            } else if (pacman.getCurrentOrientation() == Orientation.LEFT) {
                newPosition = new Coordinates(oldPosition.x - 1, oldPosition.y);
            } else if (pacman.getCurrentOrientation() == Orientation.RIGHT) {
                newPosition = new Coordinates(oldPosition.x + 1, oldPosition.y);
            } else if (pacman.getCurrentOrientation() == Orientation.DOWN) {
                newPosition = new Coordinates(oldPosition.x, oldPosition.y + 1);
            }

            if (data.getValueLevelData(newPosition) == LevelData.Symbols.Food) {
                removeNodeFromGridPane(area, (int)newPosition.y, (int)newPosition.x);
                foodEat += 1;
            }

            pacman.setPosition(new Coordinates(newPosition.x * CELL_SIZE, newPosition.y * CELL_SIZE));
            data.setValueLevelData(oldPosition, LevelData.Symbols.Empty);
            data.setValueLevelData(newPosition, LevelData.Symbols.Pacman);
            data.setPacmanPosition(newPosition);
            if (PacmanCanRotate()) {
                pacman.changeCurrentOrientation();
            }
        }

        if (PacmanInBorder()) {
            //Make after Animation go to for the board
            if (pacman.getCurrentOrientation() == Orientation.RIGHT) {
                pacman.setPosition(new Coordinates(0, pacman.getPosition().y));
                data.setPacmanPosition(new Coordinates(0, data.getPacmanPosition().y));
            } else if (pacman.getCurrentOrientation() == Orientation.LEFT) {
                pacman.setPosition(new Coordinates((CELL_N - 1) * CELL_SIZE, pacman.getPosition().y));
                data.setPacmanPosition(new Coordinates(CELL_N - 1, data.getPacmanPosition().y));
            } else if (pacman.getCurrentOrientation() == Orientation.UP) {
                pacman.setPosition(new Coordinates(pacman.getPosition().x, (CELL_N - 1) * CELL_SIZE));
                data.setPacmanPosition(new Coordinates(data.getPacmanPosition().x, CELL_N - 1));
            } else if (pacman.getCurrentOrientation() == Orientation.DOWN) {
                pacman.setPosition(new Coordinates(pacman.getPosition().x, 0));
                data.setPacmanPosition(new Coordinates(data.getPacmanPosition().x, 0));
            }
        }
        if (PacmanCanMove()) {
            pacman.move();
            pacmanView.setLayoutX(pacman.getPosition().x);
            pacmanView.setLayoutY(pacman.getPosition().y);
        } else {
            pacman.changeCurrentOrientation();
        }

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
