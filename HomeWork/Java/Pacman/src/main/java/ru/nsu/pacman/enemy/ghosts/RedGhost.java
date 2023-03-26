package ru.nsu.pacman.enemy.ghosts;

import javafx.scene.layout.GridPane;
import ru.nsu.pacman.PacmanGame;
import ru.nsu.pacman.enemy.Enemy;
import ru.nsu.pacman.generation.LevelData;

public class RedGhost implements Enemy {
    private GridPane area = null;
    private LevelData data = null;
    private PacmanGame.Orientation curentOrientation = PacmanGame.Orientation.NONE;
    private PacmanGame.Orientation nextOrientation = PacmanGame.Orientation.NONE;
    private double positionX, positionY;
    private double speed = 2;

    public void changeNextOrientation(PacmanGame.Orientation orientation) {
        nextOrientation = orientation;
    }
    public void changeCurrentOrientation() {
        curentOrientation = nextOrientation;
        nextOrientation = PacmanGame.Orientation.NONE;
    }
    public PacmanGame.Orientation getCurrentOrientation() {
        return curentOrientation;
    }
    public PacmanGame.Orientation getNextOrientation() {
        return nextOrientation;
    }


    public RedGhost(PacmanGame.Coordinates startPosition, GridPane area, LevelData data) {
        this.positionX = startPosition.x;
        this.positionY = startPosition.y;

        this.area = area;
        this.data = data;
    }
    @Override
    public void move() {
//        if (ghostCanMove()) {
//            if (curentOrientation == PacmanGame.Orientation.UP) {
//                positionY -= speed;
//            }
//            else if (curentOrientation == PacmanGame.Orientation.RIGHT) {
//                positionX += speed;
//            }
//            else if (curentOrientation == PacmanGame.Orientation.DOWN) {
//                positionY += speed;
//            }
//            else if (curentOrientation == PacmanGame.Orientation.LEFT) {
//                positionX -= speed;
//            }
//        } else {
//            changeCurrentOrientation();
//        }
    }
}
