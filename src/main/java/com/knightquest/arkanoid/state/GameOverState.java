package com.knightquest.arkanoid.state;

import com.knightquest.arkanoid.controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import  javafx.scene.text.TextAlignment;
import java.io.InputStream;


/**
 * GameOverState handles the game over screen.
 */
public class GameOverState extends GameState {
    private final  int finalScore;
    private final int levelsCompleted;
    private double animationTimer = 0;
    private Font cinzelTitle;
    private Font cinzelStats;
    private Font cinzelInstructions;


    public GameOverState(GameManager gameManager, boolean isVictory) {
        super(gameManager);
        this.finalScore = gameManager.getScore();
        this.levelsCompleted = gameManager.getCurrentLevelNumber();
        loadFonts();
    }

    public GameOverState(GameManager gameManager) {
        this(gameManager, false);
        loadFonts();
    }

    private void loadFonts() {
        String cinzelPath = "/fonts/Cinzel-Regular.ttf";

        try {
            InputStream titleStream = getClass().getResourceAsStream(cinzelPath);
            if (titleStream != null) {
                cinzelTitle = Font.loadFont(titleStream, 40);
            } else {
                throw new Exception("Font not found: " + cinzelPath);
            }

            InputStream buttonStream = getClass().getResourceAsStream(cinzelPath);
            if (buttonStream != null) {
                cinzelStats = Font.loadFont(buttonStream, 20);
            } else {
                throw new Exception("Font not found: " + cinzelPath);
            }

            InputStream instructionStream = getClass().getResourceAsStream(cinzelPath);
            if (instructionStream != null) {
                cinzelInstructions = Font.loadFont(instructionStream, 16);
            } else {
                throw new Exception("Font not found: " + cinzelPath);
            }
        } catch (Exception e) {
            System.err.println("Error loading fonts: " + e.getMessage());
            // Fallback to default fonts
            cinzelTitle = Font.font("Arial", 40);
            cinzelStats = Font.font("Arial", 20);
            cinzelInstructions = Font.font("Arial", 16);
        }
    }

    @Override
    public void enter() {
        System.out.println("Game Over");
        System.out.println("Final Score: " + finalScore);
        System.out.println("Levels Completed: " + levelsCompleted);
        animationTimer = 0;

        gameManager.getEventManager().getAudioController().stopBGM();
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
                // Play confirm sound
                gameManager.getEventManager().notifyMenuOptionSelected();
                // Restart game
                gameManager.resetGame();
                changeState(new PlayingState(gameManager));
                break;
            case M:
            case ESCAPE:
                // Play confirm sound
                gameManager.getEventManager().notifyMenuOptionSelected();
                // Return to main menu
                changeState(new MenuState(gameManager));
                break;
            default:
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // Background
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        gc.setFill(Color.rgb(15, 15, 25));
        gc.fillRect(0, 0, width, height);

        double centerX = width / 2;
        double centerY = height / 2;

        // Animated "Game Over" text
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(cinzelTitle);

        // Fade in effect
        double alpha = Math.min(1.0, animationTimer / 1.0);
        gc.setGlobalAlpha(alpha);
        gc.setFill(Color.rgb(255, 245, 200));
        gc.fillText("GAME OVER", centerX, centerY - 100);
        gc.setGlobalAlpha(1.0);

        // Skull icon
        gc.setFont(Font.font("Arial", 48));
        gc.setFill(Color.rgb(180, 180, 180));
        gc.fillText("☠", centerX, centerY - 30);

        // Statistics
        gc.setFont(cinzelStats);
        gc.setFill(Color.GRAY);
        gc.fillText("Final Score: " + finalScore, centerX, centerY + 20);
        gc.fillText("Levels Completed: " + levelsCompleted, centerX, centerY + 60);

        // Instructions
        gc.setFont(cinzelInstructions);
        gc.setFill(Color.GRAY);
        gc.fillText("Press 'R' to Restart or 'M' to return to Main Menu", centerX, centerY + 120);
        gc.fillText("Press 'ESC' to exit", centerX, centerY + 150);
    }

    @Override
    public void exit() {}
}
