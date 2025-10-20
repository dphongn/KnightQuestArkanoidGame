package com.knightquest.arkanoid.factory;

import java.util.Random;

import com.knightquest.arkanoid.model.powerup.PowerUp;
import com.knightquest.arkanoid.model.powerup.PowerUpType;
import com.knightquest.arkanoid.model.powerup.ExpandPaddlePowerUp;
import com.knightquest.arkanoid.model.powerup.FastBallPowerUp;
import com.knightquest.arkanoid.model.powerup.FireBallPowerUp;
import com.knightquest.arkanoid.model.powerup.LaserPaddlePowerUp;
import com.knightquest.arkanoid.model.powerup.MagnetPaddlePowerUp;
import com.knightquest.arkanoid.model.powerup.MultiBallPowerUp;
import com.knightquest.arkanoid.model.powerup.SlowBallPowerUp;
import com.knightquest.arkanoid.model.powerup.PierceBallPowerUp;

/**
 * Factory class to create PowerUp instances based on PowerUpType.
 */
public class PowerUpFactory {
    private static final Random random = new Random();

    /**
     * Create a PowerUp based on the given PowerUpType and position.
     */
    public static PowerUp createPowerUp(PowerUpType type, double x, double y) {
        return switch (type) {
            case EXPAND_PADDLE -> new ExpandPaddlePowerUp(x, y);
            case LASER_PADDLE -> new LaserPaddlePowerUp(x, y);
            case MAGNET_PADDLE -> new MagnetPaddlePowerUp(x, y);
            case MULTI_BALL -> new MultiBallPowerUp(x, y);
            case FAST_BALL -> new FastBallPowerUp(x, y);
            case SLOW_BALL -> new SlowBallPowerUp(x, y);
            case FIRE_BALL -> new FireBallPowerUp(x, y);
            case PIERCE_BALL -> new PierceBallPowerUp(x, y);
        };
    }

    /**
     * Create a random PowerUp at the specified position.
     */
    public static PowerUp createRandomPowerUp(double x, double y) {
        PowerUpType[] types = PowerUpType.values();
        PowerUpType randomType = types[random.nextInt(types.length)];
        return createPowerUp(randomType, x, y);
    }


}
