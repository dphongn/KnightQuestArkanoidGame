package com.knightquest.arkanoid.observer;

import java.util.ArrayList;
import java.util.List;

import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.powerup.PowerUp;

/**
 * Observer Pattern - Event Manager to manage game event listeners and notify them of events.
 */
public class GameEventManager {
    private final List<GameEventListener> listeners;

    public GameEventManager() {
        this.listeners = new ArrayList<>();
    }

    /**
     * Registers a new game event listener.
     */
    public void registerListener(GameEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a game event listener.
     */
    public void removeListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Remove all listeners.
     */
    public void clearListeners() {
        listeners.clear();
    }

    // Event notification methods would go here.

    public void notifyBrickDestroyed(Brick brick, int score) {
        for (GameEventListener listener : listeners) {
            listener.onBrickDestroyed(brick, score);
        }
    }

    public void notifyPowerUpCollected(PowerUp powerUp) {
        for (GameEventListener listener : listeners) {
            listener.onPowerUpCollected(powerUp);
        }
    }

    public void notifyLifeLost(int remainingLives) {
        for (GameEventListener listener : listeners) {
            listener.onLifeLost(remainingLives);
        }
    }

    public void notifyLevelCompleted(int levelNumber, int score) {
        for (GameEventListener listener : listeners) {
            listener.onLevelCompleted(levelNumber, score);
        }
    }
    public void notifyGameOver(boolean won, int finalScore) {
        for (GameEventListener listener : listeners) {
            listener.onGameOver(won, finalScore);
        }
    }

    public void notifyScoreChanged(int newScore) {
        for (GameEventListener listener : listeners) {
            listener.onScoreChanged(newScore);
        }
    }

    public void notifyExplosion(double x, double y, double radius) {
        for (GameEventListener listener : listeners) {
            listener.onExplosion(x, y, radius);
        }
    }

    public void notifyPaddleSizeChanged(String eventType) {
        for (GameEventListener listener : listeners) {
            listener.onPaddleSizeChanged(eventType);
        }
    }
}
