package ru.nsu.pacman.menu;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.pacman.Game;
import ru.nsu.pacman.GameData.PlayerRecord;

import java.io.*;
import java.util.*;

public class RecordsTable extends Application {
    private static final int MAX_PlayerRecordS = 10;
    @Override
    public void start(Stage primaryStage) throws IOException {
        ArrayList<PlayerRecord> PlayerRecords = loadPlayerRecords();

        VBox vbox = new VBox();
        vbox.setSpacing(15);
        vbox.setAlignment(Pos.CENTER);

        for (PlayerRecord playerRecord : PlayerRecords) {
            Text text = new Text(playerRecord.getName() + " " + playerRecord.getScore());
            text.setFont(new Font("Times new roman", 25));
            vbox.getChildren().add(text);
        }
        Button buttonQuit = new Button("Quit");
        buttonQuit.setMaxWidth(150);
        buttonQuit.setMaxHeight(100);
        buttonQuit.setStyle("-fx-background-color: aqua; -fx-border-width: 4; -fx-border-color: black;");
        buttonQuit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    MainMenu mainMenu = new MainMenu();
                    mainMenu.start(new Stage());

                    Stage stage = (Stage) buttonQuit.getScene().getWindow();
                    stage.close();
                } catch (Exception ex) {
                    ex.getStackTrace();
                }
            }
        });
        vbox.getChildren().add(buttonQuit);

        Scene scene = new Scene(vbox, 300, 550);
        primaryStage.setScene(scene);
        Image icon = new Image(Objects.requireNonNull(Game.class.getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Pacman");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private static ArrayList<PlayerRecord> loadPlayerRecords() throws IOException {
        File file = new File("records.txt");
        if (!file.exists()) {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write("NO_RECORD,0\n");
            writer.close();
        }

        ArrayList<PlayerRecord> PlayerRecords = new ArrayList<PlayerRecord>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    PlayerRecords.add(new PlayerRecord(name, score));
                }
                if (PlayerRecords.size() >= MAX_PlayerRecordS) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading record: " + e.getMessage());
        }
        return PlayerRecords;
    }
    public static void addPlayerRecord(PlayerRecord PlayerRecord) throws IOException {
        List<PlayerRecord> PlayerRecords = loadPlayerRecords();
        PlayerRecords.add(PlayerRecord);
        PlayerRecords.sort(new Comparator<PlayerRecord>() {
            @Override
            public int compare(PlayerRecord o1, PlayerRecord o2) {
                return o2.getScore() - o1.getScore();
            }
        });

        if (PlayerRecords.size() > 10) {
            PlayerRecords = PlayerRecords.subList(0, 10);
        }

        File file = new File("records.txt");
        FileWriter writer = new FileWriter(file);

        for (PlayerRecord rec : PlayerRecords) {
            writer.write(rec.getName() + "," + rec.getScore() + "\n");
        }
        writer.close();
    }
}
