package com.knightquest.arkanoid.model;

public abstract class MovableObject extends GameObject {
    protected double dx, dy; // velocity

    public MovableObject(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public void move(double deltaTime) {
        x += dx * deltaTime;
        y += dy * deltaTime;
    }
}
