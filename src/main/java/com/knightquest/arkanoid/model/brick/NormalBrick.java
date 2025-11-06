package com.knightquest.arkanoid.model.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class NormalBrick extends Brick {
    private static Image normalBrickImage;
    private static boolean imageLoadAttempted = false;
    private static final String imagePath = "/images/sprites/bricks/normalbrick.gif";

    public NormalBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 1);
        this.color = Color.GREEN;
        this.type = BrickType.NORMAL;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!imageLoadAttempted) {
            try {
                normalBrickImage = new Image(getClass().getResourceAsStream(imagePath));
                if (normalBrickImage.isError()) {
                    normalBrickImage = null;
                }
            } catch (Exception e) {
                System.err.println("Lỗi load file ảnh: " + imagePath);
                normalBrickImage = null;
            }
            imageLoadAttempted = true;
        }

        if (normalBrickImage != null) {
            gc.drawImage(normalBrickImage, x, y, width, height);
        } else {
            super.render(gc);
        }
    }
}
