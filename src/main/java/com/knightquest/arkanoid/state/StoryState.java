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

import java.util.ArrayList;
import java.util.List;

/**
 * StoryState displays the story image before starting each level.
 * For level 1, shows a sequence of 10 images.
 * For other levels, shows a single story image.
 */
public class StoryState extends GameState {
    private Image storyImage;
    private final int levelNumber;
    private double fadeAlpha = 0.0;
    private double fadeTimer = 0.0;
    private final double FADE_DURATION = 0.5; // 0.5 seconds to fade in
    private boolean imageFadedIn = false;

    private Font instructionFont;
    
    // For level 1 multi-image story
    private final List<Image> storyImages = new ArrayList<>();
    private int currentImageIndex = 0;
    private boolean isMultiImageStory = false;

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
     * For level 1: Load 10 images from lv1 folder (1_1.jpg to 1_10.jpg)
     * For other levels: Load single story image from /images/story/level{X}.png
     */
    private void loadStoryImage() {
        if (levelNumber == 1) {
            // Load multiple images for level 1
            isMultiImageStory = true;
            storyImages.clear();
            
            for (int i = 1; i <= 11; i++) {
                String imagePath = "/images/story/lv1/1_" + i + ".jpg";
                try {
                    java.net.URL imageURL = getClass().getResource(imagePath);
                    if (imageURL != null) {
                        Image img = new Image(imageURL.toString());
                        storyImages.add(img);
                        System.out.println("✅ Loaded story image " + i + "/11 for level 1");
                    } else {
                        System.err.println("⚠️ Story image not found: " + imagePath);
                    }
                } catch (Exception e) {
                    System.err.println("❌ Failed to load story image " + i + ": " + e.getMessage());
                }
            }
            
            if (storyImages.isEmpty()) {
                System.err.println("⚠️ No story images loaded for level 1");
                isMultiImageStory = false;
            } else {
                System.out.println("✅ Loaded " + storyImages.size() + " story images for level 1");
            }
        } else {
            // Load single image for other levels
            isMultiImageStory = false;
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
        currentImageIndex = 0; // Reset to first image

        // If no story image exists, skip directly to playing state
        if (isMultiImageStory && storyImages.isEmpty()) {
            System.out.println("⚠️ No story images found for level 1, skipping to PlayingState");
            changeState(new PlayingState(gameManager));
        } else if (!isMultiImageStory && storyImage == null) {
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
                if (isMultiImageStory) {
                    // For level 1, move to next image
                    if (currentImageIndex < storyImages.size() - 1) {
                        currentImageIndex++;
                        // Reset fade animation for next image
                        fadeAlpha = 0.0;
                        fadeTimer = 0.0;
                        imageFadedIn = false;
                        
                        if (eventManager != null) {
                            eventManager.notifyMenuOptionSelected();
                        }
                        System.out.println("📖 Story image " + (currentImageIndex + 1) + "/" + storyImages.size());
                    } else {
                        // Last image, start the level
                        if (eventManager != null) {
                            eventManager.notifyMenuOptionSelected();
                        }
                        System.out.println("Story completed, starting level " + levelNumber);
                        changeState(new PlayingState(gameManager));
                    }
                } else {
                    // For other levels, skip to playing state
                    if (eventManager != null) {
                        eventManager.notifyMenuOptionSelected();
                    }
                    System.out.println("Story skipped, starting level " + levelNumber);
                    changeState(new PlayingState(gameManager));
                }
                break;
            case ESCAPE:
                // Skip entire story sequence
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

        // Get current image to display
        Image currentImage = null;
        if (isMultiImageStory && !storyImages.isEmpty() && currentImageIndex < storyImages.size()) {
            currentImage = storyImages.get(currentImageIndex);
        } else if (!isMultiImageStory) {
            currentImage = storyImage;
        }

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

        // Different instructions for multi-image vs single image
        String instructionText;
        if (isMultiImageStory && currentImageIndex < storyImages.size() - 1) {
            instructionText = String.format("Press ENTER/SPACE for next (%d/%d) | ESC to skip story", 
                                          currentImageIndex + 1, storyImages.size());
        } else if (isMultiImageStory) {
            instructionText = String.format("Press ENTER/SPACE to start level (%d/%d) | ESC to skip", 
                                          currentImageIndex + 1, storyImages.size());
        } else {
            instructionText = "Press ENTER, SPACE or ESC to start level";
        }

        // Draw instruction text with glow effect
        gc.setStroke(Color.rgb(255, 215, 0, 0.8));
        gc.setLineWidth(3);
        gc.strokeText(instructionText, centerX, instructionY);

        gc.setFill(Color.WHITE);
        gc.fillText(instructionText, centerX, instructionY);

        gc.setGlobalAlpha(1.0); // Reset alpha
    }

    @Override
    public void exit() {
        System.out.println("=== EXITING STORY STATE ===");
        fadeAlpha = 0.0;
        fadeTimer = 0.0;
    }
}