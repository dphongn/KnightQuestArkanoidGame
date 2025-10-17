package com.knightquest.arkanoid.model.brick;

import com.knightquest.arkanoid.model.powerup.PowerUp;
import javafx.scene.paint.Color;

public class StrongBrick extends Brick {
    public StrongBrick(double x, double y, double width, double height, int hitPoints) {
        super(x, y, width, height, hitPoints);
        this.color = Color.BLUE;
        this.type = BrickType.STRONG;
    }

    @Override
    public void takeHit() {
        super.takeHit();
        if (hitPoints == 1 && active) {
            this.type = BrickType.NORMAL;
            this.color = Color.GREEN;
        }
    }

    @Override
    public PowerUp onDestroyed() {
        return null;
    }
}
