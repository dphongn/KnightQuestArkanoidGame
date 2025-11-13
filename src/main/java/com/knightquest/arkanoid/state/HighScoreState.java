package com.knightquest.arkanoid.state;

import com.knightquest.arkanoid.controller.GameManager;
import com.knightquest.arkanoid.manager.HighScoreManager;
import com.knightquest.arkanoid.model.data.HighScoreEntry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.List;

/**
 * HighScoreState displays the top 10 high scores.
 * Allows returning to main menu.
 * 
 * State Pattern: Encapsulates high score display logic.
 */
public class HighScoreState extends GameState {
    private HighScoreManager highScoreManager;
    private List<HighScoreEntry> highScores;
    
    private Image backgroundImage;
    private Font titleFont;
    private Font headerFont;
    private Font entryFont;
    private Font instructionFont;
    
    private double animationTimer = 0;
    private int highlightedRank = -1; // Rank to highlight (if just added)
    private double inputCooldown = 0; // Prevent immediate input after entering state
    
    /**
     * Create high score state.
     * 
     * @param gameManager Game manager reference
     */
    public HighScoreState(GameManager gameManager) {
        super(gameManager);
        this.highScoreManager = HighScoreManager.getInstance();
        this.inputCooldown = 0.3; // 300ms cooldown
    }
    
    /**
     * Create high score state with highlighted entry.
     * 
     * @param gameManager Game manager reference
     * @param highlightedRank Rank to highlight (1-based), or -1 for none
     */
    public HighScoreState(GameManager gameManager, int highlightedRank) {
        super(gameManager);
        this.highScoreManager = HighScoreManager.getInstance();
        this.highlightedRank = highlightedRank;
        this.inputCooldown = 0.3; // 300ms cooldown
    }
    
    @Override
    public void enter() {
        System.out.println("Entering HighScoreState");
        
        // Load high scores
        highScores = highScoreManager.getHighScores();
        
        // Load resources
        loadBackgroundImage();
        loadFonts();
    }
    
    /**
     * Load background image.
     */
    private void loadBackgroundImage() {
        try {
            java.net.URL bgURL = getClass().getResource("/images/backgrounds/menu_background.png");
            if (bgURL != null) {
                backgroundImage = new Image(bgURL.toString());
            } else {
                System.err.println("Background image not found for high scores.");
            }
        } catch (Exception e) {
            System.err.println("Failed to load background image: " + e.getMessage());
            backgroundImage = null;
        }
    }
    
    /**
     * Load fonts for high score display.
     */
    private void loadFonts() {
        String unifrakturPath = "/fonts/UnifrakturMaguntia-Regular.ttf";
        String cinzelPath = "/fonts/Cinzel-Regular.ttf";
        
        try {
            // Title font (large decorative)
            try (InputStream unifrakturStream = getClass().getResourceAsStream(unifrakturPath)) {
                if (unifrakturStream != null) {
                    Font baseUnifraktur = Font.loadFont(unifrakturStream, 56);
                    titleFont = Font.font(baseUnifraktur.getFamily(), FontWeight.BOLD, 56);
                } else {
                    titleFont = Font.font("Serif", FontWeight.BOLD, 56);
                }
            }
            
            // Header font (table headers)
            try (InputStream cinzelStream = getClass().getResourceAsStream(cinzelPath)) {
                if (cinzelStream != null) {
                    Font baseCinzel = Font.loadFont(cinzelStream, 24);
                    headerFont = Font.font(baseCinzel.getFamily(), FontWeight.BOLD, 24);
                    entryFont = Font.font(baseCinzel.getFamily(), FontWeight.NORMAL, 20);
                    instructionFont = Font.font(baseCinzel.getFamily(), FontWeight.NORMAL, 18);
                } else {
                    headerFont = Font.font("Serif", FontWeight.BOLD, 24);
                    entryFont = Font.font("Serif", FontWeight.NORMAL, 20);
                    instructionFont = Font.font("Serif", FontWeight.NORMAL, 18);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load fonts: " + e.getMessage());
            titleFont = Font.font("Serif", FontWeight.BOLD, 56);
            headerFont = Font.font("Serif", FontWeight.BOLD, 24);
            entryFont = Font.font("Serif", FontWeight.NORMAL, 20);
            instructionFont = Font.font("Serif", FontWeight.NORMAL, 18);
        }
    }
    
    @Override
    public void exit() {
        System.out.println("Exiting HighScoreState");
    }
    
    @Override
    public void update(double deltaTime) {
        // Animate highlight glow
        animationTimer += deltaTime;
        
        // Decrease input cooldown
        if (inputCooldown > 0) {
            inputCooldown -= deltaTime;
        }
    }
    
    @Override
    public void render(GraphicsContext gc) {
        // Background
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, 1024, 768);
        } else {
            // Fallback gradient background
            gc.setFill(Color.rgb(10, 10, 30));
            gc.fillRect(0, 0, 1024, 768);
        }
        
        // Semi-transparent overlay for readability
        gc.setGlobalAlpha(0.7);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 1024, 768);
        gc.setGlobalAlpha(1.0);
        
