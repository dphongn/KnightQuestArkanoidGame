package com.knightquest.arkanoid.level.levels;

import java.util.ArrayList;
import com.knightquest.arkanoid.level.BaseLevel;
import com.knightquest.arkanoid.model.brick.*;
import com.knightquest.arkanoid.model.powerup.PowerUpType;

import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;

public class OuterBarracksLevel extends BaseLevel {
    public OuterBarracksLevel() {
        super(4, "Outer Barracks",
                "Barracks filled with soldiers and explosive barrels. Create chain reactions to clear your path!",
                new ArrayList<>(),  4);
        buildLevel();
    }

    @Override
    protected void buildLevel() {
        int[][] map = {
                {1,0,1,0,1,0,0,1,0,1,0,1},
                {1,1,1,1,1,0,0,1,1,1,1,1},
                {3,1,3,1,3,0,0,3,1,3,1,3},
                {0,1,4,1,0,0,0,0,1,4,1,0},
                {0,1,1,1,0,0,0,0,1,1,1,0},
                {0,2,2,2,0,0,0,0,2,2,2,0},
                {2,2,3,2,2,0,0,2,2,3,2,2},
                {4,3,0,3,4,2,2,4,3,0,3,4},
                {2,0,0,0,2,3,3,2,0,0,0,2},
                {2,0,0,0,2,0,0,2,0,0,0,2},
                {2,2,1,2,2,2,2,2,2,1,2,2}
        };

        double startX = 20;
        double startY = 70;
        PowerUpType[] powerUps = {
                PowerUpType.FIRE_BALL,
                PowerUpType.PIERCE_BALL,
                PowerUpType.MULTI_BALL
        };
        int powerUpIndex = 0;
        int powerUpBrickCount = 0;

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                int type = map[row][col];
                if (type == 0) continue;
                double x = startX + col * (BRICK_WIDTH + 4);
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
                        // Prisoner luôn rơi power-up
                        brick.setPowerUpDrop(powerUps[powerUpIndex % powerUps.length]);
                        brick.setDropChance(1.0);
                        powerUpIndex++;
                        powerUpBrickCount++;
                        break;
                    case 4:
                        brick = new ExplosiveBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                }
                if (brick != null) {
                    bricks.add(brick);
                }
            }
        }
        System.out.println("✨ Level 4: " + powerUpBrickCount +
                " PrisonerBricks with guaranteed power-ups created!");
    }

    public static OuterBarracksLevel create() {
        return new OuterBarracksLevel();
    }
}
