package com.knightquest.arkanoid.factory;

import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.brick.BrickType;
import com.knightquest.arkanoid.model.brick.ExplosiveBrick;
import com.knightquest.arkanoid.model.brick.MonsterBrick;
import com.knightquest.arkanoid.model.brick.NormalBrick;
import com.knightquest.arkanoid.model.brick.PrisonerBrick;
import com.knightquest.arkanoid.model.brick.UnbreakableBrick;
import com.knightquest.arkanoid.model.brick.StrongBrick;




public class BrickFactory   {

    /**
     * Create a brick based on type.
     */
    public static Brick createBrick(BrickType type, double x, double y, double width, double height) {
        return switch (type) {
            case NORMAL -> new NormalBrick(x, y, width, height);
            case UNBREAKABLE -> new UnbreakableBrick(x, y, width, height);
            case EXPLOSIVE -> new ExplosiveBrick(x, y, width, height);
            case PRISONER -> new PrisonerBrick(x, y, width, height);
            case STRONG ->  new StrongBrick(x, y, width, height);
            default -> null;
        };
    }

    /**
     * Create a monster brick with movement boundaries.
     */
    public static Brick createMonsterBrick(double x, double y, double width, double height, double minX, double maxX, double minY, double maxY) {
        return new MonsterBrick(x, y, width, height, minX, maxX);
    }

    /**
     * Create a brick from character code.
     */
    public static Brick createBrickFromCode(char code, double x, double y, double width, double height) {
        return switch (code) {
            case 'N' -> createBrick(BrickType.NORMAL, x, y, width, height);
            case 'U' -> createBrick(BrickType.UNBREAKABLE, x, y, width, height);
            case 'E' -> createBrick(BrickType.EXPLOSIVE, x, y, width, height);
            case 'P' -> createBrick(BrickType.PRISONER, x, y, width, height);
            case 'S' -> createBrick(BrickType.STRONG, x, y, width, height);
            default -> null;
        };
    }

    public static Brick createMonsterBrickFromCode(char code, double x, double y, double width, double height, double minX, double maxX, double minY, double maxY) {
        if (code == 'M') {
            return createMonsterBrick(x, y, width, height, minX, maxX, minY, maxY);
        }
        return null;
    }

    /**
     * Get BrickType from character code.
     */
    public static BrickType getBrickTypeFromCode(char code) {
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
