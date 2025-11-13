package com.knightquest.arkanoid.model.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single high score entry.
 * Stores player name, score, level reached, and timestamp.
 */
public class HighScoreEntry implements Comparable<HighScoreEntry> {
    private String playerName;
    private int score;
    private int levelReached;
    private LocalDateTime timestamp;
    
    // For JSON serialization/deserialization
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    /**
     * Default constructor for JSON deserialization.
     */
    public HighScoreEntry() {
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Create a new high score entry.
     * 
     * @param playerName Name of the player
     * @param score Score achieved
     * @param levelReached Highest level reached
     */
    public HighScoreEntry(String playerName, int score, int levelReached) {
        this.playerName = playerName;
        this.score = score;
        this.levelReached = levelReached;
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Full constructor including timestamp (for loading from file).
     * 
     * @param playerName Name of the player
     * @param score Score achieved
     * @param levelReached Highest level reached
     * @param timestamp When the score was achieved
     */
    public HighScoreEntry(String playerName, int score, int levelReached, LocalDateTime timestamp) {
        this.playerName = playerName;
        this.score = score;
        this.levelReached = levelReached;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public int getLevelReached() {
        return levelReached;
    }
    
    public void setLevelReached(int levelReached) {
        this.levelReached = levelReached;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * Get formatted date string for display.
     * 
     * @return Formatted date (e.g., "2025-11-13")
     */
    public String getFormattedDate() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    /**
     * Get timestamp as string for JSON serialization.
     * 
     * @return ISO format timestamp string
     */
    public String getTimestampString() {
        return timestamp.format(FORMATTER);
    }
    
    /**
     * Set timestamp from string (for JSON deserialization).
     * 
     * @param timestampString ISO format timestamp string
     */
    public void setTimestampString(String timestampString) {
        this.timestamp = LocalDateTime.parse(timestampString, FORMATTER);
    }
    
    /**
     * Compare by score (descending order - higher scores first).
     * If scores are equal, compare by timestamp (earlier is better).
     */
    @Override
    public int compareTo(HighScoreEntry other) {
        // Primary sort: higher score first
        int scoreCompare = Integer.compare(other.score, this.score);
        if (scoreCompare != 0) {
            return scoreCompare;
        }
        
        // Secondary sort: earlier timestamp first
        return this.timestamp.compareTo(other.timestamp);
    }
    
    @Override
    public String toString() {
        return String.format("%s - %d points - Level %d - %s",
                playerName, score, levelReached, getFormattedDate());
    }
}
