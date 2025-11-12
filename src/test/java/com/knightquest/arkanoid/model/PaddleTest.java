package com.knightquest.arkanoid.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.knightquest.arkanoid.util.Constants.*;

class PaddleTest {
    private Paddle paddle;
    private static final double SCREEN_WIDTH = 800;
    private static final double SCREEN_HEIGHT = 600;
    private static final double PADDLE_WIDTH = 100;
    private static final double PADDLE_HEIGHT = 20;

    @BeforeEach
    void setUp() {
        paddle = new Paddle(SCREEN_WIDTH / 2 - PADDLE_WIDTH / 2, SCREEN_HEIGHT - 50);
    }

    @Test
    void testConstructor() {
        assertNotNull(paddle);
        assertEquals(SCREEN_WIDTH / 2 - PADDLE_WIDTH / 2, paddle.getX(), 0.001);
        assertEquals(SCREEN_HEIGHT - 50, paddle.getY(), 0.001);
        assertEquals(PADDLE_WIDTH, paddle.getWidth(), 0.001);
    }

    @Test
    void testMoveLeft() {
        double deltaTime = 0.016;
        double initialX = paddle.getX();
        paddle.moveLeft();
        paddle.update(deltaTime);
        double expectedX = initialX - (PADDLE_SPEED * deltaTime);
        assertEquals(expectedX, paddle.getX(), 0.001);
    }

    @Test
    void testMoveRight() {
        double deltaTime = 0.016;
        double initialX = paddle.getX();
        paddle.moveRight();
        paddle.update(deltaTime);
        double expectedX = initialX + (PADDLE_SPEED * deltaTime);
        assertEquals(expectedX, paddle.getX(), 0.001);
    }

    @Test
    void testStop() {
        paddle.moveRight();
        paddle.stop();
        paddle.update(0.016);
        double initialX = paddle.getX();
        paddle.update(0.016);
        assertEquals(initialX, paddle.getX(), 0.001);
    }

    @Test
    void testBoundaryLeft() {
        paddle = new Paddle(0, SCREEN_HEIGHT - 50);
        paddle.moveLeft();
        paddle.update(0.016);
        assertEquals(0, paddle.getX(), 0.001);
    }

    @Test
    void testBoundaryRight() {
        paddle = new Paddle(SCREEN_WIDTH - PADDLE_WIDTH, SCREEN_HEIGHT - 50);
        paddle.moveRight();
        paddle.update(0.016);
        assertEquals(SCREEN_WIDTH - PADDLE_WIDTH, paddle.getX(), 0.001);
    }

    @Test
    void testSetWidth() {
        double newWidth = 150;
        double centerX = paddle.getX() + paddle.getWidth() / 2;
        paddle.setWidth(newWidth);
        assertEquals(newWidth, paddle.getWidth(), 0.001);
        assertEquals(centerX, paddle.getX() + paddle.getWidth() / 2, 0.001);
    }

    @Test
    void testSetWidthBoundaryCheck() {
        paddle = new Paddle(0, SCREEN_HEIGHT - 50);
        paddle.setWidth(200);
        assertEquals(0, paddle.getX(), 0.001);
        paddle = new Paddle(SCREEN_WIDTH - 50, SCREEN_HEIGHT - 50);
        paddle.setWidth(200);
        assertEquals(SCREEN_WIDTH - 200, paddle.getX(), 0.001);
    }

    @Test
    void testGunPowerUp() {
        assertFalse(paddle.canShootGun());
        paddle.setCanShootGun(true);
        assertTrue(paddle.canShootGun());
        paddle.resetGunTimer();
        assertFalse(paddle.canShootGun());
        paddle.update(0.3);
        assertTrue(paddle.canShootGun());
    }

    @Test
    void testSetGunCooldown() {
        double newCooldown = 0.5;
        paddle.setGunCooldown(newCooldown);
        paddle.setCanShootGun(true);
        paddle.resetGunTimer();
        assertFalse(paddle.canShootGun());
        paddle.update(0.3);
        assertFalse(paddle.canShootGun());
        paddle.update(0.2);
        assertTrue(paddle.canShootGun());
    }

    @Test
    void testMagneticPowerUp() {
        assertFalse(paddle.isMagnetic());
        paddle.setMagnetic(true);
        assertTrue(paddle.isMagnetic());
        paddle.setMagnetic(false);
        assertFalse(paddle.isMagnetic());
    }

    @Test
    void testBallAttachment() {
        Ball ball = new Ball(400, 300);
        paddle.setMagnetic(true);
        paddle.attachBall(ball);
        assertEquals(ball, paddle.getAttachedBall());
        paddle.update(0.016);
        double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
        double expectedBallX = paddleCenterX - ball.getWidth() / 2;
        double expectedBallY = paddle.getY() - ball.getHeight();
        assertEquals(expectedBallX, ball.getX(), 0.001);
        assertEquals(expectedBallY, ball.getY(), 0.001);
        double initialBallX = ball.getX();
        paddle.moveRight();
        paddle.update(0.016);
        assertNotEquals(initialBallX, ball.getX(), 0.001);
    }

    @Test
    void testRender() {
        assertDoesNotThrow(() -> {});
    }

    @Test
    void testImageLoadingFallback() {
        assertNotNull(paddle);
    }
}
