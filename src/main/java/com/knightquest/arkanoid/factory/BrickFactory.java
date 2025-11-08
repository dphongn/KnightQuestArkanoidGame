package com.knightquest.arkanoid.factory;

import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.BrickType;
import com.knightquest.arkanoid.model.brick.ExplosiveBrick;
import com.knightquest.arkanoid.model.brick.MonsterBrick;
import com.knightquest.arkanoid.model.brick.NormalBrick;
import com.knightquest.arkanoid.model.brick.PrisonerBrick;
import com.knightquest.arkanoid.model.brick.UnbreakableBrick;
import com.knightquest.arkanoid.model.brick.StrongBrick;


public class BrickFactory {

    /**
     * Create a brick based on type.
     */
    public static Brick createBrick(BrickType type, double x, double y, double width, double height) {
        return switch (type) {
            case NORMAL -> new NormalBrick(x, y, width, height);
            case UNBREAKABLE -> new UnbreakableBrick(x, y, width, height);
            case EXPLOSIVE -> new ExplosiveBrick(x, y, width, height);
            case PRISONER -> new PrisonerBrick(x, y, width, height);
            case STRONG -> new StrongBrick(x, y, width, height);
            default -> null;
        };
    }

    /**
     * Create a monster brick with movement boundaries.
     */
    public static Brick createMonsterBrick(double x, double y, double width, double height, double minX, double maxX) {
        return new MonsterBrick(x, y, width, height, minX, maxX);
    }

    /**
     * Create a brick from int code.
     */
    public static Brick createBrickFromCode(int code, double x, double y, double width, double height) {
        return switch (code) {
            case 1 -> createBrick(BrickType.NORMAL, x, y, width, height);
            case 6 -> createBrick(BrickType.UNBREAKABLE, x, y, width, height);
            case 4 -> createBrick(BrickType.EXPLOSIVE, x, y, width, height);
            case 3 -> createBrick(BrickType.PRISONER, x, y, width, height);
            case 2 -> createBrick(BrickType.STRONG, x, y, width, height);
            default -> null;
        };
    }

    public static Brick createMonsterBrickFromCode(int code, double x, double y, double width, double height, double minX, double maxX) {
        if (code == 5) {
            return createMonsterBrick(x, y, width, height, minX, maxX);
        }
        return null;
    }

    /**
     * Get BrickType from int code.
     */
    public static BrickType getBrickTypeFromCode(int code) {
        return switch (code) {
            case 'N' -> BrickType.NORMAL;
            case 'U' -> BrickType.UNBREAKABLE;
            case 'E' -> BrickType.EXPLOSIVE;
            case 'P' -> BrickType.PRISONER;
            case 'S' -> BrickType.STRONG;
            case 'M' -> BrickType.MONSTER;
            default -> null;
        };
    }
}
