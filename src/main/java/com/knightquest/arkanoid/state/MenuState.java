package com.knightquest.arkanoid.state;

import com.knightquest.arkanoid.controller.GameManager;
import com.knightquest.arkanoid.observer.AudioController;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.TextAlignment;
import com.knightquest.arkanoid.observer.GameEventManager;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import java.io.InputStream;
import javafx.scene.input.KeyCode;


/**
 * MenuState handles the main menu of the game.
 */
public class MenuState extends GameState {
    private int selectedOption = 0;
    private final String[] menuOptions = {"START GAME", "HIGH SCORES", "EXIT GAME"};
    private double animationTimer = 0;
    private final GameEventManager eventManager;

    private Image backgroundImage;

    private Font cinzelTitle1;
    private Font cinzelTitle2;
    private Font cinzelButton;
    private Font cinzelButtonSelected;

    public MenuState(GameManager gameManager) {
        super(gameManager);
        this.eventManager = gameManager.getEventManager(); // Cache the event manager

        //Load background image
        loadBackgroundImage();

        //Load font
        loadFonts();
    }

    /**
     * Load background image from resources.
     */
    private void loadBackgroundImage() {
        try {
            java.net.URL bgURL = getClass().getResource("/images/backgrounds/menu_background.png");
            if (bgURL != null) {
                backgroundImage = new Image(bgURL.toString());
                System.out.println("✅ Loaded menu background image");
            } else {
                System.err.println("Menu background image not found.");
            }
        } catch (Exception e) {
            System.err.println("Failed to load menu background image: " + e.getMessage());
            backgroundImage = null;
        }
    }

