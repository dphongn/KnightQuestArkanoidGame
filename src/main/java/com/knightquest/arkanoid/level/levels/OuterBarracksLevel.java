package com.knightquest.arkanoid.level.levels;

import com.knightquest.arkanoid.level.BaseLevel;

import java.util.ArrayList;

import com.knightquest.arkanoid.model.brick.*;

import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;

public class OuterBarracksLevel extends BaseLevel {
    public OuterBarracksLevel() {
        super(4, "Outer Barracks", "Barracks filled with soldiers and explosive barrels. Create chain reactions to clear your path!", new ArrayList<>(), false, 4);
        buildLevel();
    }

    @Override
    protected  void buildLevel() {
        int rows = 7;
        int cols = 11;
        double startX = 75;
        double startY = 50;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = startX + col * (BRICK_WIDTH + 4);
                double y = startY + row * (BRICK_HEIGHT + 3);
                if ((row == 2 || row == 5) && col % 3 == 1) {
                    bricks.add(new ExplosiveBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else if (row == 0 || row == rows - 1) {
                    bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else if ((row + col) % 4 == 0) {
                    bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else if ((row + col) % 7 == 0) {
                    bricks.add(new PrisonerBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else {
                    bricks.add(new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                }
            }
        }
    }

    public static OuterBarracksLevel create() {
        return new OuterBarracksLevel();
    }
}
