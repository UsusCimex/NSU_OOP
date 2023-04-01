package ru.nsu.pacman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.enemy.Pacman;
import ru.nsu.pacman.generation.LevelBuilder;
import ru.nsu.pacman.generation.LevelData;
import ru.nsu.pacman.menu.MainMenu;

import java.util.*;

import static java.lang.Math.*;
import static ru.nsu.pacman.GameData.Orientation;
import static ru.nsu.pacman.GameData.EnemyData;

public class Game extends Application {
    public static final int CELL_SIZE = 32;
    public static final int CELL_N = 21;

    private LevelData data = null;
    private Scene scene = null;
    private int currentLevel = 0;
    private GameData.GameStatus status = GameData.GameStatus.NONE;
    private EnemyData pacman;
    private ArrayList<EnemyData> enemies;

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
        GridPane area = builder.buildLevel(data);
        area.setId("area");
        Pane gamePane = new Pane(area);
        gamePane.setId("gamePane");

        StackPane root = new StackPane();
        root.getChildren().add(gamePane);
        root.setStyle("-fx-background-color: AQUA;");

        Graphic.setMainRoot(root);

        scene = new Scene(root);

        enemies = data.getAllEnemies();

        if (!Graphic.settingIMG(enemies) || !addAllEnemiesInRoot(gamePane)) {
            return;
        }

        setDefaultControl();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setScene(scene);
        primaryStage.show();

        status = GameData.GameStatus.GAME;
    }

    private void setDefaultControl() {
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
                status = GameData.GameStatus.PAUSE;
            }
        });
    }

    private double getDistance(Enemy enemyA, Enemy enemyB) {
        return max(abs(enemyA.getPosition().x - enemyB.getPosition().x), abs(enemyA.getPosition().y - enemyB.getPosition().y));
    }

    private void update() {
        if (status == GameData.GameStatus.GAME) {
            //GhostsAnimation
            for (EnemyData enemy : enemies) {
                enemy.body.move();
                Graphic.rewriteEnemy(enemy);

                //Check mob collision
                if (enemy.body.getClass() != Pacman.class) {
                    if (getDistance(pacman.body, enemy.body) <= CELL_SIZE * 0.8) {
                        status = GameData.GameStatus.LOSE;
                        return;
                    }
                }
            }

            //Check finish
            if (data.getEatedFood() == data.getCountFood()) {
                status = GameData.GameStatus.WIN;
            }
        } else {
            Graphic.printText(status);
            if (status == GameData.GameStatus.LOSE) {
                scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        Graphic.removeText();

                        MainMenu mainMenu = new MainMenu();
                        try {
                            mainMenu.start(new Stage());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        Stage stage = (Stage) scene.getWindow();
                        stage.close();
                    }
                });
            } else if (status == GameData.GameStatus.WIN) {
                scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        Graphic.removeText();

                        MainMenu mainMenu = new MainMenu();
                        try {
                            mainMenu.start(new Stage());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        Stage stage = (Stage) scene.getWindow();
                        stage.close();
                    }
                });
            } else if (status == GameData.GameStatus.PAUSE) {
                scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        Graphic.removeText();

                        setDefaultControl();
                        status = GameData.GameStatus.GAME;
                    }
                });
            }
        }
    }

    private LevelData generateNextLevel() {
        currentLevel += 1;
        try {
            if (currentLevel == 1) return new LevelData(getClass().getResourceAsStream("levels/1.txt"));
            else if (currentLevel == 2) return new LevelData(getClass().getResourceAsStream("levels/2.txt"));
            else if (currentLevel == 3) return new LevelData(getClass().getResourceAsStream("levels/3.txt"));
            else status = GameData.GameStatus.NONE;
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }
}
