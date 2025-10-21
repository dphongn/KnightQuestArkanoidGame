package com.knightquest.arkanoid.state;

import com.knightquest.arkanoid.controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.TextAlignment;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * MenuState handles the main menu of the game.
 */
public class MenuState extends GameState {
    private int selectedOption = 0;
    private final String[] menuOptions = {"Start Game", "Exit"};
    private double animationTimer = 0;

    public MenuState(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void enter() {
        System.out.println("Entering MenuState");
        selectedOption = 0;
        animationTimer = 0;

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

        switch(event.getCode()) {
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
                System.exit(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        //Dark background
        gc.setFill(Color.rgb(20, 20, 20));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        double centerX = gc.getCanvas().getWidth() / 2;
        double centerY = gc.getCanvas().getHeight() / 2;

        // Title
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Arial", 48));

        double pulse = Math.sin(animationTimer * 3) * 0.1 + 1.0;
        gc.setFill(Color.GOLD);
        gc.setGlobalAlpha(pulse);
        gc.fillText("KNIGHT'S QUEST", centerX, centerY - 100);
        gc.fillText("ARKANOID", centerX, centerY - 50);
        gc.setGlobalAlpha(1.0);

        // Menu Options
        gc.setFont(Font.font("Arial", 32));
        for(int i = 0; i < menuOptions.length; i++) {
            double y = centerY + 50 + i * 60;
            if(i == selectedOption) {
                gc.setFill(Color.YELLOW);
                gc.fillText("> " + menuOptions[i] + " <", centerX, y);
            } else {
                gc.setFill(Color.LIGHTGRAY);
                gc.fillText(menuOptions[i], centerX, y);
            }
        }

        // Instructions
        gc.setFont(Font.font("Arial", 16));
        gc.setFill(Color.GRAY);
        gc.fillText("Use W/S or Up/Down to navigate", centerX, gc.getCanvas().getHeight() - 80);
        gc.fillText("Press Enter or Space to select", centerX, gc.getCanvas().getHeight() - 60);
        gc.fillText("Press Esc to exit", centerX, gc.getCanvas().getHeight() - 40);
    }

    @Override
    public void exit() {
    }

    private void handleSelection() {
        switch(selectedOption) {
            case 0:// Start Game
                gameManager.resetGame();
                gameManager.changeState(new PlayingState(gameManager));
                break;
            case 1:// Exit
                System.exit(0);
                break;
        }
    }

}
