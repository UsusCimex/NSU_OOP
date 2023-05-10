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
import ru.nsu.torrent.net.ParserIP;

import java.io.File;
import java.net.UnknownHostException;

public class TorrentApp extends Application {
    private ListView<String> progressListView;
    private static Torrent torrent;

    public static void main(String[] args) throws UnknownHostException {
        String localIP;
        int port;
        if (args.length == 0) {
            localIP = ParserIP.getIP("w"); //wifi.nsu.ru - local wifi net
            port = 6969;
        } else if (args.length == 1){
            localIP = ParserIP.getIP("w");
            port = Integer.parseInt(args[0]);
        } else {
            localIP = args[0];
            port = Integer.parseInt(args[1]);
        }
        torrent = new Torrent(localIP, port);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Java Torrent");

        Button loadTorrentButton = new Button("Загрузить торрент");
        loadTorrentButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Torrent files (*.torrent)", "*.torrent"));
            fileChooser.setInitialDirectory(new File(Torrent.TORRENTS_DIRECTORY));
            File torrentFile = fileChooser.showOpenDialog(primaryStage);
            if (torrentFile != null) {
                torrent.selectFile(new TorrentFile(torrentFile));
                if (Torrent.getTracker().getPeers().size() == 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Внимание");
                    alert.setHeaderText(null);
                    alert.setContentText("Пиры не найдены!");
                    alert.showAndWait();
                }

                torrent.startDownload();
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
        torrent.stopTorrent();
    }

    private void updateProgress() {
        if (Torrent.getFile() != null) {
            String fileName = Torrent.getFile().getName();
            long totalLength = Torrent.getFile().getLength();
            int totalPieces = Torrent.getFile().getPieceHashes().size();
            int downloadedPieces = Torrent.getFile().getDownloadedPieces();
            int remainingPieces = totalPieces - downloadedPieces;
            double percentComplete = (double) downloadedPieces / totalPieces * 100;
            int countPeers = Torrent.getTracker().getPeers().size();
            long pieceLength = Torrent.getFile().getPieceLength();

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
