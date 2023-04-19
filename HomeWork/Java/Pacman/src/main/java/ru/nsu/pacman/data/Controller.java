package ru.nsu.pacman.data;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.nsu.pacman.Game;
import ru.nsu.pacman.entity.ghosts.Ghost;
import ru.nsu.pacman.generation.LevelData;
import ru.nsu.pacman.menu.MainMenu;
import ru.nsu.pacman.menu.RecordsTable;

import java.util.ArrayList;

import static ru.nsu.pacman.Game.MAXLEVEL;
import static ru.nsu.pacman.Game.MEGAFOODDURATION;
import static ru.nsu.pacman.entity.ghosts.Ghost.GhostState.DEFAULT;
import static ru.nsu.pacman.entity.ghosts.Ghost.GhostState.SCARED;
import static ru.nsu.pacman.menu.MainMenu.FIRSTLEVEL;
import static ru.nsu.pacman.menu.MainMenu.FIRSTLIVES;

public abstract class Controller {
    public static void setMainMenuControl(TextField nameEnter, MainMenu menu) {
        nameEnter.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    menu.startEvent(new ActionEvent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static class Context {
        private final Scene scene;
        private final LevelData data;
        private GameData.GameStatus status;
        private final GameData.PlayerRecord player;
        private final ArrayList<GameTimer> timers;
        private final GameTimer eventTimer = new GameTimer(Duration.seconds(MEGAFOODDURATION), 1, () -> Ghost.changeAllStates(getData().getAllEntities(), DEFAULT));
        public Context(Scene scene, LevelData data, GameData.GameStatus status, GameData.PlayerRecord player, ArrayList<GameTimer> timers) {
            this.scene = scene;
            this.data = data;
            this.status = status;
            this.player = player;
            this.timers = timers;
        }

        public void setStatus(GameData.GameStatus newStatus) {
            status = newStatus;
        }
        public GameData.GameStatus getStatus() {
            return status;
        }
        public void pauseAllTimers() {
            for (GameTimer timer : timers) {
                timer.pause();
            }
            eventTimer.pause();
        }
        public void resumeAllTimers() {
            for (GameTimer timer : timers) {
                timer.resume();
            }
            eventTimer.resume();
        }
        public void playAllTimers() {
            for (GameTimer timer : timers) {
                timer.play();
            }
            eventTimer.play();
        }
        public void addTimer(GameTimer newTimer) {
            timers.add(newTimer);
        }
        public void resetScarredEvent() {
            eventTimer.stop();
            eventTimer.play();
            Ghost.changeAllStates(getData().getAllEntities(), SCARED);
        }
        public GameData.PlayerRecord getPlayer() {
            return player;
        }
        public LevelData getData() {
            return data;
        }
        public Scene getScene() {
            return scene;
        }
    }
    private static void handleKeyPressed(KeyEvent event, Context context) {
        if (context.getStatus() == GameData.GameStatus.GAME) {
            if (event.getCode() == KeyCode.UP) {
                context.data.getPacman().body.changeNextOrientation(GameData.Orientation.UP);
            } else if (event.getCode() == KeyCode.DOWN) {
                context.data.getPacman().body.changeNextOrientation(GameData.Orientation.DOWN);
            } else if (event.getCode() == KeyCode.LEFT) {
                context.data.getPacman().body.changeNextOrientation(GameData.Orientation.LEFT);
            } else if (event.getCode() == KeyCode.RIGHT) {
                context.data.getPacman().body.changeNextOrientation(GameData.Orientation.RIGHT);
            } else if (event.getCode() == KeyCode.ESCAPE) {
                context.setStatus(GameData.GameStatus.PAUSE);
            }
        } else if (context.getStatus() == GameData.GameStatus.WAITRESPAWN) {
            if (event.getCode() == KeyCode.ENTER) {
                Graphic.removeText();

                Graphic.removeAllEnemiesInGamePane(context.data.getAllEntities());
                context.data.resetAllEntities();

                Graphic.settingIMG(context.data.getAllEntities());
                Graphic.addAllEnemiesInGamePane(context.data.getAllEntities());

                context.setStatus(GameData.GameStatus.GAME);
                context.resumeAllTimers();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                Graphic.removeText();

                try {
                    context.player.addToScore(context.data.getEatedFood());
                    RecordsTable.addPlayerRecord(context.player);

                    MainMenu mainMenu = new MainMenu();
                    mainMenu.start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Stage stage = (Stage) context.scene.getWindow();
                stage.close();
            }
        } else if (context.getStatus() == GameData.GameStatus.LOSE) {
            if (event.getCode() == KeyCode.ESCAPE) {
                Graphic.removeText();

                try {
                    context.player.addToScore(context.data.getEatedFood());
                    RecordsTable.addPlayerRecord(context.player);

                    MainMenu mainMenu = new MainMenu();
                    mainMenu.start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Stage stage = (Stage) context.scene.getWindow();
                stage.close();
            } else if (event.getCode() ==  KeyCode.ENTER) {
                Graphic.removeText();
                Game newGame = new Game(new GameData.PlayerRecord(context.player.getName(), 0, FIRSTLEVEL, FIRSTLIVES));
                newGame.start(new Stage());

                Stage stage = (Stage) context.scene.getWindow();
                stage.close();
            }
        } else if (context.getStatus() == GameData.GameStatus.WIN) {
            if (event.getCode() == KeyCode.ENTER) {
                Graphic.removeText();
                try {
                    if (context.player.getLevel() == MAXLEVEL) {
                        context.player.addToScore(context.data.getEatedFood());
                        RecordsTable.addPlayerRecord(context.player);

                        MainMenu mainMenu = new MainMenu();
                        mainMenu.start(new Stage());
                    } else {
                        context.player.addToScore(context.data.getEatedFood());
                        Game nextGame = new Game(new GameData.PlayerRecord(context.player.getName(), context.player.getScore(), context.player.getLevel() + 1, context.player.getLives() + 1));
                        nextGame.start(new Stage());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Stage stage = (Stage) context.scene.getWindow();
                stage.close();
            }
        } else if (context.getStatus() == GameData.GameStatus.PAUSE) {
            if (event.getCode() == KeyCode.ESCAPE) {
                Graphic.removeText();

                context.setStatus(GameData.GameStatus.GAME);
                context.resumeAllTimers();
            }
        }
    }
    public static void setDefaultGameControl(Context context) {
        context.scene.setOnKeyPressed(event -> handleKeyPressed(event, context));
    }
}
