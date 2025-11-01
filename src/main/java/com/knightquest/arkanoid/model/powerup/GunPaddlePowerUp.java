package com.knightquest.arkanoid.model.powerup;

import com.knightquest.arkanoid.model.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LaserPaddlePowerUp extends PowerUp {
    private static final double LASER_COOLDOWN = 0.5;

    public LaserPaddlePowerUp(double x, double y) {
        super(x, y, PowerUpType.LASER_PADDLE, PowerUpType.LASER_PADDLE.getDefaultDuration());
    }

    @Override
    public void apply(Paddle paddle) {
        paddle.setCanShootLaser(true);
        paddle.setLaserCooldown(LASER_COOLDOWN);
    }

    @Override
    public void remove(Paddle paddle) {
        paddle.setCanShootLaser(false);
    }

    @Override
    protected void renderIcon(GraphicsContext gc) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        //Draw laser beams
        gc.strokeLine(x + 10, y + 20, x + 10, y + 5);
        gc.strokeLine(x + 20, y + 20, x + 20, y + 5);
        //Draw paddle base
        gc.setFill(Color.WHITE);
        gc.fillRect(x + 7, y + 22, 16, 4);
    }
}
