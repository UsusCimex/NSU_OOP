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
import ru.nsu.pacman.enemy.Pacman;
import ru.nsu.pacman.enemy.ghosts.RedGhost;
import ru.nsu.pacman.generation.LevelBuilder;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceArray;

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
    private final Image pacmanRightIMG = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/right.gif")));
    private final Image pacmanLeftIMG = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/left.gif")));
    private final Image pacmanUpIMG = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/up.gif")));
    private final Image pacmanDownIMG = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/down.gif")));
    private final Image pacmanStoppedIMG = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/stopped.png")));

    private final Image redGhostIMG = new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif")));
    private GridPane area = null;

    public static final int CELL_SIZE = 32;
    public static final int CELL_N = 21;
    private int currentLevel = 0;
    public static final int COUNT_LEVELS = 5;
    private int maxFood = 5;
    private boolean inGame = false;
    private boolean pause = false;

    // Enemies
    private Pacman pacman;
    private ImageView pacmanView;
    private RedGhost redGhost;
    private ImageView redGhostView;

    public static void main(String[] args) {
        launch(args);
    }

    private ArrayList<Coordinates> getPositionsInData(LevelData data, LevelData.Symbols symbol) {
        ArrayList<Coordinates> arr = new ArrayList<>();
        for (int col = 0; col < CELL_N; ++col) {
            for (int row = 0; row < CELL_N; ++row) {
                if (data.getValueLevelData(new Coordinates(col, row)) == LevelData.Symbols.Pacman) {
                    arr.add(new Coordinates(col, row));
                }
            }
        }
        return arr;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LevelData data = generateNextLevel();
        LevelBuilder builder = new LevelBuilder();
        area = builder.buildLevel(data);
        Pane root = new Pane(area);
        Scene scene = new Scene(root);
        scene.setFill(Color.AQUA);
        maxFood = data.getCountFood();

        if (getPositionsInData(data, LevelData.Symbols.Pacman).size() != 1) {
            throw new Exception("There can only be one pacman in the game");
        }
        pacman = new Pacman(getPositionsInData(data, LevelData.Symbols.Pacman).get(0), area, data);
//        redGhost = new RedGhost(new Coordinates(5 * CELL_SIZE,5 * CELL_SIZE), area, data); //factory

        pacmanView = new ImageView(pacmanRightIMG);
        pacmanView.setFitWidth(CELL_SIZE);
        pacmanView.setFitHeight(CELL_SIZE);
//        redGhostView = new ImageView(redGhostIMG); //After we use factory
//        redGhostView.setFitWidth(CELL_SIZE);
//        redGhostView.setFitHeight(CELL_SIZE);

        root.getChildren().add(pacmanView);
        pacmanView.setLayoutX(pacman.getPosition().x);
        pacmanView.setLayoutY(pacman.getPosition().y);
//        root.getChildren().add(redGhostView); //factory
//        redGhostView.setLayoutX(redGhost.getPosition().x);
//        redGhostView.setLayoutY(redGhost.getPosition().y);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                pacman.changeNextOrientation(Orientation.UP);
            } else if (event.getCode() == KeyCode.DOWN) {
                pacman.changeNextOrientation(Orientation.DOWN);
            } else if (event.getCode() == KeyCode.LEFT) {
                pacman.changeNextOrientation(Orientation.LEFT);
            } else if (event.getCode() == KeyCode.RIGHT) {
                pacman.changeNextOrientation(Orientation.RIGHT);
            } else if (event.getCode() == KeyCode.ESCAPE) {
                pause = !pause;
                pacmanView.setImage(pacmanStoppedIMG);
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setScene(scene);
        primaryStage.show();

        inGame = true;
    }

    private void update() {
        if (inGame && !pause) {
            //PacmanAnimation
            pacman.move();

            pacmanView.setLayoutX(pacman.getPosition().x);
            pacmanView.setLayoutY(pacman.getPosition().y);

            if (pacman.getCurrentOrientation() == Orientation.UP) {
                pacmanView.setImage(pacmanUpIMG);
            } else if (pacman.getCurrentOrientation() == Orientation.LEFT) {
                pacmanView.setImage(pacmanLeftIMG);
            } else if (pacman.getCurrentOrientation() == Orientation.RIGHT) {
                pacmanView.setImage(pacmanRightIMG);
            } else if (pacman.getCurrentOrientation() == Orientation.DOWN) {
                pacmanView.setImage(pacmanDownIMG);
            } else {
                pacmanView.setImage(pacmanStoppedIMG);
            }

            if (pacman.getFoodEat() == maxFood) {
                inGame = false;
                System.out.println("GAME OVER :D");
            }

            //GhostsAnimation
//            redGhost.move();
//
//            redGhostView.setLayoutX(redGhostView.getPosition().x);
//            redGhostView.setLayoutY(redGhostView.getPosition().y);
//
//            if (redGhost.getCurrentOrientation() == Orientation.UP) {
//                redGhostView.setImage(redGhostIMG);
//            } else if (redGhost.getCurrentOrientation() == Orientation.LEFT) {
//                redGhostView.setImage(redGhostIMG);
//            } else if (redGhost.getCurrentOrientation() == Orientation.RIGHT) {
//                redGhostView.setImage(redGhostIMG);
//            } else if (redGhost.getCurrentOrientation() == Orientation.DOWN) {
//                redGhostView.setImage(redGhostIMG);
//            }
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
