package com.knightquest.arkanoid.observer;

import com.knightquest.arkanoid.model.brick.Brick;
import com.knightquest.arkanoid.model.powerup.PowerUp;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Stop;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Audio Controller - Observes game events and plays corresponding sounds.
 * Implements GameEventListener to react to game events with audio feedback.
 */
public class AudioController implements GameEventListener {
    
    private boolean soundEnabled;
    private double masterVolume;
    private MediaPlayer bgmPlayer;
    
    public AudioController() {
        this.soundEnabled = true;
        this.masterVolume = 0.7;
    }
    
    @Override
    public void onBrickDestroyed(Brick brick, int score) {
        if (!soundEnabled) return;
        
        // Play different sounds based on brick type
        String brickType = brick.getClass().getSimpleName();
        switch (brickType) {
            case "NormalBrick" -> playSound("brick_break.wav");
            case "StrongBrick" -> playSound("strong_brick_hit.wav");
            case "ExplosiveBrick" -> playSound("explosion.wav");
            case "MonsterBrick" -> playSound("monster_defeat.wav");
            case "PrisonerBrick" -> playSound("prisoner_freed.wav");
            case "UnbreakableBrick" -> playSound("metal_clang.wav");
        }
    }

    @Override
    public void onBrickHit(Brick brick) {
        if (!soundEnabled) return;

        String brickType = brick.getClass().getSimpleName();
        
        // Play hit sound for strong bricks
        if ("StrongBrick".equals(brickType)) {
            playSound("strong_brick_hit.wav", masterVolume * 0.6);
        }
    }

    @Override
    public void onBallPaddleCollision() {
        if (!soundEnabled) return;

        // Play paddle hit sound
        playSound("paddle_hit.wav", masterVolume * 0.8);
    }

    @Override
    public void onPowerUpCollected(PowerUp powerUp) {
        if (!soundEnabled) return;

        // Power-up collection sound with pitch variation
        String soundFile = switch (powerUp.getType()) {
            case EXPAND_PADDLE, SLOW_BALL, MAGNET_PADDLE -> "powerup_good.wav";
            case FIRE_BALL, PIERCE_BALL, GUN_PADDLE -> "powerup_attack.wav";
            case FAST_BALL, MULTI_BALL -> "powerup_bad.wav";
            default -> "powerup_collect.wav";
        };

        playSound(soundFile);
    }
    
    @Override
    public void onLifeLost(int remainingLives) {
        if (!soundEnabled) return;
        
        //playSound("life_lost.wav");
        
        if (remainingLives == 1) {
            // Play warning sound
            playSound("warningsound.wav");
        } else if (remainingLives == 0) {
            // No sound here - game over sound will play
        }
    }
    
    @Override
    public void onLevelCompleted(int levelNumber, int score) {
        if (!soundEnabled) return;
        
        if (levelNumber == 7) {
            // Boss defeated - special victory music
            playSound("boss_victory.wav");
        } else {
            // Normal level complete
            playSound("level_complete.wav");
        }
    }
    
    @Override
    public void onGameOver(boolean won, int finalScore) {
        if (!soundEnabled) return;
        
        if (won) {
            // Play victory fanfare
            playSound("game_victory.wav");
            // Could also play story ending narration
        } else {
            // Play defeat sound
            playSound("game_over.wav");
        }
    }
    
    @Override
    public void onScoreChanged(int newScore) {
        // Optional: Play subtle tick sound for score increase
        // Usually not needed as brick destruction already plays sound
    }
    
    @Override
    public void onExplosion(double x, double y, double radius) {
        if (!soundEnabled) return;
        
        // Play explosion sound with volume based on radius
        double volume = Math.min(1.0, radius / 100.0);
        playSound("explosion.wav", volume);
    }
    
    @Override
    public void onPaddleSizeChanged(String eventType) {
        if (!soundEnabled) return;
        
        // Play paddle modification sound
        playSound("paddle_change.wav");
    }
    
    /**
     * Play a sound file at master volume.
     */
    private void playSound(String soundFile) {
        playSound(soundFile, masterVolume);
    }
    
    /**
     * Play a sound file at specified volume.
     * @param soundFile Name of the sound file in resources/sounds/sfx/
     * @param volume Volume level (0.0 to 1.0)
     */
    private void playSound(String soundFile, double volume) {
        try {
        java.net.URL soundURL = getClass().getResource("/sounds/sfx/" + soundFile);
        
        if (soundURL == null) {
            // File not found - just log, don't crash
            System.err.println("⚠️ Sound file not found: /sounds/sfx/" + soundFile);
            return;
        }
        
        String soundPath = soundURL.toString();
        AudioClip clip = new AudioClip(soundPath);
        clip.setVolume(volume);
        clip.play();
        
        System.out.println("🔊 Playing sound: " + soundFile + " at volume " + volume);
    } catch (Exception e) {
        System.err.println("❌ Could not play sound: " + soundFile + " - " + e.getMessage());
    }
    }
    
    /**
     * Play background music for a level.
     */
    public void playLevelMusic(int levelNumber) {
        if (!soundEnabled) return;
        
        String musicFile = switch (levelNumber) {
            case 1 -> "level1_forest.mp3";
            case 2 -> "level2_dungeon.mp3";
            case 3 -> "level3_bazaar.mp3";
            case 4 -> "level4_barracks.mp3";
            case 5 -> "level5_generals.mp3";
            case 6 -> "level6_nobles.mp3";
            case 7 -> "level7_boss.mp3";
            default -> "menu_theme.mp3";
        };
        
        //Stop old music
        stopBGM();
        
        try {
            java.net.URL musicURL = getClass().getResource("/sounds/bgm/" + musicFile);
            
            if (musicURL == null) {
                System.err.println("⚠️ Music file not found: /sounds/bgm/" + musicFile);
                return;
            }
            
            Media media = new Media(musicURL.toString());
            bgmPlayer = new MediaPlayer(media);
            bgmPlayer.setVolume(masterVolume * 0.5); // BGM quieter than SFX
            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever
            bgmPlayer.play();
            
            System.out.println("🎵 Playing BGM: " + musicFile);
        } catch (Exception e) {
            System.err.println("❌ Could not play music: " + musicFile + " - " + e.getMessage());
        }
        
    }

    /**
     * Stop background music.
     */
    public void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.dispose();
            bgmPlayer = null;
        }
    }
    
    /**
     * Stop all sounds and music.
     */
    public void stopAll() {
        System.out.println("Stopping all audio");
        // Stop all MediaPlayers and AudioClips
    }
    
    // Settings
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }
    
    public void setMasterVolume(double volume) {
        this.masterVolume = Math.max(0.0, Math.min(1.0, volume));
    }
    
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    public double getMasterVolume() {
        return masterVolume;
    }

    @Override
    public void onMenuSelectionChanged() {
        if (!soundEnabled) return;
        
        playSound("selectbutton.wav");
    }

    @Override
    public void onMenuOptionSelected() {
        if (!soundEnabled) return;
        
        playSound("confirmbutton.wav");
    }
}
