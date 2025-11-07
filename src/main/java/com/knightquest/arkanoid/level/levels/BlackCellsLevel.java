package com.knightquest.arkanoid.level.levels;

import java.util.ArrayList;
import com.knightquest.arkanoid.level.BaseLevel;
import com.knightquest.arkanoid.model.brick.NormalBrick;
import com.knightquest.arkanoid.model.brick.StrongBrick;
import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;

public class BlackCellsLevel extends BaseLevel {
    public BlackCellsLevel() {
        super(2, "The Black Cells",
                "Dark prison cells with reinforced walls. Enemy soldiers are better armored here.",
                new ArrayList<>(),  2);
        buildLevel();
    }

    @Override
    protected void buildLevel() {
        int[][] map = {
                {0,1,0,1,0,0,0,1,1,1,1,0},
                {1,1,0,1,0,1,1,1,1,1,1,0},
                {1,1,1,1,1,1,1,2,1,2,1,0},
                {2,1,1,1,1,2,2,0,1,0,1,1},
                {0,1,2,1,2,0,0,0,1,0,1,1},
                {0,1,0,1,0,0,0,0,1,1,1,2},
                {0,1,1,1,0,0,1,1,1,1,1,0},
                {1,1,1,1,1,1,1,1,1,2,1,1},
                {1,1,2,1,1,1,2,2,1,0,1,1},
                {2,1,0,1,2,2,0,0,1,0,1,2},
                {0,1,0,1,0,0,0,0,1,0,1,0}
        };

        double startX = 25;
        double startY = 70;
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                int type = map[row][col];
                if (type == 0) continue;
                double x = startX + col * (BRICK_WIDTH + 3);
                double y = startY + row * (BRICK_HEIGHT + 4);
                if (type == 1) {
                    bricks.add(new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else if (type == 2) {
                    bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                }
            }
        }
    }

    public static BlackCellsLevel create() {
        return new BlackCellsLevel();
    }
}
