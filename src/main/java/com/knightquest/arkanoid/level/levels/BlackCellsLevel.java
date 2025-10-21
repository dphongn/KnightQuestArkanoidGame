package com.knightquest.arkanoid.level.levels;

import com.knightquest.arkanoid.level.BaseLevel;
import com.knightquest.arkanoid.model.brick.NormalBrick;
import com.knightquest.arkanoid.model.brick.StrongBrick;
import static com.knightquest.arkanoid.util.Constants.BRICK_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.BRICK_WIDTH;


import java.util.ArrayList;

public class BlackCellsLevel extends BaseLevel {
    public BlackCellsLevel() {
        super(2, "The Black Cells","Dark prison cells with reinforced walls. Enemy soldiers are better armored here.", new ArrayList<>(), false, null, 2);
        buildLevel();
    }
    @Override
    protected void buildLevel() {
        int rows = 6;
        int cols = 11;
        double startX = 75;
        double startY = 70;
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                double x = startX + col * (BRICK_WIDTH + 3)
            }
        }
    }
}
