package com.knightquest.arkanoid.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Audio thread đơn giản để xử lý âm thanh không blocking game
 */
public class SimpleAudioThread extends Thread {
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final BlockingQueue<AudioCommand> audioQueue = new LinkedBlockingQueue<>();

    public SimpleAudioThread() {
        setName("Audio-Thread");
        setDaemon(true);
    }

    @Override
    public void run() {
        System.out.println("🔊 Audio thread started");

        while (running.get()) {
            try {
                AudioCommand command = audioQueue.poll();
                if (command != null) {
                    processAudioCommand(command);
                } else {
                    // Sleep một chút để không waste CPU
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("❌ Error in audio thread: " + e.getMessage());
            }
        }

        System.out.println("🔇 Audio thread stopped");
    }

    private void processAudioCommand(AudioCommand command) {
        try {
            switch (command.getType()) {
                case PLAY_SOUND:
                    // Đây sẽ play sound effect
                    System.out.println("🎵 Playing sound: " + command.getSoundName());
                    break;
                case PLAY_MUSIC:
                    // Đây sẽ play background music
                    System.out.println("🎶 Playing music: " + command.getSoundName());
                    break;
                case STOP_MUSIC:
                    // Stop current music
                    System.out.println("⏹️ Stopping music");
                    break;
            }
        } catch (Exception e) {
            System.err.println("❌ Error processing audio command: " + e.getMessage());
        }
    }

    /**
     * Play sound effect (non-blocking)
     */
    public void playSound(String soundName) {
        if (running.get()) {
            audioQueue.offer(new AudioCommand(AudioCommand.Type.PLAY_SOUND, soundName));
        }
    }

    /**
     * Play background music (non-blocking)
     */
    public void playMusic(String musicName) {
        if (running.get()) {
            audioQueue.offer(new AudioCommand(AudioCommand.Type.PLAY_MUSIC, musicName));
        }
    }

    /**
     * Stop music (non-blocking)
     */
    public void stopMusic() {
        if (running.get()) {
            audioQueue.offer(new AudioCommand(AudioCommand.Type.STOP_MUSIC, null));
        }
    }

    /**
     * Shutdown audio thread
     */
    public void shutdown() {
        System.out.println("🔇 Shutting down audio thread...");
        running.set(false);
        interrupt();
    }

    /**
     * Audio command class
     */
    public static class AudioCommand {
        public enum Type {
            PLAY_SOUND, PLAY_MUSIC, STOP_MUSIC
        }

        private final Type type;
        private final String soundName;

        public AudioCommand(Type type, String soundName) {
            this.type = type;
            this.soundName = soundName;
        }

        public Type getType() {
            return type;
        }

        public String getSoundName() {
            return soundName;
        }
    }
}
