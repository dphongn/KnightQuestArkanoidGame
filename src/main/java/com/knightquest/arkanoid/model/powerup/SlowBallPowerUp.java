package com.knightquest.arkanoid.model.powerup;

import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.controller.GameManager;
import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.strategy.MovementStrategy;
import com.knightquest.arkanoid.strategy.SlowMovementStrategy;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SlowBallPowerUp extends PowerUp {
    private static final double SPEED_MULTIPLIER = 0.7;

    private static final double DURATION = 8.0;
    private Map<Ball, MovementStrategy> savedStrategies = new HashMap<>();

    public SlowBallPowerUp(double x, double y) {
        super(x, y, PowerUpType.SLOW_BALL, DURATION);
    }

    @Override
    public void apply(Paddle paddle) {
        GameManager gm = GameManager.getInstance();
        // Apply slow strategy to all existing balls and remember previous strategies
        Map<com.knightquest.arkanoid.model.Ball, MovementStrategy> saved = new HashMap<>();
        for (com.knightquest.arkanoid.model.Ball b : gm.getBalls()) {
            if (b == null) continue;
            saved.put(b, b.getMovementStrategy());
            b.setMovementStrategy(new SlowMovementStrategy(SPEED_MULTIPLIER));
        }
        // Store saved strategies in this PowerUp instance for restoration on remove
        this.savedStrategies = saved;
    }

    @Override
    public void remove(Paddle paddle) {
        GameManager gm = GameManager.getInstance();
        // Restore previous movement strategies if available
        if (this.savedStrategies != null && !this.savedStrategies.isEmpty()) {
            for (Map.Entry<com.knightquest.arkanoid.model.Ball, MovementStrategy> e : this.savedStrategies.entrySet()) {
                com.knightquest.arkanoid.model.Ball b = e.getKey();
                MovementStrategy prev = e.getValue();
                if (b != null && prev != null) {
                    b.setMovementStrategy(prev);
                }
            }
            this.savedStrategies.clear();
        } else {
            // Fallback: restore primary ball to Normal
            if (gm.getBall() != null) {
                gm.getBall().setMovementStrategy(new com.knightquest.arkanoid.strategy.NormalMovementStrategy());
            }
        }
    }

    @Override
    protected void renderIcon(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        //Drawball
        gc.fillOval(x + 10, y + 10, 10, 10);
        //Draw speed lines (fewer for slow)
        gc.setLineWidth(1.5);
        gc.strokeLine(x + 5, y + 12, x + 8, y + 12);
        gc.strokeLine(x + 5, y + 18, x + 8, y + 18);
    }
}
