package com.knightquest.arkanoid.level;

import java.util.List;

import com.knightquest.arkanoid.model.Boss;
import com.knightquest.arkanoid.model.brick.Brick;

public abstract class BaseLevel extends Level {
    protected int difficulty;
    protected Boss boss;

    public BaseLevel(int levelNumber, String levelName, String description, List<Brick> bricks, int difficulty) {
        super(levelNumber, levelName, description, bricks );
        this.difficulty = difficulty;
        this.boss = null;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Boss getBoss() {
        return this.boss;
    }

    // Mỗi level sẽ implement phương thức này để khởi tạo bricks
    protected abstract void buildLevel();
}
