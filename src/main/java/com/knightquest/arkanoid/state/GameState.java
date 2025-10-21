package com.knightquest.arkanoid.state;

import com.knightquest.arkanoid.controller.GameManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;

/**
 * Abstract base class for all game states.
 * Defines the contract for state lifecycle methods.
 *
 * State Pattern: Each concrete state handles its own update, render, and input logic.
 */
public abstract class GameState {
    protected GameManager gameManager;

    public GameState(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Called when entering this state.
     * Use this to initialize state-specific resources, reset timers, etc.
     */
    public abstract void enter();

    /**
     * Update game logic for this state.
     * Called every frame.
     *
     * @param deltaTime Time elapsed since last frame (in seconds)
     */
    public abstract void update(double deltaTime);

    /**
     * Handle keyboard input specific to this state.
     *
     * @param event The keyboard event
     */
    public abstract void handleInput(KeyEvent event);

    /**
     * Render state-specific visuals.
     *
     * @param gc The graphics context to draw on
     */
    public abstract void render(GraphicsContext gc);

    /**
     * Called when exiting this state.
     * Use this to cleanup resources, save state, etc.
     */
    public abstract void exit();

    /**
     * Helper method to transition to a new state.
     *
     * @param newState The state to transition to
     */
    protected void changeState(GameState newState) {
        gameManager.changeState(newState);
    }
}
