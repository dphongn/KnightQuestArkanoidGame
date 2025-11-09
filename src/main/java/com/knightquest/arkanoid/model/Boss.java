package com.knightquest.arkanoid.model;

import static com.knightquest.arkanoid.util.Constants.SCREEN_WIDTH;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Boss extends MovableObject {

    private int health;
    private double maxHealth;

    public Boss(double x, double y, double size, int initialHealth, double speed) {
        super(x, y, size, size);
        this.health = initialHealth;
        this.maxHealth = initialHealth;
        setVelocity(speed, 0);
    }

    public void update(double deltaTime) {
        super.move(deltaTime);

        if (x <= 0) {
            x = 0;
            setDx(Math.abs(dx));
        }

        if (x + width >= SCREEN_WIDTH) {
            x = SCREEN_WIDTH - width;
            setDx(-Math.abs(dx));
        }
    }

    /**
     * Hàm render (vẽ) Boss lên màn hình.
     * @param gc GraphicsContext từ JavaFX Canvas.
     */
    public void render(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.fillRect(x, y, width, height);

        double healthPercentage = health / maxHealth;
        double healthBarWidth = width * healthPercentage;
        double healthBarHeight = 10;
        double healthBarY = y - healthBarHeight - 5; // 5 pixel phía trên boss

        gc.setFill(Color.RED);
        gc.fillRect(x, healthBarY, width, healthBarHeight);

        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(x, healthBarY, healthBarWidth, healthBarHeight);
    }

    public void takeHit() {
        if (health > 0) {
            health--;
        }
    }

    public boolean isDefeated() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }
}