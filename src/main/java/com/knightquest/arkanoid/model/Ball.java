package com.knightquest.arkanoid.model;

import static com.knightquest.arkanoid.util.Constants.BALL_RADIUS;
import static com.knightquest.arkanoid.util.Constants.BALL_SPEED;
import static com.knightquest.arkanoid.util.Constants.SCREEN_HEIGHT;

import com.knightquest.arkanoid.strategy.MovementStrategy;
import com.knightquest.arkanoid.strategy.NormalMovementStrategy;
import com.knightquest.arkanoid.strategy.PierceMovementStratrgy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends MovableObject {
    private double radius;
    private MovementStrategy movementStrategy;


    private boolean onFire = false;
    private boolean fallenOff;
    private boolean piercing = false;

    /**
     * Contructor from GameObject.
     */
    public Ball(double x, double y) {
        super(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
        this.radius = BALL_RADIUS;
        this.movementStrategy = new NormalMovementStrategy();
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
        if (onFire) {
            // Fire Ball effect - orange glow
            gc.setFill(Color.rgb(255, 100, 0, 0.5));
            gc.fillOval(x - 4, y - 4, width + 8, height + 8);
            gc.setFill(Color.ORANGE);
            gc.fillOval(x, y, width, height);
            gc.setFill(Color.YELLOW);
            gc.fillOval(x + 3, y + 3, width - 6, height - 6);
        } else if (piercing) {
            // Pierce Ball effect - white with cyan aura
            gc.setFill(Color.rgb(0, 255, 255, 0.5));
            gc.fillOval(x - 3, y - 3, width + 6, height + 6);
            gc.setFill(Color.WHITE);
            gc.fillOval(x, y, width, height);
        }
        else {
            // Normal ball
            gc.setFill(Color.RED);
            gc.fillOval(x, y, width, height);
        }

    }

    public boolean isOnFire() {
        return onFire;
    }

    public void setOnFire(boolean onFire) {
        this.onFire = onFire;
        System.err.println("onFire: " + onFire);
    }

    public boolean isPiercing() {
        return piercing;
    }

    public void setPiercing(boolean piercing) {
        this.piercing = piercing;
        System.err.println("piercing: " + piercing);
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

    public void multiplySpeed(double speedMultiplier) {
    }


}