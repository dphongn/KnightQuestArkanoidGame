package com.knightquest.arkanoid;

import com.knightquest.arkanoid.controller.GameManager;
import static com.knightquest.arkanoid.util.Constants.SCREEN_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.SCREEN_WIDTH;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private GameManager gameManager;
    private long lastTime = 0;
    private static final double TARGET_FPS = 60.0;
    private static final double TARGET_FRAME_TIME = 1_000_000_000.0 / TARGET_FPS;

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        gameManager = GameManager.getInstance();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) gameManager.getPaddle().moveLeft();
            if (e.getCode() == KeyCode.RIGHT) gameManager.getPaddle().moveRight();
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT) {
                gameManager.getPaddle().stop();
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) / 1_000_000_000.0;

                // Cap delta time to prevent large jumps (e.g., when window loses focus)
                deltaTime = Math.min(deltaTime, TARGET_FRAME_TIME * 3 / 1_000_000_000.0);

                lastTime = now;

                gameManager.update(deltaTime);
                render();
            }
        }.start();

        stage.setTitle("Knight's Quest Arkanoid");
        stage.setScene(scene);
        stage.show();
    }

    private void render() {
        // Clear screen
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Render game objects
        gameManager.getPaddle().render(gc);
        gameManager.getBall().render(gc);
        gameManager.getBricks().forEach(brick -> {
            if (brick.isActive()) {
                brick.render(gc);
            }
        });

        // Render UI text with better formatting
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", 16));
        gc.fillText("Score: " + gameManager.getScore(), 10, 25);
        gc.fillText("Lives: " + gameManager.getLives(), SCREEN_WIDTH - 80, 25);

        // Game over or win message
        if (gameManager.getLives() <= 0) {
            gc.setFont(javafx.scene.text.Font.font("Arial", 48));
            gc.setFill(Color.RED);
            gc.fillText("GAME OVER", SCREEN_WIDTH/2 - 120, SCREEN_HEIGHT/2);
        } else if (gameManager.getBricks().isEmpty()) {
            gc.setFont(javafx.scene.text.Font.font("Arial", 48));
            gc.setFill(Color.GREEN);
            gc.fillText("YOU WIN!", SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT/2);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
