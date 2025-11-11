package com.knightquest.arkanoid.controller;

import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.Bullet;
import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.model.brick.Brick;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        resetGameManagerInstance();
        gameManager = GameManager.getInstance();
    }

    @Test
    void testSingletonPattern() {
        GameManager instance1 = GameManager.getInstance();
        GameManager instance2 = GameManager.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    void testInitialGameState() {
        assertNotNull(gameManager.getPaddle());
        assertNotNull(gameManager.getBalls());
        assertFalse(gameManager.getBalls().isEmpty());
        assertNotNull(gameManager.getBricks());
        assertTrue(gameManager.getScore() >= 0);
        assertTrue(gameManager.getLives() > 0);
    }

    @Test
    void testPaddleInitialization() {
        Paddle paddle = gameManager.getPaddle();
        assertNotNull(paddle);
        assertTrue(paddle.getX() > 0);
        assertTrue(paddle.getY() > 0);
    }

    @Test
    void testBallManagement() {
        // Test getBall returns first ball
        Ball firstBall = gameManager.getBall();
        assertNotNull(firstBall);
        assertEquals(firstBall, gameManager.getBalls().get(0));

        // Test add ball
        Ball newBall = new Ball(100, 100);
        int initialCount = gameManager.getBalls().size();
        gameManager.addBall(newBall);
        assertEquals(initialCount + 1, gameManager.getBalls().size());

        // Test remove ball
        gameManager.removeBall(newBall);
        assertEquals(initialCount, gameManager.getBalls().size());
    }

    @Test
    void testShootBullet() {
        Paddle paddle = gameManager.getPaddle();
        paddle.setCanShootGun(true);

        int initialBulletCount = gameManager.getBullets().size();
        gameManager.shootBullet();

        assertTrue(gameManager.getBullets().size() > initialBulletCount);
    }

    @Test
    void testShootBulletWithoutGun() {
        Paddle paddle = gameManager.getPaddle();
        paddle.setCanShootGun(false);

        int initialBulletCount = gameManager.getBullets().size();
        gameManager.shootBullet();

        assertEquals(initialBulletCount, gameManager.getBullets().size());
    }

    @Test
    void testLevelManagement() {
        assertEquals(1, gameManager.getCurrentLevelNumber());
        assertNotNull(gameManager.getCurrentLevelName());
        assertFalse(gameManager.getCurrentLevelName().isEmpty());
    }

    @Test
    void testGameStateMethods() {
        // Test that these methods don't throw exceptions
        assertDoesNotThrow(() -> gameManager.update(0.016));
        assertDoesNotThrow(() -> gameManager.render(null));

        KeyEvent keyEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.RIGHT, false, false, false, false
        );
        assertDoesNotThrow(() -> gameManager.handleInput(keyEvent));
    }

    @Test
    void testRestartLevel() {
        int initialLevel = gameManager.getCurrentLevelNumber();
        gameManager.restartLevel();

        assertEquals(initialLevel, gameManager.getCurrentLevelNumber());
        assertNotNull(gameManager.getBall());
        assertNotNull(gameManager.getBricks());
    }

    @Test
    void testResetGame() {
        gameManager.resetGame();

        assertEquals(1, gameManager.getCurrentLevelNumber());
        assertEquals(0, gameManager.getScore());
        assertTrue(gameManager.getLives() > 0);
    }

    @Test
    void testNextLevel() {
        int initialLevel = gameManager.getCurrentLevelNumber();
        gameManager.nextLevel();

        assertTrue(gameManager.getCurrentLevelNumber() >= initialLevel);
    }

    @Test
    void testEventManager() {
        assertNotNull(gameManager.getEventManager());
        assertDoesNotThrow(() -> {
            gameManager.getEventManager().notifyScoreChanged(100);
        });
    }

    @Test
    void testPowerUpManager() {
        assertNotNull(gameManager.getPowerUpManager());
    }

    @Test
    void testExplosionEffectManager() {
        assertNotNull(gameManager.getExplosionEffectManager());
    }

    @Test
    void testBrickListNotEmpty() {
        List<Brick> bricks = gameManager.getBricks();
        assertNotNull(bricks);
        assertFalse(bricks.isEmpty());
    }

    @Test
    void testBulletList() {
        List<Bullet> bullets = gameManager.getBullets();
        assertNotNull(bullets);
    }

    // Helper method to reset singleton
    private void resetGameManagerInstance() {
        try {
            Field instanceField = GameManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset GameManager instance", e);
        }
    }
}
