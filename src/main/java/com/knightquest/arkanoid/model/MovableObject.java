package com.knightquest.arkanoid.model;

public abstract class MovableObject extends GameObject {
    protected double dx, dy; // velocity

    /**
     * Contructor from GameObject.
     */
    public MovableObject(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    /**
     * Move between 2 frames.
     */
    public void move(double deltaTime) {
        x += dx * deltaTime;
        y += dy * deltaTime;
    }

    /**
     * Setter.
     */
    public void setVelocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
