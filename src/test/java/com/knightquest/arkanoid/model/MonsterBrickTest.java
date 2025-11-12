package com.knightquest.arkanoid.model;

import com.knightquest.arkanoid.model.BrickBaseTest;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.BrickType;
import com.knightquest.arkanoid.model.brick.MonsterBrick;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class MonsterBrickTest extends BrickBaseTest {

    private static final double MIN_X = 0;
    private static final double MAX_X = 800;

    @Override
    protected Brick createBrick() {
        return new MonsterBrick(X, Y, BRICK_WIDTH, BRICK_HEIGHT, MIN_X, MAX_X);
    }

    @Test
    void testInitialState() {
        assertEquals(1, brick.getHealth());
        assertEquals(BrickType.MONSTER, brick.getType());
        assertTrue(brick.isBreakable());
    }

    @Test
    void testMovementWithinBounds() {
        MonsterBrick monsterBrick = (MonsterBrick) brick;
        List<Brick> emptyList = new ArrayList<>();

        double initialX = monsterBrick.getX();

        monsterBrick.update(0.016, emptyList);

        double newX = monsterBrick.getX();
        assertTrue(newX > initialX);
    }

    @Test
    void testImagePathThroughBehavior() {
        MonsterBrick monsterBrick = (MonsterBrick) brick;

        // Tạo Canvas tạm để lấy GraphicsContext
        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(1, 1);
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();

        // Test render behavior
        assertDoesNotThrow(() -> monsterBrick.render(gc));

        // Test movement behavior
        List<Brick> emptyList = new ArrayList<>();
        assertDoesNotThrow(() -> monsterBrick.update(0.016, emptyList));
    }


    @Test
    void testTakeHitDestroysMonster() {
        brick.takeHit();
        assertTrue(brick.isDestroyed());
    }
}
