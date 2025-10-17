package com.knightquest.arkanoid.model.brick;

import com.knightquest.arkanoid.model.powerup.PowerUp;
import javafx.scene.paint.Color;

public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 1);
        this.color = Color.GRAY;
        this.type = BrickType.UNBREAKABLE;
    }

    @Override
    public void takeHit() {
        // UnbreakableBrick can not take damage.
    }

    @Override
    public PowerUp onDestroyed() {
        return null;
    }
}

