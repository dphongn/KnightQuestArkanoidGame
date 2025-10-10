package com.knightquest.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

public abstract class GameObject {
    protected double x, y, width, height;
    protected boolean active = true;

    /**
     * Basic contructor for all objects.
     */
    public GameObject(double x, double y, double width, double height) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
    }

    /**
     * Update the object between 2 frames.
     */
    public abstract void update(double deltaTime);

    /**
     * Draw according to the object.
     */
    public abstract void render(GraphicsContext gc);

    /**
     * Returns the bounding rectangle of the object.
     */
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    /**
     * Check if 2 objects interact.
     */
    public boolean intersects(GameObject other) {
        return getBounds().intersects(other.getBounds());
    }

    /**
     * Getter and Setter.
     */
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getWidth() {
        return width;
    }
    public void setWidth(double width) {
        this.width = width;
    }
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }
}
