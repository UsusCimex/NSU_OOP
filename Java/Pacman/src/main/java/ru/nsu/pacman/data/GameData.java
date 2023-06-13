package ru.nsu.pacman.data;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.nsu.pacman.Game;
import ru.nsu.pacman.entity.Entity;

import java.util.Objects;

import static ru.nsu.pacman.generation.LevelBuilder.CELL_SIZE;

public abstract class GameData {
    public enum Orientation {
        UP,
        RIGHT,
        DOWN,
        LEFT,
        NONE
    }
    public enum GameStatus {
        GAME,
        PAUSE,
        WIN,
        LOSE,
        WAITRESPAWN,
        NONE
    }
    public static class Coordinates {
        public double x;
        public double y;

        public Coordinates(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    public static class EntityData {
        public Entity body;
        public ImageView view = new ImageView();

        private Image passiveIMG;
        private Image leftIMG;
        private Image rightIMG;
        private Image upIMG;
        private Image downIMG;

        public EntityData(Entity entity) {
            body = entity;

            view.setFitWidth(CELL_SIZE);
            view.setFitHeight(CELL_SIZE);

            view.setLayoutX(entity.getPosition().x);
            view.setLayoutY(entity.getPosition().y);
        }

        public void setImages(Image passive, Image left, Image right, Image up, Image down) {
            passiveIMG = passive;
            leftIMG = left;
            rightIMG = right;
            upIMG = up;
            downIMG = down;
        }
        public Image getImages(Orientation orientation) {
            return switch (orientation) {
                case NONE -> passiveIMG;
                case UP -> upIMG;
                case DOWN -> downIMG;
                case LEFT -> leftIMG;
                case RIGHT -> rightIMG;
            };
        }
    }
    public static class PlayerRecord {
        private final String name;
        private int score;
        private final int level;
        private int lives;

        public PlayerRecord(String name, int score, int level, int lives) {
            this.name = name;
            this.score = score;
            this.level = level;
            this.lives = lives;
        }
        public String getName() {
            return name;
        }
        public int getScore() {
            return score;
        }
        public void addToScore(int value) { score += value; }
        public int getLevel() {
            return level;
        }
        public int getLives() {
            return lives;
        }
        public void loseLive() {
            lives--;
        }
    }
}
