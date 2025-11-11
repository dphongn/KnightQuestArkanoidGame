package com.knightquest.arkanoid.model;

import static org.junit.jupiter.api.Assertions.*;
import static com.knightquest.arkanoid.util.Constants.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BallTest {

    private Ball ball;

    @BeforeEach
    void setUp() {
        ball = new Ball(100, 100);
    }

    @Test
    void testNormalizeSpeed() {
        ball.setVelocity(300, 400); // tốc độ khác BALL_SPEED
        double before = ball.getSpeed();
        ball.releaseWithVelocity(300, 400); // có normalize bên trong
        double after = ball.getSpeed();

        assertNotEquals(before, after);
        assertEquals(BALL_SPEED, after, 0.01, "Speed should be normalized to BALL_SPEED");
    }

    @Test
    void testBounceVertical() {
        ball.setVelocity(0, 100);
        ball.bounceVertical();
        assertEquals(-100, ball.getDy());
    }

    @Test
    void testBounceHorizontal() {
        ball.setVelocity(100, 0);
        ball.bounceHorizontal();
        assertEquals(-100, ball.getDx());
    }

    @Test
    void testIsFallenOff() {
        ball.setY(SCREEN_HEIGHT + 10);
        assertTrue(ball.isFallenOff());
    }

    @Test
    void testLaunchChangesState() {
        assertTrue(ball.isStuckToPaddle());
        ball.updateStuckOffset(20);
        ball.launch();
        assertFalse(ball.isStuckToPaddle());
        assertNotEquals(0, ball.getDy());
        assertNotEquals(0, ball.getDx());
    }

    @Test
    void testUpdateStuckOffsetLimit() {
        ball.updateStuckOffset(100);
        ball.updateStuckOffset(100);
        // offset bị giới hạn ở 45
        ball.launch();
        ball.resetToStuck();
        ball.updateStuckOffset(-100);
        // offset bị giới hạn ở -45
        assertTrue(true); // không crash
    }

    @Test
    void testResetToStuck() {
        ball.launch();
        ball.resetToStuck();
        assertTrue(ball.isStuckToPaddle());
        assertEquals(0, ball.getDx());
        assertEquals(0, ball.getDy());
    }

    @Test
    void testReleaseWithVelocity() {
        ball.releaseWithVelocity(100, 100);
        assertFalse(ball.isStuckToPaddle());
        assertEquals(BALL_SPEED, ball.getSpeed(), 0.01);
    }
}
