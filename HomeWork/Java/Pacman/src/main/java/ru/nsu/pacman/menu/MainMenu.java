package ru.nsu.pacman.menu;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.nsu.pacman.Game;
import ru.nsu.pacman.GameData;

import java.util.Objects;

public class MainMenu extends Application {
    private final static int FIRSTLEVEL = 1;
    private final static int FIRSTLIVES = 5;
    @FXML
    private TextField nameEnter;
    @FXML
    private Button quitButton;

    public static void main(String[] args) {
        launch(args);
    }
    @FXML
    public void record(ActionEvent actionEvent) throws Exception {
        RecordsTable leader = new RecordsTable();
        leader.start(new Stage());

        quit(actionEvent);
    }
    @FXML
    public void starte(ActionEvent actionEvent) throws Exception {
        String playerName = nameEnter.getText();
        if (playerName.trim().isEmpty()) {
            System.err.println("Enter NAME!");
            return;
        }
        if (!playerName.matches("[a-zA-Z0-9]*")) {
            System.err.println("You use invalid symbols!");
            return;
        }
        if (playerName.length() < 3 || playerName.length() > 15) {
            System.err.println("Allowed nickname length: 3-15 characters");
            return;
        }

        new Game(new GameData.PlayerRecord(playerName, 0), FIRSTLEVEL, FIRSTLIVES);
        quit(actionEvent);
    }
    @FXML
    public void quit(ActionEvent actionEvent) {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PacmanGame");
        Image icon = new Image(Objects.requireNonNull(Game.class.getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(icon);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainInterface.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
