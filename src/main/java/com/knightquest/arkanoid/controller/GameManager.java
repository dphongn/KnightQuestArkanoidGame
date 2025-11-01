package com.knightquest.arkanoid.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.knightquest.arkanoid.factory.LevelFactory;
import com.knightquest.arkanoid.level.Level;
import  com.knightquest.arkanoid.manager.PowerUpManager;
import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.Bullet;
import com.knightquest.arkanoid.observer.GameEventListener;
import com.knightquest.arkanoid.observer.GameEventManager;
import com.knightquest.arkanoid.state.GameState;
import com.knightquest.arkanoid.state.GameStateManager;
import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;
import static com.knightquest.arkanoid.util.Constants.INITIAL_LIVES;
import static com.knightquest.arkanoid.util.Constants.PADDLE_WIDTH;
import static com.knightquest.arkanoid.util.Constants.SCREEN_WIDTH;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;

public class GameManager {
    private static GameManager instance;

    private Paddle paddle;
    private List<Ball> balls;
    private List<Bullet> bullets;

    //    private Ball ball;
    private List<Brick> bricks;
    private int score, lives;
    private CollisionHandler collisionHandler;

    // Event manager
    private GameEventManager eventManager;

    // Power-up manager
    private PowerUpManager powerUpManager;

    // Level system
    private int currentLevelNumber = 3;
    private Level currentLevel;

    // State management
    private GameStateManager gameStateManager;

    private GameManager() {
        initGame();
        gameStateManager = new GameStateManager(this);
        gameStateManager.initialize(); // Start with initial state
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    private void initGame() {
        paddle = new Paddle(SCREEN_WIDTH/2 - PADDLE_WIDTH/2, 550);
        balls = new ArrayList<>();
        Ball initialBall = new Ball(SCREEN_WIDTH/2, 500);
        bullets = new ArrayList<>();
        initialBall.resetToStuck();
        balls.add(initialBall);
        lives = INITIAL_LIVES;
        score = 0;

        eventManager = new GameEventManager();
        System.out.println("GameEventManager initialized.");

        // Power-up manager
        powerUpManager = new PowerUpManager(this, eventManager);
        System.out.println("PowerUpManager initialized.");

        collisionHandler = new CollisionHandler(this, eventManager);

        // Load first level
        loadLevel(1);
    }

    private void loadLevel(int levelNumber) {
        currentLevelNumber = levelNumber;
        currentLevel = LevelFactory.createLevel(levelNumber);
        // Get bricks from level
        bricks = currentLevel.getBricks();

        // Print level info to console
        System.out.println("Loaded Level " + levelNumber);

    }

    /**
     * Update game logic.
     * @param deltaTime
     */

    public void updateGameLogic(double deltaTime) {
        paddle.update(deltaTime);
//        ball.update(deltaTime);

        // Update power-ups system
        powerUpManager.update(deltaTime, paddle);

        // Update and process bullets
        java.util.Iterator<Bullet> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet bullet = bulletIter.next();
            bullet.update(deltaTime);

            // Check bullet collision with bricks
            Iterator<Brick> brickIter = bricks.iterator();
            while (brickIter.hasNext()) {
                Brick brick = brickIter.next();
                if (bullet.isActive() && bullet.getBounds().intersects(brick.getBounds())) {
                    brick.takeHit();
                    bullet.setActive(false);
                    break;
                }
            }

            // Remove inactive bullets
            if (!bullet.isActive() || bullet.isOffScreen()) {
                bulletIter.remove();
            }
        }
        // Update and process each ball
        java.util.Iterator<Ball> ballIter = balls.iterator();
        while (ballIter.hasNext()) {
            Ball b = ballIter.next();
            b.update(deltaTime);

            // Use CollisionHandler for more precise collision detection
            collisionHandler.checkBallWallCollision(b);
            collisionHandler.checkBallPaddleCollision(b, paddle);
            collisionHandler.checkBallBrickCollision(b, bricks);

            // Ball out of screen: remove it
            if (b.isFallenOff()) {
                ballIter.remove();
                System.out.println("Life: a ball has fallen off. Remaining balls: " + balls.size());
            }
        }
        // Check collisions
//        collisionHandler.checkBallWallCollision(ball);
//        collisionHandler.checkBallPaddleCollision(ball, paddle);
//        collisionHandler.checkBallBrickCollision(ball, bricks);

        // Remove destroyed bricks and update score
        Iterator<Brick> iter = bricks.iterator();
        while (iter.hasNext()) {
            Brick brick = iter.next();
            if (brick.isDestroyed()) {
                iter.remove();
                score += 10;
                eventManager.notifyScoreChanged(score); // Observer Pattern: notify score change

            }
        }

        // Ball out
        if (balls.isEmpty()) {
            lives--;
            eventManager.notifyLifeLost(lives);
            if (lives > 0) {
                resetBall();
            } else {
                eventManager.notifyGameOver(false, score);
            }
        }

        if (bricks.isEmpty()) {
            eventManager.notifyLevelCompleted(currentLevelNumber, score);
        }
    }

