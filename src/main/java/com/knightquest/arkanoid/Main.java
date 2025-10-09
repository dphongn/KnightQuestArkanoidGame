package com.knightquest.arkanoid;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.knightquest.arkanoid.controller.GameManager;
import static com.knightquest.arkanoid.util.Constants.*;

public class Main extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private GameManager gameManager;
    private long lastTime = 0;

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

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        gameManager.getPaddle().render(gc);
        gameManager.getBall().render(gc);
        gameManager.getBricks().forEach(b -> b.render(gc));

        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + gameManager.getScore(), 10, 20);
        gc.fillText("Lives: " + gameManager.getLives(), 700, 20);
    }

    public static void main(String[] args) {
        launch();
    }
}
