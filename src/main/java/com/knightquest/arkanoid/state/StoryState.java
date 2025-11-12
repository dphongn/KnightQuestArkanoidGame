package com.knightquest.arkanoid.state;

import com.knightquest.arkanoid.controller.GameManager;
import com.knightquest.arkanoid.observer.GameEventManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * StoryState displays the story image before starting each level.
 * The story image is pre-created and loaded from resources.
 */
public class StoryState extends GameState {
    private Image storyImage;
    private final int levelNumber;
    private double fadeAlpha = 0.0;
    private double fadeTimer = 0.0;
    private final double FADE_DURATION = 0.5; // 0.5 seconds to fade in
    private boolean imageFadedIn = false;

    private Font instructionFont;

    /**
     * Constructor for StoryState
     * @param gameManager The game manager instance
     * @param levelNumber The level number to show story for
     */
    public StoryState(GameManager gameManager, int levelNumber) {
        super(gameManager);
        this.levelNumber = levelNumber;
        loadStoryImage();
        loadFonts();
    }

    /**
     * Load the story image for the current level.
     * Story images should be placed in: /images/story/level{X}.png
     * For example: level1.png, level2.png, etc.
     */
    private void loadStoryImage() {
        String storyImagePath = "/images/story/level" + levelNumber + ".png";

        try {
            java.net.URL imageURL = getClass().getResource(storyImagePath);
            if (imageURL != null) {
                storyImage = new Image(imageURL.toString());
                System.out.println("✅ Loaded story image for level " + levelNumber);
            } else {
                System.err.println("⚠️ Story image not found: " + storyImagePath);
                storyImage = null;
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to load story image: " + e.getMessage());
            storyImage = null;
        }
    }

    /**
     * Load fonts for UI elements.
     */
    private void loadFonts() {
        try {
            instructionFont = Font.font("Arial", FontWeight.BOLD, 18);
        } catch (Exception e) {
            instructionFont = Font.font("Arial", 18);
        }
    }

    @Override
    public void enter() {
        System.out.println("=== ENTERING STORY STATE FOR LEVEL " + levelNumber + " ===");
        fadeAlpha = 0.0;
        fadeTimer = 0.0;
        imageFadedIn = false;

        // If no story image exists, skip directly to playing state
        if (storyImage == null) {
            System.out.println("⚠️ No story image found, skipping to PlayingState");
            changeState(new PlayingState(gameManager));
        }
    }

    @Override
    public void update(double deltaTime) {
        // Handle fade-in animation
        if (!imageFadedIn) {
            fadeTimer += deltaTime;
            fadeAlpha = Math.min(1.0, fadeTimer / FADE_DURATION);

            if (fadeAlpha >= 1.0) {
                imageFadedIn = true;
            }
        }
    }

    @Override
    public void handleInput(KeyEvent event) {
        if (event.getEventType() != KeyEvent.KEY_PRESSED) {
            return;
        }

        KeyCode code = event.getCode();
        GameEventManager eventManager = gameManager.getEventManager();

        switch (code) {
            case ENTER:
            case SPACE:
            case ESCAPE:
                // Skip to playing state
                if (eventManager != null) {
                    eventManager.notifyMenuOptionSelected();
                }
                System.out.println("Story skipped, starting level " + levelNumber);
                changeState(new PlayingState(gameManager));
                break;
            default:
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();
        double centerX = width / 2;

        // Draw black background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        if (storyImage != null) {
            // Calculate image dimensions to fit screen while maintaining aspect ratio
            double imageWidth = storyImage.getWidth();
            double imageHeight = storyImage.getHeight();

            double scaleX = width / imageWidth;
            double scaleY = height / imageHeight;
            double scale = Math.min(scaleX, scaleY); // Maintain aspect ratio

            double scaledWidth = imageWidth * scale;
            double scaledHeight = imageHeight * scale;

            double x = (width - scaledWidth) / 2;
            double y = (height - scaledHeight) / 2;

            // Apply fade effect
            gc.setGlobalAlpha(fadeAlpha);
            gc.drawImage(storyImage, x, y, scaledWidth, scaledHeight);
            gc.setGlobalAlpha(1.0); // Reset alpha
        } else {
            // Fallback: show text message if image not found
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 32));
            gc.setFill(Color.WHITE);
            gc.fillText("LEVEL " + levelNumber, centerX, height / 2 - 50);

            gc.setFont(Font.font("Arial", 20));
            gc.setFill(Color.LIGHTGRAY);
            gc.fillText("Story image not available", centerX, height / 2);
        }

        // Draw instructions at the bottom
        gc.setGlobalAlpha(fadeAlpha); // Apply fade to instructions too
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(instructionFont);

        // Draw instruction background
        double instructionY = height - 50;
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, instructionY - 25, width, 60);

        // Draw instruction text with glow effect
        gc.setStroke(Color.rgb(255, 215, 0, 0.8));
        gc.setLineWidth(3);
        gc.strokeText("Press ENTER, SPACE or ESC to start level", centerX, instructionY);

        gc.setFill(Color.WHITE);
        gc.fillText("Press ENTER, SPACE or ESC to start level", centerX, instructionY);

        gc.setGlobalAlpha(1.0); // Reset alpha
    }

    @Override
    public void exit() {
        System.out.println("=== EXITING STORY STATE ===");
        fadeAlpha = 0.0;
        fadeTimer = 0.0;
    }
}
