package com.knightquest.arkanoid.level.levels;

import com.knightquest.arkanoid.level.BaseLevel;
import com.knightquest.arkanoid.model.brick.*;

import java.util.ArrayList;

import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;

public class HallOfGeneralsLevel extends BaseLevel {
    public HallOfGeneralsLevel() {
        super(5, "Hall of Generals",
                "The generals' hall guards it with fierce creatures. Moving enemies will counter-attack!", new ArrayList<>(), false, 5);
        buildLevel();
    }

    @Override
    protected void buildLevel() {
        int rows = 8;
        int cols = 11;
        double startX = 75;
        double startY = 40;
        double screenWidth = 800;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = startX * col * (BRICK_WIDTH + 3);
                double y = startY * row * (BRICK_HEIGHT + 3);
                if (row == 1 || row == 4 || row == 6) {
                    if (col % 4 == 0 || col % 4 == 3) {
                        double minX = Math.max(0, startX + (col - 2) + (BRICK_WIDTH + 3));
                        double maxX = Math.min(screenWidth - BRICK_WIDTH, startX + (col - 2) + (BRICK_WIDTH + 3));
                        bricks.add(new MonsterBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, minX, maxX));
                    } else {
                        bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                    }
                } else if ((row + col) % 5 == 0) {
                    bricks.add(new ExplosiveBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else if ((row + col) % 6 == 0) {
                    bricks.add(new PrisonerBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else if (row % 2 == 0) {
                    bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else {
                    bricks.add(new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                }
            }
        }
    }

    public static HallOfGeneralsLevel create() {
        return new HallOfGeneralsLevel();
    }
}
