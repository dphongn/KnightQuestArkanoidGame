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
     * Velocity setters and getters.
     */
    public void setVelocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getSpeed() {
        return Math.sqrt(dx * dx + dy * dy);
    }
}
