package com.knightquest.arkanoid.manager;

import com.knightquest.arkanoid.controller.GameManager;
import com.knightquest.arkanoid.model.powerup.PowerUp;
import com.knightquest.arkanoid.model.powerup.PowerUpType;
import com.knightquest.arkanoid.model.powerup.FireBallPowerUp;
import com.knightquest.arkanoid.model.powerup.GunPaddlePowerUp;
import com.knightquest.arkanoid.model.powerup.PierceBallPowerUp;
import com.knightquest.arkanoid.model.powerup.MultiBallPowerUp;
import com.knightquest.arkanoid.model.powerup.ExpandPaddlePowerUp;
import com.knightquest.arkanoid.model.powerup.MagnetPaddlePowerUp;
import com.knightquest.arkanoid.model.powerup.FastBallPowerUp;
import com.knightquest.arkanoid.model.powerup.SlowBallPowerUp;
import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.observer.GameEventManager;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;

public class PowerUpManager {
    private List<PowerUp> droppingPowerUps;
    private Map<PowerUpType, PowerUp> activePowerUps;
    private GameManager gameManager;
    private GameEventManager gameEventManager;

    public PowerUpManager(GameManager gameManager, GameEventManager gameEventManager) {
        this.gameManager = gameManager;
        this.gameEventManager = gameEventManager;
        droppingPowerUps = new ArrayList<>();
        activePowerUps = new HashMap<>();
    }

    public void addPowerUp(PowerUp powerUp) {
        if (powerUp != null) {
            this.droppingPowerUps.add(powerUp);
            System.out.println("PowerUpManager: Đã thêm " + powerUp.getType() + " vào danh sách rơi.");
        }
    }

    public void spawnPowerUp(PowerUpType type, double x, double y) {
        PowerUp powerUp = null;

        switch (type) {
            case FIRE_BALL:
                powerUp = new FireBallPowerUp(x, y);
                break;

            case PIERCE_BALL:
                powerUp = new PierceBallPowerUp(x, y);
                break;
            case FAST_BALL:
                powerUp = new FastBallPowerUp(x, y);
                System.out.println("🎁 Fast Ball power-up spawned at (" + x + ", " + y + ")");
                break;

            case SLOW_BALL:
                powerUp = new SlowBallPowerUp(x, y);
                System.out.println("🎁 Slow Ball power-up spawned at (" + x + ", " + y + ")");
                break;

            case EXPAND_PADDLE:
                powerUp = new ExpandPaddlePowerUp(x, y);
                System.out.println("🎁 Expand Paddle power-up spawned at (" + x + ", " + y + ")");
                break;

            case GUN_PADDLE:
                powerUp = new GunPaddlePowerUp(x, y);
                System.out.println("🎁 Gun Paddle power-up spawned at (" + x + ", " + y + ")");
                break;


            case MAGNET_PADDLE:
                powerUp = new MagnetPaddlePowerUp(x, y);
                System.out.println("🎁 Magnet Paddle power-up spawned at (" + x + ", " + y + ")");
                break;

            case MULTI_BALL:
                powerUp = new MultiBallPowerUp(x, y);
                break;

            default:
                System.out.println("PowerUp type not implemented: " + type);
                return;
        }

        if (powerUp != null) {
            droppingPowerUps.add(powerUp);
            System.out.println("Dropping power up: " + powerUp);
        }
    }

    /**
     * Add a PowerUp that will drop from a brick
     */
    public void adddDroppedPowerUp(PowerUp powerUp) {
        if (powerUp != null) {
            droppingPowerUps.add(powerUp);
        }
    }

    /**
     * Update all PowerUps (dropping and active)
     */
    public void update(double deltaTime, Paddle paddle) {
       // Debug: Show dropping count if any
        if (!droppingPowerUps.isEmpty()) {
            System.out.println("Power-ups dropping: " + droppingPowerUps.size());
        }

        //Update droppong PowerUps
        Iterator<PowerUp> dropIterator = droppingPowerUps.iterator();
        while (dropIterator.hasNext()) {
            PowerUp powerUp = dropIterator.next();
            powerUp.update(deltaTime);

            //Check collision with paddle
            if (powerUp.isActive() && powerUp.intersects(paddle)) {
                collectPowerUp(powerUp, paddle);
                dropIterator.remove();
            }

            //Remove if fallen off screen or inactive
            else if (!powerUp.isActive()) {
                dropIterator.remove();
            }
        }

        //Update active PowerUps durations
        Iterator<Map.Entry<PowerUpType, PowerUp>> activeIterator = activePowerUps.entrySet().iterator();
        while (activeIterator.hasNext()) {
            Map.Entry<PowerUpType, PowerUp> entry = activeIterator.next();
            PowerUp powerUp = entry.getValue();

            powerUp.decreaseDuration(deltaTime);

            if (powerUp.isExpired()) {
                powerUp.remove(paddle);
                activeIterator.remove();
                onPowerUpExpired(powerUp);
            }
        }
    }

