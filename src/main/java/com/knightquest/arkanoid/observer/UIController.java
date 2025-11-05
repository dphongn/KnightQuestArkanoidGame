package com.knightquest.arkanoid.observer;

import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.powerup.PowerUp;

/**
 * UI Controller - Observes game events and updates the user interface.
 * Implements GameEventListener to react to game state changes.
 */
public class UIController implements GameEventListener {
    
    private int currentScore;
    private int currentLives;
    private String statusMessage;
    
    public UIController() {
        this.currentScore = 0;
        this.currentLives = 3;
        this.statusMessage = "Ready!";
    }
    
    @Override
    public void onBrickDestroyed(Brick brick, int score) {
        // Update UI elements
        String brickType = brick.getClass().getSimpleName();
        System.out.println("Brick destroyed: " + brickType + " (+" + score + " points)");
        
        // Could trigger visual effects like particle explosions
        // updateParticleEffect(brick.getX(), brick.getY());
    }
    
    @Override
    public void onPowerUpCollected(PowerUp powerUp) {
        // Display power-up notification
        statusMessage = "Power-up collected: " + powerUp.getType();
        System.out.println(statusMessage);
        
        // Could show floating text or icon animation
        // showPowerUpNotification(powerUp);
    }
    
    @Override
    public void onLifeLost(int remainingLives) {
        this.currentLives = remainingLives;
        statusMessage = "Life lost! Remaining: " + remainingLives;
        System.out.println(statusMessage);
        
        // Update hearts/lives display
        // updateLivesDisplay(remainingLives);
        
        if (remainingLives == 1) {
            // Show warning effect
            System.out.println("WARNING: Last life!");
        }
    }
    
    @Override
    public void onLevelCompleted(int levelNumber, int score) {
        statusMessage = "Level " + levelNumber + " completed! Score: " + score;
        System.out.println(statusMessage);
        
        // Show level complete screen
        // displayLevelCompleteScreen(levelNumber, score);
    }
    
    @Override
    public void onGameOver(boolean won, int finalScore) {
        if (won) {
            statusMessage = "Victory! Princess Evelyn is saved! Final Score: " + finalScore;
        } else {
            statusMessage = "Defeat! The kingdom needs you... Final Score: " + finalScore;
        }
        System.out.println(statusMessage);
        
        // Show game over screen
        // displayGameOverScreen(won, finalScore);
    }
    
    @Override
    public void onScoreChanged(int newScore) {
        this.currentScore = newScore;
        // Update score display
        // updateScoreLabel(newScore);
    }
    
    @Override
    public void onExplosion(double x, double y, double radius) {
        System.out.println("BOOM! Explosion at (" + x + ", " + y + ")");
        
        // Trigger explosion visual effect
        // createExplosionEffect(x, y, radius);
        // shakeScreen(radius / 50); // Screen shake based on explosion size
    }
    
    @Override
    public void onPaddleSizeChanged(String eventType) {
        System.out.println("Paddle: " + eventType);
        
        // Update paddle visual indicator
        // updatePaddleIndicator(eventType);
    }

    @Override
    public void onMenuSelectionChanged() {
        // Update menu highlight or sound
        // updateMenuHighlight();
    }

    @Override
    public void onMenuOptionSelected() {
        // Trigger menu option action feedback
        // playMenuSelectSound();
    }
    
    // Getters for UI state
    public int getCurrentScore() {
        return currentScore;
    }
    
    public int getCurrentLives() {
        return currentLives;
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }
    
    public void setStatusMessage(String message) {
        this.statusMessage = message;
    }
}
