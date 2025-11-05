package com.knightquest.arkanoid.model.brick;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ExplosiveBrick extends Brick {
    private static final double EXPLOSION_RADIUS = 80.0;
    private boolean hasExploded = false;

    public ExplosiveBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 1);
        this.color = Color.ORANGE;
        this.type = BrickType.EXPLOSIVE;
    }

    @Override
    public void takeHit() {
        super.takeHit();
        if (!isActive()) {
            hasExploded = true;
            explode(null);
        }
    }

    public void takeHit(List<Brick> allBricks) {
        super.takeHit();
        if (!isActive()) {
            explode(allBricks);
        }
    }

    public void explode(List<Brick> allBricks) {
        if (allBricks == null) return;
        double radius = EXPLOSION_RADIUS;
        for (Brick other : allBricks) {
            if (other != this && other.isActive()) {
                double dx = other.getX() - this.getX();
                double dy = other.getY() - this.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance <= radius) {
                    if (other instanceof ExplosiveBrick explosiveBrick) {
                        explosiveBrick.takeHit(allBricks);
                    }
                    other.takeHit();
                }
            }
        }
    }

    public List<Brick> getExplosionTargets(List<Brick> allBricks) {
        List<Brick> targets = new ArrayList<>();
        double centerX = x + width / 2;
        double centerY = y + height / 2;
        for (Brick brick : allBricks) {
            if (!brick.isActive()) {
                continue;
            }
            double brickCenterX = brick.getX() + brick.getWidth() / 2;
            double brickCenterY = brick.getY() + brick.getHeight() / 2;
            double distance = Math.sqrt(
                    (centerX - brickCenterX) * (centerX - brickCenterX)
                            + (centerY - brickCenterY) * (centerY - brickCenterY)
            );
            if (distance <= EXPLOSION_RADIUS) {
                targets.add(brick);
            }
        }
        return targets;
    }

    public boolean hasExploded() {
        return hasExploded;
    }

    public double getExplosionRadius() {
        return EXPLOSION_RADIUS;
    }
}
