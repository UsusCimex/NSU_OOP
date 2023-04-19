package ru.nsu.pacman.data;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameTimer {
    private Timeline timeline;
    private boolean isPaused;
    private boolean isWorked;

    public GameTimer(Duration duration, int cycleCount, Runnable onFinished) {
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    isWorked = true;
                }),
                new KeyFrame(duration, event -> {
                    onFinished.run();
                    if(cycleCount != Timeline.INDEFINITE) isWorked = false;
                })
        );
        timeline.setCycleCount(cycleCount);
        isPaused = false;
        isWorked = false;
    }

    public void play() {
        if (!isPaused && !isWorked) {
            timeline.play();
            isWorked = true;
        }
    }

    public void pause() {
        if (!isPaused && isWorked) {
            timeline.pause();
            isPaused = true;
        }
    }

    public void resume() {
        if (isPaused && isWorked) {
            timeline.play();
            isPaused = false;
        }
    }

    public void stop() {
        timeline.stop();
        isPaused = false;
        isWorked = false;
    }

    public double getRemainingSeconds() {
        Duration currentTime = timeline.getCurrentTime();
        return (double)Math.round((timeline.getCycleDuration().toSeconds() - currentTime.toSeconds()) * 10) / 10;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isWorked() {
        return isWorked;
    }
}
