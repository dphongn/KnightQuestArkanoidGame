package com.knightquest.arkanoid.controller;

import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.ExplosiveBrick;
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
        // Tạo CollisionHandler với null parameters - chỉ test collision logic thuần túy
        collisionHandler = new CollisionHandler(null, null);
        ball = new Ball(100, 100);
        paddle = new Paddle(90, 200);
    }

    @Test
    void testCheckBallPaddleCollision_NoCollision() {
        // Arrange - ball far from paddle
        ball.setX(500);
        ball.setY(500);
        double initialX = ball.getX();
        double initialY = ball.getY();

        // Act
        collisionHandler.checkBallPaddleCollision(ball, paddle);

        // Assert - no changes
        assertEquals(initialX, ball.getX(), 0.001);
        assertEquals(initialY, ball.getY(), 0.001);
    }

    @Test
    void testCheckBallPaddleCollision_WithCollision() {
        // Arrange - ball colliding with paddle
        ball.setX(95);
        ball.setY(195);
        ball.setDx(5);
        ball.setDy(5);

        // Act
        collisionHandler.checkBallPaddleCollision(ball, paddle);

        // Assert - ball should bounce and be repositioned
        assertTrue(ball.getDy() < 0); // Should bounce upward
        assertEquals(paddle.getY() - ball.getHeight(), ball.getY(), 0.001);
        assertNotEquals(0, ball.getDx()); // Should have horizontal movement
    }

    @Test
    void testCheckBallPaddleCollision_MagneticPaddle() {
        // Arrange
        ball.setX(95);
        ball.setY(195);
        paddle.setMagnetic(true);

        // Act
        collisionHandler.checkBallPaddleCollision(ball, paddle);

        // Assert - ball should be positioned above paddle
        assertEquals(paddle.getY() - ball.getHeight(), ball.getY(), 0.001);
        // Magnetic behavior tested through positioning
    }

    @Test
    void testCheckBallPaddleCollision_DifferentHitPositions() {
        // Test that ball gets different angles based on hit position
        Paddle testPaddle = new Paddle(100, 200);

        // Test left side hit
        Ball leftBall = new Ball(105, 195);
        leftBall.setDy(5);
        collisionHandler.checkBallPaddleCollision(leftBall, testPaddle);
        assertTrue(leftBall.getDx() < 0); // Should bounce left

        // Test right side hit
        Ball rightBall = new Ball(185, 195);
        rightBall.setDy(5);
        collisionHandler.checkBallPaddleCollision(rightBall, testPaddle);
        assertTrue(rightBall.getDx() > 0); // Should bounce right

        // Test center hit
        Ball centerBall = new Ball(145, 195);
        centerBall.setDy(5);
        collisionHandler.checkBallPaddleCollision(centerBall, testPaddle);
        assertTrue(Math.abs(centerBall.getDx()) < 1.0); // Minimal horizontal movement
    }

    @Test
    void testCheckBallBrickCollision_NoCollision() {
        // Arrange
        List<Brick> bricks = new ArrayList<>();
        Brick brick = new NormalBrick(200, 200, 60, 20);
        bricks.add(brick);

        ball.setX(100);
        ball.setY(100);
        double initialDx = ball.getDx();
        double initialDy = ball.getDy();

        // Act
        collisionHandler.checkBallBrickCollision(ball, bricks);

        // Assert - no changes to ball or brick
        assertEquals(initialDx, ball.getDx(), 0.001);
        assertEquals(initialDy, ball.getDy(), 0.001);
        assertTrue(brick.isActive());
    }

    @Test
    void testCheckBallBrickCollision_WithCollision() {
        // Arrange
        List<Brick> bricks = new ArrayList<>();
        Brick brick = new NormalBrick(95, 95, 60, 20);
        bricks.add(brick);

        ball.setX(100);
        ball.setY(100);
        double initialDx = ball.getDx();
        double initialDy = ball.getDy();

        // Act
        collisionHandler.checkBallBrickCollision(ball, bricks);

        // Assert - ball should bounce and brick should be destroyed
        assertNotEquals(initialDx, ball.getDx());
        assertNotEquals(initialDy, ball.getDy());
        assertTrue(brick.isDestroyed());
    }

    @Test
    void testCheckBallBrickCollision_UnbreakableBrick() {
        // Arrange
        List<Brick> bricks = new ArrayList<>();
        Brick brick = new UnbreakableBrick(95, 95, 60, 20);
        bricks.add(brick);

        ball.setX(100);
        ball.setY(100);
        double initialDx = ball.getDx();
        double initialDy = ball.getDy();

        // Act
        collisionHandler.checkBallBrickCollision(ball, bricks);

        // Assert - ball should bounce but brick not destroyed
        assertNotEquals(initialDx, ball.getDx());
        assertNotEquals(initialDy, ball.getDy());
        assertFalse(brick.isDestroyed()); // Unbreakable!
    }

    @Test
    void testCheckBallBrickCollision_MultipleBricks() {
        // Arrange
        List<Brick> bricks = new ArrayList<>();
        Brick brick1 = new NormalBrick(95, 95, 60, 20);  // Should be hit
        Brick brick2 = new NormalBrick(200, 200, 60, 20); // Too far
        bricks.add(brick1);
        bricks.add(brick2);

        ball.setX(100);
        ball.setY(100);

        // Act
        collisionHandler.checkBallBrickCollision(ball, bricks);

        // Assert - only one brick hit
        assertTrue(brick1.isDestroyed());
        assertFalse(brick2.isDestroyed()); // Should remain active
    }

    @Test
    void testCheckBallWallCollision_LeftWall() {
        // Arrange
        ball.setX(-5);
        ball.setDx(-5);

        // Act
        collisionHandler.checkBallWallCollision(ball);

        // Assert
        assertEquals(0, ball.getX(), 0.001);
        assertTrue(ball.getDx() > 0); // Should bounce right
    }

    @Test
    void testCheckBallWallCollision_RightWall() {
        // Arrange
        ball.setX(790); // At right boundary (800 - 10)
        ball.setDx(5);

        // Act
        collisionHandler.checkBallWallCollision(ball);

        // Assert
        assertEquals(790, ball.getX(), 0.001);
        assertTrue(ball.getDx() < 0); // Should bounce left
    }

    @Test
    void testCheckBallWallCollision_TopWall() {
        // Arrange
        ball.setY(-5);
        ball.setDy(-5);

        // Act
        collisionHandler.checkBallWallCollision(ball);

        // Assert
        assertEquals(0, ball.getY(), 0.001);
        assertTrue(ball.getDy() > 0); // Should bounce down
    }

    @Test
    void testCheckBallWallCollision_NoCollision() {
        // Arrange
        double initialX = ball.getX();
        double initialY = ball.getY();
        double initialDx = ball.getDx();
        double initialDy = ball.getDy();

        // Act
        collisionHandler.checkBallWallCollision(ball);

        // Assert - no changes
        assertEquals(initialX, ball.getX(), 0.001);
        assertEquals(initialY, ball.getY(), 0.001);
        assertEquals(initialDx, ball.getDx(), 0.001);
        assertEquals(initialDy, ball.getDy(), 0.001);
    }

    @Test
    void testProcessBrickDestruction_ExplosiveChainReaction() {
        // Arrange
        List<Brick> bricks = new ArrayList<>();

        ExplosiveBrick explosive = new ExplosiveBrick(100, 100, 60, 20);
        NormalBrick nearby = new NormalBrick(120, 120, 60, 20); // Within explosion radius
        NormalBrick farAway = new NormalBrick(300, 300, 60, 20); // Outside radius

        bricks.add(explosive);
        bricks.add(nearby);
        bricks.add(farAway);

        // Destroy explosive to trigger explosion
        explosive.takeHit();

        // Act
        collisionHandler.processBrickDestruction(explosive, bricks);

        // Assert - explosion should destroy nearby bricks
        assertTrue(explosive.isDestroyed());
        assertTrue(nearby.isDestroyed()); // Destroyed by explosion
        assertFalse(farAway.isDestroyed()); // Should survive
    }

    @Test
    void testProcessBrickDestruction_NormalBrickNoChain() {
        // Arrange
        List<Brick> bricks = new ArrayList<>();
        NormalBrick brick = new NormalBrick(100, 100, 60, 20);
        brick.takeHit(); // Destroy it
        bricks.add(brick);

        // Act
        collisionHandler.processBrickDestruction(brick, bricks);

        // Assert - normal destruction, no chain reaction
        assertTrue(brick.isDestroyed());
    }

    @Test
    void testProcessBrickDestruction_MultipleExplosives() {
        // Arrange
        List<Brick> bricks = new ArrayList<>();

        ExplosiveBrick explosive1 = new ExplosiveBrick(100, 100, 60, 20);
        ExplosiveBrick explosive2 = new ExplosiveBrick(130, 130, 60, 20); // Close enough
        NormalBrick normal = new NormalBrick(160, 160, 60, 20);

        bricks.add(explosive1);
        bricks.add(explosive2);
        bricks.add(normal);

        // Destroy first explosive
        explosive1.takeHit();

        // Act
        collisionHandler.processBrickDestruction(explosive1, bricks);

        // Assert - chain reaction should destroy all
        assertTrue(explosive1.isDestroyed());
        assertTrue(explosive2.isDestroyed()); // Chain reaction
        assertTrue(normal.isDestroyed()); // Also destroyed
    }

    @Test
    void testBallBounceConsistency() {
        // Test that ball consistently bounces with predictable behavior
        ball.setX(100);
        ball.setY(100);
        ball.setDx(5);
        ball.setDy(5);

        double initialSpeed = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());

        // Bounce off horizontal surface
        ball.bounceHorizontal();
        double afterBounceSpeed = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());

        // Speed should be approximately conserved (allowing for floating point errors)
        assertEquals(initialSpeed, afterBounceSpeed, 0.1);
    }

    @Test
    void testCollisionWithPaddleEdgeCases() {
        // Test collision when ball is at extreme positions relative to paddle

        // Ball hitting exactly at paddle's left edge
        Ball edgeBall = new Ball(89, 195); // Left edge collision
        edgeBall.setDy(5);
        collisionHandler.checkBallPaddleCollision(edgeBall, paddle);
        assertTrue(edgeBall.getDy() < 0); // Should still bounce

        // Ball hitting exactly at paddle's right edge
        Ball rightEdgeBall = new Ball(171, 195); // Right edge collision
        rightEdgeBall.setDy(5);
        collisionHandler.checkBallPaddleCollision(rightEdgeBall, paddle);
        assertTrue(rightEdgeBall.getDy() < 0); // Should still bounce
    }

    @Test
    void testBallStuckPrevention() {
        // Test that ball is repositioned to prevent sticking
        ball.setX(95);
        ball.setY(199); // Almost inside paddle
        ball.setDy(5);

        double initialY = ball.getY();

        collisionHandler.checkBallPaddleCollision(ball, paddle);

        // Ball should be moved above paddle
        assertTrue(ball.getY() < initialY);
        assertEquals(paddle.getY() - ball.getHeight(), ball.getY(), 0.001);
    }
}
