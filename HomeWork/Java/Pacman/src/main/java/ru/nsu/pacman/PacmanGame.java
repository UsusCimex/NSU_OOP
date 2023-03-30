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
import ru.nsu.pacman.enemy.ghosts.BlueGhost;
import ru.nsu.pacman.enemy.ghosts.OrangeGhost;
import ru.nsu.pacman.enemy.ghosts.PinkGhost;
import ru.nsu.pacman.enemy.ghosts.RedGhost;
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
    }

    public static class Coordinates {
        public double x;
        public double y;

        public Coordinates(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public static class EnemyData {
        public Enemy body;
        public ImageView view = new ImageView();

        private Image passiveIMG;
        private Image leftIMG;
        private Image rightIMG;
        private Image upIMG;
        private Image downIMG;

        public EnemyData(Enemy enemy) {
            body = enemy;

            view.setFitWidth(CELL_SIZE);
            view.setFitHeight(CELL_SIZE);

            view.setLayoutX(enemy.getPosition().x);
            view.setLayoutY(enemy.getPosition().y);
        }

        public void setImages(Image passive, Image left, Image right, Image up, Image down) {
            passiveIMG = passive;
            leftIMG = left;
            rightIMG = right;
            upIMG = up;
            downIMG = down;
        }

        public void changeOrientationVew() {
            if (body.getCurrentOrientation() == Orientation.UP) {
                view.setImage(upIMG);
            } else if (body.getCurrentOrientation() == Orientation.LEFT) {
                view.setImage(leftIMG);
            } else if (body.getCurrentOrientation() == Orientation.RIGHT) {
                view.setImage(rightIMG);
            } else if (body.getCurrentOrientation() == Orientation.DOWN) {
                view.setImage(downIMG);
            } else {
                view.setImage(passiveIMG);
            }
        }
    }
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

    private ArrayList<EnemyData> getAllEnemies(LevelData data) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        ArrayList<EnemyData> result = new ArrayList<EnemyData>();
        for (int col = 0; col < CELL_N; ++col) {
            for (int row = 0; row < CELL_N; ++row) {
                Enemy enemy = EnemyFactory.getInstance().createEnemy(data.getValueLevelData(new Coordinates(col, row)), new Coordinates(col, row), area, data);
                if (enemy != null) {
                    result.add(new EnemyData(enemy));
                }
            }
        }
        return result;
    }
    private boolean settingIMG() {
        for (EnemyData enemy : enemies) {
            if (enemy.body.getClass().equals(Pacman.class)) {
                enemy.setImages(new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/stopped.png"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/left.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/up.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/pacman/down.gif"))));
            } else if (enemy.body.getClass().equals(RedGhost.class)) {
                enemy.setImages(new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))));
            } else if (enemy.body.getClass().equals(BlueGhost.class)) {
                enemy.setImages(new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))));
            } else if (enemy.body.getClass().equals(OrangeGhost.class)) {
                enemy.setImages(new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))));
            } else if (enemy.body.getClass().equals(PinkGhost.class)) {
                enemy.setImages(new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream("sprites/ghosts/red/right.gif"))));
            } else {
                System.out.println("Enemy not found! Add new enemy in setting IMG");
                return false;
            }
        }
        return true;
    }

    private boolean addAllEnemiesInRoot(Pane root) {
        boolean pacmanChecker = false;
        for(int i = 0; i < enemies.size(); ++i) {
            if (enemies.get(i).body.getClass() == Pacman.class) {
                if (!pacmanChecker) {
                    System.out.println("PACMAN ADDED!");
                    pacman = enemies.get(i);
                    pacmanChecker = true;
                } else {
                    System.out.println("In game play only 1 PACMAN!");
                    return false;
                }
            }
            root.getChildren().add(enemies.get(i).view);
        }
        if (!pacmanChecker) {
            System.out.println("PACMAN NOT FOUND!");
            return false;
        }
        return true;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        data = generateNextLevel();
        LevelBuilder builder = new LevelBuilder();
        area = builder.buildLevel(data);
        Pane root = new Pane(area);
        Scene scene = new Scene(root);
        scene.setFill(Color.AQUA);

        enemies = getAllEnemies(data);

        if (!settingIMG()) {
            return;
        }
        if (!addAllEnemiesInRoot(root)) {
            return;
        }

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                pacman.body.changeNextOrientation(Orientation.UP);
            } else if (event.getCode() == KeyCode.DOWN) {
                pacman.body.changeNextOrientation(Orientation.DOWN);
            } else if (event.getCode() == KeyCode.LEFT) {
                pacman.body.changeNextOrientation(Orientation.LEFT);
            } else if (event.getCode() == KeyCode.RIGHT) {
                pacman.body.changeNextOrientation(Orientation.RIGHT);
            } else if (event.getCode() == KeyCode.ESCAPE) {
                pause = !pause;
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
            //GhostsAnimation
            for (int i = 0; i < enemies.size(); ++i) {
                EnemyData enemy = enemies.get(i);

                enemy.body.move();

                enemy.view.setLayoutX(enemy.body.getPosition().x);
                enemy.view.setLayoutY(enemy.body.getPosition().y);

                enemy.changeOrientationVew();

                //Check mob collision
                if (enemy.body.getClass() != Pacman.class) {
                    if (getDistance(pacman.body, enemy.body) <= CELL_SIZE * 0.8) {
                        System.out.println("YOU LOSE!");
                        inGame = false;
                        break;
                    }
                }
            }

            //Check finish
            if (data.getEatedFood() == data.getCountFood()) {
                inGame = false;
                System.out.println("YOU WIN!");
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
