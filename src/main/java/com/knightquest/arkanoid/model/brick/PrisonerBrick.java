package com.knightquest.arkanoid.model.brick;

import com.knightquest.arkanoid.model.powerup.PowerUpType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class PrisonerBrick extends Brick {
    private boolean powerUpDropped = false;
    private final Color prisonerColor = Color.PURPLE;
    private final Color chainColor = Color.DARKGRAY;
    private static int nextPowerUpIndex = 0;
    private static final String imagePath = "/images/sprites/bricks/prisonerbrick.gif";
    private static Image prisonerBrickImage;
    private static boolean imageLoaded = false;

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
        if (!imageLoaded) {
            try {
                prisonerBrickImage = new Image(getClass().getResourceAsStream(imagePath));
                if (prisonerBrickImage.isError()) {
                    prisonerBrickImage = null;
                }
            } catch (Exception e) {
                System.err.println("Không thể load ảnh: " + imagePath);
                prisonerBrickImage = null;
            }
            imageLoaded = true;
        }

        if (prisonerBrickImage != null) {
            gc.drawImage(prisonerBrickImage, x, y, width, height);
        } else {
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
    }

    public boolean hasPowerUpDropped() {
        return powerUpDropped;
    }

    public double[] getPowerUpSpawnPosition() {
        return new double[]{x + width / 2, y + height / 2};
    }

    @Override
    public PowerUpType getPowerUpDrop() {
        if (powerUpDropped) {
            PowerUpType[] allTypes = PowerUpType.values();
            PowerUpType typeToDrop = allTypes[nextPowerUpIndex];
            nextPowerUpIndex++;
            if (nextPowerUpIndex >= allTypes.length) {
                nextPowerUpIndex = 0; // Quay vòng lại từ đầu
            }
            powerUpDropped = false;
            return typeToDrop;
        }
        return null;
    }
}