    /**
     * Load custom fonts from resources.
     */
    private void loadFonts() {
        String unifrakturPath = "/fonts/UnifrakturMaguntia-Regular.ttf";
        String cinzelPath = "/fonts/Cinzel-Regular.ttf";

        try {
            try (InputStream unifrakturStream = getClass().getResourceAsStream(unifrakturPath)) {
                if (unifrakturStream != null) {
                    Font baseUnifraktur = Font.loadFont(unifrakturStream, 64);
                    cinzelTitle1 = Font.font(baseUnifraktur.getFamily(), FontWeight.NORMAL, 64);
                    cinzelTitle2 = Font.font(baseUnifraktur.getFamily(), FontWeight.NORMAL, 48);
                } else {
                    throw new Exception("Font not found: " + unifrakturPath);
                }
            }

            try (InputStream cinzelStream = getClass().getResourceAsStream(cinzelPath)) {
                if (cinzelStream != null) {
                    Font baseCinzel = Font.loadFont(cinzelStream, 16);
                    cinzelButton = Font.font(baseCinzel.getFamily(), FontWeight.NORMAL, 16);
                    cinzelButtonSelected = Font.font(baseCinzel.getFamily(), FontWeight.BOLD, 18);
                } else {
                    throw new Exception("Font not found: " + cinzelPath);
                }
            }

            System.out.println("✅ Loaded custom fonts for MenuState");
        } catch (Exception e) {
            System.err.println("⚠️ Failed to load fonts: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback
            cinzelTitle1 = Font.font("Serif", FontWeight.BOLD, 64);
            cinzelTitle2 = Font.font("Serif", FontWeight.BOLD, 48);
            cinzelButton = Font.font("Serif", 16);
            cinzelButtonSelected = Font.font("Serif", FontWeight.BOLD, 18);
        }
    }
    
    @Override
    public void enter() {
        System.out.println("Entering MenuState");
        selectedOption = 0;
        animationTimer = 0;

        //Add play menu music
        AudioController audioController = gameManager.getEventManager().getAudioController();
        if (audioController != null) {
            audioController.playLevelMusic(0);
        }
    }

    @Override
    public void update(double deltaTime) {
        // Update animation timer
        animationTimer += deltaTime;
    }

    @Override
    public void handleInput(KeyEvent event) {
        if(event.getEventType() != KeyEvent.KEY_PRESSED) {
            return;
        }

        KeyCode code = event.getCode();

        switch(code) {
            case UP:
            case W:
                selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
                if (eventManager != null) { 
                    eventManager.notifyMenuSelectionChanged();
                }
                break;
            case DOWN:
            case S:
                selectedOption = (selectedOption + 1) % menuOptions.length;
                if (eventManager != null) {
                    eventManager.notifyMenuSelectionChanged();
                }
                break;
            case ENTER:
            case SPACE:
                if (eventManager != null) {
                    eventManager.notifyMenuOptionSelected();
                }
                handleSelection();
                break;
            case ESCAPE:
                System.exit(0);
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
        double centerY = height / 2;

        //Draw background image
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, width, height);
        } else {
            // Fallback: solid color background
            gc.setFill(Color.DARKSLATEBLUE);
            gc.fillRect(0, 0, width, height);
        }
        //Draw title "Knight's Quest Arkanoid"
        gc.setTextAlign(TextAlignment.CENTER);

        //Title line 1
        gc.setFont(cinzelTitle1); 

        //Main text(gold)
        gc.setFill(Color.rgb(255, 245, 200));
        gc.fillText("Knight's Quest", centerX, 120);

        //Title line 2
        gc.setFont(cinzelTitle2); 

        gc.setFill(Color.rgb(255, 245, 200));
        gc.fillText("Arkanoid", centerX, 180);

        //Draw menu buttons with medieval style
        double buttonWidth = 175;
        double buttonHeight = 35;
        double buttonStartY = centerY + 80;

        for ( int i = 0; i < menuOptions.length; i++) {
            double y = buttonStartY + i * 45;
            boolean isSelected = (i == selectedOption);

            //Button scale animation
            double buttonScale = isSelected ? 1.05 : 1.0;
            double scaledWidth = buttonWidth * buttonScale;
            double scaledHeight = buttonHeight * buttonScale;

            double buttonX = centerX - scaledWidth / 2;
            double buttonY = y - scaledHeight / 2;

            //Outer glow (if selected)
            if (isSelected) {
                gc.setFill(Color.rgb(200, 210, 220, 0.25));
                gc.fillRoundRect(buttonX - 4, buttonY - 4, scaledWidth + 8, scaledHeight + 8, 8, 8);
            }

            //Button background
            gc.setFill(Color.rgb(40, 45, 55, 0.95));
            gc.fillRoundRect(buttonX, buttonY, scaledWidth, scaledHeight, 6, 6);

            //Button border
            gc.setStroke(isSelected ? Color.rgb(220, 225, 230) : Color.rgb(140, 150, 160));
            gc.setLineWidth(isSelected ? 3 : 2);
            gc.strokeRoundRect(buttonX, buttonY, scaledWidth, scaledHeight, 6, 6);

            //Inner border
            gc.setStroke(isSelected ? Color.rgb(160, 170, 180, 0.5) : Color.rgb(100, 110, 120, 0.5));
            gc.setLineWidth(2);
            gc.strokeRoundRect(buttonX + 2, buttonY + 2, scaledWidth - 4, scaledHeight - 4, 5, 5);

            //Button text
            gc.setFont(isSelected ? cinzelButtonSelected : cinzelButton);

            //Text shadow
            gc.setFill(Color.rgb(0, 0, 0, 0.8));
            gc.fillText(menuOptions[i], centerX + 1, y + 4);

            //Main text
            gc.setFill(isSelected ? Color.WHITE : Color.rgb(210, 215, 220));
            gc.fillText(menuOptions[i], centerX, y + 3);

            //Draw instructions at the bottom
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            gc.setFill(Color.rgb(200, 200, 200, 0.8));
            gc.fillText("Press W/S or ↑/↓ to navigate  |  ENTER to select  |  ESC to exit", 
                   centerX, height - 15);
        }
    }

    @Override
    public void exit() {
        //Stop menu music when exiting
        AudioController audioController = gameManager.getEventManager().getAudioController();
        if (audioController != null) {
            audioController.stopBGM();
        }
    }

    private void handleSelection() {
        switch(selectedOption) {
            case 0:// Start Game
                gameManager.resetGame();
                gameManager.changeState(new StoryState(gameManager, gameManager.getCurrentLevelNumber()));
                break;
            case 1:// High Scores
                gameManager.changeState(new HighScoreState(gameManager));
                break;
            case 2:// Exit
                System.exit(0);
                break;
        }
    }

}
