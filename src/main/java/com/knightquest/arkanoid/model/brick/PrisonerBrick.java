package com.knightquest.arkanoid.model.brick;

import com.knightquest.arkanoid.model.powerup.PowerUp;
import com.knightquest.arkanoid.model.powerup.PowerUpType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PrisonerBrick extends Brick {
    private boolean powerUpDropped = false;
    private final Color prisonerColor = Color.PURPLE;
    private final Color chainColor = Color.DARKGRAY;

    public PrisonerBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 1);
        this.color = prisonerColor;
        this.type = BrickType.PRISONER;
    }

    @Override
    public void takeHit() {
        super.takeHit();
        if (!active && !powerUpDropped) {
            powerUpDropped = true;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);

        gc.setStroke(chainColor);
        gc.setLineWidth(2);
        int numBars = 4;
        for (int i = 1; i < numBars; ++i) {
            double barX = x + (width / numBars) * i;
            gc.strokeLine(barX, y, barX, y + height);
        }

        gc.setFill(Color.LIGHTGRAY);
        double headSize = width / 2;
        gc.fillOval(x + width / 2 - headSize / 2,
                y + headSize / 3, headSize, headSize);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, width, height);
    }

    public boolean hasPowerUpDropped() {
        return powerUpDropped;
    }

    public double[] getPowerUpSpawnPosition() {
        return new double[]{x + width / 2, y + height / 2};
    }
}
