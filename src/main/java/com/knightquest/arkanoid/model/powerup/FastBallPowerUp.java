package com.knightquest.arkanoid.model.powerup;

import java.util.HashMap;
import java.util.Map;

import com.knightquest.arkanoid.controller.GameManager;
import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.strategy.FastMovementStrategy;
import com.knightquest.arkanoid.strategy.MovementStrategy;
import com.knightquest.arkanoid.strategy.NormalMovementStrategy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FastBallPowerUp extends PowerUp {
    private static final double SPEED_MULTIPLIER = 1.5;
    private static final double DURATION = 8.0;

    private Map<Ball, MovementStrategy> savedStrategies = new HashMap<>();

    public FastBallPowerUp(double x, double y) {
        super(x, y, PowerUpType.FAST_BALL, DURATION);
    }

    @Override
    public void apply(Paddle paddle) {
        GameManager gm = GameManager.getInstance();
        // Apply fast strategy to all existing balls and remember previous strategies
        Map<Ball, MovementStrategy> saved = new HashMap<>();
        for (Ball b : gm.getBalls()) {
            if (b == null) continue;
            saved.put(b, b.getMovementStrategy());
            b.setMovementStrategy(new FastMovementStrategy(SPEED_MULTIPLIER));
        }
        // Store saved strategies in this PowerUp instance for restoration on remove
        this.savedStrategies = saved;
    }

    @Override
    public void remove(Paddle paddle) {
        GameManager gm = GameManager.getInstance();
        // Restore previous movement strategies if available
        if (this.savedStrategies != null && !this.savedStrategies.isEmpty()) {
            for (Map.Entry<Ball, MovementStrategy> e : this.savedStrategies.entrySet()) {
                Ball b = e.getKey();
                MovementStrategy prev = e.getValue();
                if (b != null && prev != null) {
                    b.setMovementStrategy(prev);
                }
            }
            this.savedStrategies.clear();
        } else {
            // Fallback: restore primary ball to Normal
            if (gm.getBall() != null) {
                gm.getBall().setMovementStrategy(new NormalMovementStrategy());
            }
        }
    }

    @Override
    protected void renderIcon(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        //Draw ball
        gc.fillOval(x + 10, y + 10, 10, 10);

        //Draw multiple speed lines (mỏe lines = faster)
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeLine(x + 3, y + 10, x + 7, y + 10);
        gc.strokeLine(x + 3, y + 15, x + 7, y + 15);
        gc.strokeLine(x + 3, y + 20, x + 7, y + 20);


        //Arrow for speed indication
        gc.fillPolygon(
                new double[]{x + 23, x + 27, x + 23},
                new double[]{y + 12, y + 15, y + 18},
                3
        );
    }
}
