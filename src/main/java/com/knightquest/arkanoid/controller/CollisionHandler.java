package com.knightquest.arkanoid.controller;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

import com.knightquest.arkanoid.factory.PowerUpFactory;
import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.GameObject;
import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.ExplosiveBrick;
import com.knightquest.arkanoid.model.powerup.PowerUp;
import com.knightquest.arkanoid.model.powerup.PowerUpType;
import com.knightquest.arkanoid.observer.GameEventManager;

import static com.knightquest.arkanoid.util.Constants.SCREEN_WIDTH;

import javafx.geometry.Rectangle2D;

public class CollisionHandler {
    private GameManager gameManager;
    private GameEventManager eventManager;

    public CollisionHandler(GameManager gameManager, GameEventManager eventManager) {
        this.gameManager = gameManager;
        this.eventManager = eventManager;
    }

    /**
     * Check and handle collision between ball and paddle with angle variation
     */
    public void checkBallPaddleCollision(Ball ball, Paddle paddle) {
        if (!isColliding(ball, paddle)) {
            return;
        }

        //Notify paddle collision
        eventManager.notifyBallPaddleCollision();

        // Position ball above paddle to prevent sticking
        ball.setY(paddle.getY() - ball.getHeight());


        // If paddle is magnetic, catch the ball
        if (paddle.isMagnetic() && !ball.isStuckToPaddle()) {
            ball.resetToStuck(); // Use existing method to set stuck state
            System.out.println("🧲 Ball caught by magnetic paddle!");
            return; // Don't bounce, just stick
        }

        // Calculate where ball hit the paddle (0 = left edge, 1 = right edge)
        double ballCenterX = ball.getX() + ball.getWidth() / 2;
        double hitPosition = (ballCenterX - paddle.getX()) / paddle.getWidth();

        // Clamp hit position to prevent extreme angles
        hitPosition = Math.max(0.1, Math.min(0.9, hitPosition));

        // Calculate new angle based on hit position
        // Center hit (0.5) = straight up, edges = angled
        double angleVariation = (hitPosition - 0.5) * 2.0; // Range: -1 to 1
        double baseSpeed = ball.getSpeed();

        // New velocity with angle variation
        ball.setDx(angleVariation * baseSpeed * 0.8); // Horizontal component
        ball.setDy(-Math.abs(ball.getDy())); // Always bounce upward

        // Ensure minimum vertical speed
        if (Math.abs(ball.getDy()) < baseSpeed * 0.5) {
            ball.setDy(-baseSpeed * 0.7);
        }

        //SoundManager.play("paddle_hit");
    }

    /**
     * Check and handle collisions between the ball and the bricks
     */
    public void checkBallBrickCollision(Ball ball, List<Brick> bricks) {
        Iterator<Brick> iterator = bricks.iterator();

        while (iterator.hasNext()) {
            Brick brick = iterator.next();

            if (!brick.isActive() || !isColliding(ball, brick)) {
                continue;
            }

            double ballCenterX = ball.getX() + ball.getWidth() / 2;
            double ballCenterY = ball.getY() + ball.getHeight() / 2;
            double brickCenterX = brick.getX() + brick.getWidth() / 2;
            double brickCenterY = brick.getY() + brick.getHeight() / 2;

            double dx = ballCenterX - brickCenterX;
            double dy = ballCenterY - brickCenterY;

            double overlapX = (brick.getWidth() + ball.getWidth()) / 2 - Math.abs(dx);
            double overlapY = (brick.getHeight() + ball.getHeight()) / 2 - Math.abs(dy);

            boolean shouldBounce = ball.getMovementStrategy().handleBrickCollision(ball, brick);

            eventManager.notifyBrickHit(brick);
            
            handleBrickDestruction(brick);
            processBrickDestruction(brick, bricks);
            //handleBrickDestruction(brick);

            if (shouldBounce) {
                if (overlapX < overlapY) {
                    ball.bounceHorizontal();
                    if (dx > 0) {
                        ball.setX(brickCenterX + brick.getWidth() / 2);
                    } else {
                        ball.setX(brickCenterX - brick.getWidth() / 2 - ball.getWidth());
                    }
                } else {
                    ball.bounceVertical();
                    if (dy > 0) {
                        ball.setY(brickCenterY + brick.getHeight() / 2);
                    } else {
                        ball.setY(brickCenterY - brick.getHeight() / 2 - ball.getHeight());
                    }
                }
                break;
            }
            break;
        }
    }

    private void processBrickDestruction(Brick initialBrick, List<Brick> allBricks) {
        Queue<Brick> destructionQueue = new LinkedList<>();
        Set<Brick> processedSet = new HashSet<>();

        if (initialBrick.isDestroyed()) {
            destructionQueue.add(initialBrick);
        }

        while (!destructionQueue.isEmpty()) {
            Brick currentBrick = destructionQueue.poll();

            if (processedSet.contains(currentBrick)) {
                continue;
            }
            processedSet.add(currentBrick);
            handleBrickDestruction(currentBrick);

            if (currentBrick instanceof ExplosiveBrick) {
                ExplosiveBrick explosiveBrick = (ExplosiveBrick) currentBrick;
                if (explosiveBrick.hasExploded()) {
                    System.out.println("💣 Kích hoạt vụ nổ tại (" + explosiveBrick.getX() + ", " + explosiveBrick.getY() + ")");

                    List<Brick> targets = explosiveBrick.getExplosionTargets(allBricks);

                    for (Brick target : targets) {
                        if (target.isActive()) {
                            target.takeHit();
                            destructionQueue.add(target);
                        }
                    }
                }
            }
        }
    }

    /**
     * Check and handle collisions between the ball and the walls
     */
    public void checkBallWallCollision(Ball ball) {
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.bounceHorizontal();
        }

        if (ball.getX() + ball.getWidth() >= SCREEN_WIDTH) {
            ball.setX(SCREEN_WIDTH - ball.getWidth());
            ball.bounceHorizontal();
        }

        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.bounceVertical();
        }
    }

    /**
     * Check AABB collision between 2 game objects
     */
    private boolean isColliding(GameObject a, GameObject b) {
        Rectangle2D boundsA = a.getBounds();
        Rectangle2D boundsB = b.getBounds();
        return boundsA.intersects(boundsB);
    }

    /**
     * Handling when bricks are destroyed
     */
    private void handleBrickDestruction(Brick brick) {
        if (brick.isDestroyed()) {
            int points = 10; //  point value
            PowerUpType powerUpType = brick.getPowerUpDrop();
            if (powerUpType != null) {
                double powerUpX = brick.getX() + (brick.getWidth() - 30) / 2;
                double powerUpY = brick.getY();
                System.out.println("Brick dropped power-up at (" + powerUpX + ", " + powerUpY + ")");
                PowerUp newPowerUp = PowerUpFactory.createPowerUp(powerUpType, powerUpX, powerUpY);
                if (newPowerUp != null) {
                    gameManager.getPowerUpManager().spawnPowerUp(powerUpType, powerUpX, powerUpY);
                    System.out.println("Power-up successfully created and added to manager.");
                } else {
                    System.err.println("Failed to create PowerUp object for type: " + powerUpType);
                }
            }
            eventManager.notifyBrickDestroyed(brick, points);
        }
    }
}


