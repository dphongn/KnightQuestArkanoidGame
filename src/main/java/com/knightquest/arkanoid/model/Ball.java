package com.knightquest.arkanoid.model;

import static com.knightquest.arkanoid.util.Constants.BALL_RADIUS;
import static com.knightquest.arkanoid.util.Constants.BALL_SPEED;
import static com.knightquest.arkanoid.util.Constants.SCREEN_HEIGHT;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends MovableObject {
    private double radius;
    private boolean onFire;
    private boolean fallenOff;
    private boolean piercing;

    /**
     * Contructor from GameObject.
     */
    public Ball(double x, double y) {
        super(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
        this.radius = BALL_RADIUS;
        setVelocity(200, -BALL_SPEED); //up right
    }

    /**
     * Update location with boundary checking moved to CollisionHandler.
     */
    @Override
    public void update(double deltaTime) {
        move(deltaTime);

        // Clamp velocity to prevent excessive speeds
        double maxSpeed = BALL_SPEED * 2; // Allow up to 2x normal speed
        if (Math.abs(getDx()) > maxSpeed) setDx(Math.signum(getDx()) * maxSpeed);
        if (Math.abs(getDy()) > maxSpeed) setDy(Math.signum(getDy()) * maxSpeed);

        // Ensure minimum speed to prevent ball getting stuck
        double minSpeed = BALL_SPEED * 0.3;
        if (Math.abs(getDx()) < minSpeed) setDx(Math.signum(getDx()) * minSpeed);
        if (Math.abs(getDy()) < minSpeed) setDy(Math.signum(getDy()) * minSpeed);

    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(x, y, width, height);
    }

    public void bounceVertical() {
        setDy(-getDy());
    }

    public void bounceHorizontal() {
        setDx(-getDx());
    }

    public boolean isFallenOff() {
        return y > SCREEN_HEIGHT;
    }

    public double getRadius() {
        return radius;
    }

    public void setPiercing(boolean b) {
    }

    public void multiplySpeed(double speedMultiplier) {
    }

    public void setOnFire(boolean b) {
    }
}