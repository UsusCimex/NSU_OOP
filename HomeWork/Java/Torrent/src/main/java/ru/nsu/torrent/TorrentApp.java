package ru.nsu.torrent;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.File;
public class TorrentApp extends Application {
    private TextArea progressTextArea;
    private TorrentClient torrentClient = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Java Torrent");

        Button loadTorrentButton = new Button("Загрузить торрент");
        loadTorrentButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Torrent files (*.torrent)", "*.torrent"));
            File torrentFile = fileChooser.showOpenDialog(primaryStage);
            if (torrentFile != null) {
                torrentClient = new TorrentClient(new TorrentFile(torrentFile));
                updateProgress();
            }
        });

        Label progressLabel = new Label("Прогресс загрузки:");
        progressTextArea = new TextArea();
        progressTextArea.setEditable(false);

        VBox vBox = new VBox(10, loadTorrentButton, progressLabel, progressTextArea);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(15));

        Timeline progressUpdater = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateProgress()));
        progressUpdater.setCycleCount(Timeline.INDEFINITE);
        progressUpdater.play();

        primaryStage.setScene(new Scene(vBox, 400, 300));
        primaryStage.show();
    }

    private void updateProgress() {
        if (torrentClient != null) {
            String fileName = torrentClient.getFileName().replace(".torrent","");
            int totalPieces = torrentClient.getTotalPieces();
            int downloadedPieces = torrentClient.getDownloadedPieces();
            int remainingPieces = totalPieces - downloadedPieces;
            double downloadSpeed = torrentClient.getDownloadSpeed();
            double uploadSpeed = torrentClient.getUploadSpeed();
            double percentComplete = (double) downloadedPieces / totalPieces * 100;

            String progressText = String.format(
                    "Загружается торрент: \"%s\"\n\n" +
                            "Процент завершения: %.2f%%\n" +
                            "Загружено кусочков: %d из %d\n" +
                            "Оставшиеся кусочки: %d\n" +
                            "Скорость загрузки: %.2f kB/s\n" +
                            "Скорость раздачи: %.2f kB/s",

                    fileName,
                    percentComplete,
                    downloadedPieces,
                    totalPieces,
                    remainingPieces,
                    downloadSpeed,
                    uploadSpeed
            );

            progressTextArea.setText(progressText);
        } else {
            progressTextArea.setText("Enter .torrent file!");
        }
    }

}
