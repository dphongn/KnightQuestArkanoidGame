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
    void testBallPaddleCollisionBounce() {
        ball.releaseWithVelocity(0, 5);
        ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight() + 1);

        collisionHandler.checkBallPaddleCollision(ball, paddle);

        assertTrue(ball.getDy() < 0, "Ball phải bounce lên sau va chạm paddle");
        assertEquals(paddle.getY() - ball.getHeight(), ball.getY(), 0.001, "Ball phải được đặt ngay trên paddle");
    }

    @Test
    void testBallBrickCollisionNormal() {
        NormalBrick brick = new NormalBrick(95, 95, 60, 20);
        List<Brick> bricks = new ArrayList<>();
        bricks.add(brick);

        ball.releaseWithVelocity(5, 5);
        ball.setX(100);
        ball.setY(100);

        collisionHandler.checkBallBrickCollision(ball, bricks);

        assertNotEquals(5, ball.getDx(), "Ball dx phải thay đổi sau va chạm brick");
        assertNotEquals(5, ball.getDy(), "Ball dy phải thay đổi sau va chạm brick");

        assertTrue(brick.isDestroyed(), "NormalBrick bị destroy sau va chạm");
    }

    @Test
    void testBallBrickCollisionUnbreakable() {
        UnbreakableBrick brick = new UnbreakableBrick(95, 95, 60, 20);
        List<Brick> bricks = new ArrayList<>();
        bricks.add(brick);

        ball.setX(100);
        ball.setY(100);
        ball.setVelocity(5, 5);

        collisionHandler.checkBallBrickCollision(ball, bricks);
        
        assertNotEquals(5, ball.getDx());
        assertNotEquals(5, ball.getDy());
        // Unbreakable vẫn không destroyed
        assertFalse(brick.isDestroyed());
    }


    @Test
    void testBallPaddleCollisionMagnetic() {
        paddle.setMagnetic(true);
        paddle.attachBall(ball); // attach ball vào paddle
        ball.releaseWithVelocity(0, 5); // đảm bảo ball có velocity
        ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight() + 1);

        collisionHandler.checkBallPaddleCollision(ball, paddle);

        assertEquals(paddle.getY() - ball.getHeight(), ball.getY(), 0.001, "Ball dính paddle khi magnetic");
        assertEquals(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2, ball.getX(), 0.001, "Ball dính giữa paddle khi magnetic");
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
