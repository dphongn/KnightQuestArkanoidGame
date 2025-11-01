package com.knightquest.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Bullet fired from paddle when Gun Paddle power-up is active.
 * Travels upward and destroys bricks on contact.
 */
public class Bullet extends MovableObject {
    private static final double BULLET_SPEED = 400.0; // Pixels per second
    private static final double BULLET_WIDTH = 8.0;   // Tăng từ 4 -> 8
    private static final double BULLET_HEIGHT = 15.0; // Tăng từ 10 -> 15

    public Bullet(double x, double y) {
        super(x, y, BULLET_WIDTH, BULLET_HEIGHT);
        this.dy = -BULLET_SPEED; // Move upward
    }

    @Override
    public void update(double deltaTime) {
        move(deltaTime);

        // Deactivate if bullet goes off top of screen
        if (y + height < 0) {
            setActive(false);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;

        // Draw bullet với màu sáng hơn, dễ nhìn hơn
        // Vẽ core màu cam sáng
        gc.setFill(Color.ORANGE);
        gc.fillRect(x, y, width, height);

        // Add glow effect màu vàng
        gc.setFill(Color.rgb(255, 255, 0, 0.7));
        gc.fillRect(x - 2, y, width + 4, height);

        // Viền trắng để nổi bật
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, width, height);
    }

    /**
     * Check if bullet has gone off screen.
     */
    public boolean isOffScreen() {
        return y + height < 0;
    }
}

