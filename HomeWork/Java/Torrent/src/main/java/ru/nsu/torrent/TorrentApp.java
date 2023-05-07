package ru.nsu.torrent;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TorrentApp extends Application {
    private ListView<String> progressListView;
    private static TorrentClient torrentClient;

    public static void main(String[] args) throws UnknownHostException {
        InetAddress localAddress = InetAddress.getLocalHost();
        String localIP = localAddress.getHostAddress();
        torrentClient = new TorrentClient(localIP, 6969);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Java Torrent");

        Button loadTorrentButton = new Button("Загрузить торрент");
        loadTorrentButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Torrent files (*.torrent)", "*.torrent"));
            fileChooser.setInitialDirectory(new File(TorrentClient.TORRENTS_DIRECTORY));
            File torrentFile = fileChooser.showOpenDialog(primaryStage);
            if (torrentFile != null) {
                torrentClient.selectFile(new TorrentFile(torrentFile));
                if (TorrentClient.getTracker().getPeers().size() == 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Внимание");
                    alert.setHeaderText(null);
                    alert.setContentText("Пиры не найдены!");
                    alert.showAndWait();
                }

                torrentClient.startDownload();
            }
        });

        Label progressLabel = new Label("Прогресс загрузки:");
        progressListView = new ListView<>();

        VBox vBox = new VBox(10, loadTorrentButton, progressLabel, progressListView);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(15));

        Timeline progressUpdater = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateProgress()));
        progressUpdater.setCycleCount(Timeline.INDEFINITE);
        progressUpdater.play();

        primaryStage.setScene(new Scene(vBox, 400, 400));
        primaryStage.show();
    }
    @Override
    public void stop() {
        torrentClient.stopTorrent();
    }

    private void updateProgress() {
        if (TorrentClient.getFile() != null) {
            String fileName = TorrentClient.getFile().getName();
            long totalLength = TorrentClient.getFile().getLength();
            int totalPieces = TorrentClient.getFile().getPieceHashes().size();
            int downloadedPieces = TorrentClient.getFile().getDownloadedPieces();
            int remainingPieces = totalPieces - downloadedPieces;
            double percentComplete = (double) downloadedPieces / totalPieces * 100;
            int countPeers = TorrentClient.getTracker().getPeers().size();
            long pieceLength = TorrentClient.getFile().getPieceLength();

            ObservableList<String> progressInfo = FXCollections.observableArrayList(
                    String.format("Загружается торрент: \"%s\"", fileName),
                    String.format("Размер файла: %d байт", totalLength),
                    String.format("Количество кусочков: %d", totalPieces),
                    String.format("Размер кусочков: %d байт", pieceLength),
                    "",
                    String.format("Процент завершения: %.2f%%", percentComplete),
                    String.format("Загружено кусочков: %d", downloadedPieces),
                    String.format("Оставшиеся кусочки: %d", remainingPieces),
                    "",
                    String.format("Количество пиров: %d", countPeers)
            );

            progressListView.setItems(progressInfo);
        } else {
            progressListView.setItems(FXCollections.observableArrayList("Enter .torrent file!"));
        }
    }
}