    /**
     * Collect a PowerUp when paddle touches it
     */
    private void collectPowerUp(PowerUp powerUp, Paddle paddle) {
        //Remove conflicting PowerUps
        removeConflictingPowerUps(powerUp, paddle);

        //Apply the PowerUp effect
        powerUp.apply(paddle);

        //Add to active PowerUps if not instant/permanent
        if (!powerUp.isPermanent()) {
            activePowerUps.put(powerUp.getType(), powerUp);
        }

        //Notify and play sound
        onPowerUpCollected(powerUp);
    }

    /**
     * Remove PowerUps that conflict with the new one
     */
    private void removeConflictingPowerUps(PowerUp newPowerUp, Paddle paddle) {
        PowerUpType newType = newPowerUp.getType();

        //Fast Ball conflicts with Slow Ball
        if (newType == PowerUpType.FAST_BALL && activePowerUps.containsKey(PowerUpType.SLOW_BALL)) {
            PowerUp conflicting = activePowerUps.remove(PowerUpType.SLOW_BALL);
            conflicting.remove(paddle);
        }

        //Slow Ball conflicts with Fast Ball
        else if (newType == PowerUpType.SLOW_BALL && activePowerUps.containsKey(PowerUpType.FAST_BALL)) {
            PowerUp conflicting = activePowerUps.remove(PowerUpType.FAST_BALL);
            conflicting.remove(paddle);
        }

        //Pierce Ball conflicts with Fire Ball
        else if (newType == PowerUpType.PIERCE_BALL && activePowerUps.containsKey(PowerUpType.FIRE_BALL)) {
            PowerUp conflicting = activePowerUps.remove(PowerUpType.FIRE_BALL);
            conflicting.remove(paddle);
        }

        //Fire Ball conflicts with Pierce Ball
        else if (newType == PowerUpType.FIRE_BALL && activePowerUps.containsKey(PowerUpType.PIERCE_BALL)) {
            PowerUp conflicting = activePowerUps.remove(PowerUpType.PIERCE_BALL);
            conflicting.remove(paddle);
        }
    }

    /**
     * Render all dropping PowerUps
     */
    public void render(GraphicsContext gc) {
        // Debug: Show dropping count if any
        if (!droppingPowerUps.isEmpty()) {
            System.out.println("Rendering power-ups dropping: " + droppingPowerUps.size());

        }

        for (PowerUp powerUp : droppingPowerUps) {
            if (powerUp.isActive()) {
                System.out.println("Power-up collected: " + powerUp.getType());
                powerUp.render(gc);
            }
        }


    }

    /**
     * Clean all PowerUps (when game ends or resets)
     */
    public void clearAll(Paddle paddle) {
        //Remove all active PowerUp effects
        for (PowerUp powerUp : activePowerUps.values()) {
            powerUp.remove(paddle);
        }
        activePowerUps.clear();
        droppingPowerUps.clear();
    }

    /**
     * Remove a specific active PowerUp
     */
    public void removeActivePowerUp(PowerUpType type, Paddle paddle) {
        PowerUp powerUp = activePowerUps.remove(type);
        if (powerUp != null) {
            powerUp.remove(paddle);
        }
    }

    /**
     * Check if a PowerUp type is curently active
     */
    public boolean isActive(PowerUpType type) {
        return activePowerUps.containsKey(type);
    }

    /**
     * Get remaining duration of an active PowerUp
     */
    public double getRemainDuration(PowerUpType type) {
        PowerUp powerUp = activePowerUps.get(type);
        return powerUp != null ? powerUp.getDuration() : 0;
    }

    /**
     * Get all active PowerUp types
     */
    public Set<PowerUpType> getActivePowerUpType() {
        return new HashSet<>(activePowerUps.keySet());
    }

    /**
     * Get number of dropping PowerUps
     */
    public int getDroppingPowerUpCount() {
        return droppingPowerUps.size();
    }

    /**
     * Get number of active PowerUps
     */
    public int getActivePowerUpCount() {
        return activePowerUps.size();
    }

    //Event callbacks <can be connected to Observer pattern later)
    private void onPowerUpCollected(PowerUp powerUp) {
        System.out.println("PowerUp collected: " + powerUp.getType().getDisplayName());
    }

    private void onPowerUpExpired(PowerUp powerUp) {
        System.out.println("PowerUp expired: " + powerUp.getType().getDisplayName());
    }

    /**
     * Get list of currently dropping PowerUps
     */
    public List<PowerUp> getActivePowerUps() {
        return droppingPowerUps;
    }
}

