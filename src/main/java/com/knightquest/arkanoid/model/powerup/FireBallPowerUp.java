package com.knightquest.arkanoid.model.powerup;

import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FireBallPowerUp extends PowerUp {
    public FireBallPowerUp(double x, double y) {
        super(x, y, PowerUpType.FIRE_BALL, PowerUpType.FIRE_BALL.getDefaultDuration());
    }

    @Override
    public void apply(Paddle paddle) {
        GameManager gm = GameManager.getInstance();
        Ball ball = gm.getBall();
        if (ball != null) {
            ball.setOnFire(true);
        }

        //Apply to all balls if multi-ball is active
        if (gm.getBalls() != null) {
            for (Ball b : gm.getBalls()) {
                b.setOnFire(true);
            }
        }
    }

    @Override
    public void remove(Paddle paddle) {
        GameManager gm = GameManager.getInstance();
        Ball ball = gm.getBall();
        if (ball != null) {
            ball.setOnFire(false);
        }

        //Remove from all balls
        if (gm.getBalls() != null) {
            for (Ball b : gm.getBalls()) {
                b.setOnFire(false);
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        //Draw fire ball
        gc.setFill(Color.ORANGE);
        gc.fillOval(x + 9, y + 9, 12, 12);

        //Draw flames
        gc.setFill(Color.rgb(255, 100, 0));

        //Flame 1 (left)
        gc.fillPolygon(
                new double[]{x + 8, x + 11, x + 9},
                new double[]{y + 12, y + 6, y + 9},
                3
        );

        //Flame 2 (center)
        gc.fillPolygon(
                new double[]{x + 13, x + 16, x + 14},
                new double[]{y + 14, y + 4, y + 8},
                3
        );

        //Flame 3 (right)
        gc.fillPolygon(
                new double[]{x + 18, x + 21, x + 20},
                new double[]{y + 12, y + 7, y + 10},
                3
        );

        //Draw ball core (brighter)
        gc.setFill(Color.YELLOW);
        gc.fillOval(x + 12, y + 12, 6, 6);
    }
}
