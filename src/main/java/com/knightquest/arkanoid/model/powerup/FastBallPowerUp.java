package com.knightquest.arkanoid.model.powerup;

import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FastBallPowerUp extends PowerUp {
    private static final double SPEED_MULTIPLIER = 1.5;

    public FastBallPowerUp(double x, double y) {
        super(x, y, PowerUpType.FAST_BALL, PowerUpType.FAST_BALL.getDefaultDuration());
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
        //Draw ball
        gc.fillOval(x + 10, y + 10, 10, 10);

        //Draw multiple speed lines (mỏe lines = faster)
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeLine(x + 3, y + 10, x + 7, y + 10);
        gc.strokeLine(x + 3, y + 15, x + 7, y + 15);
        gc.strokeLine(x + 3, y + 20, x + 7, y + 20);


        //Arrow for speed indication
        gc.fillPolygon(
                new double[]{x + 23, x + 27, x + 23},
                new double[]{y + 12, y + 15, y + 18},
                3
        );
    }
}
