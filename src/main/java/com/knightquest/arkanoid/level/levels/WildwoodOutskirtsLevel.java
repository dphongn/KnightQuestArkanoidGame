package com.knightquest.arkanoid.level.levels;

import java.util.ArrayList;

import com.knightquest.arkanoid.factory.BrickFactory;
import com.knightquest.arkanoid.level.BaseLevel;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.NormalBrick;

import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;


public class WildwoodOutskirtsLevel extends BaseLevel {
    public WildwoodOutskirtsLevel() {
        super(1, "The Wildwood Outskirts",
                "The forest entrance. Wild beasts lurk but the path is clear. Master your basic skills here.",
                new ArrayList<>(), 1);
        buildLevel();
    }

    @Override
    protected void buildLevel() {
        int[][] map = {
                {1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1},
                {1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1},
                {1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0},
                {0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0},
                {0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1},
                {1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1}
        };

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

        System.out.println("🌲 Level 1: " + bricks.size() + " NormalBricks created for training.");
    }

    public static WildwoodOutskirtsLevel create() {
        return new WildwoodOutskirtsLevel();
    }
}
