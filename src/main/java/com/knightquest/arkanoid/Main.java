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

        scene.setOnKeyPressed(e -> gameManager.handleInput(e));
        scene.setOnKeyReleased(e -> gameManager.handleInput(e));

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
        gameManager.render(gc);
    }

    public static void main(String[] args) {
        launch();
    }
}
