package com.knightquest.arkanoid.model.brick;

import com.knightquest.arkanoid.model.GameObject;
import com.knightquest.arkanoid.model.powerup.PowerUpType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public abstract class Brick extends GameObject {
    protected int hitPoints; //HP of brick
    protected Color color;
    protected BrickType type;
    protected PowerUpType powerUpType;
    protected double dropChance = 0.3; // Chance to drop power-up upon destruction
    private static final Map<String, Image> imageCache = new HashMap<>();

    /**
     * Basic contructor for every brick.
     */
    public Brick(double x, double y, double width, double height, int hitPoints) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
    }

    @Override
    public void update(double deltaTime) {
        // Nothing because of static object
    }

    protected String getImagePath() {
        return null;
    }

    public void render(GraphicsContext gc) {
        String path = getImagePath();
        Image img = null;

        if (path != null) {
            img = imageCache.computeIfAbsent(path, p -> {
                try {
                    Image i = new Image(getClass().getResourceAsStream(p));
                    return i.isError() ? null : i;
                } catch (Exception e) {
                    System.err.println("Không thể load ảnh: " + p);
                    return null;
                }
            });
        }

        if (img != null) {
            gc.drawImage(img, x, y, width, height);
        } else {
            renderFallback(gc);
        }
    }

    protected void renderFallback(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, width, height);
    }

    /**
     * Update when take hit.
     */
    public void takeHit() {
        hitPoints--;
        if (hitPoints <= 0) {
            active = false;
        }
    }

    /**
     * Destroy when 0 HP.
     */
    public boolean isDestroyed() {
        return !active;
    }

    public BrickType getType() {
        return type;
    }

    public PowerUpType getPowerUpDrop() {
        if (powerUpType != null && Math.random() <= dropChance) {
            return powerUpType;
        }
        return null;
    }

    public void setPowerUpDrop(PowerUpType type) {
        this.powerUpType = type;
    }

    public void setDropChance(double chance) {
        this.dropChance = Math.max(0.0, Math.min(1, chance));
    }

    public int getHealth() {
        return hitPoints;
    }

    public void destroy() {
        hitPoints = 0;
        active = false;
    }
}