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
    private Ball ball;
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
        ball = new Ball(SCREEN_WIDTH/2, 500);
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
        ball.update(deltaTime);

        // Update power-ups system
        powerUpManager.update(deltaTime, paddle);

        // Check collisions
        collisionHandler.checkBallWallCollision(ball);
        collisionHandler.checkBallPaddleCollision(ball, paddle);
        collisionHandler.checkBallBrickCollision(ball, bricks);

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
        if (ball.isFallenOff()) {
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
        ball = new Ball(SCREEN_WIDTH/2, 500);
        ball.resetToStuck();
        // Clear active power-ups
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
    public Ball getBall() {
        return ball;
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

    public List<Ball> getBalls() {
        return balls;
    }

    public void addBall(Ball ball) {
        if (balls == null) {
            balls = new ArrayList<>();
        }
        balls.add(ball);
    }

    public void removeBall(Ball ball) {
        if (balls != null) {
            balls.remove(ball);
        }
    }
}