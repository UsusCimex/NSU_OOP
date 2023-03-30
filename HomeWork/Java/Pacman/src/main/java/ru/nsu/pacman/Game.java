package ru.nsu.pacman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
import static ru.nsu.pacman.Controller.Orientation;
import static ru.nsu.pacman.Controller.Coordinates;
import static ru.nsu.pacman.Controller.EnemyData;

public class Game extends Application {
    public static final int CELL_SIZE = 32;
    public static final int CELL_N = 21;

    private GridPane area = null;
    private LevelData data = null;
    private StackPane root = null;
    private int currentLevel = 0;
    private boolean inGame = false;
    private boolean waitMode = false;
    private boolean lose = false;
    private boolean pause = false;
    private EnemyData pacman;
    private ArrayList<EnemyData> enemies;

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
        for (EnemyData enemy : enemies) {
            if (enemy.body.getClass() == Pacman.class) {
                if (!pacmanChecker) {
                    System.out.println("PACMAN ADDED!");
                    pacman = enemy;
                    pacmanChecker = true;
                } else {
                    System.out.println("In game play only 1 PACMAN!");
                    return false;
                }
            }
            root.getChildren().add(enemy.view);
        }
        if (!pacmanChecker) {
            System.out.println("PACMAN NOT FOUND!");
            return false;
        }
        return true;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PacmanGame");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(icon);

        data = generateNextLevel();
        LevelBuilder builder = new LevelBuilder();
        area = builder.buildLevel(data);
        Pane gamePane = new Pane(area);

        root = new StackPane();
        root.getChildren().add(gamePane);
        root.setStyle("-fx-background-color: AQUA;");

        Scene scene = new Scene(root);

        enemies = getAllEnemies(data);

        if (!settingIMG()) {
            return;
        }
        if (!addAllEnemiesInRoot(gamePane)) {
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
                waitMode = false;
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

    private void printWin() {
        Text text1 = new Text("You WIN");
        text1.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 80));
        text1.setFill(Color.YELLOW);

        Text text2 = new Text("Press ENTER to continue!");
        text2.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 60));
        text2.setFill(Color.YELLOW);

        VBox vbox = new VBox(text1, text2);
        vbox.setAlignment(Pos.CENTER);

        root.getChildren().add(vbox);
    }

    private void printLose() {
        Text text1 = new Text("You LOSE");
        text1.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 80));
        text1.setFill(Color.YELLOW);
        text1.setStroke(Color.BLACK);
        text1.setStrokeWidth(2.0);

        Text text2 = new Text("Press ESC to exit!");
        text2.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 60));
        text2.setFill(Color.YELLOW);
        text2.setStroke(Color.BLACK);
        text2.setStrokeWidth(2.0);

        VBox vbox = new VBox(text1, text2);
        vbox.setAlignment(Pos.CENTER);

        root.getChildren().add(vbox);
    }
    private void printPause() {
        Text text1 = new Text("Game in pause!");
        text1.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 60));
        text1.setFill(Color.YELLOW);
        text1.setStroke(Color.BLACK);
        text1.setStrokeWidth(2.0);

        Text text2 = new Text("Press ESC to continue!");
        text2.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 40));
        text2.setFill(Color.YELLOW);
        text2.setStroke(Color.BLACK);
        text2.setStrokeWidth(2.0);

        VBox vbox = new VBox(text1, text2);
        vbox.setAlignment(Pos.CENTER);

        root.getChildren().add(vbox);
    }

    private void update() {
        if (inGame && !pause) {
            //GhostsAnimation
            for (EnemyData enemy : enemies) {
                enemy.body.move();

                enemy.view.setLayoutX(enemy.body.getPosition().x);
                enemy.view.setLayoutY(enemy.body.getPosition().y);

                enemy.changeOrientationVew();

                //Check mob collision
                if (enemy.body.getClass() != Pacman.class) {
                    if (getDistance(pacman.body, enemy.body) <= CELL_SIZE * 0.8) {
                        System.out.println("YOU LOSE!");
                        inGame = false;
                        lose = true;
                        break;
                    }
                }
            }

            //Check finish
            if (data.getEatedFood() == data.getCountFood()) {
                inGame = false;
                lose = false;
                System.out.println("YOU WIN!");
            }
        } else if (!inGame && !waitMode) {
            waitMode = true;
            if (lose) {
                printLose();
            }
            else {
                printWin();
            }
        } else if (pause && !waitMode) {
            waitMode = true;
            printPause();
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
