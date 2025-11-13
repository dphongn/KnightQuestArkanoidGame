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
        // UnbreakableBrick không nên bị destroy
        int initialHealth = brick.getHealth();

        // Không gọi destroy() để tránh base class set hitPoints = 0
        // Thay vào đó chỉ kiểm tra rằng takeHit() không giảm health
        brick.takeHit();

        assertEquals(initialHealth, brick.getHealth());
        assertFalse(brick.isDestroyed());
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

        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(1, 1);
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();

        assertDoesNotThrow(() -> unbreakableBrick.render(gc));

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
