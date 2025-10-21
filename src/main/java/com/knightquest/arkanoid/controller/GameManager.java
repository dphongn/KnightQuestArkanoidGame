package com.knightquest.arkanoid.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.NormalBrick;
import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;
import static com.knightquest.arkanoid.util.Constants.INITIAL_LIVES;
import static com.knightquest.arkanoid.util.Constants.PADDLE_WIDTH;
import static com.knightquest.arkanoid.util.Constants.SCREEN_WIDTH;

public class GameManager {
    private static GameManager instance;

    private Paddle paddle;
    private List<Ball> balls;
    private Ball ball;
    private List<Brick> bricks;
    private int score, lives;
    private CollisionHandler collisionHandler;

    private GameManager() {
        initGame();
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
        bricks = new ArrayList<>();
        lives = INITIAL_LIVES;
        score = 0;
        collisionHandler = new CollisionHandler(this);

        // Create brick (10 x 5)
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                bricks.add(new NormalBrick(
                        col * (BRICK_WIDTH + 5) + 50,
                        row * (BRICK_HEIGHT + 5) + 50,
                        BRICK_WIDTH, BRICK_HEIGHT
                ));
            }
        }
    }

    public void update(double deltaTime) {
        paddle.update(deltaTime);
        ball.update(deltaTime);

        // Use CollisionHandler for more precise collision detection
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
            }
        }

        // Ball out
        if (ball.isFallenOff()) {
            lives--;
            if (lives > 0) {
                resetBall();
            }
        }

        // Win condition
        if (bricks.isEmpty()) {
            // Player wins - could add level progression here
            initGame(); // For now, restart the game
        }
    }

    private void resetBall() {
        ball = new Ball(SCREEN_WIDTH/2, 500);
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