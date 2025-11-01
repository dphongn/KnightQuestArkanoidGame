package com.knightquest.arkanoid.model.powerup;

import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import static com.knightquest.arkanoid.util.Constants.BALL_SPEED;

import javafx.scene.paint.Color;

public class MultiBallPowerUp extends PowerUp {
    private static final int NUMBER_OF_EXTRA_BALLS = 2;

    public MultiBallPowerUp(double x, double y) {
        super(x, y, PowerUpType.MULTI_BALL, 0.0);
        this.permanent = true;
    }

    @Override
    public void apply(Paddle paddle) {
        GameManager gm = GameManager.getInstance();
        Ball originalBall = gm.getBall();

        if (originalBall == null) return;

        //Create addditional balls
        for (int i = 0; i < NUMBER_OF_EXTRA_BALLS; i++) {
            Ball newBall = new Ball(
                    originalBall.getX(),
                    originalBall.getY()
            );

            //Set velocity at different angles
            double angleOffset = (i + 1) * 30;
            double currentAngle = Math.atan2(-originalBall.getDy(), -originalBall.getDx());
            double newAngle = currentAngle + Math.toRadians(angleOffset * (i % 2 == 0 ? 1 : -1));
//            double speed = originalBall.getSpeed();
//
//            newBall.setVelocity(
//                    speed * Math.cos(newAngle),
//                    speed * Math.sin(newAngle)
//            );

            // Always use BALL_SPEED for consistency - all spawned balls have same speed
            double speed = BALL_SPEED;

            double nvx = speed * Math.cos(newAngle);
            double nvy = speed * Math.sin(newAngle);

            // Release the spawned ball from stuck state and give it velocity so it moves
            newBall.releaseWithVelocity(nvx, nvy);
            gm.addBall(newBall);
        }
    }

    @Override
    public void remove(Paddle paddle) {
    }

    @Override
    protected void renderIcon(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        //Draw three small balls
        gc.fillOval(x + 5, y + 5, 8, 8);
        gc.fillOval(x + 17, y + 5, 8, 8);
        gc.fillOval(x + 11, y + 17, 8, 8);
    }
}
