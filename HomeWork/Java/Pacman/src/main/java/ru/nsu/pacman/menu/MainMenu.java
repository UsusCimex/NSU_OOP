package ru.nsu.pacman.menu;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.nsu.pacman.data.Controller;
import ru.nsu.pacman.Game;
import ru.nsu.pacman.data.GameData;

import java.util.Objects;

public class MainMenu extends Application {
    public final static int FIRSTLEVEL = 4;
    public final static int FIRSTLIVES = 5;
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
    public void startEvent(ActionEvent actionEvent) {
        String playerName = nameEnter.getText();
        if (playerName.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Enter NAME!");
            alert.showAndWait();
            return;
        }
        if (!playerName.matches("[a-zA-Z0-9]*")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("You use invalid symbols! [a-zA-Z0-9]");
            alert.showAndWait();
            return;
        }
        if (playerName.length() < 3 || playerName.length() > 15) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Allowed nickname length: 3-15 characters");
            alert.showAndWait();
            return;
        }

        Game newGame = new Game(new GameData.PlayerRecord(playerName, 0, FIRSTLEVEL, FIRSTLIVES));
        newGame.start(new Stage());
        quit(actionEvent);
    }
    @FXML
    public void quit(ActionEvent ignoredActionEvent) {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PacmanGame");
        primaryStage.setResizable(false);
        Image icon = new Image(Objects.requireNonNull(Game.class.getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(icon);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainInterface.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        nameEnter = (TextField) loader.getNamespace().get("nameEnter");
        quitButton = (Button) loader.getNamespace().get("quitButton");
        Controller.setMainMenuControl(nameEnter, this);
    }
}
