package com.knightquest.arkanoid.controller;

import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.Bullet;
import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.model.brick.Brick;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private GameManager gameManager;
    private Canvas canvas;

    // =================== Khởi động JavaFX toolkit ===================
    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown); // Khởi động toolkit
        latch.await(5, TimeUnit.SECONDS);
    }

    // =================== Setup trước mỗi test ===================
    @BeforeEach
    void setUp() throws InterruptedException {
        resetGameManagerInstance();
        gameManager = GameManager.getInstance();

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            canvas = new Canvas(1, 1); // Canvas tạm cho render
            latch.countDown();
        });
        latch.await(1, TimeUnit.SECONDS);
    }

    // =================== Singleton ===================
    @Test
    void testSingletonPattern() {
        GameManager instance1 = GameManager.getInstance();
        GameManager instance2 = GameManager.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    // =================== Initial state ===================
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
        assertTrue(paddle.getX() >= 0);
        assertTrue(paddle.getY() >= 0);
    }

    // =================== Ball management ===================
    @Test
    void testBallManagement() {
        Ball firstBall = gameManager.getBall();
        assertNotNull(firstBall);
        assertEquals(firstBall, gameManager.getBalls().get(0));

        Ball newBall = new Ball(100, 100);
        int initialCount = gameManager.getBalls().size();
        gameManager.addBall(newBall);
        assertEquals(initialCount + 1, gameManager.getBalls().size());

        gameManager.removeBall(newBall);
        assertEquals(initialCount, gameManager.getBalls().size());
    }

    // =================== Shooting ===================
    @Test
    void testShootBullet() throws InterruptedException {
        Paddle paddle = gameManager.getPaddle();
        paddle.setCanShootGun(true);

        int initialBulletCount = gameManager.getBullets().size();

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            gameManager.shootBullet();
            latch.countDown();
        });
        latch.await(1, TimeUnit.SECONDS);

        assertTrue(gameManager.getBullets().size() > initialBulletCount);
    }

    @Test
    void testShootBulletWithoutGun() throws InterruptedException {
        Paddle paddle = gameManager.getPaddle();
        paddle.setCanShootGun(false);

        int initialBulletCount = gameManager.getBullets().size();

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            gameManager.shootBullet();
            latch.countDown();
        });
        latch.await(1, TimeUnit.SECONDS);

        assertEquals(initialBulletCount, gameManager.getBullets().size());
    }

    // =================== Level management ===================
    @Test
    void testLevelManagement() {
        assertEquals(1, gameManager.getCurrentLevelNumber());
        assertNotNull(gameManager.getCurrentLevelName());
        assertFalse(gameManager.getCurrentLevelName().isEmpty());
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
    void testNextLevel() {
        int initialLevel = gameManager.getCurrentLevelNumber();
        gameManager.nextLevel();
        assertTrue(gameManager.getCurrentLevelNumber() >= initialLevel);
    }

    @Test
    void testResetGame() {
        gameManager.resetGame();
        assertEquals(1, gameManager.getCurrentLevelNumber());
        assertEquals(0, gameManager.getScore());
        assertTrue(gameManager.getLives() > 0);
    }

    // =================== Update & Render ===================
    @Test
    void testGameStateMethods() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> gameManager.update(0.016));
            assertDoesNotThrow(() -> gameManager.render(canvas.getGraphicsContext2D()));

            KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.RIGHT, false, false, false, false);
            assertDoesNotThrow(() -> gameManager.handleInput(keyEvent));

            latch.countDown();
        });
        latch.await(1, TimeUnit.SECONDS);
    }

    // =================== Managers ===================
    @Test
    void testEventManager() {
        assertNotNull(gameManager.getEventManager());
        assertDoesNotThrow(() -> gameManager.getEventManager().notifyScoreChanged(100));
    }

    @Test
    void testPowerUpManager() {
        assertNotNull(gameManager.getPowerUpManager());
    }

    @Test
    void testExplosionEffectManager() {
        assertNotNull(gameManager.getExplosionEffectManager());
    }

    // =================== Lists ===================
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

    // =================== Helper method ===================
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
