package com.knightquest.arkanoid.strategy;

import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.brick.Brick;

public class PierceMovementStratrgy implements MovementStrategy {

    @Override
    public void move(Ball ball, double deltaTime) {
        // Pierce movement behaves like normal movement
        ball.move(deltaTime);
    }

    @Override
    public boolean handleBrickCollision(Ball ball, Brick brick) {
        // Pierce balls do not bounce off bricks; they pass through
        brick.takeHit();
        return false; // Ball should not bounce
    }

    @Override
    public String getStrategyName() {
        return "Pierce";
    }
}
