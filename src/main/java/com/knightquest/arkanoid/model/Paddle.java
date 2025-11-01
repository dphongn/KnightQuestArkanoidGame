package com.knightquest.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import static com.knightquest.arkanoid.util.Constants.*;

public class Paddle extends MovableObject {
    private double speed = PADDLE_SPEED;
    private double width = PADDLE_WIDTH;
    // Gun power-up properties
    private boolean canShootGun = false;
    private double gunCooldown = 0.3;
    private double gunTimer = 0.0;

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


        // Update gun cooldown timer
        if (gunTimer > 0) {
            gunTimer -= deltaTime;
        }

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


    /**
     * Set the paddle width and keep it centered around its current center.
     */
    public void setWidth(double newWidth) {
        double centerX = x + width / 2.0;
        this.width = newWidth;
        // Re-center the paddle around the same center
        this.x = centerX - this.width / 2.0;
        // Clamp to screen boundaries
        if (x < 0) x = 0;
        if (x + width > SCREEN_WIDTH) x = SCREEN_WIDTH - width;
    }


    // Gun power-up methods
    public boolean canShootGun() {
        return canShootGun && gunTimer <= 0;
    }

    public void setCanShootGun(boolean canShoot) {
        this.canShootGun = canShoot;
    }

    public void setGunCooldown(double cooldown) {
        this.gunCooldown = cooldown;
    }

    public void resetGunTimer() {
        this.gunTimer = gunCooldown;
    }

    public double getGunCooldown() {
        return gunCooldown;
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