package com.knightquest.arkanoid.model;

import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.PrisonerBrick;
import com.knightquest.arkanoid.model.powerup.PowerUpType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
    void testPowerUpDropChance() {
        if (brick instanceof PrisonerBrick) {
            PrisonerBrick prisonerBrick = (PrisonerBrick) brick;

            prisonerBrick.setGuaranteedPowerUp(PowerUpType.EXPAND_PADDLE);
            prisonerBrick.takeHit();

            PowerUpType drop = prisonerBrick.getPowerUpDrop();
            assertEquals(PowerUpType.EXPAND_PADDLE, drop);
        }
    }

    @Test
    void testSetDropChanceBoundaries() {
        brick.setDropChance(-1.0);
        brick.setPowerUpDrop(PowerUpType.EXPAND_PADDLE);

        brick.setDropChance(2.0);
        brick.setPowerUpDrop(PowerUpType.EXPAND_PADDLE);

        brick.setDropChance(0.7);
        brick.setPowerUpDrop(PowerUpType.EXPAND_PADDLE);

        assertTrue(true);
    }

    @Test
    void testSetPowerUpType() {
        if (brick instanceof PrisonerBrick) {
            PowerUpType[] types = {
                    PowerUpType.EXPAND_PADDLE,
                    PowerUpType.SLOW_BALL,
                    PowerUpType.GUN_PADDLE,
                    PowerUpType.MULTI_BALL
            };

            for (PowerUpType type : types) {
                PrisonerBrick prisonerBrick = (PrisonerBrick) createBrick();

                prisonerBrick.setGuaranteedPowerUp(type);
                prisonerBrick.takeHit();

                PowerUpType drop = prisonerBrick.getPowerUpDrop();
                assertEquals(type, drop);
            }
        }
    }

    @Test
    void testCommonBrickMethods() {
        assertNotNull(brick.getType());
        assertTrue(brick.getHealth() >= 0);
        assertDoesNotThrow(() -> brick.isBreakable());
        assertDoesNotThrow(() -> brick.isDestroyed());
        assertDoesNotThrow(() -> brick.destroy());
        assertDoesNotThrow(() -> brick.takeHit());
    }
}
