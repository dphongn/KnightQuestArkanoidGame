package com.knightquest.arkanoid.factory;

import com.knightquest.arkanoid.level.Level;
//import com.knightquest.arkanoid.level.levels.BazaarOfChaosLevel;
//import com.knightquest.arkanoid.level.levels.BlackCellsLevel;
//import com.knightquest.arkanoid.level.levels.HallOfGeneralsLevel;
//import com.knightquest.arkanoid.level.levels.NobleDistrictLevel;
//import com.knightquest.arkanoid.level.levels.OuterBarracksLevel;
//import com.knightquest.arkanoid.level.levels.ThroneRoomLevel;
import com.knightquest.arkanoid.level.levels.WildwoodOutskirtsLevel;
import com.knightquest.arkanoid.model.brick.StrongBrick;


/**
 * Factory class to create levels based on level number, name, ....
 */


public class LevelFactory {
    /** Create a level based on level number. */
    public static Level createLevel(int levelNumber) {
        return switch (levelNumber) {
            case 1 -> new WildwoodOutskirtsLevel().create();
//            case 2 -> new BlackCellsLevel().create();
//            case 3 -> new BazaarOfChaosLevel().create();
//            case 4 -> new OuterBarracksLevel().create();
//            case 5 -> new HallOfGeneralsLevel().create();
//            case 6 -> new NobleDistrictLevel().create();
//            case 7 -> new ThroneRoomLevel().create();
            default -> WildwoodOutskirtsLevel.create(); // Default level
        };
    }

    /** Get total number of levels available. */
    public static int getTotalLevels() {
        return 1;
    }

    /** Get level name based on level number. */
    public static String getLevelName(int levelNumber) {
        return switch (levelNumber) {
            case 1 -> "Wildwood Outskirts";
            case 2 -> "The Black Cells";
            case 3 -> "Bazaar of Chaos";
            case 4 -> "Outer Barracks";
            case 5 -> "Hall of Generals";
            case 6 -> "Noble District";
            case 7 -> "Throne Room";
            default -> "Unknown Level";
        };
    }

    /** Check if level is a boss level. */
    public static boolean isBossLevel(int levelNumber) {
        return levelNumber == 7;
    }

    /**
     * Get level difficulty based on level number.
     */
    public static String getLevelDifficulty(int levelNumber) {
        return switch (levelNumber) {
            case 1, 2 -> "Easy";
            case 3, 4 -> "Medium";
            case 5, 6 -> "Hard";
            case 7 -> "Boss";
            default -> "Unknown";
        };
    }
}
