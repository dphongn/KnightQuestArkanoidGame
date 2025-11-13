package com.knightquest.arkanoid.model;

import static com.knightquest.arkanoid.util.Constants.BALL_RADIUS;
import static com.knightquest.arkanoid.util.Constants.BALL_SPEED;
import static com.knightquest.arkanoid.util.Constants.SCREEN_HEIGHT;
import com.knightquest.arkanoid.strategy.MovementStrategy;
import com.knightquest.arkanoid.strategy.NormalMovementStrategy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Ball extends MovableObject {
    private double radius;
    private MovementStrategy movementStrategy;


    private boolean onFire = false;
    private boolean piercing = false;

    // Sticky ball state
    private boolean stuckToPaddle = false;
    private double stuckOffsetX = 0;

    private Image[] normalBallImages;
    private Image[] fireBallImages;
    private Image[] piercingBallImages;

    private static final String[] NORMAL_BALL_IMAGES = {
        "/images/sprites/ball/ball1.png",
        "/images/sprites/ball/ball2.png",
        "/images/sprites/ball/ball3.png",
        "/images/sprites/ball/ball4.png"
    };

    private static final String[] FIRE_BALL_IMAGES = {
        "/images/sprites/ball/fire_ball1.png",
        "/images/sprites/ball/fire_ball2.png",
        "/images/sprites/ball/fire_ball3.png",
        "/images/sprites/ball/fire_ball4.png"
    };

    private static final String[] PIERCING_BALL_IMAGES = {
        "/images/sprites/ball/piercing_ball1.png",
        "/images/sprites/ball/piercing_ball2.png",
        "/images/sprites/ball/piercing_ball3.png",
        "/images/sprites/ball/piercing_ball4.png"
    };

    private int currentImageIndex = 0;
    private double rotation = 0;

    private double animationTimer = 0;
    private static final double FRAME_DURATION = 0.1;

    /**
     * Constructor from GameObject.
     */
    public Ball(double x, double y) {
        super(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
        this.radius = BALL_RADIUS;
        this.movementStrategy = new NormalMovementStrategy();
//        setVelocity(200, -BALL_SPEED); //up right
        this.stuckToPaddle = true;
        this.stuckOffsetX = 0;

        loadAllBallImage();
    }

    /**
     * Load all ball image.
     */
    private void loadAllBallImage() {
        normalBallImages = new Image[NORMAL_BALL_IMAGES.length];
        for (int i = 0; i < NORMAL_BALL_IMAGES.length; i++) {
        try {
            java.net.URL imageURL = getClass().getResource(NORMAL_BALL_IMAGES[i]);

            if (imageURL != null) {
                normalBallImages[i] = new Image(imageURL.toString());
                System.out.println("✅ Loaded ball image: " + NORMAL_BALL_IMAGES[i]);
            } else {
                System.err.println("⚠️ Ball image not found: " + NORMAL_BALL_IMAGES[i]);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error loading ball image: " + e.getMessage());
        }
        }

        fireBallImages = new Image[FIRE_BALL_IMAGES.length];
        for (int i = 0; i < FIRE_BALL_IMAGES.length; i++) {
        try {
            java.net.URL imageURL = getClass().getResource(FIRE_BALL_IMAGES[i]);

            if (imageURL != null) {
                fireBallImages[i] = new Image(imageURL.toString());
                System.out.println("✅ Loaded fire ball image: " + FIRE_BALL_IMAGES[i]);
            } else {
                System.err.println("⚠️ Fire Ball image not found: " + FIRE_BALL_IMAGES[i]);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error loading fire ball image: " + e.getMessage());
            e.printStackTrace();
        }
        }

        piercingBallImages = new Image[PIERCING_BALL_IMAGES.length];
        for (int i = 0; i < PIERCING_BALL_IMAGES.length; i++) {
        try {
            java.net.URL imageURL = getClass().getResource(PIERCING_BALL_IMAGES[i]);

            if (imageURL != null) {
                piercingBallImages[i] = new Image(imageURL.toString());
                System.out.println("✅ Loaded piercing ball image: " + PIERCING_BALL_IMAGES[i]);
            } else {
                System.err.println("⚠️ Piercing Ball image not found: " + PIERCING_BALL_IMAGES[i]);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error loading piercing ball image: " + e.getMessage());
        }
        }
    }

    /**
     * Update location with boundary checking moved to CollisionHandler.
     */
    @Override
    public void update(double deltaTime) {

        if(stuckToPaddle) {
            currentImageIndex = 0;
            return;
        }

        movementStrategy.move(this, deltaTime);

        // Normalize velocity to exactly BALL_SPEED to ensure all balls move at same speed
        // This guarantees consistent speed regardless of power-ups, collisions, or multi-ball spawns
        normalizeSpeed();

        double velocity = Math.sqrt(getDx() * getDx() + getDy() * getDy());
        rotation += velocity * deltaTime * 0.3; // Adjust rotation speed

        if (velocity > 10) {
            animationTimer += deltaTime;
            if (animationTimer >= FRAME_DURATION) {
                animationTimer = 0;
                Image[] currentImages;
                if (piercing) {
                    currentImages = piercingBallImages;
                } else if (onFire) {
                    currentImages = fireBallImages;
                } else {
                    currentImages = normalBallImages;
                }
                if (currentImages != null) {
                    currentImageIndex = (currentImageIndex + 1) % currentImages.length;
                }
            }
        }
    }

    /**
     * Normalize velocity magnitude to exactly BALL_SPEED while preserving direction.
     * This ensures all balls always move at the same speed.
     */
    private void normalizeSpeed() {
        double currentSpeed = Math.sqrt(getDx() * getDx() + getDy() * getDy());

        // If speed is effectively zero, ball is stuck - don't normalize
        if (currentSpeed < 0.01) {
            return;
        }

        // Scale velocity to exact BALL_SPEED
        double factor = BALL_SPEED / currentSpeed;
        setDx(getDx() * factor);
        setDy(getDy() * factor);
    }

    @Override
    public void render(GraphicsContext gc) {   
        Image[] currentImages;
        if (piercing) {
            currentImages = piercingBallImages;
        } else if (onFire) {
            currentImages = fireBallImages;
        } else {
            currentImages = normalBallImages;
        }

        Image currentImage = (currentImages != null && currentImageIndex < currentImages.length) 
                             ? currentImages[currentImageIndex] 
                             : null;
        if (currentImage != null) {
            gc.save();
            if (onFire && !piercing) {
                // Fire Ball effect - orange glow
                gc.setFill(Color.rgb(255, 100, 0, 0.5));
                gc.fillOval(x - 4, y - 4, width + 8, height + 8);
            } else if (piercing) {
                // Pierce Ball effect - white with cyan aura
                gc.setFill(Color.rgb(0, 255, 255, 0.5));
                gc.fillOval(x - 3, y - 3, width + 6, height + 6);
            }

            gc.translate(x + width / 2, y + height / 2);
            gc.rotate(rotation);
            gc.translate(-(x + width / 2), -(y + height / 2));

            gc.drawImage(currentImage, x, y, width, height);

            gc.restore();
        } else {
            //Fallback rendering
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
            } else {
            // Normal ball
                gc.setFill(Color.RED);
                gc.fillOval(x, y, width, height);
            }
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


    /**
     * Release ball from stuck state and set initial velocity so it will move.
     * Normalizes speed to exactly BALL_SPEED to ensure consistency.
     */
    public void releaseWithVelocity(double vx, double vy) {
        this.stuckToPaddle = false;
        setVelocity(vx, vy);
        // Normalize to exact BALL_SPEED to ensure all spawned balls have identical speed
        normalizeSpeed();
        System.out.println("🔓 Spawned ball released with velocity: (" + String.format("%.1f", getDx()) + ", " + String.format("%.1f", getDy()) + ") | Speed: " + String.format("%.1f", getSpeed()));
    }
}