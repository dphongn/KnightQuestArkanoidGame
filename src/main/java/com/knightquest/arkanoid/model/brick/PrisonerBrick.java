package com.knightquest.arkanoid.model.brick;

import com.knightquest.arkanoid.model.powerup.PowerUp;
import com.knightquest.arkanoid.model.powerup.PowerUpType;
import javafx.scene.paint.Color;

public class PrisonerBrick extends Brick {
    private PowerUpType guaranteedPowerUp;

    public PrisonerBrick(double x, double y,
                         double width, double height,
                         int hitPoints, PowerUpType guaranteedPowerUp) {
        super(x, y, width, height, hitPoints);
        this.guaranteedPowerUp = guaranteedPowerUp;
        this.color = Color.WHITE;
        this.type = BrickType.PRISONER; 
    }

    @Override
    public void takeHit() {
        super.takeHit();
        if (isDestroyed()) {
            onDestroyed();
        }
    }

    @Override
    public PowerUp onDestroyed() {
        System.out.println("PrisonerBrick released PowerUp: " + guaranteedPowerUp);
        return null;
    }

    public PowerUpType getGuaranteedPowerUp() {
        return guaranteedPowerUp;
    }

    public void setGuaranteedPowerUp(PowerUpType guaranteedPowerUp) {
        this.guaranteedPowerUp = guaranteedPowerUp;
    }
}
