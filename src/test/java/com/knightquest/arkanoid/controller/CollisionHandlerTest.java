package com.knightquest.arkanoid.controller;

import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.NormalBrick;
import com.knightquest.arkanoid.model.brick.UnbreakableBrick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollisionHandlerTest {

    private CollisionHandler collisionHandler;
    private Ball ball;
    private Paddle paddle;

    @BeforeEach
    void setUp() {
        collisionHandler = new CollisionHandler(null, null);

        ball = new Ball(100, 100);
        ball.setVelocity(5, 5);

        paddle = new Paddle(90, 200);
    }

    @Test
    void testBallPaddleNoCollision() {
        ball.setX(500);
        ball.setY(500);
        double oldX = ball.getX();
        double oldY = ball.getY();

        collisionHandler.checkBallPaddleCollision(ball, paddle);

        assertEquals(oldX, ball.getX(), 0.001);
        assertEquals(oldY, ball.getY(), 0.001);
    }
    

    @Test
    void testBallBrickNoCollision() {
        List<Brick> bricks = new ArrayList<>();
        bricks.add(new NormalBrick(300, 300, 60, 20));

        double oldDx = ball.getDx();
        double oldDy = ball.getDy();

        collisionHandler.checkBallBrickCollision(ball, bricks);

        assertEquals(oldDx, ball.getDx(), 0.001);
        assertEquals(oldDy, ball.getDy(), 0.001);
    }

    @Test
    void testBallWallCollision() {
        ball.setX(-5);
        ball.setDx(-5);
        collisionHandler.checkBallWallCollision(ball);
        assertTrue(ball.getDx() > 0);

        ball.setX(795);
        ball.setDx(5);
        collisionHandler.checkBallWallCollision(ball);
        assertTrue(ball.getDx() < 0);

        ball.setY(-5);
        ball.setDy(-5);
        collisionHandler.checkBallWallCollision(ball);
        assertTrue(ball.getDy() > 0);
    }

    @Test
    void testBallBounceConsistency() {
        double speedBefore = Math.sqrt(ball.getDx()*ball.getDx() + ball.getDy()*ball.getDy());
        ball.bounceHorizontal();
        double speedAfter = Math.sqrt(ball.getDx()*ball.getDx() + ball.getDy()*ball.getDy());
        assertEquals(speedBefore, speedAfter, 0.01);
    }
}