    // Update game state
    public void update(double deltaTime) {
        gameStateManager.update(deltaTime);
    }

    // Handle input
    public void handleInput(KeyEvent event) {
        gameStateManager.handleInput(event);
    }

    // Render game
    public void render(GraphicsContext gc) {
        gameStateManager.render(gc);
    }

    public void changeState(GameState newState) {
        gameStateManager.changeState(newState);
    }

    private void resetBall() {
//        ball = new Ball(SCREEN_WIDTH/2, 500);
//        ball.resetToStuck();
        // Clear active power-ups
        if (balls == null) balls = new ArrayList<>();
        balls.clear();
        Ball newBall = new Ball(SCREEN_WIDTH/2, 500);
        newBall.resetToStuck();
        balls.add(newBall);
        powerUpManager.clearAll(paddle);
    }

    // Restart level
    public void restartLevel() {
        loadLevel(currentLevelNumber);
        resetBall();
        // Clear active power-ups
        powerUpManager.clearAll(paddle);
    }

    // Reset game to level 1
    public void resetGame() {
        currentLevelNumber = 3;
        lives = INITIAL_LIVES;
        score = 0;
        loadLevel(3);
        resetBall();
    }

    // Advance to next level
    public void nextLevel() {
        if (currentLevelNumber < LevelFactory.getTotalLevels()) {
            loadLevel(currentLevelNumber + 1);
            resetBall();
        }
        else {
            // All levels completed - victory!
            System.out.println("========================================");
            System.out.println("🎉 CONGRATULATIONS! 🎉");
            System.out.println("You have completed all " + LevelFactory.getTotalLevels() + " levels!");
            System.out.println("Final Score: " + score);
            System.out.println("========================================");
        }
    }

    // Getters
    public Paddle getPaddle() {
        return paddle;
    }

    public List<Brick> getBricks() {
        return bricks;
    }
    public int getScore() {
        return score;
    }
    public int getLives() {
        return lives;
    }

    // Level info
    public int getCurrentLevelNumber() {
        return currentLevelNumber;
    }

    public String getCurrentLevelName() {
        return currentLevel != null ? LevelFactory.getLevelName(currentLevelNumber) : "Unknown";
    }

    public PowerUpManager getPowerUpManager() {
        return powerUpManager;
    }


    /**
     * Expose the event manager for states/controllers that need to send events.
     */
    public GameEventManager getEventManager() {
        return eventManager;
    }

    /**
     * Return the primary ball (first in list) or null if none.
     */
    public Ball getBall() {
        return (balls != null && !balls.isEmpty()) ? balls.get(0) : null;
    }

    /**
     * Return live view of all balls.
     */
    public java.util.List<Ball> getBalls() {
        return balls;
    }

    public void addBall(Ball ball) {
        if (balls == null) balls = new java.util.ArrayList<>();
        if (ball != null) balls.add(ball);
    }

    public void removeBall(Ball ball) {
        if (balls != null && ball != null) balls.remove(ball);
    }


    /**
     * Shoot a bullet from the paddle (when Gun Paddle power-up is active).
     */
    public void shootBullet() {
        System.out.println("🔫 shootBullet() called. Can shoot: " + paddle.canShootGun());
        if (paddle.canShootGun()) {
            // Create two bullets, one from each side of paddle
            double paddleCenterX = paddle.getX() + paddle.getWidth() / 2.0;
            double paddleTop = paddle.getY();

            // Left bullet
            Bullet leftBullet = new Bullet(paddleCenterX - 15, paddleTop);
            bullets.add(leftBullet);

            // Right bullet
            Bullet rightBullet = new Bullet(paddleCenterX + 10, paddleTop);
            bullets.add(rightBullet);

            // Reset cooldown timer
            paddle.resetGunTimer();

            System.out.println("💥 Bullets fired! Total bullets: " + bullets.size());
        } else {
            System.out.println("❌ Cannot shoot - Gun power-up not active or on cooldown");
        }
    }

    /**
     * Get bullets for rendering.
     */
    public List<Bullet> getBullets() {
        return bullets;
    }
}