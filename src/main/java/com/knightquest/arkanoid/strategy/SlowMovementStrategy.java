package com.knightquest.arkanoid.strategy;

import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.brick.Brick;

/**
 * Slow movement strategy - reduces effective movement by scaling the deltaTime.
 * This keeps the ball's velocity magnitude normalized but makes it travel slower per frame.
 */
public class SlowMovementStrategy implements MovementStrategy {
    private final double slowFactor;

    public SlowMovementStrategy(double slowFactor) {
        this.slowFactor = slowFactor;
    }

    @Override
    public void move(Ball ball, double deltaTime) {
        // Scale the deltaTime to slow down movement while keeping velocity magnitude normalized
        ball.move(deltaTime * slowFactor);
    }

    @Override
    public boolean handleBrickCollision(Ball ball, Brick brick) {
        // Normal collision behavior (bounce and damage)
        brick.takeHit();
        return true;
    }

    @Override
    public String getStrategyName() {
        return "Slow";
    }
}
