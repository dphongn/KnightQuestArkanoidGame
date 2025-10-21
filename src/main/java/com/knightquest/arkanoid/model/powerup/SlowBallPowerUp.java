package com.knightquest.arkanoid.model.powerup;

import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SlowBallPowerUp extends PowerUp {
    private static final double SPEED_MULTIPLIER = 0.7;

    public SlowBallPowerUp(double x, double y) {
        super(x, y, PowerUpType.SLOW_BALL, PowerUpType.SLOW_BALL.getDefaultDuration());
    }

    @Override
    public void apply(Paddle paddle) {
        GameManager gm = GameManager.getInstance();
        if (gm.getBall() != null) {
            gm.getBall().multiplySpeed(SPEED_MULTIPLIER);
        }
    }

    @Override
    public void remove(Paddle paddle) {
        GameManager gm = GameManager.getInstance();
        if (gm.getBall() != null) {
            gm.getBall().multiplySpeed(1.0 / SPEED_MULTIPLIER);
        }
    }

    @Override
    protected void renderIcon(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        //Drawball
        gc.fillOval(x + 10, y + 10, 10, 10);
        //Draw speed lines (fewer for slow)
        gc.setLineWidth(1.5);
        gc.strokeLine(x + 5, y + 12, x + 8, y + 12);
        gc.strokeLine(x + 5, y + 18, x + 8, y + 18);
    }
}
