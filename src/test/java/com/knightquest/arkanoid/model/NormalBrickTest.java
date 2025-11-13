package com.knightquest.arkanoid.model;

import com.knightquest.arkanoid.model.BrickBaseTest;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.BrickType;
import com.knightquest.arkanoid.model.brick.NormalBrick;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NormalBrickTest extends BrickBaseTest {

    @Override
    protected Brick createBrick() {
        return new NormalBrick(X, Y, BRICK_WIDTH, BRICK_HEIGHT);
    }

    @Test
    void testInitialState() {
        assertEquals(1, brick.getHealth());
        assertEquals(BrickType.NORMAL, brick.getType());
        assertTrue(brick.isBreakable());
    }

    @Test
    void testTakeHit() {
        brick.takeHit();
        assertTrue(brick.isDestroyed());
        assertEquals(0, brick.getHealth());
    }

    @Test
    void testDestroyMethod() {
        brick.destroy();
        assertTrue(brick.isDestroyed());
        assertEquals(0, brick.getHealth());
    }

    @Test
    void testImagePathThroughBehavior() {
        NormalBrick normalBrick = (NormalBrick) brick;

        // Test type-specific behavior
        assertEquals(BrickType.NORMAL, normalBrick.getType());

        // Test health và breakable mà không gọi render
        assertEquals(1, normalBrick.getHealth());
        assertTrue(normalBrick.isBreakable());
    }

    @Test
    void testMultipleHits() {
        brick.takeHit();
        assertTrue(brick.isDestroyed());

        brick.takeHit();
        assertTrue(brick.isDestroyed());
    }
}
