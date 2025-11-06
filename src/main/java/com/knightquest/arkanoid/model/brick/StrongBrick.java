package com.knightquest.arkanoid.model.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class StrongBrick extends Brick {
    private static final int INITIAL_HP = 3;
    private static final Color[] colorStages = {
            Color.DARKBLUE,
            Color.BLUE,
            Color.LIGHTBLUE
    };
    private static final String[] imagePaths = {
            "/images/sprites/bricks/strongbrick1.gif",
            "/images/sprites/bricks/strongbrick2.gif",
            "/images/sprites/bricks/normalbrick.gif"
    };
    private static final Image[] images = new Image[INITIAL_HP];
    private static boolean imagesLoaded = false;

    public StrongBrick(double x, double y, double width, double height) {
        super(x, y, width, height, INITIAL_HP);
        this.color = colorStages[0];
        this.type = BrickType.STRONG;
    }

    @Override
    public void takeHit() {
        super.takeHit();
        updateColor();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!imagesLoaded) {
            for (int i = 0; i < INITIAL_HP; ++i) {
                try {
                    images[i] = new Image(getClass().getResourceAsStream(imagePaths[i]));
                    if (images[i].isError()) {
                        images[i] = null;
                    }
                } catch (Exception e) {
                    System.err.println("Không thể load ảnh: " + imagePaths[i]);
                    images[i] = null;
                }
            }
            imagesLoaded = true;
        }

        int index = Math.max(0, Math.min(INITIAL_HP - 1, INITIAL_HP - hitPoints));
        Image img = images[index];
        if (img != null) {
            gc.drawImage(img, x, y, width, height);
        } else {
            super.render(gc);
        }
    }

    public void updateColor() {
        if (hitPoints > 0 && hitPoints <= INITIAL_HP) {
            this.color = colorStages[INITIAL_HP - hitPoints];
        }
    }
}
