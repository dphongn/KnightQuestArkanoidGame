package com.knightquest.arkanoid.model.brick;

import com.knightquest.arkanoid.model.powerup.PowerUp;
import javafx.scene.paint.Color;

public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x, y, width, height, Integer.MAX_VALUE);
        this.color = Color.GRAY;
        this.type = BrickType.UNBREAKABLE;
    }

    @Override
    public void takeHit() {
        // Unbreakable - no damage taken
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }

    @Override
    public PowerUp onDestroyed() {
        return null;
    }
}

