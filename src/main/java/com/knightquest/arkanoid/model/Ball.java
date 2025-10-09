package com.knightquest.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import static com.knightquest.arkanoid.util.Constants.*;

public class Ball extends MovableObject {
    private double radius;

    /**
     * Contructor from GameObject.
     */
    public Ball(double x, double y) {
        super(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
        this.radius = BALL_RADIUS;
        setVelocity(200, -BALL_SPEED); //up right
    }

    /**
     * Update location.
     */
    @Override
    public void update(double deltaTime) {
        move(deltaTime);
        if (x <= 0 || x + width >= SCREEN_WIDTH) {
            dx = -dx;
        }
        if (y <= 0) {
            dy = -dy;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(x, y, width, height);
    }

    public void bounceVertical() {
        dy = -dy;
    }
    public void bounceHorizontal() {
        dx = -dx;
    }

    public boolean isFallenOff() {
        return y > SCREEN_HEIGHT;
    }
}