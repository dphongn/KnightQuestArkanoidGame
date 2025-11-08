package com.knightquest.arkanoid.level.levels;

import java.util.ArrayList;

import com.knightquest.arkanoid.level.BaseLevel;
import com.knightquest.arkanoid.model.brick.*;
import com.knightquest.arkanoid.model.powerup.PowerUpType;

import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;

public class NobleDistrictLevel extends BaseLevel {
    public NobleDistrictLevel() {
        super(6, "Noble District",
                "The nobles' luxurious fortress. Steel walls, armed guards, and deadly traps await!",
                new ArrayList<>(), 6);
        buildLevel();
    }

    @Override
    protected void buildLevel() {
        int[][] map = {
                {0,4,1,1,6,6,6,6,1,1,4,0},
                {0,1,1,6,3,1,1,3,6,1,1,0},
                {4,1,6,3,1,4,4,1,3,6,1,4},
                {1,1,3,0,3,1,1,3,0,3,1,1},
                {1,1,0,0,0,3,3,0,0,0,1,1},
                {1,6,0,0,0,0,0,0,0,0,6,1},
                {1,0,0,5,4,0,0,4,5,0,0,1},
                {1,0,0,0,1,0,0,1,0,0,0,1},
                {1,5,0,0,1,0,0,1,0,0,5,1},
                {1,0,0,2,1,2,2,1,2,0,0,1},
                {6,0,2,2,2,2,2,2,2,2,0,6}
        };

        PowerUpType[] powerUps = {
                PowerUpType.FIRE_BALL,
                PowerUpType.PIERCE_BALL,
                PowerUpType.MULTI_BALL
        };
        int powerUpIndex = 0;
        int powerUpBrickCount = 0;

        double startX = 30;
        double startY = 70;
        double screenWidth = 800;

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                int type = map[row][col];
                if (type == 0) continue;

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
                    case 4:
                        brick = new ExplosiveBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                    case 5 :
                        double minX = Math.max(0, startX + (col - 2) * (BRICK_WIDTH + 3));
                        double maxX = Math.min(screenWidth - BRICK_WIDTH, startX + (col + 2) * (BRICK_WIDTH + 3));
                        bricks.add(new MonsterBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, minX, maxX));
                        break;
                    case 6:
                        brick = new UnbreakableBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                        break;

                }
                if (brick != null) {
                    bricks.add(brick);
                }
            }
        }
        System.out.println("✨ Level 6: " + powerUpBrickCount +
                " PrisonerBricks with guaranteed power-ups created!");
    }

    public static NobleDistrictLevel create() {
        return new NobleDistrictLevel();
    }
}
