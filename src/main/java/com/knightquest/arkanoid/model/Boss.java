package com.knightquest.arkanoid.model;

import static com.knightquest.arkanoid.util.Constants.SCREEN_WIDTH;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Boss extends MovableObject {

    public interface BossStateListener {
        void onBossEnraged();
    }

    private int health;
    private double maxHealth;
    private double invulnerabilityTimer;
    private boolean isEnraged = false;
    private double baseSpeed;
    private double spawnTimer;
    private double prisonerSpawnTimer;
    private Image normalImage;
    private Image enragedImage;
    private BossStateListener stateListener;

    public Boss(double x, double y, double size, int initialHealth, double speed) {
        super(x, y, size, size);
        this.health = initialHealth;
        this.maxHealth = initialHealth;
        setVelocity(speed, 0);
        this.invulnerabilityTimer = 0;
        this.baseSpeed = speed;
        this.spawnTimer = 5.0;
        this.prisonerSpawnTimer = 7.0;
        try {
            normalImage = new Image(getClass().getResourceAsStream("/images/sprites/bot/skull-laugh.gif"));
            if (normalImage.isError()) throw new Exception("Lỗi tải ảnh skull-laugh");
        } catch (Exception e) {
            System.err.println("Không tải được ảnh 'skull-laugh.gif'. Sẽ dùng hình vuông màu.");
            normalImage = null;
        }
        try {
            enragedImage = new Image(getClass().getResourceAsStream("/images/sprites/bot/skull-angry.gif"));
            if (enragedImage.isError()) throw new Exception("Lỗi tải ảnh skull-angry");
        } catch (Exception e) {
            System.err.println("Không tải được ảnh 'skull-angry.gif'. Sẽ dùng hình vuông màu.");
            enragedImage = null;
        }
    }

    public void setBossStateListener(BossStateListener listener) {
        this.stateListener = listener;
    }

    public void reverseDirection() {
        setDx(-getDx());
    }

    public void update(double deltaTime) {
        super.move(deltaTime);

        if (x <= 0) {
            x = 0;
            reverseDirection();
        }

        if (x + width >= SCREEN_WIDTH) {
            x = SCREEN_WIDTH - width;
            reverseDirection();
        }

        if (invulnerabilityTimer > 0) {
            invulnerabilityTimer -= deltaTime;
        }
    }

    public void render(GraphicsContext gc) {
        boolean shouldDraw = true;
        if (invulnerabilityTimer > 0) {
            if ((int) (invulnerabilityTimer * 20) % 2 != 0) {
                shouldDraw = false;
            }
        }

        if (shouldDraw) {
            Image imageToDraw = isEnraged ? enragedImage : normalImage;

            if (imageToDraw != null && !imageToDraw.isError()) {
                gc.drawImage(imageToDraw, x, y, width, height);
            } else {
                if (isEnraged) {
                    gc.setFill(Color.RED);
                } else {
                    gc.setFill(Color.YELLOW);
                }
                gc.fillRect(x, y, width, height);
            }
        }

        double healthPercentage = health / maxHealth;
        double healthBarWidth = width * healthPercentage;
        double healthBarHeight = 10;
        double healthBarY = y - healthBarHeight - 5;

        gc.setFill(Color.RED);
        gc.fillRect(x, healthBarY, width, healthBarHeight);

        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(x, healthBarY, healthBarWidth, healthBarHeight);
    }

    public void takeHit() {
        if (invulnerabilityTimer > 0) {
            return;
        }

        if (health > 0) {
            health--;
        }

        if (!isEnraged && health <= maxHealth / 2) {
            isEnraged = true;
            setDx(Math.signum(getDx()) * baseSpeed * 1.5);
            System.out.println("BOSS IS ENRAGED!");

            if (stateListener != null) {
                stateListener.onBossEnraged();
            }
        }

        invulnerabilityTimer = 0.2;
    }

    public boolean isDefeated() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }

    public void updateSpawnTimer(double deltaTime) {
        if (spawnTimer > 0) {
            spawnTimer -= deltaTime;
        }
    }

    public boolean isReadyToSpawn() {
        return spawnTimer <= 0;
    }

    public void resetSpawnTimer() {
        if (isEnraged) {
            this.spawnTimer = 2.0;
        } else {
            this.spawnTimer = 5.0;
        }
    }

    public boolean isEnraged() {
        return this.isEnraged;
    }

    public void updatePrisonerSpawnTimer(double deltaTime) {
        if (prisonerSpawnTimer > 0) {
            prisonerSpawnTimer -= deltaTime;
        }
    }

    public boolean isReadyToSpawnPrisoner() {
        return prisonerSpawnTimer <= 0;
    }

    public void resetPrisonerSpawnTimer() {
        this.prisonerSpawnTimer = 10.0;
    }
}