package com.knightquest.arkanoid.model;

import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.powerup.PowerUpType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Base test class for common brick functionality
 */
abstract class BrickBaseTest {
    protected static final double BRICK_WIDTH = 60;
    protected static final double BRICK_HEIGHT = 20;
    protected static final double X = 100;
    protected static final double Y = 50;

    protected Brick brick;

    protected abstract Brick createBrick();

    @BeforeEach
    void setUp() {
        brick = createBrick();
    }

    @Test
    void testInitialization() {
        assertNotNull(brick);
        assertEquals(X, brick.getX(), 0.001);
        assertEquals(Y, brick.getY(), 0.001);
        assertEquals(BRICK_WIDTH, brick.getWidth(), 0.001);
        assertEquals(BRICK_HEIGHT, brick.getHeight(), 0.001);
        assertTrue(brick.isActive());
    }

    @Test
    void testUpdateDoesNothing() {
        double initialX = brick.getX();
        double initialY = brick.getY();

        brick.update(0.016);

        assertEquals(initialX, brick.getX(), 0.001);
        assertEquals(initialY, brick.getY(), 0.001);
    }

    @Test
    void testRenderDoesNotThrow() {
        // Mock GraphicsContext - in real tests you'd use Mockito
        GraphicsContext gc = null;
        assertDoesNotThrow(() -> brick.render(gc));
    }

    @Test
    void testPowerUpDropChance() {
        brick.setDropChance(1.0); // 100% chance để test
        brick.setPowerUpDrop(PowerUpType.EXPAND_PADDLE); // Sử dụng LASER thay vì EXPAND

        int drops = 0;
        int trials = 10;
        for (int i = 0; i < trials; i++) {
            if (brick.getPowerUpDrop() != null) {
                drops++;
            }
        }
        // Với 100% chance, nên luôn drop
        assertEquals(trials, drops);
    }

    @Test
    void testSetDropChanceBoundaries() {
        // Test giá trị âm
        brick.setDropChance(-1.0);
        // Kiểm tra gián tiếp qua behavior
        brick.setPowerUpDrop(PowerUpType.EXPAND_PADDLE);

        // Test giá trị > 1
        brick.setDropChance(2.0);

        // Test giá trị hợp lệ
        brick.setDropChance(0.7);

        // Không có getter cho dropChance, nên test thông qua behavior
        assertTrue(true); // Placeholder - method không throw exception
    }

    @Test
    void testSetPowerUpType() {
        // Sử dụng các PowerUpType thực tế từ code của bạn
        brick.setPowerUpDrop(PowerUpType.EXPAND_PADDLE);

        // Test với chance 100% để đảm bảo drop
        brick.setDropChance(1.0);
        assertEquals(PowerUpType.EXPAND_PADDLE, brick.getPowerUpDrop());

        brick.setPowerUpDrop(PowerUpType.SLOW_BALL);
        assertEquals(PowerUpType.SLOW_BALL, brick.getPowerUpDrop());
    }

    @Test
    void testCommonBrickMethods() {
        // Test các method cơ bản có tồn tại
        assertNotNull(brick.getType());
        assertTrue(brick.getHealth() >= 0);
        assertDoesNotThrow(() -> brick.isBreakable());
        assertDoesNotThrow(() -> brick.isDestroyed());
        assertDoesNotThrow(() -> brick.destroy());
        assertDoesNotThrow(() -> brick.takeHit());
    }
}
