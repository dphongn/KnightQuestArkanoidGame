package com.knightquest.arkanoid.model.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class UnbreakableBrick extends Brick {
    private static final String imagePath = "/images/sprites/bricks/unbreakablebrick.gif";
    private static Image unbreakableBrickImage;
    private static boolean imageLoaded = false;

    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x, y, width, height, Integer.MAX_VALUE);
        this.color = Color.GRAY;
        this.type = BrickType.UNBREAKABLE;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!imageLoaded) {
            try {
                unbreakableBrickImage = new Image(getClass().getResourceAsStream(imagePath));
                if (unbreakableBrickImage.isError()) {
                    unbreakableBrickImage = null;
                }
            } catch (Exception e) {
                System.err.println("Không thể load ảnh: " + imagePath);
                unbreakableBrickImage = null;
            }
            imageLoaded = true;
        }

        if (unbreakableBrickImage != null) {
            gc.drawImage(unbreakableBrickImage, x, y, width, height);
        } else {
            super.render(gc);
        }
    }

    @Override
    public void takeHit() {
        // Unbreakable - no damage taken
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }
}

