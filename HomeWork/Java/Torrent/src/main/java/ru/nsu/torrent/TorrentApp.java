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
import javafx.scene.control.Alert;

import java.io.File;

public class TorrentApp extends Application {
    private TextArea progressTextArea;
    private static TorrentClient torrentClient;

    public static void main(String[] args) {
//        if (args.length < 2) {
//            throw new RuntimeException("Usage: ./torrent yourHost yourPort");
//        }
//        torrentClient = new TorrentClient(args[0], Integer.parseInt(args[1]));
        torrentClient = new TorrentClient("localhost", 6969); //temp
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
                torrentClient.selectFile(new TorrentFile(torrentFile));
                if (torrentClient.getTracker().getPeers().size() == 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Внимание");
                    alert.setHeaderText(null);
                    alert.setContentText("Пиры не найдены!");
                    alert.showAndWait();
                }
                else {
                    torrentClient.start();
                }
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
    @Override
    public void stop() {
        torrentClient.stopTorrentListener();
    }

    private void updateProgress() {
        if (torrentClient.getFile() != null) {
            String fileName = torrentClient.getFile().getName();
            long totalLength = torrentClient.getTotalLength();
            int totalPieces = torrentClient.getTotalPieces();
            int downloadedPieces = torrentClient.getDownloadedPieces();
            int remainingPieces = totalPieces - downloadedPieces;
            double percentComplete = (double) downloadedPieces / totalPieces * 100;
            int countPeers = torrentClient.getTracker().getPeers().size();
            long pieceLength = torrentClient.getPieceLength();

            String progressText = String.format(
                    """
                    Загружается торрент: "%s"
                    Размер файла: %d байт

                    Процент завершения: %.2f%%
                    Загружено кусочков: %d из %d
                    Оставшиеся кусочки: %d
                    Количество пиров: %d
                    Размер кусочков: %d байт
                    """,

                    fileName,
                    totalLength,
                    percentComplete,
                    downloadedPieces,
                    totalPieces,
                    remainingPieces,
                    countPeers,
                    pieceLength
            );

            progressTextArea.setText(progressText);
        } else {
            progressTextArea.setText("Enter .torrent file!");
        }
    }

}
