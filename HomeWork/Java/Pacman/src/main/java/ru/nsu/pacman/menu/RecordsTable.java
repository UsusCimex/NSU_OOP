package ru.nsu.pacman.menu;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.pacman.Game;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class RecordsTable extends Application {
    private static final int MAX_RECORDS = 10;
    @Override
    public void start(Stage primaryStage) throws IOException, URISyntaxException {
        ArrayList<Record> records = loadRecords();

        VBox vbox = new VBox();
        vbox.setSpacing(15);
        vbox.setAlignment(Pos.CENTER);

        for (int i = 0; i < records.size(); ++i) {
            Text text = new Text(records.get(i).name + " " + records.get(i).score);
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

        Scene scene = new Scene(vbox, 250, 550);
        primaryStage.setScene(scene);
        Image icon = new Image(Objects.requireNonNull(Game.class.getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Pacman");
        primaryStage.show();
    }

    private static ArrayList<Record> loadRecords() throws IOException {
        File file = new File("records.txt");
        if (!file.exists()) {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write("NO_RECORD,0");
            writer.close();
        }

        ArrayList<Record> records = new ArrayList<Record>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    records.add(new Record(name, score));
                }
                if (records.size() >= MAX_RECORDS) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading records: " + e.getMessage());
        }
        return records;
    }
    public static void addRecord(Record record) throws IOException {
        List<Record> records = loadRecords();
        records.add(record);
        Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return o2.score - o1.score;
            }
        });

        if (records.size() > 10) {
            records = records.subList(0, 10);
        }

        File file = new File("records.txt");
        FileWriter writer = new FileWriter(file, true);

        for (Record rec : records) {
            writer.write(rec.getName() + "," + rec.getScore() + "\n");
        }
        writer.close();
    }

    private static class Record {
        private String name;
        private int score;

        public Record(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }
}
