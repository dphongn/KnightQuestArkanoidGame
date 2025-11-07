package com.knightquest.arkanoid.model.brick;

import javafx.scene.paint.Color;

public class UnbreakableBrick extends Brick {
    private static final String imagePath = "/images/sprites/bricks/unbreakablebrick.gif";

    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x, y, width, height, Integer.MAX_VALUE);
        this.color = Color.GRAY;
        this.type = BrickType.UNBREAKABLE;
    }

    protected String getImagePath() {
        return imagePath;
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

