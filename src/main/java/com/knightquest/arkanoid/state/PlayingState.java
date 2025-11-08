package com.knightquest.arkanoid.state;

import java.util.List;

import com.knightquest.arkanoid.controller.GameManager;
import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.model.brick.Brick;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import com.knightquest.arkanoid.observer.AudioController;

import static com.knightquest.arkanoid.util.Constants.*;

/**
 * PlayingState handles the main gameplay.
 */

public class PlayingState extends GameState {
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private Image backgroundImage;
    private Image heartImage;
    private Font arcadeFont;

    public PlayingState(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void enter() {
        System.out.println("=== GAME STARTED ===");
        // Load background image
        int level = gameManager.getCurrentLevelNumber();
        String backgroundPath = "/images/backgrounds/level" + level + ".jpg";
        try {
            backgroundImage = new Image(getClass().getResourceAsStream(backgroundPath));
        } catch (Exception e) {
            backgroundImage = null;
            System.err.println("Không tìm thấy background cho level " + level);
        }
        //Load heart image
        String heartPath = "/images/ui/icons/heart.gif";
        try {
            heartImage = new Image(getClass().getResourceAsStream(heartPath));
        } catch (Exception e) {
            heartImage = null;
            System.err.println("Không tìm thấy heart.gif");
        }
        // Load Arcade Classic font
        try {
            arcadeFont = Font.loadFont(getClass().getResourceAsStream("/fonts/arcadeclassic.ttf"), 28);
            if (arcadeFont == null) {
                arcadeFont = Font.font("Arial", 20);
                System.err.println("Không thể tải arcadeclassic.ttf, dùng Arial thay thế.");
            }
        } catch (Exception e) {
            arcadeFont = Font.font("Arial", 20);
            System.err.println("Không thể tải arcadeclassic.ttf, fallback Arial.");
        }

        //Start background music
        AudioController audioController = gameManager.getEventManager().getAudioController();
        if (audioController != null) {
            audioController.playLevelMusic(gameManager.getCurrentLevelNumber());
        }
    }

    @Override
    public void update(double deltaTime) {
        // Update paddle movement based on key presses
        Paddle paddle = gameManager.getPaddle();
        Ball ball = gameManager.getBall();

        double paddleOldx = paddle.getX();

        if (leftPressed && !rightPressed) {
            paddle.moveLeft();
        } else if (rightPressed && !leftPressed) {
            paddle.moveRight();
        } else {
            paddle.stop(); // Stop when no keys pressed or both pressed
        }

        if (ball.isStuckToPaddle()) {
            double paddleDx = paddle.getX() - paddleOldx;
            ball.updateStuckOffset(paddleDx);
            ball.stickToPaddle(paddle);
        }

        // Update game logic
        gameManager.updateGameLogic(deltaTime);

        // Check for win/loss conditions
        if (gameManager.getBricks().isEmpty()) {
            handleLevelComplete();
        }

        if (gameManager.getLives() <= 0) {
            changeState(new GameOverState(gameManager));
        }

    }


    @Override
    public void handleInput(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        boolean keyPressed = event.getEventType() == KeyEvent.KEY_PRESSED;

        //Handle pause
        if (keyPressed && keyCode == KeyCode.ESCAPE) {
            changeState(new PausedState(gameManager));
            return;
        }

        // Handle ball launch (Enter or Space)
        Ball ball = gameManager.getBall();
        if (keyPressed && (keyCode == KeyCode.SPACE || keyCode == KeyCode.ENTER)) {
            if (ball != null && ball.isStuckToPaddle()) {
                ball.launch();
                return;
            }
            // If ball is not stuck, try to shoot bullets (Gun Paddle power-up)
            else {
                gameManager.shootBullet();
            }
        }

        // Handle paddle movement keys
        switch (keyCode) {
            case LEFT:
            case A:
                leftPressed = keyPressed;
                break;
            case RIGHT:
            case D:
                rightPressed = keyPressed;
                break;
            default:
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        } else {
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        }

        //Draw bricks
        for (Brick brick : gameManager.getBricks()) {
            if (brick.isActive()) {
                brick.render(gc);
            }
        }

        // Draw paddle
        Paddle paddle = gameManager.getPaddle();
        paddle.render(gc);

        List<Ball> balls = gameManager.getBalls();
        if (balls != null) {
            for (Ball b : balls) {
                if (b != null) b.render(gc);
            }
        }

        // Draw bullets (Gun Paddle power-up)
        java.util.List<com.knightquest.arkanoid.model.Bullet> bullets = gameManager.getBullets();
        if (bullets != null) {
            for (com.knightquest.arkanoid.model.Bullet bullet : bullets) {
                if (bullet != null && bullet.isActive()) {
                    bullet.render(gc);
                }
            }
        }

        gameManager.getPowerUpManager().render(gc);
        drawUI(gc);
    }

    @Override
    public void exit() {
        leftPressed = false;
        rightPressed = false;

        //Stop music when exiting playing state
        AudioController audioController = gameManager.getEventManager().getAudioController();
        if (audioController != null) {
            audioController.stopBGM();
        }
    }

    public void drawUI(GraphicsContext gc) {
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(Font.font("Arial", 20));
        gc.setFill(Color.WHITE);

        // === SCORE ===
        gc.setFont(Font.font(arcadeFont.getFamily(), 22));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.setFill(Color.GOLD);
        gc.strokeText("SCORE: " + gameManager.getScore(), 20, 28);
        gc.fillText("SCORE: " + gameManager.getScore(), 20, 28);

        // === LEVEL TITLE ===
        String levelText = "LEVEL " + gameManager.getCurrentLevelNumber() + ": " + gameManager.getCurrentLevelName();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font(arcadeFont.getFamily(), 26));
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        gc.setFill(Color.GOLD);

        gc.strokeText(levelText, gc.getCanvas().getWidth() / 2, 45);
        gc.fillText(levelText, gc.getCanvas().getWidth() / 2, 45);

        // Draw lives
        if (heartImage != null) {
            double heartSize = 24;
            for (int i = 0; i < gameManager.getLives(); ++i) {
                gc.drawImage(heartImage, 20 + i * (heartSize + 5), 55 - heartSize, heartSize, heartSize);
            }
        } else {
            StringBuilder livesStr = new StringBuilder("Lives: ");
            for (int i = 0; i < gameManager.getLives(); i++) {
                livesStr.append("♥ ");
            }
            gc.fillText(livesStr.toString(), 20, 55);
        }

        // Draw instructions
        gc.setFont(Font.font("Arial", 14));
        gc.setFill(Color.GRAY);
        gc.fillText("<- Move Left    -> Move Right    ESC - Pause", gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() - 20);
    }

    private void handleLevelComplete() {
        System.out.println("Level Complete");
        // Transition to LevelCompleteState
        changeState(new LevelCompleteState(gameManager));
    }
}
