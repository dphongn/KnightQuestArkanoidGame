package com.knightquest.arkanoid.observer;

import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.powerup.PowerUp;

/**
 * Interface for listening to game events such as brick destruction and power-up collection.
 */
public interface GameEventListener {
    /**
     * Called when a brick is destroyed.
     * @param brick The brick that was destroyed.
     * @param score The score awarded for destroying the brick.
     */
    void onBrickDestroyed(Brick brick, int score);

    /**
     * Called when a power-up is collected.
     * @param powerUp The power-up that was collected.
     */
    void onPowerUpCollected(PowerUp powerUp);

    /**
     * Called when the player loses a life.
     * @param remainingLives The number of lives remaining after the loss.
     */
    void onLifeLost(int remainingLives);

    /**
     * Called when a level is completed.
     * @param levelNumber The number of the level that was completed.
     * @param score The score achieved in the completed level.
     */
    void onLevelCompleted(int levelNumber, int score);

    /**
     * Called when the game is over.
     * @param won True if the player won, false if they lost.
     * @param finalScore The final score of the game.
     */
    void onGameOver(boolean won, int finalScore);

    /**
     * Called when the score changes.
     * @param newScore The updated score.
     */
    void onScoreChanged(int newScore);

    /**
     * Called when an explosion occurs in the game.
     * @param x The x-coordinate of the explosion.
     * @param y The y-coordinate of the explosion.
     * @param radius The radius of the explosion effect.
     */
    void onExplosion(double x, double y, double radius);

    /**
     * Called when the paddle size changes.
     * @param evenType
     */
    void onPaddleSizeChanged(String eventType);

    /**
     * Called when menu selection changes.
     */
    void onMenuSelectionChanged();

    /**
     * Called when menu option is confirmed.
     */
    void onMenuOptionSelected();

    /**
     * Called when a brick is hit(but not necessarily destroyed).
     * Useful for StrongBrick that needs multiple hits.
     */
    void onBrickHit(Brick brick);

    /**
     * Called when ball collides with paddle.
     */
    void onBallPaddleCollision();
}
