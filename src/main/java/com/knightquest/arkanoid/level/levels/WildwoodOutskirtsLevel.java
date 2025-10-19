
package com.knightquest.arkanoid.level.levels;

import java.util.ArrayList;

import com.knightquest.arkanoid.level.BaseLevel;
import com.knightquest.arkanoid.model.brick.NormalBrick;
import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;

/**
 * Level 1: The Wildwood Outskirts - Tutorial Level
 */
public class WildwoodOutskirtsLevel extends BaseLevel {
    public WildwoodOutskirtsLevel() {
        super(1, "The Wildwood Outskirts",
                "The forest entrance. Wild beasts lurk but the path is clear. Master your basic skills here.",
                new ArrayList<>(), false, 1);
        buildLevel();
    }

    @Override
    protected void buildLevel() {
        int rows = 5;
        int cols = 10;
        double startX = 100;
        double startY = 80;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = startX + col * (BRICK_WIDTH + 5);
                double y = startY + row * (BRICK_HEIGHT + 5);
                if (row == 2 && col % 3 == 1) {
                    continue;
                }
                bricks.add(new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
            }
        }
    }

    public static WildwoodOutskirtsLevel create() {
        return new WildwoodOutskirtsLevel();
    }
}
