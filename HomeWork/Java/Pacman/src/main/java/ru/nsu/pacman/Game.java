package ru.nsu.pacman;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.nsu.pacman.data.Controller;
import ru.nsu.pacman.data.GameData;
import ru.nsu.pacman.data.GameTimer;
import ru.nsu.pacman.data.Graphic;
import ru.nsu.pacman.entity.Entity;
import ru.nsu.pacman.entity.Pacman;
import ru.nsu.pacman.entity.ghosts.Ghost;
import ru.nsu.pacman.generation.LevelData;

import java.util.*;

import static java.lang.Math.*;
import static ru.nsu.pacman.data.GameData.EntityData;
import static ru.nsu.pacman.entity.ghosts.Ghost.GhostState.*;

public class Game extends Application {
    public static final int CELL_SIZE = 32;
    public static final int CELL_N = 21;
    private static final int TIMECICLE = 20;
    private static final int MEGAFOODDURATION = 10;
    public final static int MAXLEVEL = 3;
    private static boolean signalToChangeMode = false;
    private final GameData.PlayerRecord player;
    private Controller.Context context;

    public static void signalChangeMode() {
        signalToChangeMode = true;
    }
    public Game(GameData.PlayerRecord player) {
        this.player = player;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PacmanGame");
        primaryStage.setResizable(false);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(icon);
        LevelData data = generateLevel(player.getLevel());
        assert data != null;

        StackPane root = Graphic.generateMainRoot(data);
        Scene scene = new Scene(root);

        Graphic.settingIMG(data.getAllEntities());
        Graphic.addAllEnemiesInGamePane(data.getAllEntities());

        ArrayList<GameTimer> allTimers = new ArrayList<>();
        GameTimer timeToDestroyBarriers = new GameTimer(Duration.seconds(10), 1, data::removeAllBarriers);
        allTimers.add(timeToDestroyBarriers);

        primaryStage.setScene(scene);
        primaryStage.show();

        Graphic.rewriteScore(player.getScore() + data.getEatedFood());
        Graphic.rewriteLives(player.getLives());
        Graphic.rewriteName(player.getName());

        GameData.GameStatus status = GameData.GameStatus.GAME;

        GameTimer gameCicle = new GameTimer(Duration.millis(TIMECICLE), Timeline.INDEFINITE, this::update);
        allTimers.add(gameCicle);

        context = new Controller.Context(scene, data, status, player, allTimers);
        Controller.setDefaultGameControl(context);
        context.playAllTimers();
    }

    private double getDistance(Entity entityA, Entity entityB) {
        return max(abs(entityA.getPosition().x - entityB.getPosition().x), abs(entityA.getPosition().y - entityB.getPosition().y));
    }

    private void update() {
        if (context.getStatus() == GameData.GameStatus.GAME) {
            Graphic.update();
            if (signalToChangeMode) {
                signalToChangeMode = false;
                Ghost.changeAllStates(context.getData().getAllEntities(), SCARED);
                GameTimer timerScaredMode = new GameTimer(Duration.seconds(MEGAFOODDURATION), 1, () -> Ghost.changeAllStates(context.getData().getAllEntities(), DEFAULT));
                context.addTimer(timerScaredMode);
                timerScaredMode.play();
            }

            Graphic.rewriteScore(player.getScore() + context.getData().getEatedFood());
            //GhostsAnimation
            for (EntityData enemy : context.getData().getAllEntities()) {
                enemy.body.move();
                Graphic.rewriteEnemy(enemy);

                //Check mob collision
                if (enemy.body.getClass() != Pacman.class) {
                    Ghost ghost = (Ghost) enemy.body;
                    if (getDistance(context.getData().getPacman().body, enemy.body) <= CELL_SIZE * 0.8) {
                        if (ghost.getState() == DEFAULT) {
                            player.loseLive();
                            Graphic.rewriteLives(player.getLives());
                            if (player.getLives() == 0) {
                                context.setStatus(GameData.GameStatus.LOSE);
                            } else {
                                context.setStatus(GameData.GameStatus.WAITRESPAWN);
                            }
                            return;
                        } else if (ghost.getState() == SCARED) {
                            ghost.setState(DEAD);
                            player.addToScore(5);
                        }
                    }
                }
            }

            //Check finish
            if (context.getData().getEatedFood() == context.getData().getCountFood() || context.getData().getEatedFood() >= 10) {
                context.setStatus(GameData.GameStatus.WIN);
            }
        } else {
            context.pauseAllTimers();
            Graphic.printText(context.getStatus());
        }
    }

    private LevelData generateLevel(int level) {
        try {
            if (level == 1) {
                return new LevelData(getClass().getResourceAsStream("levels/1.txt"));
            } else if (level == 2) {
                return new LevelData(getClass().getResourceAsStream("levels/2.txt"));
            } else if (level == 3) {
                return new LevelData(getClass().getResourceAsStream("levels/3.txt"));
            } else {
                throw new Exception("level not found!");
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }
}
