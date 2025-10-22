package com.knightquest.arkanoid.level.levels;

import com.knightquest.arkanoid.level.BaseLevel;
import com.knightquest.arkanoid.model.brick.NormalBrick;
import com.knightquest.arkanoid.model.brick.PrisonerBrick;
import com.knightquest.arkanoid.model.brick.StrongBrick;

import java.util.ArrayList;

import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;

public class BazaarOfChaosLevel extends BaseLevel {
    public BazaarOfChaosLevel() {
        super(3, "Bazaar of Chaos", "Chaotic marketplace where allies are held captive. Free them to gain their aid!", new ArrayList<>(), false, 3);
    }

    @Override
    protected void buildLevel() {
        int rows = 7;
        int cols = 12;
        double startX = 55;
        double startY = 60;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = startX + col * (BRICK_WIDTH + 2);
                double y = startY + row * (BRICK_HEIGHT + 3);
                if (row == 2 || row == 4) {
                    if (col % 4 == 1) {
                        bricks.add(new PrisonerBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                    } else if (col % 4 == 0 || col % 4 == 2) {
                        bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                    } else {
                        bricks.add(new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                    }
                } else {
                    if ((row + col) % 3 == 0) {
                        bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                    } else {
                        bricks.add(new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                    }
                }
            }
        }
    }

    public static BazaarOfChaosLevel create() {
        return new BazaarOfChaosLevel();
    }
}