        // Title
        gc.setFont(titleFont);
        gc.setFill(Color.GOLD);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("HIGH SCORES", 512, 80);
        
        // Decorative line
        gc.setStroke(Color.GOLD);
        gc.setLineWidth(2);
        gc.strokeLine(312, 100, 712, 100);
        
        // Table headers
        gc.setFont(headerFont);
        gc.setFill(Color.LIGHTGRAY);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Rank", 180, 150);
        gc.fillText("Player", 280, 150);
        gc.fillText("Score", 480, 150);
        gc.fillText("Level", 600, 150);
        gc.fillText("Date", 700, 150);
        
        // Header underline
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        gc.strokeLine(180, 160, 840, 160);
        
        // High score entries
        gc.setFont(entryFont);
        int yPosition = 190;
        int rank = 1;
        
        for (HighScoreEntry entry : highScores) {
            // Highlight if this is the newly added score
            boolean isHighlighted = (rank == highlightedRank);
            
            if (isHighlighted) {
                // Animated glow effect
                double glowAlpha = 0.3 + 0.2 * Math.sin(animationTimer * 3);
                gc.setGlobalAlpha(glowAlpha);
                gc.setFill(Color.GOLD);
                gc.fillRect(170, yPosition - 22, 680, 32);
                gc.setGlobalAlpha(1.0);
                
                gc.setFill(Color.YELLOW);
            } else if (rank <= 3) {
                // Medal colors for top 3
                switch (rank) {
                    case 1 -> gc.setFill(Color.GOLD);
                    case 2 -> gc.setFill(Color.SILVER);
                    case 3 -> gc.setFill(Color.rgb(205, 127, 50)); // Bronze
                }
            } else {
                gc.setFill(Color.WHITE);
            }
            
            // Render entry
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(String.valueOf(rank), 215, yPosition);
            
            gc.setTextAlign(TextAlignment.LEFT);
            gc.fillText(entry.getPlayerName(), 280, yPosition);
            gc.fillText(String.valueOf(entry.getScore()), 480, yPosition);
            gc.fillText("Level " + entry.getLevelReached(), 600, yPosition);
            gc.fillText(entry.getFormattedDate(), 700, yPosition);
            
            yPosition += 45;
            rank++;
            
            if (rank > 10) break; // Only show top 10
        }
        
        // Empty state message
        if (highScores.isEmpty()) {
            gc.setFont(entryFont);
            gc.setFill(Color.LIGHTGRAY);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("No high scores yet!", 512, 350);
            gc.fillText("Be the first to set a record!", 512, 390);
        }
        
        // Instructions
        gc.setFont(instructionFont);
        gc.setFill(Color.LIGHTGRAY);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Press ESC or ENTER to return to menu", 512, 720);
    }
    
    @Override
    public void handleInput(KeyEvent event) {
        // Ignore input during cooldown period
        if (inputCooldown > 0) {
            return;
        }
        
        if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER) {
            // Return to menu
            gameManager.changeState(new MenuState(gameManager));
        }
    }
}
