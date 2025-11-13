package com.knightquest.arkanoid.model.powerup;

import com.knightquest.arkanoid.model.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ExpandPaddlePowerUp extends PowerUp {
    private static final double EXPANSION_FACTOR = 1.5;

    public ExpandPaddlePowerUp(double x, double y) {
        // Use a reasonable default duration (seconds) for paddle expansion
        super(x, y, PowerUpType.EXPAND_PADDLE, 8.0);
    }

    @Override
    public void apply(Paddle paddle) {
        // Always use the original width to prevent stacking multiplier
        double targetWidth = paddle.getOriginalWidth() * EXPANSION_FACTOR;
        paddle.setWidth(targetWidth);
    }

    @Override
    public void remove(Paddle paddle) {
        // Reset to original width
        paddle.setWidth(paddle.getOriginalWidth());
    }

    @Override
    protected void renderIcon(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        //Draw expanded paddle icon
        gc.fillRect(x + 5, y + 12, 20, 6);
        //Draw arrows indicating expansion
        gc.fillPolygon(
                new double[]{x + 3, x + 6, x + 3},
                new double[]{y + 15, y + 12, y + 9},
                3
        );
        gc.fillPolygon(
                new double[]{x + 27, x + 24, x + 27},
                new double[]{y + 15, y + 12, y + 9},
                3
        );
    }
}
