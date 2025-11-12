package com.knightquest.arkanoid.thread;

import com.knightquest.arkanoid.controller.GameManager;
import javafx.scene.input.KeyEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Game loop thread chạy riêng biệt để xử lý game logic
 * Đây là implementation đơn giản cho multithreading
 */
public class GameLoop extends Thread {
    private final GameManager gameManager;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final BlockingQueue<KeyEvent> inputQueue = new LinkedBlockingQueue<>();

    // Target FPS cho game logic
    private static final double TARGET_UPS = 60.0; // Updates per second
    private static final long TARGET_TIME_NS = (long)(1_000_000_000.0 / TARGET_UPS);

    public GameLoop(GameManager gameManager) {
        this.gameManager = gameManager;
        setName("GameLoop-Thread");
        setDaemon(true); // Thread sẽ tự động dừng khi main thread kết thúc
    }

    @Override
    public void run() {
        System.out.println("🎮 GameLoop thread started");

        long lastTime = System.nanoTime();

        while (running.get()) {
            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - lastTime) / 1_000_000_000.0;

            // Xử lý input events
            processInputEvents();

            // Update game logic
            try {
                gameManager.update(deltaTime);
            } catch (Exception e) {
                System.err.println("❌ Error in game update: " + e.getMessage());
                // Log error but continue running
            }

            lastTime = currentTime;

            // Maintain target update rate
            long frameTime = System.nanoTime() - currentTime;
            long sleepTime = TARGET_TIME_NS - frameTime;

            if (sleepTime > 0) {
                try {
                    long sleepMs = sleepTime / 1_000_000;
                    int sleepNs = (int)(sleepTime % 1_000_000);
                    Thread.sleep(sleepMs, sleepNs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        System.out.println("🛑 GameLoop thread stopped");
    }

    /**
     * Xử lý tất cả input events trong queue
     */
    private void processInputEvents() {
        KeyEvent event;
        while ((event = inputQueue.poll()) != null) {
            try {
                gameManager.handleInput(event);
            } catch (Exception e) {
                System.err.println("❌ Error processing input: " + e.getMessage());
            }
        }
    }

    /**
     * Thêm input event vào queue (được gọi từ JavaFX thread)
     */
    public void addInputEvent(KeyEvent event) {
        if (running.get()) {
            inputQueue.offer(event);
        }
    }

    /**
     * Dừng game loop một cách an toàn
     */
    public void shutdown() {
        System.out.println("🛑 Shutting down GameLoop thread...");
        running.set(false);
        interrupt(); // Wake up nếu đang sleep
    }

    /**
     * Kiểm tra xem thread có đang chạy không
     */
    public boolean isRunning() {
        return running.get() && isAlive();
    }
}
