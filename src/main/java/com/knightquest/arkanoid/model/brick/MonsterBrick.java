package com.knightquest.arkanoid.model.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class MonsterBrick extends Brick {
    private double moveSpeed = 75.0;
    private double moveDirection = 1; // 1 for rights, -1 for left
    private double minX, maxX;
    private double attackTimer = 0;
    private static final double ATTACK_INTERVAL = 3.0;
    private static final String imagePath = "/images/sprites/bricks/monsterbrick.gif";

    public MonsterBrick(double x, double y, double width, double height,
                        double minX, double maxX) {
        super(x, y, width, height, 1);
        this.color = Color.DARKRED;
        this.minX = minX;
        this.maxX = maxX;
        this.type = BrickType.MONSTER;
    }

    public void update(double deltaTime, List<Brick> allBricks) {
        double newX = x + moveSpeed * moveDirection * deltaTime;

        if (newX <= minX || newX + width >= maxX) {
            moveDirection *= -1;
            newX = x + moveSpeed * moveDirection * deltaTime;
        }

        for (Brick other : allBricks) {
            if (other == this || other.isDestroyed()) continue;
            if (newX < other.getX() + other.getWidth() &&
                    newX + width > other.getX() &&
                    y < other.getY() + other.getHeight() &&
                    y + height > other.getY()) {
                moveDirection *= -1;
                newX = x + moveSpeed * moveDirection * deltaTime;
                break;
            }
        }
        x = newX;
        attackTimer += deltaTime;
    }

    public boolean canAttack() {
        if (attackTimer >= ATTACK_INTERVAL) {
            attackTimer = 0;
            return true;
        }
        return false;
    }

    protected String getImagePath() {
        return imagePath;
    }

    @Override
    protected void renderFallback(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);

        gc.setFill(Color.YELLOW);
        double eyeSize = width / 6;
        gc.fillOval(x + width * 0.25 - eyeSize / 2, y + height * 0.3,
                eyeSize, eyeSize);
        gc.fillOval(x + width * 0.75 - eyeSize / 2, y + height * 0.3,
                eyeSize, eyeSize);

        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, width, height);
    }

    public double[] getProjectileSpawnPosition() {
        return new double[]{x + width / 2, y + height};
    }
}
