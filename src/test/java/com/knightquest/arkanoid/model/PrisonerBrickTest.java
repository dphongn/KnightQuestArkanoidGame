package com.knightquest.arkanoid.model;

import com.knightquest.arkanoid.model.BrickBaseTest;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.BrickType;
import com.knightquest.arkanoid.model.brick.PrisonerBrick;
import com.knightquest.arkanoid.model.powerup.PowerUpType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrisonerBrickTest extends BrickBaseTest {

    @Override
    protected Brick createBrick() {
        return new PrisonerBrick(X, Y, BRICK_WIDTH, BRICK_HEIGHT);
    }

    @Test
    void testInitialState() {
        assertEquals(1, brick.getHealth());
        assertEquals(BrickType.PRISONER, brick.getType());
        assertTrue(brick.isBreakable());

        PrisonerBrick prisonerBrick = (PrisonerBrick) brick;
        assertFalse(prisonerBrick.hasPowerUpDropped());
    }

    @Test
    void testTakeHitTriggersPowerUpDrop() {
        PrisonerBrick prisonerBrick = (PrisonerBrick) brick;

        brick.takeHit();

        assertTrue(brick.isDestroyed());
        assertTrue(prisonerBrick.hasPowerUpDropped());
    }

    @Test
    void testPowerUpSpawnPosition() {
        PrisonerBrick prisonerBrick = (PrisonerBrick) brick;
        double[] position = prisonerBrick.getPowerUpSpawnPosition();

        assertEquals(X + BRICK_WIDTH / 2, position[0], 0.001);
        assertEquals(Y + BRICK_HEIGHT / 2, position[1], 0.001);
    }

    @Test
    void testImagePathThroughBehavior() {
        PrisonerBrick prisonerBrick = (PrisonerBrick) brick;

        // Gián tiếp test behavior thay vì render
        assertEquals(BrickType.PRISONER, prisonerBrick.getType());
        assertEquals(1, prisonerBrick.getHealth());
        assertTrue(prisonerBrick.isBreakable());

        // Kiểm tra vị trí power-up spawn
        double[] pos = prisonerBrick.getPowerUpSpawnPosition();
        assertEquals(X + BRICK_WIDTH / 2, pos[0], 0.001);
        assertEquals(Y + BRICK_HEIGHT / 2, pos[1], 0.001);
    }


    @Test
    void testGuaranteedPowerUpDrop() {
        PrisonerBrick prisonerBrick = (PrisonerBrick) brick;

        prisonerBrick.setGuaranteedPowerUp(PowerUpType.EXPAND_PADDLE);

        brick.takeHit();

        PowerUpType dropped = prisonerBrick.getPowerUpDrop();
        assertEquals(PowerUpType.EXPAND_PADDLE, dropped);

        PowerUpType secondDrop = prisonerBrick.getPowerUpDrop();
        assertNull(secondDrop);
    }

    @Test
    void testDifferentPowerUpTypes() {
        PrisonerBrick prisonerBrick = (PrisonerBrick) brick;

        PowerUpType[] testTypes = {
                PowerUpType.EXPAND_PADDLE,
                PowerUpType.GUN_PADDLE,
                PowerUpType.MAGNET_PADDLE,
                PowerUpType.MULTI_BALL
        };

        for (PowerUpType type : testTypes) {
            prisonerBrick.setGuaranteedPowerUp(type);
            brick.takeHit();

            PowerUpType dropped = prisonerBrick.getPowerUpDrop();
            assertEquals(type, dropped);

            prisonerBrick = new PrisonerBrick(X, Y, BRICK_WIDTH, BRICK_HEIGHT);
            brick = prisonerBrick;
        }
    }

    @Test
    void testPowerUpDropReset() {
        PrisonerBrick prisonerBrick = (PrisonerBrick) brick;
        prisonerBrick.setGuaranteedPowerUp(PowerUpType.SLOW_BALL);

        brick.takeHit();

        assertTrue(prisonerBrick.hasPowerUpDropped());

        prisonerBrick.getPowerUpDrop();
        assertFalse(prisonerBrick.hasPowerUpDropped());
    }

    @Test
    void testNoPowerUpByDefault() {
        PrisonerBrick prisonerBrick = (PrisonerBrick) brick;

        brick.takeHit();

        PowerUpType dropped = prisonerBrick.getPowerUpDrop();
        assertNull(dropped);
    }

    @Test
    void testPrisonerSpecificMethods() {
        PrisonerBrick prisonerBrick = (PrisonerBrick) brick;

        // Test các public methods cụ thể của PrisonerBrick
        assertDoesNotThrow(() -> prisonerBrick.hasPowerUpDropped());
        assertDoesNotThrow(() -> prisonerBrick.getPowerUpSpawnPosition());
        assertDoesNotThrow(() -> prisonerBrick.setGuaranteedPowerUp(PowerUpType.EXPAND_PADDLE));

        // Test behavior gián tiếp, không gọi render
        assertEquals(BrickType.PRISONER, prisonerBrick.getType());
        assertEquals(1, prisonerBrick.getHealth());
        assertTrue(prisonerBrick.isBreakable());
    }


    @Test
    void testPrisonerBrickDestruction() {
        PrisonerBrick prisonerBrick = (PrisonerBrick) brick;

        // Trước khi takeHit
        assertFalse(prisonerBrick.hasPowerUpDropped());
        assertFalse(prisonerBrick.isDestroyed());

        // Sau khi takeHit
        prisonerBrick.takeHit();
        assertTrue(prisonerBrick.hasPowerUpDropped());
        assertTrue(prisonerBrick.isDestroyed());
    }
}
