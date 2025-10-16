package com.knightquest.arkanoid.model.brick;

import com.knightquest.arkanoid.model.powerup.PowerUp;
import javafx.scene.paint.Color;

public class StrongBrick extends Brick {
    public StrongBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 2);
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
