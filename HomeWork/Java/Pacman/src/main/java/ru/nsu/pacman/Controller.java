package ru.nsu.pacman;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.nsu.pacman.enemy.Enemy;

public class Controller {
    public enum Orientation {
        UP,
        RIGHT,
        DOWN,
        LEFT,
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
    public static class EnemyData {
        public Enemy body;
        public ImageView view = new ImageView();

        private Image passiveIMG;
        private Image leftIMG;
        private Image rightIMG;
        private Image upIMG;
        private Image downIMG;

        public EnemyData(Enemy enemy) {
            body = enemy;

            view.setFitWidth(Game.CELL_SIZE);
            view.setFitHeight(Game.CELL_SIZE);

            view.setLayoutX(enemy.getPosition().x);
            view.setLayoutY(enemy.getPosition().y);
        }

        public void setImages(Image passive, Image left, Image right, Image up, Image down) {
            passiveIMG = passive;
            leftIMG = left;
            rightIMG = right;
            upIMG = up;
            downIMG = down;
        }

        public void changeOrientationVew() {
            if (body.getCurrentOrientation() == Orientation.UP) {
                view.setImage(upIMG);
            } else if (body.getCurrentOrientation() == Orientation.LEFT) {
                view.setImage(leftIMG);
            } else if (body.getCurrentOrientation() == Orientation.RIGHT) {
                view.setImage(rightIMG);
            } else if (body.getCurrentOrientation() == Orientation.DOWN) {
                view.setImage(downIMG);
            } else {
                view.setImage(passiveIMG);
            }
        }
    }
}
