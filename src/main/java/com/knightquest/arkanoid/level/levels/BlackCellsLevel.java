package com.knightquest.arkanoid.level.levels;

import java.util.ArrayList;

import com.knightquest.arkanoid.factory.BrickFactory;
import com.knightquest.arkanoid.level.BaseLevel;
import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.NormalBrick;
import com.knightquest.arkanoid.model.brick.StrongBrick;

import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;

public class BlackCellsLevel extends BaseLevel {
    public BlackCellsLevel() {
        super(2, "The Black Cells",
                "Dark prison cells with reinforced walls. Enemy soldiers are better armored here.",
                new ArrayList<>(), 2);
        buildLevel();
    }

    @Override
    protected void buildLevel() {
        int[][] map = {
                {0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0},
                {1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 0},
                {2, 1, 1, 1, 1, 2, 2, 0, 1, 0, 1, 1},
                {0, 1, 2, 1, 2, 0, 0, 0, 1, 0, 1, 1},
                {0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 2},
                {0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1},
                {1, 1, 2, 1, 1, 1, 2, 2, 1, 0, 1, 1},
                {2, 1, 0, 1, 2, 2, 0, 0, 1, 0, 1, 2},
                {0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0}
        };

        double startX = 25;
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
    }

    public static BlackCellsLevel create() {
        return new BlackCellsLevel();
    }
}
