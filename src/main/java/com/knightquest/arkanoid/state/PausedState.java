package com.knightquest.arkanoid.state;

import com.knightquest.arkanoid.controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * PausedState handles the paused state of the game.
 */
public class PausedState extends  GameState {
    private int selectedOption = 0; // 0: Resume, 1: Restart, 2: Main Menu
    private final String[] menuOptions = {"Resume", "Restart", "Main Menu"};

    public PausedState(GameManager gameManager) {
        super(gameManager);
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

        switch (event.getCode()) {
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

        // Draw pause menu
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        // Semi-transparent overlay
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, width, height);
        double centerX = width / 2;
        double centerY = height / 2;

        // Draw pause box background
        double boxWidth = 400;
        double boxHeight = 300;
        gc.setFill(Color.rgb(30, 30, 40));
        gc.fillRect(centerX - boxWidth / 2, centerY - boxHeight / 2, boxWidth, boxHeight);

        //Draw pause box border
        gc.setStroke(Color.GOLD);
        gc.setLineWidth(3);
        gc.strokeRect(centerX - boxWidth / 2, centerY - boxHeight / 2, boxWidth, boxHeight);

        // Draw title
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Arial", 42));
        gc.setFill(Color.GOLD);
        gc.fillText("PAUSED", centerX, centerY - 80);

        // Draw menu options
        gc.setFont(Font.font("Arial", 28));
        for (int i = 0; i < menuOptions.length; i++) {
            double y = centerY - 10 + i * 50;

            if (i == selectedOption) {
                gc.setFill(Color.GOLD);
                gc.fillText("> " + menuOptions[i] + " <", centerX, y);
            } else {
                gc.setFill(Color.LIGHTGRAY);
                gc.fillText(menuOptions[i], centerX, y);
            }
        }

        // Draw instructions
        gc.setFont(Font.font("Arial", 14));
        gc.setFill(Color.GRAY);
        gc.fillText("Use W/S or Up/Down to navigate | Enter to select | ESC to resume", centerX, centerY + boxHeight/2 - 20);

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
