package com.knightquest.arkanoid.model.powerup;

import com.knightquest.arkanoid.model.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class MagnetPaddlePowerUp extends PowerUp {

    public MagnetPaddlePowerUp(double x, double y) {
        super(x, y, PowerUpType.MAGNET_PADDLE, PowerUpType.MAGNET_PADDLE.getDefaultDuration());
    }

    @Override
    public void apply(Paddle paddle) {
        paddle.setMagnetic(true);
    }

    @Override
    public void remove(Paddle paddle) {
        paddle.setMagnetic(false);
        paddle.releaseBall();   //Release ball if it's stuck
    }

    @Override
    protected void renderIcon(GraphicsContext gc) {
        //Draw U-shaped magnet
        gc.setFill(Color.WHITE);
        gc.setLineWidth(3);

        //Left pole (red)
        gc.setFill(Color.rgb(231, 76, 60));
        gc.fillRect(x + 6, y + 8, 5, 14);

        //Right pole (blue)
        gc.setFill(Color.rgb(52, 152, 219));
        gc.fillRect(x + 19, y + 8, 5, 14);

        //Bottom connector
        gc.setStroke(Color.WHITE);
        gc.strokeArc(x + 6, y + 14, 18, 16, 180, 180, ArcType.OPEN);

        //Draw magnetic field lines
        gc.setStroke(Color.LIGHTBLUE);
        gc.setLineWidth(1);
        gc.strokeArc(x + 3, y + 6, 24, 10, 0, 180, ArcType.OPEN);
        gc.strokeArc(x + 5, y + 7, 20, 8, 0, 180, ArcType.OPEN);
    }
}
