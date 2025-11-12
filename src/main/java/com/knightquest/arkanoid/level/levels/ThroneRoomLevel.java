
package com.knightquest.arkanoid.level.levels;

import java.util.ArrayList;

import com.knightquest.arkanoid.factory.BrickFactory;
import com.knightquest.arkanoid.level.BaseLevel;
import com.knightquest.arkanoid.model.brick.*;
import com.knightquest.arkanoid.model.powerup.PowerUpType;

import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;
import static com.knightquest.arkanoid.util.Constants.SCREEN_WIDTH;

import com.knightquest.arkanoid.model.Boss;

/**
 * Level 7: Throne of the Dark King - BOSS BATTLE
 * Special: AI-CONTROLLED OPPONENT (Aggressive AI)
 * Theme: Final confrontation in the throne room
 * Mechanics:
 * - Player controls bottom paddle (rescue princess)
 * - AI controls top paddle (Dark King defending)
 * - Complex brick fortress pattern
 * - High power-up drop rate to help player
 * Victory condition: Destroy all destructible bricks OR defeat AI
 */
public class ThroneRoomLevel extends BaseLevel {
    public ThroneRoomLevel() {
        super(7, "Throne of the Dark King - BOSS BATTLE",
                "The Dark King's throne room. He will fight you directly! Destroy his defenses and save the princess!",
                new ArrayList<>(), 7);
        buildLevel();
    }

    @Override
    protected void buildLevel() {
        int[][] map = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        PowerUpType[] powerUps = {
                //PowerUpType.FIRE_BALL,
                //PowerUpType.PIERCE_BALL,
                //PowerUpType.MULTI_BALL,
                //PowerUpType.EXPAND_PADDLE,
                PowerUpType.FAST_BALL,
                //PowerUpType.SLOW_BALL,
                PowerUpType.GUN_PADDLE,
                //PowerUpType.MAGNET_PADDLE
        };
        int powerUpIndex = 0;
        int powerUpBrickCount = 0;

        double startX = 60;
        double startY = 40;
        double screenWidth = 800;

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                int type = map[row][col];
                if (type == 0) continue;

                double x = startX + col * (BRICK_WIDTH + 2);
                double y = startY + row * (BRICK_HEIGHT + 3);

                Brick brick = null;

                if (type == 5) {
                    double minX = Math.max(0, startX + (col - 2) * (BRICK_WIDTH + 3));
                    double maxX = Math.min(screenWidth - BRICK_WIDTH, startX + (col + 2) * (BRICK_WIDTH + 3));
                    brick = BrickFactory.createMonsterBrickFromCode(5, x, y, BRICK_WIDTH, BRICK_HEIGHT, minX, maxX);
                } else {
                    brick = BrickFactory.createBrickFromCode(type, x, y, BRICK_WIDTH, BRICK_HEIGHT);
                }
                if (brick instanceof PrisonerBrick) {
                    PowerUpType nextPowerUp = powerUps[powerUpIndex % powerUps.length];
                    ((PrisonerBrick) brick).setGuaranteedPowerUp(nextPowerUp);
                    powerUpIndex++;
                    powerUpBrickCount++;
                }
                if (brick != null) {
                    bricks.add(brick);
                }
            }
        }
        double bossSize = 100;
        double bossX = (SCREEN_WIDTH / 2.0) - (bossSize / 2.0);
        double bossY = 100;
        int bossHealth = 50;
        double bossSpeed = 150;
        this.boss = new Boss(bossX, bossY, bossSize, bossHealth, bossSpeed);

        System.out.println("✨ Level 6: " + powerUpBrickCount +
                " PrisonerBricks with guaranteed power-ups created!");
    }


    public static ThroneRoomLevel create() {
        return new ThroneRoomLevel();
    }
}
