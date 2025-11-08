
package com.knightquest.arkanoid.level.levels;

import java.util.ArrayList;

import com.knightquest.arkanoid.level.BaseLevel;
import com.knightquest.arkanoid.model.brick.*;
import com.knightquest.arkanoid.model.powerup.PowerUpType;
import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;

/**
 * Level 3: Bazaar of Chaos - Rescue Mission
 * Introduces: PRISONER bricks (guaranteed power-ups!)
 * Theme: Marketplace with captured allies
 * Power-ups: Fire Ball and Pierce Ball available for testing
 */
public class BazaarOfChaosLevel extends BaseLevel {
    public BazaarOfChaosLevel() {
        super(3, "Bazaar of Chaos",
                "Chaotic marketplace where allies are held captive. Free them to gain their aid!",
                new ArrayList<>(), 3);
        buildLevel();
    }

    @Override
    protected void buildLevel() {
        double startX = 30;
        double startY = 70;

        int[][] layout = {
                {1,1,1,3,1,1,1,3,1,1,1,3},
                {3,1,3,0,3,1,3,0,3,1,3,0},
                {0,1,0,0,0,1,0,0,0,1,0,0},
                {0,1,0,0,0,1,0,0,0,1,0,0},
                {0,1,0,0,0,1,0,0,0,1,0,0},
                {0,1,0,0,0,1,0,0,0,1,0,0},
                {1,2,1,0,1,2,1,0,1,2,1,0},
                {2,3,2,1,2,3,2,1,2,3,2,1},
                {1,2,1,0,1,2,1,0,1,2,1,0},
                {0,0,0,0,0,0,0,0,0,0,0,0},
                {2,2,2,2,2,2,2,2,2,2,2,2}
        };

        PowerUpType[] powerUps = {
                PowerUpType.FIRE_BALL,
                PowerUpType.PIERCE_BALL,
                PowerUpType.MULTI_BALL
        };
        int powerUpIndex = 0;
        int powerUpBrickCount = 0;

        for (int row = 0; row < layout.length; row++) {
            for (int col = 0; col < layout[row].length; col++) {
                int type = layout[row][col];
                if (type == 0) continue; // ô trống, bỏ qua

                double x = startX + col * (BRICK_WIDTH + 2);
                double y = startY + row * (BRICK_HEIGHT + 3);
                Brick brick = null;

                switch (type) {
                    case 1:
                        brick = new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                    case 2:
                        brick = new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                    case 3:
                        brick = new PrisonerBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                    /*case 4:
                        brick = new ExplosiveBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                    case 5:
                        brick = new MonsterBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                    case 6:
                        brick = new UnbreakableBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                        break;

                     */
                }

                if (brick != null) {
                    bricks.add(brick);
                }
            }
        }

        System.out.println("✨ Level 3: " + powerUpBrickCount +
                " PrisonerBricks with guaranteed power-ups created!");
    }


    public static BazaarOfChaosLevel create() {
        return new BazaarOfChaosLevel();
    }
}
