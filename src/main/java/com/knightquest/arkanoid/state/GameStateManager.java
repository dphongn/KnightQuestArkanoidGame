package com.knightquest.arkanoid.state;

import com.knightquest.arkanoid.controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;

/**
 * Manages game state transitions.
 * Ensures proper enter/exit hooks are called when changing states.
 *
 * Responsibilities:
 * - Hold reference to current state
 * - Handle state transitions with lifecycle management
 * - Delegate update/render/input to current state
 */
public class GameStateManager {
    private GameState currentState;
    private GameManager gameManager;

    public GameStateManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Change to a new state.
     * Calls exit() on old state and enter() on new state.
     *
     * @param newState The state to transition to
     */
    public void changeState(GameState newState) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        currentState.enter();
    }

    /**
     * Update current state logic.
     *
     * @param deltaTime Time elapsed since last frame
     */
    public void update(double deltaTime) {
        if (currentState != null) {
            currentState.update(deltaTime);
        }
    }

    /**
     * Handle input for current state.
     *
     * @param event Keyboard event
     */
    public void handleInput(KeyEvent event) {
        if (currentState != null) {
            currentState.handleInput(event);
        }
    }

    /**
     * Render current state visuals.
     *
     * @param gc Graphics context
     */
    public void render(GraphicsContext gc) {
        if (currentState != null) {
            currentState.render(gc);
        }
    }

    /**
     * Get the current active state.
     *
     * @return Current state
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Initialize the state manager with the starting state (MenuState).
     */
    public void initialize() {
        changeState(new MenuState(gameManager));
    }
}
