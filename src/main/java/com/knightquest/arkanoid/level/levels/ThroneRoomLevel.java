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

public class ThroneRoomLevel extends BaseLevel {
    public ThroneRoomLevel() {
        super(7, "Throne of the Dark King - BOSS BATTLE", "The Dark King's throne room. He will fight you directly! Destroy his defenses and save the princess!", new ArrayList<>(), true, 7);
        buildLevel();
    }

    @Override
    protected void buildLevel() {
        int rows = 10;
        int cols = 13;
        double startX = 45;
        double startY = 100;
        double screenWidth = 800;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = startX + col * (BRICK_WIDTH + 1);
                double y = startY + row * (BRICK_HEIGHT + 2);
                int centerCol = col / 2;
                int distFromCenter = Math.abs(col - centerCol);
                if ((row == 0 || row == rows -1 || col == 0 || col == cols - 1) && (col + row) % 3 != 0) {
                    bricks.add(new UnbreakableBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else if (row >= 3 && row <= 6 && distFromCenter <= 2) {
                    if (row == 4 && col == centerCol) {
                        bricks.add(new ExplosiveBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                    } else if (distFromCenter == 2) {
                        double minX = Math.max(0, x - 30);
                        double maxX = Math.min(screenWidth - BRICK_WIDTH, x + 30);
                        bricks.add(new MonsterBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, minX, maxX));
                    } else {
                        bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                    }
                } else if ((row + col) % 5 == 0) {
                    bricks.add(new ExplosiveBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else if ((row + col) % 6 == 0) {
                    bricks.add(new PrisonerBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else if ((row + col) % 3 == 0) {
                    bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else if ((row + col) % 4 != 2) {
                    bricks.add(new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                }
            }
        }
    }

    public static ThroneRoomLevel create() {
        return new ThroneRoomLevel();
    }
}
