package com.knightquest.arkanoid.model;

import static com.knightquest.arkanoid.util.Constants.SCREEN_WIDTH;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Boss extends MovableObject {

    private int health;
    private double maxHealth;
    private double invulnerabilityTimer;
    private boolean isEnraged = false;
    private double baseSpeed;

    public Boss(double x, double y, double size, int initialHealth, double speed) {
        super(x, y, size, size);
        this.health = initialHealth;
        this.maxHealth = initialHealth;
        setVelocity(speed, 0);
        this.invulnerabilityTimer = 0;
        this.baseSpeed = speed;
    }

    public void reverseDirection() {
        setDx(-getDx());
    }

    public void update(double deltaTime) {
        super.move(deltaTime);

        if (x <= 0) {
            x = 0;
            reverseDirection();
        }

        if (x + width >= SCREEN_WIDTH) {
            x = SCREEN_WIDTH - width;
            reverseDirection();
        }

        if (invulnerabilityTimer > 0) {
            invulnerabilityTimer -= deltaTime;
        }
    }

    /**
     * Hàm render (vẽ) Boss lên màn hình.
     *
     * @param gc GraphicsContext từ JavaFX Canvas.
     */
    public void render(GraphicsContext gc) {
        boolean shouldDraw = true;
        if (invulnerabilityTimer > 0) {
            if ((int) (invulnerabilityTimer * 20) % 2 != 0) {
                shouldDraw = false;
            }
        }

        if (shouldDraw) {
            if (isEnraged) {
                gc.setFill(Color.RED);
            } else {
                gc.setFill(Color.YELLOW);
            }
            gc.fillRect(x, y, width, height);
        }

        double healthPercentage = health / maxHealth;
        double healthBarWidth = width * healthPercentage;
        double healthBarHeight = 10;
        double healthBarY = y - healthBarHeight - 5;

        gc.setFill(Color.RED);
        gc.fillRect(x, healthBarY, width, healthBarHeight);

        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(x, healthBarY, healthBarWidth, healthBarHeight);
    }

    public void takeHit() {
        if (invulnerabilityTimer > 0) {
            return;
        }

        if (health > 0) {
            health--;
        }

        if (!isEnraged && health <= maxHealth / 2) {
            isEnraged = true;
            setDx(Math.signum(getDx()) * baseSpeed * 1.5);
            System.out.println("BOSS IS ENRAGED!");
        }

        invulnerabilityTimer = 0.2;
    }

    public boolean isDefeated() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }
}