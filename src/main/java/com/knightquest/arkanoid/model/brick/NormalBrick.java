package com.knightquest.arkanoid.model.brick;

import javafx.scene.paint.Color;

public class NormalBrick extends Brick {
    public NormalBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 1);
        this.color = Color.GREEN;
        this.type = BrickType.NORMAL;
    }
}
