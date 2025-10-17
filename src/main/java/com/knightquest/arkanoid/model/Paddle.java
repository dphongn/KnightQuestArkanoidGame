package com.knightquest.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import static com.knightquest.arkanoid.util.Constants.*;

public class Paddle extends MovableObject {
    private double speed = PADDLE_SPEED;

    /**
     * Contructor from GameObject.
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
}