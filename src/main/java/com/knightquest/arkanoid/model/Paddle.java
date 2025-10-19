package com.knightquest.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import static com.knightquest.arkanoid.util.Constants.*;

public class Paddle extends MovableObject {
    private double speed = PADDLE_SPEED;
    private double width = PADDLE_WIDTH;
    private boolean canShootLaser;
    private double laserCooldown;
    private double currentLaserCooldown;
    private boolean isMagnetic;
    private Ball attachedBall;

    /**
     * Constructor from GameObject.
     */
    public Paddle(double x, double y) {
        super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
    }

    /**
     * Update location, limit in screen.
     */
    @Override
    public void update(double deltaTime) {
        move(deltaTime);
        if (x < 0) x = 0;
        if (x + width > SCREEN_WIDTH) x = SCREEN_WIDTH - width;

        if (attachedBall != null & isMagnetic) {
            attachedBall.x = x + width / 2 - attachedBall.getWidth() / 2;
            attachedBall.y = y - attachedBall.getHeight();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillRect(x, y, width, height);
    }

    public void moveLeft() {
        dx = -speed;
    }
    public void moveRight() {
        dx = speed;
    }
    public void stop() {
        dx = 0;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setCanShootLaser(boolean canShootLaser) {
        this.canShootLaser = canShootLaser;
    }

    public boolean canShootLaser() {
        return canShootLaser;
    }

    public void setLaserCooldown(double laserCooldown) {
        this.laserCooldown = laserCooldown;
        this.currentLaserCooldown = 0;
    }

    public double getLaserCooldown() {
        return laserCooldown;
    }

    public double getCurrentLaserCooldown() {
        return currentLaserCooldown;
    }

    public void updateLaserCooldown(double deltaTime) {
        if (currentLaserCooldown > 0) {
            currentLaserCooldown -= deltaTime;
        }
    }

    public boolean canFireLaser() {
        return canShootLaser && currentLaserCooldown <= 0;
    }

    public void resetLaserCooldown() {
        currentLaserCooldown = laserCooldown;
    }

    public void setMagnetic(boolean magnetic) {
        this.isMagnetic = magnetic;
    }

    public boolean isMagnetic() {
        return isMagnetic;
    }

    public void attachBall(Ball ball) {
        this.attachedBall = ball;
        ball.setVelocity(0, 0);
    }

    public void releaseBall() {
        if (attachedBall != null) {
            attachedBall.setVelocity(0, -BALL_SPEED);
            attachedBall = null;
        }
    }

    public Ball getAttachedBall() {
        return attachedBall;
    }

}