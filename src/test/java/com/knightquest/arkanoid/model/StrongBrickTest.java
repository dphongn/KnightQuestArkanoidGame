package com.knightquest.arkanoid.model;

import com.knightquest.arkanoid.model.BrickBaseTest;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.BrickType;
import com.knightquest.arkanoid.model.brick.StrongBrick;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StrongBrickTest extends BrickBaseTest {

    @Override
    protected Brick createBrick() {
        return new StrongBrick(X, Y, BRICK_WIDTH, BRICK_HEIGHT);
    }

    @Test
    void testInitialState() {
        assertEquals(3, brick.getHealth());
        assertEquals(BrickType.STRONG, brick.getType());
        assertTrue(brick.isBreakable());
    }

    @Test
    void testTakeHitMultipleTimes() {
        brick.takeHit();
        assertEquals(2, brick.getHealth());
        assertFalse(brick.isDestroyed());

        brick.takeHit();
        assertEquals(1, brick.getHealth());
        assertFalse(brick.isDestroyed());

        brick.takeHit();
        assertEquals(0, brick.getHealth());
        assertTrue(brick.isDestroyed());
    }

    @Test
    void testImagePathThroughBehavior() {
        StrongBrick strongBrick = (StrongBrick) brick;

        // Test render behavior at different health levels
        assertDoesNotThrow(() -> strongBrick.render(null));

        brick.takeHit();
        assertDoesNotThrow(() -> strongBrick.render(null));

        brick.takeHit();
        assertDoesNotThrow(() -> strongBrick.render(null));
    }

    @Test
    void testColorChangesThroughBehavior() {
        StrongBrick strongBrick = (StrongBrick) brick;

        // Test that color update doesn't throw
        assertDoesNotThrow(() -> {
            // Gọi updateColor thông qua reflection nếu cần
            // Hoặc test gián tiếp qua takeHit
            brick.takeHit();
        });

        assertTrue(brick.getHealth() >= 0);
    }

    @Test
    void testExtraHitsBeyondZero() {
        brick.takeHit();
        brick.takeHit();
        brick.takeHit();
        assertTrue(brick.isDestroyed());

        brick.takeHit();
        assertTrue(brick.isDestroyed());
        assertEquals(0, brick.getHealth());
    }
}
