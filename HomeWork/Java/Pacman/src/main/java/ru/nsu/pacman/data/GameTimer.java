package ru.nsu.pacman.data;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameTimer {
    private Timeline timeline;
    private boolean isPaused;
    private boolean isCompleted;

    public GameTimer(Duration duration, int cycleCount, Runnable onFinished) {
        timeline = new Timeline(
                new KeyFrame(duration, event -> {
                    onFinished.run();
                    isCompleted = true;
                })
        );
        timeline.setCycleCount(cycleCount);
        timeline.setOnFinished(event -> {
            isCompleted = true;
        });
        isPaused = false;
        isCompleted = false;
    }

    public void play() {
        if (!isPaused && !isCompleted) {
            timeline.play();
        }
    }

    public void pause() {
        if (!isPaused) {
            timeline.pause();
            isPaused = true;
        }
    }

    public void resume() {
        if (isPaused) {
            timeline.play();
            isPaused = false;
        }
    }

    public void stop() {
        timeline.stop();
        isPaused = false;
        isCompleted = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
