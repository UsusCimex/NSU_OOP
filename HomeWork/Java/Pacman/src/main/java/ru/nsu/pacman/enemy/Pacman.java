package ru.nsu.pacman.enemy;

import ru.nsu.pacman.PacmanGame;

public class Pacman implements Enemy {
    private PacmanGame.Orientation curentOrientation = PacmanGame.Orientation.NONE;
    private PacmanGame.Orientation nextOrientation = PacmanGame.Orientation.NONE;
    private double speed = 2;
    private double positionX, positionY;
    public Pacman(PacmanGame.Coordinates startPosition) {
        positionX = startPosition.x;
        positionY = startPosition.y;
    }
    public PacmanGame.Coordinates getPosition() {
        return new PacmanGame.Coordinates(positionX, positionY);
    }
    public void setPosition(PacmanGame.Coordinates newPosition) {
        positionX = newPosition.x;
        positionY = newPosition.y;
    }
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
    @Override
    public void move() {
        if (curentOrientation == PacmanGame.Orientation.UP) {
            positionY -= speed;
        }
        else if (curentOrientation == PacmanGame.Orientation.RIGHT) {
            positionX += speed;
        }
        else if (curentOrientation == PacmanGame.Orientation.DOWN) {
            positionY += speed;
        }
        else if (curentOrientation == PacmanGame.Orientation.LEFT) {
            positionX -= speed;
        }
    }
}
