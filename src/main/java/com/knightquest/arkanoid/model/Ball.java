package com.knightquest.arkanoid.model;

import static com.knightquest.arkanoid.util.Constants.BALL_RADIUS;
import static com.knightquest.arkanoid.util.Constants.BALL_SPEED;
import static com.knightquest.arkanoid.util.Constants.SCREEN_HEIGHT;

import com.knightquest.arkanoid.strategy.MovementStrategy;
import com.knightquest.arkanoid.strategy.NormalMovementStrategy;
import com.knightquest.arkanoid.strategy.PierceMovementStratrgy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends MovableObject {
    private double radius;
    private MovementStrategy movementStrategy;


    private boolean onFire = false;
    private boolean fallenOff;
    private boolean piercing = false;

    // Sticky ball state
    private boolean stuckToPaddle = false;
    private double stuckOffsetX = 0;

    /**
     * Contructor from GameObject.
     */
    public Ball(double x, double y) {
        super(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
        this.radius = BALL_RADIUS;
        this.movementStrategy = new NormalMovementStrategy();
//        setVelocity(200, -BALL_SPEED); //up right
        this.stuckToPaddle = true;
        this.stuckOffsetX = 0;
    }

    /**
     * Update location with boundary checking moved to CollisionHandler.
     */
    @Override
    public void update(double deltaTime) {

        if(stuckToPaddle) {
            return;
        }

        movementStrategy.move(this, deltaTime);

        // Clamp velocity to prevent excessive speeds
        double maxSpeed = BALL_SPEED * 2; // Allow up to 2x normal speed
        if (Math.abs(getDx()) > maxSpeed) setDx(Math.signum(getDx()) * maxSpeed);
        if (Math.abs(getDy()) > maxSpeed) setDy(Math.signum(getDy()) * maxSpeed);

        // Ensure minimum speed to prevent ball getting stuck
        double minSpeed = BALL_SPEED * 0.3;
        if (Math.abs(getDx()) < minSpeed) setDx(Math.signum(getDx()) * minSpeed);
        if (Math.abs(getDy()) < minSpeed) setDy(Math.signum(getDy()) * minSpeed);

    }

    @Override
    public void render(GraphicsContext gc) {
        if (onFire) {
            // Fire Ball effect - orange glow
            gc.setFill(Color.rgb(255, 100, 0, 0.5));
            gc.fillOval(x - 4, y - 4, width + 8, height + 8);
            gc.setFill(Color.ORANGE);
            gc.fillOval(x, y, width, height);
            gc.setFill(Color.YELLOW);
            gc.fillOval(x + 3, y + 3, width - 6, height - 6);
        } else if (piercing) {
            // Pierce Ball effect - white with cyan aura
            gc.setFill(Color.rgb(0, 255, 255, 0.5));
            gc.fillOval(x - 3, y - 3, width + 6, height + 6);
            gc.setFill(Color.WHITE);
            gc.fillOval(x, y, width, height);
        }
        else {
            // Normal ball
            gc.setFill(Color.RED);
            gc.fillOval(x, y, width, height);
        }

    }

    public boolean isOnFire() {
        return onFire;
    }

    public void bounceVertical() {
        setDy(-getDy());
    }

    public void bounceHorizontal() {
        setDx(-getDx());
    }

    public boolean isFallenOff() {
        return y > SCREEN_HEIGHT;
    }

    public double getRadius() {
        return radius;
    }

    public double getSpeed() {
        return Math.sqrt(getDx() * getDx() + getDy() * getDy());
    }

    public MovementStrategy getMovementStrategy() {
        return movementStrategy;
    }

    public void setMovementStrategy(MovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
        System.out.println("Ball movement strategy set to: " + movementStrategy.getClass().getSimpleName());
    }

    public void setOnFire(boolean onFire) {
        this.onFire = onFire;
        System.err.println("onFire: " + onFire);
    }

    public boolean isPiercing() {
        return piercing;
    }

    public void setPiercing(boolean piercing) {
        this.piercing = piercing;
        System.err.println("piercing: " + piercing);
    }

    public void multiplySpeed(double speedMultiplier) {
    }

    // Sticky ball methods
    public boolean isStuckToPaddle() {
        return stuckToPaddle;
    }

    /**
     * Stick ball to paddle
     */
    public void stickToPaddle (Paddle paddle) {
        if (!stuckToPaddle) return;

        double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
        this.x = paddleCenterX + stuckOffsetX - radius;
        this.y = paddle.getY() - height - 2;
    }

    /**
     *  Lauch ball from paddle
     */
    public void launch() {
        if (!stuckToPaddle) return;

        stuckToPaddle = false;

        // Calculate launch angle based on offset from paddle center
        // stuckOffsetX range: -50 to +50 (paddle width = 100)

        double maxOffset = 50.0; // Half of paddle width
        double normalizedOffset = Math.max(-1.0, Math.min(1.0, stuckOffsetX / maxOffset));

        // Angle range: 45° (left edge) to 135° (right edge), 90° (center straight up)
        double baseAngle = 90.0; // Straight up
        double angleVariation = 45.0; // ±45° variation
        double angle = Math.toRadians(baseAngle + (normalizedOffset * angleVariation));

        // Use BALL_SPEED constant directly
        double speed = BALL_SPEED;

        // Calculate velocity components
        double vx = Math.cos(angle) * speed; // Horizontal component
        double vy = -Math.abs(Math.sin(angle)) * speed; // Vertical component (always negative = upward)

        setDx(vx);
        setDy(vy);

        System.out.println("🚀 Ball launched! Offset: " + String.format("%.1f", stuckOffsetX)
                + " | Angle: " + String.format("%.1f", Math.toDegrees(angle)) + "°"
                + " | Velocity: (" + String.format("%.1f", vx) + ", " + String.format("%.1f", vy) + ")");


    }

    public void updateStuckOffset(double dx) {
        if (!stuckToPaddle) return;

        stuckOffsetX += dx;
        stuckOffsetX = Math.max(-45, Math.min(45, stuckOffsetX));
    }

    /**
     * Reset ball to stuck state
     */
    public void resetToStuck() {
        stuckToPaddle = true;
        stuckOffsetX = 0;
        setVelocity(0, 0);
        System.out.println("Ball reset to stuck");
    }
}