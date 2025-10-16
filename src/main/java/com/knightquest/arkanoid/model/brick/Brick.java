package com.knightquest.arkanoid.model.brick;

import com.knightquest.arkanoid.model.GameObject;
import com.knightquest.arkanoid.model.powerup.PowerUp;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Brick extends GameObject {
    protected int hitPoints; //HP of brick
    protected Color color;
    protected BrickType type;

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

    @Override
    public void render(GraphicsContext gc) {
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

    public abstract PowerUp onDestroyed();
}