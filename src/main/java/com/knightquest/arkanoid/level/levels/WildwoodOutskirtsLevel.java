package com.knightquest.arkanoid.level.levels;

import java.util.ArrayList;

import com.knightquest.arkanoid.level.BaseLevel;
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
        int[][] layout = {
                {1,1,0,1,1,1,1,0,1,1,1,1},
                {1,1,1,0,1,1,1,1,0,1,1,1},
                {1,0,0,0,1,1,1,1,0,0,1,1},
                {1,0,1,0,0,1,0,0,1,0,1,0},
                {0,0,1,1,0,0,0,1,1,0,0,0},
                {0,1,1,1,1,0,1,1,1,1,0,0},
                {0,0,1,1,0,1,1,1,1,0,0,1},
                {1,0,0,1,0,0,0,1,0,0,1,1},
                {1,1,0,0,0,1,0,0,0,1,1,1},
                {1,1,1,1,1,1,0,1,1,1,1,1},
                {0,1,1,1,0,0,1,1,1,0,0,1}
        };

        double startX = 30;
        double startY = 70;

        for (int row = 0; row < layout.length; row++) {
            for (int col = 0; col < layout[row].length; col++) {
                if (layout[row][col] == 0) continue; // bỏ qua ô trống

                double x = startX + col * (BRICK_WIDTH + 2);
                double y = startY + row * (BRICK_HEIGHT + 3);

                bricks.add(new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
            }
        }

        System.out.println("🌲 Level 1: " + bricks.size() + " NormalBricks created for training.");
    }

    public static WildwoodOutskirtsLevel create() {
        return new WildwoodOutskirtsLevel();
    }
}
