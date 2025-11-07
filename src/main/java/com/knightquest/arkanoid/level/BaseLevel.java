package com.knightquest.arkanoid.level;

import java.util.List;

import com.knightquest.arkanoid.model.brick.Brick;

public abstract class BaseLevel extends Level {
    protected int difficulty;

    public BaseLevel(int levelNumber, String levelName, String description, List<Brick> bricks, int difficulty) {
        super(levelNumber, levelName, description, bricks );
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    // Mỗi level sẽ implement phương thức này để khởi tạo bricks
    protected abstract void buildLevel();
}
