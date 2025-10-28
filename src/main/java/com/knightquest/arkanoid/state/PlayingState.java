package com.knightquest.arkanoid.state;

import com.knightquest.arkanoid.controller.GameManager;
import com.knightquest.arkanoid.model.Ball;
import com.knightquest.arkanoid.model.Paddle;
import com.knightquest.arkanoid.model.brick.Brick;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import static com.knightquest.arkanoid.util.Constants.*;

/**
 * PlayingState handles the main gameplay.
 */

public class PlayingState extends GameState {
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public PlayingState(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void enter() {
        System.out.println("Entering PlayingState");
    }

    @Override
    public void update(double deltaTime) {
        // Update paddle movement based on key presses
        Paddle paddle = gameManager.getPaddle();
        Ball ball = gameManager.getBall();

        double paddleOldx = paddle.getX();

        if (leftPressed && !rightPressed) {
            paddle.moveLeft();
        } else if (rightPressed && !leftPressed) {
            paddle.moveRight();
        } else {
            paddle.stop(); // Stop when no keys pressed or both pressed
        }

        if (ball.isStuckToPaddle()) {
            double paddleDx = paddle.getX() - paddleOldx;
            ball.updateStuckOffset(paddleDx);
            ball.stickToPaddle(paddle);
        }

        // Update game logic
        gameManager.updateGameLogic(deltaTime);

        // Check for win/loss conditions
        if (gameManager.getBricks().isEmpty()) {
            handleLevelComplete();
        }

        if (gameManager.getLives() <= 0) {
            changeState(new GameOverState(gameManager));
        }

    }


    @Override
    public void handleInput(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        boolean keyPressed = event.getEventType() == KeyEvent.KEY_PRESSED;

        //Handle pause
        if (keyPressed && keyCode == KeyCode.ESCAPE) {
            changeState(new PausedState(gameManager));
            return;
        }

        // Handle ball launch (Enter or Space)
        Ball ball = gameManager.getBall();
        if (keyPressed && (keyCode == KeyCode.SPACE || keyCode == KeyCode.ENTER)) {
            if (ball.isStuckToPaddle()) {
                ball.launch();
                return;
            }
        }

        // Handle paddle movement keys
        switch (keyCode) {
            case LEFT:
            case A:
                leftPressed = keyPressed;
                break;
            case  RIGHT:
            case D:
                rightPressed = keyPressed;
                break;
            default:
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // Clear screen
        gc.setFill(Color.rgb(15 ,15 ,25));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        //Draw bricks
        for (Brick brick : gameManager.getBricks()) {
            if(brick.isActive()) {
                brick.render(gc);
            }
        }

        // Draw paddle
        Paddle paddle = gameManager.getPaddle();
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

        // Draw ball
        Ball ball = gameManager.getBall();
//        gc.setFill(Color.WHITE);
//        gc.fillOval(ball.getX() - ball.getRadius(), ball.getY() - ball.getRadius(),
//                ball.getRadius() * 2, ball.getRadius() * 2);
//
//        // Draw score and lives
        ball.render(gc);
        gameManager.getPowerUpManager().render(gc);
        drawUI(gc);
    }

    @Override
    public void exit() {
        leftPressed = false;
        rightPressed = false;
    }

    public void drawUI(GraphicsContext gc) {
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(Font.font("Arial", 20));
        gc.setFill(Color.WHITE);

        // Draw score
        gc.fillText("Score: " + gameManager.getScore(), 20, 30);

        // Draw lives
        StringBuilder livesStr = new StringBuilder("Lives: ");
        for (int i = 0; i < gameManager.getLives(); i++) {
            livesStr.append("♥ ");
        }
        gc.fillText(livesStr.toString(), 20, 55);

        // Draw level
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Arial", 18));
        gc.setFill(Color.GOLD);
        String levelText = "Level " + gameManager.getCurrentLevelNumber() + ": " + gameManager.getCurrentLevelName();
        gc.fillText(levelText, gc.getCanvas().getWidth() / 2, 30);

        // Draw instructions
        gc.setFont(Font.font("Arial", 14));
        gc.setFill(Color.GRAY);
        gc.fillText("<- Move Left    -> Move Right    ESC - Pause", gc.getCanvas().getWidth() / 2,gc.getCanvas().getHeight() - 20);
    }

    private void handleLevelComplete() {
        System.out.println("Level Complete");
        // Transition to LevelCompleteState
        changeState(new LevelCompleteState(gameManager));
    }
}
