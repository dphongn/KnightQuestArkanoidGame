package com.knightquest.arkanoid.model.powerup;

import com.knightquest.arkanoid.model.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GunPaddlePowerUp extends PowerUp {
    private static final double DURATION = 10.0; // 10 seconds
    private static final double GUN_COOLDOWN = 0.3; // 0.3s between shots

    public GunPaddlePowerUp(double x, double y) {
        super(x, y, PowerUpType.GUN_PADDLE, DURATION);
    }

    @Override
    public void apply(Paddle paddle) {
        System.out.println("🔫 Gun Paddle power-up APPLIED!");
        paddle.setCanShootGun(true);
        paddle.setGunCooldown(GUN_COOLDOWN);
        System.out.println("   Can shoot: " + paddle.canShootGun());
    }

    @Override
    public void remove(Paddle paddle) {
        System.out.println("🔫 Gun Paddle power-up REMOVED!");
        paddle.setCanShootGun(false);
    }

    @Override
    protected void renderIcon(GraphicsContext gc) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        // Draw gun barrels (laser beams pointing up)
        gc.strokeLine(x + 10, y + 20, x + 10, y + 5);
        gc.strokeLine(x + 20, y + 20, x + 20, y + 5);
        // Draw paddle base
        gc.setFill(Color.WHITE);
        gc.fillRect(x + 7, y + 22, 16, 4);
    }
}
