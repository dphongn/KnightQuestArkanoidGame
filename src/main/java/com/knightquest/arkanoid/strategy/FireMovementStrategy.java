package com.knightquest.arkanoid.strategy;

import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.brick.Brick;

/**
 * Fire Movement Strategy for Ball movement behaviors.
 */
public class FireMovementStrategy implements MovementStrategy {
    private static final double FIRE_SPEED_MULTIPLIER = 1.2;

    @Override
    public void move(Ball ball, double deltaTime) {
        // Fire movement - move faster than normal balls
        ball.move(deltaTime * FIRE_SPEED_MULTIPLIER);
    }

    @Override
    public boolean handleBrickCollision(Ball ball, Brick brick) {
        // Fire behavior: destroys bricks instantly and bounces
        while (brick.isActive()) {
            brick.takeHit();
        }
        return true;
    }

    @Override
    public String getStrategyName() {
        return "Fire";
    }

}

