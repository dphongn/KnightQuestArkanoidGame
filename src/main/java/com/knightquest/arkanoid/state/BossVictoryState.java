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
 * BossVictoryState displays victory screen when the boss is defeated.
 */

public class BossVictoryState extends GameState {
    private Image[] victoryImages;
    private final String[] imagePaths = {
            "/images/story/bosswin1.png",
            "/images/story/bosswin2.png",
            "/images/story/gamevictory.png"
    };

    private int currentImageIndex = 0;
    private double fadeAlpha = 0.0;
    private double fadeTimer = 0.0;
    private final double FADE_DURATION = 0.8;
    private boolean imageFadedIn = false;

    private Font instructionFont;
    private Font titleFont;

    /**
     * Constructor for BossVictoryState.
     */
    public BossVictoryState(GameManager gameManager) {
        super(gameManager);
        loadVictoryImages();
        loadFonts();
    }

    /**
     * Load victory images from resources.
     */
    private void loadVictoryImages() {
        victoryImages = new Image[imagePaths.length];
        
        for (int i = 0; i < imagePaths.length; i++) {
            try {
                java.net.URL imageURL = getClass().getResource(imagePaths[i]);
                if (imageURL != null) {
                    victoryImages[i] = new Image(imageURL.toString());
                    System.out.println("✅ Loaded victory image: " + imagePaths[i]);
                } else {
                    System.err.println("⚠️ Victory image not found: " + imagePaths[i]);
                    victoryImages[i] = null;
                }
            } catch (Exception e) {
                System.err.println("❌ Failed to load victory image " + imagePaths[i] + ": " + e.getMessage());
                victoryImages[i] = null;
            }
        }
    }

    /**
     * Load fonts for UI elements
     */
    private void loadFonts() {
        try {
            instructionFont = Font.font("Arial", FontWeight.BOLD, 18);
            titleFont = Font.font("Arial", FontWeight.BOLD, 36);
        } catch (Exception e) {
            instructionFont = Font.font("Arial", 18);
            titleFont = Font.font("Arial", 36);
        }
    }

     @Override
    public void enter() {
        System.out.println("=== ENTERING BOSS VICTORY STATE ===");
        currentImageIndex = 0;
        fadeAlpha = 0.0;
        fadeTimer = 0.0;
        imageFadedIn = false;
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
                if (eventManager != null) {
                    eventManager.notifyMenuOptionSelected();
                }
                
                // Move to next image
                if (currentImageIndex < victoryImages.length - 1) {
                    currentImageIndex++;
                    fadeAlpha = 0.0;
                    fadeTimer = 0.0;
                    imageFadedIn = false;
                    System.out.println("Showing victory image " + (currentImageIndex + 1) + "/" + victoryImages.length);
                } else {
                    // Last image shown, go to menu
                    System.out.println("Victory sequence complete, returning to main menu");
                    changeState(new MenuState(gameManager));
                }
                break;
                
            case ESCAPE:
                // Skip entire sequence
                if (eventManager != null) {
                    eventManager.notifyMenuOptionSelected();
                }
                System.out.println("Victory sequence skipped");
                changeState(new MenuState(gameManager));
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

        // Get current image
        Image currentImage = victoryImages[currentImageIndex];
        
        if (currentImage != null) {
            // Calculate image dimensions to fit screen while maintaining aspect ratio
            double imageWidth = currentImage.getWidth();
            double imageHeight = currentImage.getHeight();

            double scaleX = width / imageWidth;
            double scaleY = height / imageHeight;
            double scale = Math.min(scaleX, scaleY); // Maintain aspect ratio

            double scaledWidth = imageWidth * scale;
            double scaledHeight = imageHeight * scale;

            double x = (width - scaledWidth) / 2;
            double y = (height - scaledHeight) / 2;

            // Apply fade effect
            gc.setGlobalAlpha(fadeAlpha);
            gc.drawImage(currentImage, x, y, scaledWidth, scaledHeight);
            gc.setGlobalAlpha(1.0); // Reset alpha
        } else {
            // Fallback: show text message if image not found
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(titleFont);
            gc.setFill(Color.GOLD);
            gc.fillText("BOSS DEFEATED!", centerX, height / 2 - 50);

            gc.setFont(Font.font("Arial", 24));
            gc.setFill(Color.WHITE);
            gc.fillText("CONGRATULATIONS!", centerX, height / 2);
            
            gc.setFont(Font.font("Arial", 20));
            gc.setFill(Color.LIGHTGRAY);
            gc.fillText("Victory image not available", centerX, height / 2 + 40);
        }

        // Draw instructions at the bottom
        gc.setGlobalAlpha(fadeAlpha);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(instructionFont);

        // Draw instruction background
        double instructionY = height - 30;
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, instructionY - 25, width, 50);

        // Draw instruction text with glow effect
        String instructionText;
        if (currentImageIndex < victoryImages.length - 1) {
            instructionText = "Press ENTER or SPACE to continue | ESC to skip";
        } else {
            instructionText = "Press ENTER, SPACE or ESC to return to menu";
        }
        
        gc.setStroke(Color.rgb(255, 215, 0, 0.8));
        gc.setLineWidth(3);
        gc.strokeText(instructionText, centerX, instructionY);

        gc.setFill(Color.WHITE);
        gc.fillText(instructionText, centerX, instructionY);

        gc.setGlobalAlpha(1.0); // Reset alpha
    }

    @Override
    public void exit() {
        System.out.println("=== EXITING BOSS VICTORY STATE ===");
        fadeAlpha = 0.0;
        fadeTimer = 0.0;
    }
}
