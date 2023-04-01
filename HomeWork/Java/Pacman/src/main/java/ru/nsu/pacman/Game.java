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
import ru.nsu.pacman.menu.RecordsTable;

import java.io.IOException;
import java.util.*;

import static java.lang.Math.*;
import static ru.nsu.pacman.GameData.Orientation;
import static ru.nsu.pacman.GameData.EnemyData;

public class Game extends Application {
    public static final int CELL_SIZE = 32;
    public static final int CELL_N = 21;

    private LevelData data = null;
    private Scene scene = null;
    private final static int MAXLEVEL = 3;
    private int curLevel = 0;
    private GameData.GameStatus status = GameData.GameStatus.NONE;
    private EnemyData pacman = null;
    private boolean waitMode = false;
    private ArrayList<EnemyData> enemies = null;
    private GameData.PlayerRecord player = null;
    private int lives = 5;


    public Game(GameData.PlayerRecord player, int level, int countLives) throws Exception {
        this.player = player;
        this.curLevel = level;
        this.lives = countLives;
        data = generateLevel(level);

        start(new Stage());
    }
    private EnemyData searchPacman() {
        if (enemies == null) enemies = data.getAllEnemies();

        boolean pacmanChecker = false;
        EnemyData tempPacman = null;
        for (GameData.EnemyData enemy : enemies) {
            if (enemy.body.getClass() == Pacman.class) {
                if (!pacmanChecker) {
                    tempPacman = enemy;
                    pacmanChecker = true;
                } else {
                    System.err.println("In game play only 1 PACMAN!");
                    throw new RuntimeException();
                }
            }
        }
        if (!pacmanChecker) {
            System.err.println("PACMAN NOT FOUND!");
            throw new RuntimeException();
        }
        return tempPacman;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PacmanGame");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(icon);

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
        pacman = searchPacman();

        Graphic.settingIMG(enemies);
        Graphic.addAllEnemiesInGamePane(enemies);

        setDefaultControl();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Timeline timeToDestroyBarriers = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            data.removeAllBarriers();
        }));
        timeToDestroyBarriers.setCycleCount(1);
        timeToDestroyBarriers.play();

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
                        lives--;
                        if (lives == 0) {
                            status = GameData.GameStatus.LOSE;
                        } else {
                            status = GameData.GameStatus.WAITRESPAWN;
                        }
                        return;
                    }
                }
            }

            //Check finish
            if (data.getEatedFood() == data.getCountFood()) {
                status = GameData.GameStatus.WIN;
            }
        } else if (!waitMode){
            waitMode = true;
            Graphic.printText(status);
            if (status == GameData.GameStatus.WAITRESPAWN) {
                scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        Graphic.removeText();
                        setDefaultControl();

                        Graphic.removeAllEnemiesInGamePane(enemies);
                        data.resetAllEnemies();
                        enemies = data.getAllEnemies();
                        pacman = data.getPacman();

                        Graphic.settingIMG(enemies);
                        Graphic.addAllEnemiesInGamePane(enemies);

                        status = GameData.GameStatus.GAME;
                        waitMode = false;
                    } else if (event.getCode() == KeyCode.ESCAPE) {
                        Graphic.removeText();

                        try {
                            player.addToScore(data.getEatedFood());
                            RecordsTable.addPlayerRecord(player);

                            MainMenu mainMenu = new MainMenu();
                            mainMenu.start(new Stage());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        Stage stage = (Stage) scene.getWindow();
                        stage.close();
                    }
                });
            } else if (status == GameData.GameStatus.LOSE) {
                scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        Graphic.removeText();

                        try {
                            player.addToScore(data.getEatedFood());
                            RecordsTable.addPlayerRecord(player);

                            MainMenu mainMenu = new MainMenu();
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
                        try {
                            if (curLevel == MAXLEVEL) {
                                RecordsTable.addPlayerRecord(player);

                                MainMenu mainMenu = new MainMenu();
                                mainMenu.start(new Stage());
                            } else {
                                player.addToScore(data.getEatedFood());
                                new Game(player, curLevel + 1, lives + 1);
                            }
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
                        setDefaultControl();
                        Graphic.removeText();

                        status = GameData.GameStatus.GAME;
                        waitMode = false;
                    }
                });
            }
        }
    }

    private LevelData generateLevel(int level) {
        try {
            if (level == 1) return new LevelData(getClass().getResourceAsStream("levels/1.txt"));
            else if (level == 2) return new LevelData(getClass().getResourceAsStream("levels/2.txt"));
            else if (level == 3) return new LevelData(getClass().getResourceAsStream("levels/3.txt"));
            else throw new Exception("level not found!");
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }
}
