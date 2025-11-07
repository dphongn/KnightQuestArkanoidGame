package com.knightquest.arkanoid.model.brick;

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
    protected String getImagePath() {
        int index = Math.max(0, INITIAL_HP - hitPoints);
        if (index >= imagePaths.length) {
            index = imagePaths.length - 1;
        }
        return imagePaths[index];
    }

    public void updateColor() {
        if (hitPoints > 0 && hitPoints <= INITIAL_HP) {
            this.color = colorStages[INITIAL_HP - hitPoints];
        }
    }
}
