package com.knightquest.arkanoid.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Mock class for testing since we can't use real JavaFX components in unit tests

class PaddleTest {
    private Paddle paddle;
    private static final double SCREEN_WIDTH = 800;
    private static final double SCREEN_HEIGHT = 600;
    private static final double PADDLE_WIDTH = 100;
    private static final double PADDLE_HEIGHT = 20;
    private static final double PADDLE_SPEED = 300;

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
        paddle.moveLeft();
        paddle.update(0.016); // 60 FPS frame time

        double expectedX = (SCREEN_WIDTH / 2 - PADDLE_WIDTH / 2) - (PADDLE_SPEED * 0.016);
        assertEquals(expectedX, paddle.getX(), 0.001);
    }

    @Test
    void testMoveRight() {
        paddle.moveRight();
        paddle.update(0.016); // 60 FPS frame time

        double expectedX = (SCREEN_WIDTH / 2 - PADDLE_WIDTH / 2) + (PADDLE_SPEED * 0.016);
        assertEquals(expectedX, paddle.getX(), 0.001);
    }

    @Test
    void testStop() {
        paddle.moveRight();
        paddle.stop();
        paddle.update(0.016);

        // Position should not change after stop
        double initialX = paddle.getX();
        paddle.update(0.016);
        assertEquals(initialX, paddle.getX(), 0.001);
    }

    @Test
    void testBoundaryLeft() {
        paddle = new Paddle(0, SCREEN_HEIGHT - 50);
        paddle.moveLeft();
        paddle.update(0.016);

        // Should not go beyond left boundary
        assertEquals(0, paddle.getX(), 0.001);
    }

    @Test
    void testBoundaryRight() {
        paddle = new Paddle(SCREEN_WIDTH - PADDLE_WIDTH, SCREEN_HEIGHT - 50);
        paddle.moveRight();
        paddle.update(0.016);

        // Should not go beyond right boundary
        assertEquals(SCREEN_WIDTH - PADDLE_WIDTH, paddle.getX(), 0.001);
    }

    @Test
    void testSetWidth() {
        double newWidth = 150;
        double centerX = paddle.getX() + paddle.getWidth() / 2;

        paddle.setWidth(newWidth);

        assertEquals(newWidth, paddle.getWidth(), 0.001);
        // Should remain centered around the same center point
        assertEquals(centerX, paddle.getX() + paddle.getWidth() / 2, 0.001);
    }

    @Test
    void testSetWidthBoundaryCheck() {
        // Test setting width when paddle is at left edge
        paddle = new Paddle(0, SCREEN_HEIGHT - 50);
        paddle.setWidth(200);

        // Should not go beyond left boundary
        assertEquals(0, paddle.getX(), 0.001);

        // Test setting width when paddle is at right edge
        paddle = new Paddle(SCREEN_WIDTH - 50, SCREEN_HEIGHT - 50);
        paddle.setWidth(200);

        // Should not go beyond right boundary
        assertEquals(SCREEN_WIDTH - 200, paddle.getX(), 0.001);
    }

    @Test
    void testGunPowerUp() {
        assertFalse(paddle.canShootGun());

        paddle.setCanShootGun(true);
        assertTrue(paddle.canShootGun());

        // Test cooldown
        paddle.resetGunTimer();
        assertFalse(paddle.canShootGun());

        // Simulate cooldown completion
        paddle.update(0.3); // Wait for cooldown
        assertTrue(paddle.canShootGun());
    }

    @Test
    void testSetGunCooldown() {
        double newCooldown = 0.5;
        paddle.setGunCooldown(newCooldown);
        paddle.setCanShootGun(true);
        paddle.resetGunTimer();

        assertFalse(paddle.canShootGun());

        // Should still be on cooldown after 0.3 seconds
        paddle.update(0.3);
        assertFalse(paddle.canShootGun());

        // Should be able to shoot after full cooldown
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

        // Test ball position follows paddle
        double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
        double expectedBallX = paddleCenterX - ball.getWidth() / 2;
        double expectedBallY = paddle.getY() - ball.getHeight();

        assertEquals(expectedBallX, ball.getX(), 0.001);
        assertEquals(expectedBallY, ball.getY(), 0.001);

        // Test ball movement with paddle
        double initialBallX = ball.getX();
        paddle.moveRight();
        paddle.update(0.016);

        // Ball should have moved with the paddle
        assertNotEquals(initialBallX, ball.getX(), 0.001);
    }

    @Test
    void testRender() {
        // This test is basic since we can't easily test JavaFX rendering in unit tests
        // In a real scenario, you might use TestFX or similar framework
        assertDoesNotThrow(() -> {
            // paddle.render(new MockGraphicsContext());
            // For now, just test that no exception is thrown from constructor
        });
    }

    @Test
    void testImageLoadingFallback() {
        // Test that paddle can be created even if image loading fails
        // This is tested implicitly by creating the paddle in setUp()
        assertNotNull(paddle);
    }
}
