package com.knightquest.arkanoid.strategy;

import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.brick.Brick;

/**
 * Normal Movement Strategy for Ball movement behaviors.
 * Balls move in a straight line and bounce off surfaces normally.
 */
public class NormalMovementStrategy implements MovementStrategy {
    @Override
    public void move(Ball ball, double deltaTime) {
        ball.move(deltaTime); // Default movement behavior
    }

    @Override
    public boolean handleBrickCollision(Ball ball, Brick brick) {
        // Normal balls do not have special collision handling
        brick.takeHit();
        return true; // Ball should bounce
    }

    @Override
    public String getStrategyName() {
        return "Normal";
    }
}
