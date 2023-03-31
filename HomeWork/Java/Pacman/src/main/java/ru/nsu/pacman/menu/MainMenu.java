package ru.nsu.pacman.menu;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.nsu.pacman.Game;

import java.util.Objects;

public class MainMenu extends Application {
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
        Game game = new Game();
        game.start(new Stage());

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
