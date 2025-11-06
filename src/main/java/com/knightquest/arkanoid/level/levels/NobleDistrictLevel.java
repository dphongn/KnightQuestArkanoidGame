package com.knightquest.arkanoid.level.levels;

import com.knightquest.arkanoid.level.BaseLevel;
import com.knightquest.arkanoid.model.brick.ExplosiveBrick;
import com.knightquest.arkanoid.model.brick.MonsterBrick;
import com.knightquest.arkanoid.model.brick.NormalBrick;
import com.knightquest.arkanoid.model.brick.PrisonerBrick;
import com.knightquest.arkanoid.model.brick.StrongBrick;
import com.knightquest.arkanoid.model.brick.UnbreakableBrick;

import java.util.ArrayList;

import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;

public class NobleDistrictLevel extends BaseLevel {
    public NobleDistrictLevel() {
        super(6, "Noble District", "The nobles' luxurious fortress. Steel walls, armed guards, and deadly traps await!", new ArrayList<>(), false, 6);
        buildLevel();
    }

    @Override
    protected void buildLevel() {
        int rows = 8;
        int cols = 12;
        double startX = 55;
        double startY = 45;
        double screenWidth = 800;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = startX + col * (BRICK_WIDTH + 2);
                double y = startY + row * (BRICK_HEIGHT + 3);
                if ((row == 0 || row == rows-1) && col % 2 == 0) {
                    bricks.add(new UnbreakableBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else if (col % 4 == 0 && row % 2 == 1) {
                    bricks.add(new UnbreakableBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else if (row == 2 || row == 5) {
                    if (col % 3 == 1) {
                        double minX = Math.max(0, startX + (col - 2) * (BRICK_WIDTH + 3));
                        double maxX = Math.min(screenWidth - BRICK_WIDTH, startX + (col + 2) * (BRICK_WIDTH + 3));
                        bricks.add(new MonsterBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, minX, maxX));
                    } else {
                        bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                    }
                } else if ((row + col) % 7 == 0) {
                    bricks.add(new ExplosiveBrick(x, y ,BRICK_WIDTH, BRICK_HEIGHT));
                } else if ((row + col) % 8 == 0) {
                    bricks.add(new PrisonerBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else if ((row + col) % 3 == 0) {
                    bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else {
                    bricks.add(new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                }
            }
        }
    }

    public  static NobleDistrictLevel create() {
        return new NobleDistrictLevel();
    }
}
