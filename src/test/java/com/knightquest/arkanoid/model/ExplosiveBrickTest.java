package com.knightquest.arkanoid.model;

import com.knightquest.arkanoid.model.BrickBaseTest;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.BrickType;
import com.knightquest.arkanoid.model.brick.ExplosiveBrick;
import com.knightquest.arkanoid.model.brick.NormalBrick;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class ExplosiveBrickTest extends BrickBaseTest {

    @Override
    protected Brick createBrick() {
        return new ExplosiveBrick(X, Y, BRICK_WIDTH, BRICK_HEIGHT);
    }

    @Test
    void testInitialState() {
        assertEquals(1, brick.getHealth());
        assertEquals(BrickType.EXPLOSIVE, brick.getType());
        assertTrue(brick.isBreakable());

        ExplosiveBrick explosiveBrick = (ExplosiveBrick) brick;
        assertFalse(explosiveBrick.hasExploded());
        assertEquals(65.0, explosiveBrick.getExplosionRadius(), 0.001);
    }

    @Test
    void testTakeHitTriggersExplosion() {
        ExplosiveBrick explosiveBrick = (ExplosiveBrick) brick;

        brick.takeHit();

        assertTrue(brick.isDestroyed());
        assertTrue(explosiveBrick.hasExploded());
    }

    @Test
    void testGetExplosionTargets() {
        ExplosiveBrick explosiveBrick = (ExplosiveBrick) brick;
        List<Brick> allBricks = new ArrayList<>();

        allBricks.add(explosiveBrick);
        allBricks.add(new NormalBrick(X + 30, Y + 30, BRICK_WIDTH, BRICK_HEIGHT));
        allBricks.add(new NormalBrick(X + 200, Y + 200, BRICK_WIDTH, BRICK_HEIGHT));

        List<Brick> targets = explosiveBrick.getExplosionTargets(allBricks);

        assertFalse(targets.contains(explosiveBrick));
        assertEquals(1, targets.size());
    }

    @Test
    void testImagePathThroughBehavior() {
        ExplosiveBrick explosiveBrick = (ExplosiveBrick) brick;

        // Test render behavior
        assertDoesNotThrow(() -> explosiveBrick.render(null));

        // Test explosion behavior
        explosiveBrick.takeHit();
        assertDoesNotThrow(() -> explosiveBrick.render(null));
    }

    @Test
    void testMultipleExplosions() {
        ExplosiveBrick explosiveBrick = (ExplosiveBrick) brick;

        brick.takeHit();
        assertTrue(explosiveBrick.hasExploded());

        brick.takeHit();
        assertTrue(explosiveBrick.hasExploded());
    }
}
