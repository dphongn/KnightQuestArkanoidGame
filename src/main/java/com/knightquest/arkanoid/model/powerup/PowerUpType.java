package com.knightquest.arkanoid.model.powerup;

public enum PowerUpType {
    EXPAND_PADDLE("Expand Paddle", 10.0, "Increase paddle width"),
    FAST_BALL("Fast Ball", 8.0, "Increase ball speed"),
    SLOW_BALL("Slow Ball", 8.0, "Decrease ball speed"),
    MULTI_BALL("Multi Ball", 0.0, "Spaw s additional balls"),
    PIERCE_BALL("Pierce Ball", 12.0, "Ball pierces through bricks"),
    FIRE_BALL("Fire Ball", 10.0, "Ball destroys bricks in one hit"),
    LASER_PADDLE("Laser Paddle", 15.0, "Enables laser shooting"),
    MAGNET_PADDLE("Magnet Paddle", 10.0, "Ball sticks to paddle");

    private final String displayName;
    private final double defaultDuration;
    private final String description;

    PowerUpType(String displayName, double defaultDuration, String description) {
        this.displayName = displayName;
        this.defaultDuration = defaultDuration;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDefaultDuration() {
        return defaultDuration;
    }

    public String getDescription() {
        return description;
    }
}
