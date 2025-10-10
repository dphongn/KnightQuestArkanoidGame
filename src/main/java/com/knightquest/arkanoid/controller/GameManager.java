package com.knightquest.arkanoid.controller;

import com.knightquest.arkanoid.model.*;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.NormalBrick;
import java.util.*;
import static com.knightquest.arkanoid.util.Constants.*;

public class GameManager {
    private static GameManager instance;

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private int score, lives;

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

        // Interact ball - paddle
        if (ball.intersects(paddle)) {
            ball.bounceVertical();
        }

        // Interact ball - brick
        Iterator<Brick> iter = bricks.iterator();
        while (iter.hasNext()) {
            Brick brick = iter.next();
            if (ball.intersects(brick)) {
                brick.takeHit();
                ball.bounceVertical();
                score += 10;
                if (brick.isDestroyed()) {
                    iter.remove();
                }
            }
        }

        // Ball out
        if (ball.isFallenOff()) {
            lives--;
            if (lives > 0) {
                ball = new Ball(SCREEN_WIDTH/2, 500);
            }
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
}