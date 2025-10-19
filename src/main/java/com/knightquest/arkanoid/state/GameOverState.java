package com.knightquest.arkanoid.state;

import com.knightquest.arkanoid.controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import  javafx.scene.text.TextAlignment;
/**
 * GameOverState handles the game over screen.
 */

public class GameOverState extends GameState {
    private final  int finalScore;
    private final int levelsCompleted;
    private double animationTimer = 0;


    public GameOverState(GameManager gameManager) {
        super(gameManager);
        this.finalScore = gameManager.getScore();
        this.levelsCompleted = gameManager.getCurrentLevelNumber();
    }

    @Override
    public void enter() {
        System.out.println("Game Over");
        System.out.println("Final Score: " + finalScore);
        System.out.println("Levels Completed: " + levelsCompleted);
        animationTimer = 0;
    }

    @Override
    public void update(double deltaTime) {
        animationTimer += deltaTime;
    }

    @Override
    public void handleInput(KeyEvent event) {
        if (event.getEventType() != KeyEvent.KEY_PRESSED) {
            return;
        }

        switch (event.getCode()) {
            case R:
                // Restart game
                gameManager.resetGame();
                changeState(new PlayingState(gameManager));
                break;
            case M:
            case ESCAPE:
                // Return to main menu
                changeState(new MenuState(gameManager));
                break;
            default:
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // Gradient background
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        // Background
        gc.setFill(Color.rgb(40, 10, 10));
        gc.fillRect(0, 0, width, height);

        double centerX = width / 2;
        double centerY = height / 2;

        // Animated "Game Over" text
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Arial", 64));

        // Fade in effect
        double alpha = Math.min(1.0, animationTimer / 1.0);
        gc.setGlobalAlpha(alpha);
        gc.setFill(Color.RED);
        gc.fillText("GAME OVER", centerX, centerY - 100);
        gc.setGlobalAlpha(1.0);

        // Skull icon
        gc.setFont(Font.font("Arial", 48));
        gc.setFill(Color.DARKRED);
        gc.fillText("☠", centerX, centerY - 30);

        // Statistics
        gc.setFont(Font.font("Arial", 28));
        gc.setFill(Color.WHITE);
        gc.fillText("Final Score: " + finalScore, centerX, centerY + 20);
        gc.fillText("Levels Completed: " + levelsCompleted, centerX, centerY + 60);

        // Instructions
        gc.setFont(Font.font("Arial", 20));
        gc.setFill(Color.GOLD);
        gc.fillText("Press 'R' to Restart or 'M' to return to Main Menu", centerX, centerY + 120);
        gc.fillText("Press 'ESC' to exit", centerX, centerY + 150);


    }

    @Override
    public void exit() {

    }
}
