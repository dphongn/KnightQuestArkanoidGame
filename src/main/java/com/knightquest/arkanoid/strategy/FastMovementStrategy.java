package com.knightquest.arkanoid.strategy;

import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.brick.Brick;

/**
 * Fast movement strategy - increases effective movement by scaling the deltaTime.
 * This keeps the ball's velocity magnitude normalized but makes it travel faster per frame.
 */
public class FastMovementStrategy implements MovementStrategy {
    private final double speedFactor;

    public FastMovementStrategy(double speedFactor) {
        this.speedFactor = speedFactor;
    }

    @Override
    public void move(Ball ball, double deltaTime) {
        // Scale the deltaTime to speed up movement while keeping velocity magnitude normalized
        ball.move(deltaTime * speedFactor);
    }

    @Override
    public boolean handleBrickCollision(Ball ball, Brick brick) {
        // Normal collision behavior (bounce and damage)
        brick.takeHit();
        return true;
    }

    @Override
    public String getStrategyName() {
        return "Fast";
    }
}
