package com.knightquest.arkanoid.state;

import com.knightquest.arkanoid.controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.io.InputStream;

/**
 * PausedState handles the paused state of the game.
 */
public class PausedState extends  GameState {
    private int selectedOption = 0; // 0: Resume, 1: Restart, 2: Main Menu
    private final String[] menuOptions = {"Resume", "Restart", "Main Menu"};

    private Font cinzelTitle;
    private Font cinzelButton;
    private Font cinzelButtonSelected;

    public PausedState(GameManager gameManager) {
        super(gameManager);
        loadFonts();
    }

    private void loadFonts() {
        String cinzelPath = "/fonts/Cinzel-Regular.ttf";

        try {
            InputStream titleStream = getClass().getResourceAsStream(cinzelPath);
            if (titleStream != null) {
                cinzelTitle = Font.loadFont(titleStream, 40);
            } else {
                throw new Exception("Font file not found: " + cinzelPath);
            }

            InputStream buttonStream = getClass().getResourceAsStream(cinzelPath);
            if (buttonStream != null) {
                cinzelButton = Font.loadFont(buttonStream, 20);
                cinzelButtonSelected = Font.loadFont(buttonStream, 24);
            } else {
                throw new Exception("Font file not found: " + cinzelPath);
            }
        } catch (Exception e) {
            System.err.println("Error loading fonts: " + e.getMessage());
            // Fallback to default fonts
            cinzelTitle = Font.font("Arial", 48);
            cinzelButton = Font.font("Arial", 16);
            cinzelButtonSelected = Font.font("Arial", 18);
        }
    }

    @Override
    public void enter() {
        System.out.println("Game Paused");
        selectedOption = 0;
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void handleInput(KeyEvent event) {
        if (event.getEventType() != KeyEvent.KEY_PRESSED) {
            return;
        }

        KeyCode code = event.getCode();

        switch (code) {
            case UP:
            case W:
                selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
                break;
            case DOWN:
            case S:
                selectedOption = (selectedOption + 1) % menuOptions.length;
                break;
            case ENTER:
            case SPACE:
                handleSelection();
                break;
            case ESCAPE:
                // Resume game
                changeState(new PlayingState(gameManager));
                break;
            case R:
                // Restart game
                gameManager.restartLevel();
                changeState(new PlayingState(gameManager));
                break;
            case M:
                // Go to main menu
                changeState(new MenuState(gameManager));
                break;
            default:
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // Dim the background
        rendeGameBackground(gc);

        // Semi-transparent overlay
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        double centerX = gc.getCanvas().getWidth() / 2;
        double centerY = gc.getCanvas().getHeight() / 2;

        // Draw pause box background (Dark Stone Color)
        double boxWidth = 400;
        double boxHeight = 300;
        gc.setFill(Color.rgb(40, 45, 55, 0.95));
        gc.fillRoundRect(centerX - boxWidth / 2, centerY - boxHeight / 2, boxWidth, boxHeight, 10, 10);

        // Draw pause box border (Silver/Metal)
        gc.setStroke(Color.rgb(140, 150, 160));
        gc.setLineWidth(3);
        gc.strokeRoundRect(centerX - boxWidth / 2, centerY - boxHeight / 2, boxWidth, boxHeight, 10, 10);
        
        // Inner border highlight
        gc.setStroke(Color.rgb(100, 110, 120, 0.5));
        gc.setLineWidth(2);
        gc.strokeRoundRect(centerX - boxWidth / 2 + 3, centerY - boxHeight / 2 + 3, boxWidth - 6, boxHeight - 6, 8, 8);


        // Draw title
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(cinzelTitle);
        gc.setFill(Color.rgb(255, 245, 200));
        gc.fillText("PAUSED", centerX, centerY - 80);

        // Draw menu options
        gc.setFont(cinzelButton);
        for (int i = 0; i < menuOptions.length; i++) {
            double y = centerY - 10 + i * 50;
            boolean isSelected = (i == selectedOption);

            gc.setFont(isSelected ? cinzelButtonSelected : cinzelButton);

            if (isSelected) {
                // Highlight for selected option
                gc.setFill(Color.rgb(200, 210, 220, 0.25)); 
                gc.fillRoundRect(centerX - 150, y - 20, 300, 30, 8, 8);
                gc.setFill(Color.WHITE);
                gc.fillText("> " + menuOptions[i] + " <", centerX, y);
            } else {
                gc.setFill(Color.rgb(210, 215, 220));
                gc.fillText(menuOptions[i], centerX, y);
            }
        }

        // Draw instructions
        gc.setFont(Font.font("Arial", 14));
        gc.setFill(Color.GRAY);
        gc.fillText("Use W/S or ↑/↓ to navigate | Enter to select | ESC to resume", centerX, centerY + boxHeight/2 - 20);
    }

    @Override
    public void exit() {
        System.out.println("Resuming Game");
    }
    private void handleSelection() {
        switch (selectedOption) {
            case 0: // Resume
                changeState(new PlayingState(gameManager));
                break;
            case 1: // Restart
                gameManager.restartLevel();
                changeState(new PlayingState(gameManager));
                break;
            case 2: // Main Menu
                changeState(new MenuState(gameManager));
                break;
        }
    }
    private void rendeGameBackground(GraphicsContext gc) {
        // Render frozen game state as background
        gc.setFill(Color.rgb(15, 15, 25));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        //Draw bricks
        gc.setGlobalAlpha(0.5);
        gameManager.getBricks().forEach(brick -> {
            if(brick.isActive()) {
                brick.render(gc);
            }
        });

        // Draw paddle
        var paddle = gameManager.getPaddle();
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

        // Draw ball
        var ball = gameManager.getBall();
        gc.setFill(Color.WHITE);
        double ballSize = 10;
        gc.fillOval(ball.getX() - ballSize / 2, ball.getY() - ballSize / 2, ballSize, ballSize);

        gc.setGlobalAlpha(1.0);
    }
}
