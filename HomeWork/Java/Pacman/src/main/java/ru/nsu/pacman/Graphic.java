package ru.nsu.pacman;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ru.nsu.pacman.enemy.Pacman;
import ru.nsu.pacman.enemy.ghosts.BlueGhost;
import ru.nsu.pacman.enemy.ghosts.OrangeGhost;
import ru.nsu.pacman.enemy.ghosts.PinkGhost;
import ru.nsu.pacman.enemy.ghosts.RedGhost;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Graphic {
    private static StackPane root = null;
    private static boolean textIsWriten = false;
    private static GameData.GameStatus curTextStatus = GameData.GameStatus.NONE;

    public static boolean settingIMG(ArrayList<GameData.EnemyData> enemies) {
        for (GameData.EnemyData enemy : enemies) {
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
                enemy.setImages(new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))));
            } else if (enemy.body.getClass().equals(OrangeGhost.class)) {
                enemy.setImages(new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))));
            } else if (enemy.body.getClass().equals(PinkGhost.class)) {
                enemy.setImages(new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))),
                        new Image(Objects.requireNonNull(Game.class.getResourceAsStream("sprites/ghosts/red/right.gif"))));
            } else {
                System.out.println("Enemy not found! Add new enemy in setting IMG");
                return false;
            }
        }
        return true;
    }
    public static void setMainRoot(StackPane root) { Graphic.root = root; }
    private static GridPane getArea() {
        return (GridPane) root.lookup("#gamePane #area");
    }
    public static void removeNodeFromArea(GameData.Coordinates cord) {
        GridPane area = getArea();
        for (Node node : area.getChildren()) {
            if (GridPane.getColumnIndex(node) == (int)cord.x && GridPane.getRowIndex(node) == (int)cord.y) {
                area.getChildren().remove(node);
                break;
            }
        }
    }

    public static void rewriteEnemy(GameData.EnemyData enemy) {
        enemy.view.setLayoutX(enemy.body.getPosition().x);
        enemy.view.setLayoutY(enemy.body.getPosition().y);

        enemy.view.setImage(enemy.getImages(enemy.body.getCurrentOrientation()));
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
            }

            curTextStatus = GameData.GameStatus.NONE;
            textIsWriten = false;
        }
    }
    public static void printText(GameData.GameStatus status) {
        if (!textIsWriten) {
            if (status == GameData.GameStatus.WIN) {
                printWin();
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
        text2.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 60));
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
        text2.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 60));
        text2.setFill(Color.YELLOW);
        text2.setStroke(Color.BLACK);
        text2.setStrokeWidth(2.0);

        VBox vbox = new VBox(text1, text2);
        vbox.setId("loseText");
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
