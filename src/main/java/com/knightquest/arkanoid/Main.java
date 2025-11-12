package com.knightquest.arkanoid;

import com.knightquest.arkanoid.controller.GameManager;
import com.knightquest.arkanoid.thread.GameLoop;
import com.knightquest.arkanoid.thread.SimpleAudioThread;
import static com.knightquest.arkanoid.util.Constants.SCREEN_HEIGHT;
import static com.knightquest.arkanoid.util.Constants.SCREEN_WIDTH;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private GameManager gameManager;

    // Multithreading components
    private GameLoop gameLoop;
    private SimpleAudioThread audioThread;

    @Override
    public void start(Stage stage) {
        System.out.println("🚀 Starting Knight's Quest Arkanoid with Multithreading!");

        // Initialize JavaFX components
        canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        gameManager = GameManager.getInstance();

        // Initialize threads
        gameLoop = new GameLoop(gameManager);
        audioThread = new SimpleAudioThread();

        // Start threads
        gameLoop.start();
        audioThread.start();

        System.out.println("✅ All threads started successfully");

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Input events now go to GameLoop thread
        scene.setOnKeyPressed(e -> gameLoop.addInputEvent(e));
        scene.setOnKeyReleased(e -> gameLoop.addInputEvent(e));

        // JavaFX thread chỉ lo rendering với FPS cao
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Chỉ render, không update game logic
                render();
            }
        }.start();

        stage.setTitle("Knight's Quest Arkanoid (Multithreaded)");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> shutdown());
        stage.show();
    }

    private void render() {
        gameManager.render(gc);
    }

    /**
     * Shutdown tất cả threads khi đóng game
     */
    private void shutdown() {
        System.out.println("🛑 Shutting down application...");

        if (gameLoop != null) {
            gameLoop.shutdown();
        }

        if (audioThread != null) {
            audioThread.shutdown();
        }

        System.out.println("✅ All threads shut down successfully");
    }

    @Override
    public void stop() throws Exception {
        shutdown();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
