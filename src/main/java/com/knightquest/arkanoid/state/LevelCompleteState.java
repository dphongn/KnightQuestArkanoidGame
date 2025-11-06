package com.knightquest.arkanoid.state;

import com.knightquest.arkanoid.controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import com.knightquest.arkanoid.observer.GameEventManager;

/**
 * LevelCompleteState handles the state when a level is completed.
 */
public class LevelCompleteState extends GameState {
    private int selectedOption = 0; // 0: Continue, 1: Replay, 2: Main Menu
    private final String[] menuOptions = {"CONTINUE TO NEXT LEVEL", "REPLAY THIS LEVEL", "MAIN MENU"};
    private final int completedLevelNumber;
    private final String completedLevelName;
    private final int currentScore;
    private final int currentLives;

    public LevelCompleteState(GameManager gameManager) {
        super(gameManager);
        this.completedLevelNumber = gameManager.getCurrentLevelNumber();
        this.completedLevelName = gameManager.getCurrentLevelName();
        this.currentScore = gameManager.getScore();
        this.currentLives = gameManager.getLives();
    }

    @Override
    public void enter() {
        System.out.println("Entering level complete state");
        selectedOption = 0;
    }

    @Override
    public void update(double deltaTime) {
        // No dynamic elements to update in this state for now
    }

    @Override
    public void handleInput(KeyEvent event) {
        if (event.getEventType() != KeyEvent.KEY_PRESSED) {
            return;
        }

        GameEventManager eventManager= gameManager.getEventManager();

        switch (event.getCode()) {
            case UP:
            case W:
                selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
                if (eventManager != null) eventManager.notifyMenuSelectionChanged();
                break;
            case DOWN:
            case S:
                selectedOption = (selectedOption + 1) % menuOptions.length;
                if (eventManager != null) eventManager.notifyMenuSelectionChanged();
                break;
            case ENTER:
            case SPACE:
                if (eventManager != null) eventManager.notifyMenuOptionSelected();
                handleSelection();
                break;
            case C:
                if (eventManager != null) eventManager.notifyMenuOptionSelected();
                continueToNextLevel();
                break;
            case R:
                if (eventManager != null) eventManager.notifyMenuOptionSelected();
                replayLevel();
                break;
            case M:
            case ESCAPE:
                if (eventManager != null) eventManager.notifyMenuOptionSelected();
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

        // Background
        gc.setFill(Color.rgb(10, 20, 35));
        gc.fillRect(0, 0, width, height);

        double centerX = width / 2;
        double centerY = height / 2;

        // Draw box background
        double boxWidth = 500;
        double boxHeight = 450;
        gc.setFill(Color.rgb(30, 40, 55, 0.95));
        gc.fillRect(centerX - boxWidth / 2, centerY - boxHeight / 2, boxWidth, boxHeight);

        // Draw box border
        gc.setStroke(Color.GOLD);
        gc.setLineWidth(4);
        gc.strokeRect(centerX - boxWidth / 2, centerY - boxHeight / 2, boxWidth, boxHeight);

        // Inner border for effect
        gc.setStroke(Color.rgb(255, 223, 0, 0.5));
        gc.setLineWidth(2);
        gc.strokeRect(centerX - boxWidth / 2 + 5, centerY - boxHeight / 2 + 5, boxWidth - 10, boxHeight-10);

        // Draw celebration icon
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        gc.setFill(Color.GOLD);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("🎉", centerX, centerY - 140);

        // Draw Title
        gc.setFont(Font.font("Arial", 40));
        gc.setFill(Color.GOLD);
        gc.fillText("LEVEL COMPLETE!", centerX, centerY - 90);

        // Draw Level Info
        gc.setFont(Font.font("Arial", 24));
        gc.setFill(Color.LIGHTBLUE);
        gc.fillText("Level " + completedLevelNumber + ": " + completedLevelName, centerX, centerY - 50);

        // Draw Score and Lives
        gc.setFont(Font.font("Arial", 20));
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + currentScore, centerX, centerY - 15);

        StringBuilder livesStr = new StringBuilder("Lives: ");
        for (int i = 0; i < currentLives; i++) {
            livesStr.append("♥ ");
        }
        gc.fillText(livesStr.toString(), centerX, centerY + 15);

        // Draw separator line
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(2);
        gc.strokeLine(centerX - 200, centerY + 35, centerX + 200, centerY + 35);

        // Draw menu options
        gc.setFont(Font.font("Arial", 24));
        for (int i = 0; i < menuOptions.length; i++) {
            double y = centerY + 75 + i * 45;

            if (i == selectedOption) {
                gc.setFill(Color.rgb(255, 215, 0, 0.3));
                gc.fillRect(centerX - 220, y - 30, 440, 40);

                gc.setFill(Color.GOLD);
                gc.fillText("> " + menuOptions[i] + " <", centerX, y);
            } else {
                gc.setFill(Color.LIGHTGRAY);
                gc.fillText(menuOptions[i], centerX, y);
            }
        }

        // Draw instructions
        gc.setFont(Font.font("Arial", 10));
        gc.setFill(Color.GRAY);
        gc.fillText("Use UP/DOWN or W/S to navigate, ENTER/SPACE to select, C to continue, R to replay", centerX, centerY + boxHeight / 2 - 20);
    }

    @Override
    public void exit() {
        System.out.println("Exiting level complete state");
    }
    void handleSelection() {
        switch (selectedOption) {
            case 0:
                continueToNextLevel();
                break;
            case 1:
                replayLevel();
                break;
            case 2:
                changeState(new MenuState(gameManager));
                break;
        }
    }

    void continueToNextLevel() {
        System.out.println("Continuing to next level");
        gameManager.nextLevel();
        changeState(new PlayingState(gameManager));
    }

    private void replayLevel() {
        System.out.println("Replaying current level");
        gameManager.restartLevel();
        changeState(new PlayingState(gameManager));
    }
}
