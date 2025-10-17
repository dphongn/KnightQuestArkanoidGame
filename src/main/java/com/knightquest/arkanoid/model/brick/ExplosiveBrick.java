package com.knightquest.arkanoid.model.brick;

import com.knightquest.arkanoid.model.powerup.PowerUp;
import javafx.scene.paint.Color;

public class ExplosiveBrick extends Brick {
    private double explosionRadius;

    public ExplosiveBrick(double x, double y, double width, double height, int hitPoints, double explosionRadius) {
        super(x, y, width, height, hitPoints);
        this.color = Color.ORANGE;
        this.type = BrickType.EXPLOSIVE;
        this.explosionRadius = explosionRadius;
    }

    public void explode() {
        System.out.println("ExplosiveBrick exploded at (" + x + ", " + y +
                ") with radius " + explosionRadius);
    }

    @Override
    public void takeHit() {
        super.takeHit();
        if (isDestroyed()) {
            explode();
        }
    }

    @Override
    public PowerUp onDestroyed() {
        return null;
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }

    public void setExplosionRadius(double explosionRadius) {
        this.explosionRadius = explosionRadius;
    }
}
