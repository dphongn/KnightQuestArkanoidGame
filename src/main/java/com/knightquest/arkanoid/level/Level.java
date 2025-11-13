package com.knightquest.arkanoid.level;

import java.util.ArrayList;
import java.util.List;

import com.knightquest.arkanoid.model.Boss;
import com.knightquest.arkanoid.model.brick.Brick;

public class Level {
    private int levelNumber;
    private String levelName;
    private String description;
    protected List<Brick> bricks;
    private boolean completed;

    public Level(int levelNumber, String levelName, List<Brick> bricks) {
        this(levelNumber, levelName, "", bricks);
    }

    public Level(int levelNumber, String levelName, String description,
                 List<Brick> bricks) {
        this.levelNumber = levelNumber;
        this.levelName = levelName;
        this.description = description;
        this.bricks = new ArrayList<>(bricks);
        this.completed = false;
    }

    public boolean isCompleted() {
        for (Brick brick : bricks) {
            if (brick.isActive() && brick.getClass().getSimpleName().equals("UnbreakableBrick") == false) {
                return false;
            }
        }
        return true;
    }

    public List<Brick> getActiveBricks() {
        List<Brick> active = new ArrayList<>();
        for (Brick brick : bricks) {
            if (brick.isActive()) {
                active.add(brick);
            }
        }
        return active;
    }

    // Getters
    public int getLevelNumber() {
        return levelNumber;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getDescription() {
        return description;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Boss getBoss() {
        return null;
    }

    /**
     * Check if the level has a boss.
     */
    public boolean hasBoss() {
        return getBoss() != null;
    }
}
