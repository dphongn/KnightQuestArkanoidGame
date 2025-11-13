package com.knightquest.arkanoid.manager;

import com.knightquest.arkanoid.model.data.HighScoreEntry;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages high scores with offline file persistence.
 * Saves/loads high scores to/from a local JSON-like text file.
 * 
 * Singleton Pattern: Only one instance manages all high scores.
 */
public class HighScoreManager {
    private static HighScoreManager instance;
    
    private static final String HIGH_SCORE_FILE = "highscores.dat";
    private static final int MAX_HIGH_SCORES = 10;
    
    private List<HighScoreEntry> highScores;
    private Path savePath;
    
    /**
     * Private constructor (Singleton).
     */
    private HighScoreManager() {
        highScores = new ArrayList<>();
        initializeSavePath();
        loadHighScores();
    }
    
    /**
     * Get singleton instance.
     * 
     * @return The high score manager instance
     */
    public static HighScoreManager getInstance() {
        if (instance == null) {
            instance = new HighScoreManager();
        }
        return instance;
    }
    
    /**
     * Initialize the save file path.
     * Uses user's home directory for cross-platform compatibility.
     */
    private void initializeSavePath() {
        try {
            // Save in user home directory under .knightquest folder
            String userHome = System.getProperty("user.home");
            Path gameDir = Paths.get(userHome, ".knightquest");
            
            // Create directory if it doesn't exist
            if (!Files.exists(gameDir)) {
                Files.createDirectories(gameDir);
            }
            
            savePath = gameDir.resolve(HIGH_SCORE_FILE);
        } catch (Exception e) {
            System.err.println("Error initializing save path: " + e.getMessage());
            // Fallback to current directory
            savePath = Paths.get(HIGH_SCORE_FILE);
        }
    }
    
    /**
     * Load high scores from file.
     */
    private void loadHighScores() {
        if (!Files.exists(savePath)) {
            System.out.println("No existing high scores file found.");
            return;
        }
        
        try (BufferedReader reader = Files.newBufferedReader(savePath)) {
            highScores.clear();
            String line;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }
                
                HighScoreEntry entry = parseEntry(line);
                if (entry != null) {
                    highScores.add(entry);
                }
            }
            
            // Sort entries
            Collections.sort(highScores);
            
            System.out.println("Loaded " + highScores.size() + " high scores from file.");
        } catch (IOException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
        }
    }
    
    /**
     * Parse a single high score entry from a line.
     * Format: playerName|score|level|timestamp
     * 
     * @param line The line to parse
     * @return Parsed entry or null if invalid
     */
    private HighScoreEntry parseEntry(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length != 4) {
                return null;
            }
            
            String playerName = parts[0];
            int score = Integer.parseInt(parts[1]);
            int level = Integer.parseInt(parts[2]);
            LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
            
            return new HighScoreEntry(playerName, score, level, timestamp);
        } catch (Exception e) {
            System.err.println("Error parsing high score entry: " + line);
            return null;
        }
    }
    
    /**
     * Save high scores to file.
     */
    private void saveHighScores() {
        try (BufferedWriter writer = Files.newBufferedWriter(savePath)) {
            // Write header comment
            writer.write("# Knight Quest Arkanoid High Scores\n");
            writer.write("# Format: PlayerName|Score|Level|Timestamp\n");
            writer.write("# Generated: " + LocalDateTime.now() + "\n");
            writer.write("\n");
            
            // Write each entry
            for (HighScoreEntry entry : highScores) {
                String line = String.format("%s|%d|%d|%s\n",
                        entry.getPlayerName(),
                        entry.getScore(),
                        entry.getLevelReached(),
                        entry.getTimestampString());
                writer.write(line);
            }
            
            System.out.println("Saved " + highScores.size() + " high scores to file.");
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }
    
    /**
     * Add a new high score entry.
     * 
     * @param playerName Name of the player
     * @param score Score achieved
     * @param levelReached Highest level reached
     * @return true if score made it to top 10, false otherwise
     */
    public boolean addHighScore(String playerName, int score, int levelReached) {
        HighScoreEntry newEntry = new HighScoreEntry(playerName, score, levelReached);
        
        // Add and sort
        highScores.add(newEntry);
        Collections.sort(highScores);
        
        // Keep only top MAX_HIGH_SCORES
        boolean madeIt = true;
        if (highScores.size() > MAX_HIGH_SCORES) {
            if (highScores.indexOf(newEntry) >= MAX_HIGH_SCORES) {
                madeIt = false;
            }
            highScores = highScores.subList(0, MAX_HIGH_SCORES);
        }
        
        // Save to file
        saveHighScores();
        
        return madeIt;
    }
    
    /**
     * Check if a score qualifies for high scores list.
     * 
     * @param score Score to check
     * @return true if score would make top 10
     */
    public boolean isHighScore(int score) {
        if (highScores.size() < MAX_HIGH_SCORES) {
            return true; // List not full yet
        }
        
        // Check if score beats the lowest high score
        return score > highScores.get(highScores.size() - 1).getScore();
    }
    
    /**
     * Get the list of high scores.
     * 
     * @return Unmodifiable list of high scores
     */
    public List<HighScoreEntry> getHighScores() {
        return Collections.unmodifiableList(highScores);
    }
    
    /**
     * Get the rank of a score (1-based).
     * 
     * @param score Score to rank
     * @return Rank (1 = highest), or -1 if doesn't make top 10
     */
    public int getRank(int score) {
        for (int i = 0; i < highScores.size(); i++) {
            if (score > highScores.get(i).getScore()) {
                return i + 1;
            }
        }
        
        if (highScores.size() < MAX_HIGH_SCORES) {
            return highScores.size() + 1;
        }
        
        return -1; // Doesn't make top 10
    }
    
    /**
     * Clear all high scores (for testing).
     */
    public void clearHighScores() {
        highScores.clear();
        saveHighScores();
    }
    
    /**
     * Get maximum number of high scores stored.
     * 
     * @return Maximum number of entries
     */
    public int getMaxHighScores() {
        return MAX_HIGH_SCORES;
    }
}
