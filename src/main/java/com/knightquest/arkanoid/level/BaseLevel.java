package com.knightquest.arkanoid.level;

import java.util.List;

import com.knightquest.arkanoid.model.brick.Brick;

public abstract class BaseLevel extends Level {
    protected int difficulty;

    public BaseLevel(int levelNumber, String levelName, String description, List<Brick> bricks, boolean isBossLevel, int difficulty) {
        super(levelNumber, levelName, description, bricks, isBossLevel);
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    protected abstract void buildLevel();
}
