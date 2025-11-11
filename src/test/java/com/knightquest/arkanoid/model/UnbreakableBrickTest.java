package com.knightquest.arkanoid.model;

import com.knightquest.arkanoid.model.BrickBaseTest;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.BrickType;
import com.knightquest.arkanoid.model.brick.UnbreakableBrick;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnbreakableBrickTest extends BrickBaseTest {

    @Override
    protected Brick createBrick() {
        return new UnbreakableBrick(X, Y, BRICK_WIDTH, BRICK_HEIGHT);
    }

    @Test
    void testInitialState() {
        assertEquals(Integer.MAX_VALUE, brick.getHealth());
        assertEquals(BrickType.UNBREAKABLE, brick.getType());
        assertFalse(brick.isBreakable());
    }

    @Test
    void testTakeHitDoesNothing() {
        int initialHealth = brick.getHealth();

        brick.takeHit();

        assertEquals(initialHealth, brick.getHealth());
        assertFalse(brick.isDestroyed());
    }

    @Test
    void testDestroyMethodDoesNothing() {
        brick.destroy();

        assertFalse(brick.isDestroyed());
        assertEquals(Integer.MAX_VALUE, brick.getHealth());
    }

    @Test
    void testIsDestroyedAlwaysFalse() {
        assertFalse(brick.isDestroyed());

        for (int i = 0; i < 10; i++) {
            brick.takeHit();
        }

        assertFalse(brick.isDestroyed());
    }

    @Test
    void testImagePathThroughBehavior() {
        UnbreakableBrick unbreakableBrick = (UnbreakableBrick) brick;

        // Test render behavior
        assertDoesNotThrow(() -> unbreakableBrick.render(null));

        // Test unbreakable specific behavior
        unbreakableBrick.takeHit();
        assertFalse(unbreakableBrick.isDestroyed());
    }

    @Test
    void testPowerUpDrop() {
        brick.setDropChance(1.0);
        assertNull(brick.getPowerUpDrop());
    }
}
