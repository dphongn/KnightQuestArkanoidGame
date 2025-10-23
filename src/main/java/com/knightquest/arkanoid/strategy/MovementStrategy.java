package com.knightquest.arkanoid.strategy;

import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.brick.Brick;

/**
 * Strategy Pattern - Movement Strategy for Ball movement behaviors.
 */
public interface MovementStrategy {
    /**
     * Moves the ball according to the specific strategy.
     * @param ball The ball to move.
     * @param deltaTime The time elapsed since the last update.
     */
    void move(Ball ball, double deltaTime);

    /**
     * Handles collision between the ball and a brick.
     * @param ball The ball involved in the collision.
     * @param brick The brick involved in the collision.
     * @return True if the collision was handled, false otherwise.
     */
    boolean handleBrickCollision(Ball ball, Brick brick);

    /**
     * Gets the name of the movement strategy.
     * @return The name of the strategy.
     */
    String getStrategyName();

}
