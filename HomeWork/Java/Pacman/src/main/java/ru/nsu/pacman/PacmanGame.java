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
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.enemy.EnemyFactory;
import ru.nsu.pacman.enemy.Pacman;
import ru.nsu.pacman.generation.LevelBuilder;
import ru.nsu.pacman.generation.LevelData;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static java.lang.Math.*;

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
    
    public static class EnemyData {
        public Enemy enemy;
        public ImageView imageView = new ImageView();
        
        public EnemyData(Enemy enemy) {
            this.enemy = enemy;

            this.imageView.setFitWidth(CELL_SIZE);
            this.imageView.setFitHeight(CELL_SIZE);

            this.imageView.setLayoutX(enemy.getPosition().x);
            this.imageView.setLayoutY(enemy.getPosition().y);
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
    LevelData data = null;

    public static final int CELL_SIZE = 32;
    public static final int CELL_N = 21;
    private int currentLevel = 0;
    private boolean inGame = false;
    private boolean pause = false;
    EnemyData pacman;
    ArrayList<EnemyData> enemies;

    public static void main(String[] args) {
        launch(args);
    }

    private ArrayList<Coordinates> getPositionsInData(LevelData data, LevelData.Symbols symbol) {
        ArrayList<Coordinates> arr = new ArrayList<>();
        for (int col = 0; col < CELL_N; ++col) {
            for (int row = 0; row < CELL_N; ++row) {
                if (data.getValueLevelData(new Coordinates(col, row)) == symbol) {
                    arr.add(new Coordinates(col, row));
                }
            }
        }
        return arr;
    }
    private ArrayList<EnemyData> getAllEnemies(LevelData data) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        ArrayList<EnemyData> result = new ArrayList<EnemyData>();
        for (int col = 0; col < CELL_N; ++col) {
            for (int row = 0; row < CELL_N; ++row) {
                Enemy enemy = EnemyFactory.getInstance().createEnemy(data.getValueLevelData(new Coordinates(col, row)), new Coordinates(col, row), area, data);
                if (enemy != null && enemy.getClass() != Pacman.class) {
                    result.add(new EnemyData(enemy));
                }
            }
        }
        return result;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        data = generateNextLevel();
        LevelBuilder builder = new LevelBuilder();
        area = builder.buildLevel(data);
        Pane root = new Pane(area);
        Scene scene = new Scene(root);
        scene.setFill(Color.AQUA);

        if (getPositionsInData(data, LevelData.Symbols.Pacman).size() != 1) {
            throw new Exception("There can only be one pacman in the game");
        }
        pacman = new EnemyData(new Pacman(getPositionsInData(data, LevelData.Symbols.Pacman).get(0), area, data));
        pacman.imageView.setImage(pacmanStoppedIMG);
        enemies = getAllEnemies(data);

        root.getChildren().add(pacman.imageView);
        for(int i = 0; i < enemies.size(); ++i) {
            root.getChildren().add(enemies.get(i).imageView);
        }

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                pacman.enemy.changeNextOrientation(Orientation.UP);
            } else if (event.getCode() == KeyCode.DOWN) {
                pacman.enemy.changeNextOrientation(Orientation.DOWN);
            } else if (event.getCode() == KeyCode.LEFT) {
                pacman.enemy.changeNextOrientation(Orientation.LEFT);
            } else if (event.getCode() == KeyCode.RIGHT) {
                pacman.enemy.changeNextOrientation(Orientation.RIGHT);
            } else if (event.getCode() == KeyCode.ESCAPE) {
                pause = !pause;
                pacman.imageView.setImage(pacmanStoppedIMG);
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setScene(scene);
        primaryStage.show();

        inGame = true;
    }

    private double getDistance(Enemy enemyA, Enemy enemyB) {
        return max(abs(enemyA.getPosition().x - enemyB.getPosition().x), abs(enemyA.getPosition().y - enemyB.getPosition().y));
    }

    private void update() {
        if (inGame && !pause) {
            //PacmanAnimation
            pacman.enemy.move();

            pacman.imageView.setLayoutX(pacman.enemy.getPosition().x);
            pacman.imageView.setLayoutY(pacman.enemy.getPosition().y);

            if (pacman.enemy.getCurrentOrientation() == Orientation.UP) {
                pacman.imageView.setImage(pacmanUpIMG);
            } else if (pacman.enemy.getCurrentOrientation() == Orientation.LEFT) {
                pacman.imageView.setImage(pacmanLeftIMG);
            } else if (pacman.enemy.getCurrentOrientation() == Orientation.RIGHT) {
                pacman.imageView.setImage(pacmanRightIMG);
            } else if (pacman.enemy.getCurrentOrientation() == Orientation.DOWN) {
                pacman.imageView.setImage(pacmanDownIMG);
            } else {
                pacman.imageView.setImage(pacmanStoppedIMG);
            }

            if (data.getEatedFood() == data.getCountFood()) {
                inGame = false;
                System.out.println("YOU WIN!");
            }

            //GhostsAnimation
            for (int i = 0; i < enemies.size(); ++i) {
                EnemyData ghost = enemies.get(i);

                ghost.enemy.move();

                ghost.imageView.setLayoutX(ghost.enemy.getPosition().x);
                ghost.imageView.setLayoutY(ghost.enemy.getPosition().y);

                if (ghost.enemy.getCurrentOrientation() == Orientation.UP) {
                    ghost.imageView.setImage(redGhostIMG);
                } else if (ghost.enemy.getCurrentOrientation() == Orientation.LEFT) {
                    ghost.imageView.setImage(redGhostIMG);
                } else if (ghost.enemy.getCurrentOrientation() == Orientation.RIGHT) {
                    ghost.imageView.setImage(redGhostIMG);
                } else if (ghost.enemy.getCurrentOrientation() == Orientation.DOWN) {
                    ghost.imageView.setImage(redGhostIMG);
                }

                //Check mob collision
                if (getDistance(pacman.enemy, ghost.enemy) <= CELL_SIZE * 0.8) {
                    System.out.println("YOU LOSE!");
                    inGame = false;
                    break;
                }
            }
        }
    }

    private LevelData generateNextLevel() {
        currentLevel += 1;
        try {
            if (currentLevel == 1) return new LevelData(getClass().getResourceAsStream("levels/1.txt"));
            else if (currentLevel == 2) return new LevelData(getClass().getResourceAsStream("levels/2.txt"));
            else if (currentLevel == 3) return new LevelData(getClass().getResourceAsStream("levels/3.txt"));
            else inGame = false;
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }
}
