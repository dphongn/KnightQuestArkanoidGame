package com.knightquest.arkanoid.controller;

import com.knightquest.arkanoid.model.*;
import com.knightquest.arkanoid.model.brick.Brick;
import javafx.geometry.Rectangle2D;
import java.util.Iterator;
import java.util.List;
import com.knightquest.arkanoid.model.GameObject;
import static com.knightquest.arkanoid.util.Constants.SCREEN_WIDTH;

public class CollisionHandler {
    private GameManager gameManager;

    public CollisionHandler(GameManager gameManger) {
        this.gameManager = gameManager;
    }

    /**
     * Check and handle collision between ball and paddle
     */
    public void checkBallPaddleCollision(Ball ball, Paddle paddle) {
        if (!isColliding(ball, paddle)) {
            return;
        }

        ball.bounceVertical();

        ball.setY(paddle.getY() - ball.getHeight());

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
            handleBrickDestruction(brick);
            //SoundManager.play("brick_hit");

            break;
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
        brick.takeHit();

        /*if (brick.isDestroyed()) {
            gameManager.addScore(10);
            SoundManager.play("brick_break");
        }*/
    }
}


