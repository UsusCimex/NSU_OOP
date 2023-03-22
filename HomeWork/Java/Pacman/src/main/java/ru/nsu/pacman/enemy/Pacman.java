package ru.nsu.pacman.enemy;

import ru.nsu.pacman.PacmanGame;

public class Pacman implements Enemy {
    private double posX, posY;
    private double dX = 2, dY = 2;
    public Pacman(double startPosX, double startPosY) {
        posX = startPosX;
        posY = startPosY;
    }
    public PacmanGame.Coordinates getPosition() {
        return new PacmanGame.Coordinates(posX, posY);
    }
    public void move(PacmanGame.WalkDir dir) {
        if (dir == PacmanGame.WalkDir.UP) {
            posY -= dY;
        }
        else if (dir == PacmanGame.WalkDir.RIGHT) {
            posX += dX;
        }
        else if (dir == PacmanGame.WalkDir.DOWN) {
            posY += dY;
        }
        else if (dir == PacmanGame.WalkDir.LEFT) {
            posX -= dX;
        }
    }

    @Override
    public void move() {

    }
}
