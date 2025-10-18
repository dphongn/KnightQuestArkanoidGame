package com.knightquest.arkanoid.model.brick;

import com.knightquest.arkanoid.model.powerup.PowerUp;
import javafx.scene.paint.Color;

public class StrongBrick extends Brick {
    private static final int INITIAL_HP = 3;
    private static final Color[] colorStages = {
            Color.DARKBLUE,
            Color.BLUE,
            Color.LIGHTBLUE
    };

    public StrongBrick(double x, double y, double width, double height) {
        super(x, y, width, height, INITIAL_HP);
        this.color = colorStages[0];
    }

    @Override
    public void takeHit() {
        super.takeHit();
        updateColor();
    }

    public void updateColor() {
        if (hitPoints > 0 && hitPoints <= INITIAL_HP) {
            this.color = colorStages[INITIAL_HP - hitPoints];
        }
    }

    @Override
    public PowerUp onDestroyed() {
        return null;
    }
}
