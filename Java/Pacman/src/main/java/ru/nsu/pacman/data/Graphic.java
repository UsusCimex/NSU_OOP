package ru.nsu.pacman.data;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ru.nsu.pacman.Game;
import ru.nsu.pacman.entity.Pacman;
import ru.nsu.pacman.entity.ghosts.*;
import ru.nsu.pacman.generation.LevelBuilder;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Objects;

import static ru.nsu.pacman.generation.LevelBuilder.CELL_SIZE;

public abstract class Graphic {
    private static LevelData data = null;
    private static StackPane root = null;
    private static boolean textIsWriten = false;
    private static GameData.GameStatus curTextStatus = GameData.GameStatus.NONE;
    private static ImageView cherryView = new ImageView(new Image(Game.class.getResourceAsStream("sprites/cherry.png")));
    public static ImageView getCherryView() {
        cherryView.setId("Cherry");
        GridPane.setHalignment(cherryView, HPos.CENTER);
        GridPane.setValignment(cherryView, VPos.CENTER);
        return cherryView;
    }

    public static void settingIMG(ArrayList<GameData.EntityData> enemies) {
        for (GameData.EntityData entity : enemies) {
            if (entity.body.getClass().equals(Pacman.class)) {
                entity.setImages(new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/pacman/stopped.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/pacman/left.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/pacman/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/pacman/up.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/pacman/down.gif"))));
            } else if (entity.body.getClass().equals(RedGhost.class)) {
                entity.setImages(new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/left.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/left.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/up.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/down.gif"))));
            } else if (entity.body.getClass().equals(BlueGhost.class)) {
                entity.setImages(new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/blue/left.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/blue/left.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/blue/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/blue/up.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/blue/down.gif"))));
            } else if (entity.body.getClass().equals(GreenGhost.class)) {
                entity.setImages(new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/green/left.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/green/left.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/green/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/green/up.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/green/down.gif"))));
            } else if (entity.body.getClass().equals(PinkGhost.class)) {
                entity.setImages(new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/pink/left.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/pink/left.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/pink/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/pink/up.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/pink/down.gif"))));
            } else {
                System.err.println("Entity not found! Add new entity in setting IMG");
                throw new RuntimeException();
            }
        }
    }
    public static void addAllEnemiesInGamePane(ArrayList<GameData.EntityData> enemies) {
        Pane gamePane = (Pane) root.lookup("#gamePane");
        for (GameData.EntityData entity : enemies) {
            gamePane.getChildren().add(entity.view);
        }
    }
    public static void removeAllEnemiesInGamePane(ArrayList<GameData.EntityData> enemies) {
        Pane gamePane = (Pane) root.lookup("#gamePane");
        for (GameData.EntityData entity : enemies) {
            gamePane.getChildren().remove(entity.view);
        }
    }
    public static StackPane generateMainRoot(LevelData data) {
        Graphic.data = data;
        LevelBuilder builder = new LevelBuilder();
        GridPane area = builder.buildLevel(data);
        area.setId("area");

        Pane gamePane = new Pane(area);
        gamePane.setId("gamePane");
        Text name = new Text();
        name.setFont(Font.font("OCR A Extended", 25));
        name.setId("name");
        Text countLives = new Text();
        countLives.setFont(Font.font("OCR A Extended", 25));
        countLives.setId("countLives");
        Text score = new Text();
        score.setFont(Font.font("OCR A Extended", 25));
        score.setId("score");

        HBox infoPain = new HBox(name, countLives, score);
        infoPain.setSpacing(20);
        infoPain.setAlignment(Pos.CENTER);
        infoPain.setId("infoPain");

        Label eventTimer = new Label("Scared: 0.0");
        eventTimer.setFont(Font.font("OCR A Extended", 50));
        eventTimer.setAlignment(Pos.CENTER);
        eventTimer.setId("eventTimer");

        VBox mainPain = new VBox(gamePane, infoPain, eventTimer);
        mainPain.setAlignment(Pos.CENTER);
        mainPain.setId("mainPain");

        root = new StackPane();
        root.getChildren().add(mainPain);
        root.setStyle("-fx-background-color: AQUA;");
        return root;
    }
    public static void update() {
        if (data == null || root == null) return;
        GridPane area = getArea();
        ArrayList<Node> removeList = new ArrayList<>();
        boolean isNewCherry = false;
        int colCherry = 0, rowCherry = 0;
        for (Node node : area.getChildren()) {
            if (data.getValueLevelData(new GameData.Coordinates(GridPane.getColumnIndex(node), GridPane.getRowIndex(node))) == LevelData.Symbols.Empty) {
                removeList.add(node);
            }
            if ((data.getValueLevelData(new GameData.Coordinates(GridPane.getColumnIndex(node), GridPane.getRowIndex(node))) == LevelData.Symbols.Cherry )&& (!Objects.equals(node.getId(), "Cherry"))) {
                isNewCherry = true;
                colCherry = GridPane.getColumnIndex(node);
                rowCherry = GridPane.getRowIndex(node);
                removeList.add(node);
            }
        }
        for (Node node : removeList) {
            area.getChildren().remove(node);
        }
        if (isNewCherry) {
            area.add(getCherryView(), colCherry, rowCherry);
        }
    }
    private static GridPane getArea() {
        return (GridPane) root.lookup("#gamePane #area");
    }
    private static Image scaredIMG = new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/death.gif")));
    private static Image getScaredImage() {
        return scaredIMG;
    }

    public static void rewriteEnemy(GameData.EntityData entity) {
        entity.view.setLayoutX(entity.body.getPosition().x);
        entity.view.setLayoutY(entity.body.getPosition().y);
        if (entity.body instanceof Ghost) {
            Ghost ghost = (Ghost) entity.body;
            if (ghost.getState() == Ghost.GhostState.DEFAULT) {
                entity.view.setImage(entity.getImages(entity.body.getCurrentOrientation()));
                entity.view.setOpacity(1);
            } else if (ghost.getState() == Ghost.GhostState.SCARED) {
                entity.view.setImage(getScaredImage());
                entity.view.setOpacity(1);
            } else {
                entity.view.setImage(getScaredImage());
                entity.view.setOpacity(0.2);
            }
        } else {
            entity.view.setImage(entity.getImages(entity.body.getCurrentOrientation()));
        }
    }
    public static void rewriteScore(int score) {
        Text scoreText = (Text)root.lookup("#mainPain #infoPain #score");
        scoreText.setText("Score: " + score);
    }
    public static void rewriteLives(int lives) {
        Text textLives = (Text)root.lookup("#mainPain #infoPain #countLives");
        textLives.setText("Lives: " + lives);
    }
    public static void rewriteName(String name) {
        Text textLives = (Text)root.lookup("#mainPain #infoPain #name");
        textLives.setText("Nickname: " + name);
    }
    public static void removeText() {
        if (textIsWriten) {
            if (curTextStatus == GameData.GameStatus.WIN) {
                VBox text = (VBox) root.lookup("#winText");
                root.getChildren().remove(text);
            } else if (curTextStatus == GameData.GameStatus.LOSE) {
                VBox text = (VBox) root.lookup("#loseText");
                root.getChildren().remove(text);
            } else if (curTextStatus == GameData.GameStatus.PAUSE) {
                VBox text = (VBox) root.lookup("#pauseText");
                root.getChildren().remove(text);
            } else if (curTextStatus == GameData.GameStatus.WAITRESPAWN) {
                VBox text = (VBox) root.lookup("#respawnText");
                root.getChildren().remove(text);
            }

            curTextStatus = GameData.GameStatus.NONE;
            textIsWriten = false;
        }
    }
    public static void printText(GameData.GameStatus status) {
        if (!textIsWriten) {
            if (status == GameData.GameStatus.WIN) {
                printWin();
            } else if (status == GameData.GameStatus.WAITRESPAWN) {
                printRespawn();
            } else if (status == GameData.GameStatus.LOSE) {
                printLose();
            } else if (status == GameData.GameStatus.PAUSE) {
                printPause();
            }

            textIsWriten = true;
            curTextStatus = status;
        }
    }
    private static void printWin() {
        Text text1 = new Text("You WIN");
        text1.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 80));
        text1.setFill(Color.YELLOW);

        Text text2 = new Text("Press ENTER to continue!");
        text2.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 40));
        text2.setFill(Color.YELLOW);

        VBox vbox = new VBox(text1, text2);
        vbox.setId("winText");
        vbox.setAlignment(Pos.CENTER);

        root.getChildren().add(vbox);
    }

    private static void printLose() {
        Text text1 = new Text("You LOSE");
        text1.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 80));
        text1.setFill(Color.YELLOW);
        text1.setStroke(Color.BLACK);
        text1.setStrokeWidth(2.0);

