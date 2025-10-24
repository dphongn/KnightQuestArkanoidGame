package com.knightquest.arkanoid.model.powerup;

import com.knightquest.arkanoid.model.GameObject;
import com.knightquest.arkanoid.model.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Base class for all PowerUps in the game.
 */

public abstract class PowerUp extends GameObject {
    protected PowerUpType type;
    protected double duration;
    protected boolean permanent;
    protected double fallSpeed = 150.0;
    protected double dy;

    public PowerUp(double x, double y, PowerUpType type, double duration) {
        super(x, y, 30, 30);
        this.type = type;
        this.duration = duration;
        this.permanent = false;
        this.dy = fallSpeed;
    }

    @Override
    public void update(double deltaTime) {
        if (!active) return;
        y += dy * deltaTime;

        // Check if PowerUp has fallen off the screen
        if (y > 600) {
            setActive(false);
        }
    }

    /**
     * Check if the PowerUp has fallen off the bottom of the screen.
     */
    public boolean hasFallenOff(double screenHeight) {
        return y > screenHeight;
    }

    /**
     * Check intersection with the paddle for collection.
     */
    public boolean intersects(Paddle paddle) {
        return getBounds().intersects(paddle.getBounds());
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;

        gc.setFill(getColorForType());
        gc.fillOval(x, y, width, height);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeOval(x, y, width, height);

        renderIcon(gc);
    }

    /**
     * Apply the PowerUp effect to the paddle.
     */
    public abstract void apply(Paddle paddle);

    /**
     * Remove the PowerUp effect from the paddle.
     */
    public abstract void remove(Paddle paddle);

    /**
     * Render the specific icon for this PowerUp type.
     */
    protected abstract void renderIcon(GraphicsContext gc);

    /**
     * Check if the PowerUp duration has expired.
     */
    public boolean isExpired() {
        return !permanent && duration <= 0;
    }

    /**
     * Decrease the duration of the PowerUp
     */
    public void decreaseDuration(double deltaTime) {
        if (!permanent) {
            duration -= deltaTime;
        }
    }

    /**
     * Get the color assiocated with this PowerUp type.
     */
    protected Color getColorForType() {
        return switch (type) {
            case EXPAND_PADDLE -> Color.rgb(52, 152, 219);
            case FAST_BALL -> Color.rgb(231, 76, 60);
            case SLOW_BALL -> Color.rgb(46, 204, 113);
            case MULTI_BALL -> Color.rgb(230, 126, 34);
            case PIERCE_BALL -> Color.rgb(155, 89, 182);
            case FIRE_BALL -> Color.rgb(192, 57, 43);
            case LASER_PADDLE -> Color.rgb(241, 196, 15);
            case MAGNET_PADDLE -> Color.rgb(26, 188, 156);
        };
    }

    public PowerUpType getType() {
        return type;
    }

    public double getDuration() {
        return duration;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public double getFallSpeed() {
        return fallSpeed;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setFallSpeed(double fallSpeed) {
        this.fallSpeed = fallSpeed;
        this.dy = fallSpeed;
    }
}
