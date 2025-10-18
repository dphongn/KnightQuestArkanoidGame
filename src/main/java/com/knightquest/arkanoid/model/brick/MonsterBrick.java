package com.knightquest.arkanoid.model.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MonsterBrick extends Brick {
    private double moveSpeed = 50.0;
    private double moveDirection = 1; // 1 for rights, -1 for left
    private double minX, maxX;
    private double attackTimer = 0;
    private static final double ATTACK_INTERVAL = 3.0;

    public MonsterBrick(double x, double y, double width, double height,
                        double minX, double maxX) {
        super(x, y, width, height, 2);
        this.color = Color.DARKRED;
        this.minX = minX;
        this.maxX = maxX;
        this.type = BrickType.MONSTER;
    }

    @Override
    public void update(double deltaTime) {
        x += moveSpeed * moveDirection * deltaTime;
        if (x <= minX || x + width >= maxX) {
            moveDirection *= -1;
        }
        attackTimer += deltaTime;
    }

    public boolean canAttack() {
        if (attackTimer >= ATTACK_INTERVAL) {
            attackTimer = 0;
            return true;
        }
        return false;
    }

    @Override
    public void render(GraphicsContext gc) {
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