        Text text2 = new Text("Press ESC to exit!");
        text2.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 40));
        text2.setFill(Color.YELLOW);
        text2.setStroke(Color.BLACK);
        text2.setStrokeWidth(2.0);

        Text text3 = new Text("Press ENTER to start new game!");
        text3.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 40));
        text3.setFill(Color.YELLOW);
        text3.setStroke(Color.BLACK);
        text3.setStrokeWidth(2.0);

        VBox vbox = new VBox(text1, text2, text3);
        vbox.setId("loseText");
        vbox.setAlignment(Pos.CENTER);

        root.getChildren().add(vbox);
    }
    private static void printRespawn() {
        Text text1 = new Text("You DEAD");
        text1.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 80));
        text1.setFill(Color.YELLOW);
        text1.setStroke(Color.BLACK);
        text1.setStrokeWidth(2.0);

        Text text2 = new Text("Press ENTER to respawn!");
        text2.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 40));
        text2.setFill(Color.YELLOW);
        text2.setStroke(Color.BLACK);
        text2.setStrokeWidth(2.0);

        Text text3 = new Text("Press ESC to exit!");
        text3.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 40));
        text3.setFill(Color.YELLOW);
        text3.setStroke(Color.BLACK);
        text3.setStrokeWidth(2.0);

        VBox vbox = new VBox(text1, text2, text3);
        vbox.setId("respawnText");
        vbox.setAlignment(Pos.CENTER);

        root.getChildren().add(vbox);
    }
    private static void printPause() {
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
        vbox.setId("pauseText");
        vbox.setAlignment(Pos.CENTER);

        root.getChildren().add(vbox);
    }
    public static void printTimer(GameTimer timer) {
        Label eventTimer = (Label) root.lookup("#mainPain #eventTimer");
        eventTimer.setText(String.valueOf("Scared: " + timer.getRemainingSeconds()));
    }
}
