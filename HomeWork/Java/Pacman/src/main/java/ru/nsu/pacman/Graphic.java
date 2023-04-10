package ru.nsu.pacman;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ru.nsu.pacman.entity.Pacman;
import ru.nsu.pacman.entity.ghosts.BlueGhost;
import ru.nsu.pacman.entity.ghosts.OrangeGhost;
import ru.nsu.pacman.entity.ghosts.PinkGhost;
import ru.nsu.pacman.entity.ghosts.RedGhost;
import ru.nsu.pacman.generation.LevelBuilder;
import ru.nsu.pacman.generation.LevelData;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Graphic {
    private static LevelData data = null;
    private static StackPane root = null;
    private static boolean textIsWriten = false;
    private static GameData.GameStatus curTextStatus = GameData.GameStatus.NONE;

    public static void settingIMG(ArrayList<GameData.EntityData> enemies) {
        for (GameData.EntityData enemy : enemies) {
            if (enemy.body.getClass().equals(Pacman.class)) {
                enemy.setImages(new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/pacman/stopped.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/pacman/left.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/pacman/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/pacman/up.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/pacman/down.gif"))));
            } else if (enemy.body.getClass().equals(RedGhost.class)) {
                enemy.setImages(new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))));
            } else if (enemy.body.getClass().equals(BlueGhost.class)) {
                enemy.setImages(new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/blue/test.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/blue/test.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/blue/test.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/blue/test.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/blue/test.png"))));
            } else if (enemy.body.getClass().equals(OrangeGhost.class)) {
                enemy.setImages(new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/orange/test.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/orange/test.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/orange/test.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/orange/test.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/orange/test.png"))));
            } else if (enemy.body.getClass().equals(PinkGhost.class)) {
                enemy.setImages(new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/pink/test.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/pink/test.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/pink/test.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/pink/test.png"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/pink/test.png"))));
            } else {
                System.err.println("Enemy not found! Add new enemy in setting IMG");
                throw new RuntimeException();
            }
        }
    }
    public static void addAllEnemiesInGamePane(ArrayList<GameData.EntityData> enemies) {
        Pane gamePane = (Pane) root.lookup("#gamePane");
        for (GameData.EntityData enemy : enemies) {
            gamePane.getChildren().add(enemy.view);
        }
    }
    public static void removeAllEnemiesInGamePane(ArrayList<GameData.EntityData> enemies) {
        Pane gamePane = (Pane) root.lookup("#gamePane");
        for (GameData.EntityData enemy : enemies) {
            gamePane.getChildren().remove(enemy.view);
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

        VBox mainPain = new VBox(gamePane, infoPain);
        mainPain.setId("mainPain");

        root = new StackPane();
        root.getChildren().add(mainPain);
        root.setStyle("-fx-background-color: AQUA;");
        return root;
    }
    public static void update() {
        if (data == null || root == null) return;
        GridPane area = getArea();
        for (Node node : area.getChildren()) {
            if (data.getValueLevelData(new GameData.Coordinates(GridPane.getColumnIndex(node), GridPane.getRowIndex(node))) == LevelData.Symbols.Empty) {
                area.getChildren().remove(node);
                break;
            }
        }
    }
    private static GridPane getArea() {
        return (GridPane) root.lookup("#gamePane #area");
    }
    public static void rewriteEnemy(GameData.EntityData enemy) {
        enemy.view.setLayoutX(enemy.body.getPosition().x);
        enemy.view.setLayoutY(enemy.body.getPosition().y);

        enemy.view.setImage(enemy.getImages(enemy.body.getCurrentOrientation()));
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
}
