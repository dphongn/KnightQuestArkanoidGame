package com.knightquest.arkanoid.manager;

import com.knightquest.arkanoid.observer.GameEventListener;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExplosionEffectManager implements GameEventListener {
    private static final String EXPLOSION_GIF_PATH = "/images/particles/explosion.gif";
    private final Image explosionGif = new Image(getClass().getResourceAsStream(EXPLOSION_GIF_PATH));
    private final List<ExplosionEffect> effects = new ArrayList<>();
    private static final double DEFAULT_DURATION_MS = 600;

    @Override
    public void onExplosion(double x, double y, double radius) {
        double size = radius * 2.0;
        effects.add(new ExplosionEffect(x, y, size, DEFAULT_DURATION_MS, explosionGif));
    }

    public void update(double deltaTimeSeconds) {
        double dtMs = deltaTimeSeconds * 1000.0;
        Iterator<ExplosionEffect> it = effects.iterator();
        while (it.hasNext()) {
            ExplosionEffect e = it.next();
            e.update(dtMs);
            if (!e.isAlive()) {
                it.remove();
            }
        }
    }

    public void render(GraphicsContext gc) {
        for (ExplosionEffect e : effects) {
            e.render(gc);
        }
    }

    public void onBrickDestroyed(com.knightquest.arkanoid.model.brick.Brick brick, int score) {
    }

    public void onPowerUpCollected(com.knightquest.arkanoid.model.powerup.PowerUp powerUp) {
    }

    public void onLifeLost(int remainingLives) {
    }

    public void onLevelCompleted(int levelNumber, int score) {
    }

    public void onGameOver(boolean won, int finalScore) {
    }

    public void onScoreChanged(int newScore) {
    }

    public void onPaddleSizeChanged(String eventType) {
    }

    public void onMenuSelectionChanged() {
    }

    public void onMenuOptionSelected() {
    }

    public void onBrickHit(com.knightquest.arkanoid.model.brick.Brick brick) {
    }

    public void onBallPaddleCollision() {
    }
}

class ExplosionEffect {
    private final double x, y, size;
    private final double durationMs;
    private final Image gif;
    private double elapsedMs = 0;

    ExplosionEffect(double x, double y, double size, double durationMs, Image gif) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.durationMs = durationMs;
        this.gif = gif;
    }

    void update(double dtMs) {
        elapsedMs += dtMs;
    }

    boolean isAlive() {
        return elapsedMs < durationMs;
    }

    void render(GraphicsContext gc) {
        double drawX = x - size / 2.0;
        double drawY = y - size / 2.0;
        gc.drawImage(gif, drawX, drawY, size, size);
    }
}