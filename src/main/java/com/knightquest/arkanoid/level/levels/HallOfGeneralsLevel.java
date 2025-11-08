package com.knightquest.arkanoid.level.levels;

import java.util.ArrayList;

import com.knightquest.arkanoid.factory.BrickFactory;
import com.knightquest.arkanoid.level.BaseLevel;
import com.knightquest.arkanoid.model.brick.*;
import com.knightquest.arkanoid.model.powerup.PowerUpType;

import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;

public class HallOfGeneralsLevel extends BaseLevel {
    public HallOfGeneralsLevel() {
        super(5, "Hall of Generals",
                "The generals' hall guards it with fierce creatures. Moving enemies will counter-attack!",
                new ArrayList<>(), 5);
        buildLevel();
    }

    @Override
    protected void buildLevel() {
        int[][] map = {
                {1,0,5,0,0,0,0,0,0,0,0,0},
                {1,3,0,1,0,5,0,0,0,0,0,0},
                {1,4,3,1,3,0,1,0,5,0,0,0},
                {1,0,4,1,4,3,1,3,0,1,0,0},
                {1,0,0,1,0,4,1,4,3,1,3,0},
                {1,3,0,1,0,0,1,0,4,1,4,0},
                {1,4,3,1,3,0,1,0,0,1,0,0},
                {1,0,4,1,4,3,1,3,0,1,0,0},
                {1,0,0,1,0,4,1,4,3,1,3,0},
                {1,0,0,1,0,0,1,0,4,1,4,0},
                {2,2,2,2,2,2,2,2,2,2,2,1}
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

                if (type == 5) {
                    double minX = Math.max(0, startX + (col - 2) * (BRICK_WIDTH + 3));
                    double maxX = Math.min(screenWidth - BRICK_WIDTH, startX + (col + 2) * (BRICK_WIDTH + 3));
                    brick = BrickFactory.createMonsterBrickFromCode(5, x, y, BRICK_WIDTH, BRICK_HEIGHT, minX, maxX);
                } else {
                    brick = BrickFactory.createBrickFromCode(type, x, y, BRICK_WIDTH, BRICK_HEIGHT);
                }
                if (brick != null) {
                    bricks.add(brick);
                }
            }
        }
        System.out.println("✨ Level 5: " + powerUpBrickCount +
                " PrisonerBricks with guaranteed power-ups created!");
    }

    public static HallOfGeneralsLevel create() {
        return new HallOfGeneralsLevel();
    }
}
