package com.knightquest.arkanoid.model.powerup;

import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PierceBallPowerUp extends PowerUp {

    public PierceBallPowerUp(double x, double y) {
        super(x, y, PowerUpType.PIERCE_BALL, PowerUpType.PIERCE_BALL.getDefaultDuration());
    }

    @Override
    public void apply(Paddle paddle) {
        GameManager gm = GameManager.getInstance();
        Ball ball = gm.getBall();
        if (ball != null) {
            ball.setPiercing(true);
        }

        //Apply to all balls if multi-ball is active
        if (gm.getBalls() != null) {
            for (Ball b : gm.getBalls()) {
                b.setPiercing(true);
            }
        }
    }

    @Override
    public void remove(Paddle paddle) {
        GameManager gm = GameManager.getInstance();
        Ball ball = gm.getBall();
        if (ball != null) {
            ball.setPiercing(false);
        }

        //Remove from all balls
        if (gm.getBalls() != null) {
            for (Ball b : gm.getBalls()) {
                b.setPiercing(false);
            }
        }
    }

    @Override
    protected void renderIcon(GraphicsContext gc) {
        //Draw ball with pierce effect
        gc.setFill(Color.WHITE);
        gc.fillOval(x + 10, y + 10, 10, 10);

        //Draw pierce arrows going through
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2.5);

        //Left arrow
        gc.strokeLine(x + 5, y + 15, x + 12, y + 15);
        gc.fillPolygon(
                new double[]{x + 3, x + 7, x + 7},
                new double[]{y + 15, y + 12, y + 18},
                3
        );

        //Right arrow
        gc.strokeLine(x + 18, y + 15, x + 25, y + 15);
        gc.fillPolygon(
                new double[]{x + 27, x + 23, x + 23},
                new double[]{y + 15, y + 12, y + 18},
                3
        );
    }
}
