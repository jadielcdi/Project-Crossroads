package com.jadielsantiago.crossroadsvn.controller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Manages persistent tracking and audit logging of story completions for
 * Project Crossroads.
 * Tracks individual story completion states, per-story counters, total
 * completions,
 * and maintains a human-readable audit log in the saves directory.
 * 
 * Provides unlock verification for the secret 4th story (to be added) (requires
 * completing Jules, Maya and Noras tories).
 */
public class ProgressManager {

    private static final String PROGRESS_FILE_NAME = "player_progress.properties";
    private static final String LOG_FILE_NAME = "story_completions.log";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final Set<Integer> completedStories = new HashSet<>();
    private static final Map<Integer, Integer> storyCompletionCounts = new HashMap<>();
    private static final List<String> completionHistory = new ArrayList<>();
    private static int totalCompletions = 0;
    private static boolean initialized = false;

    static {
        loadProgress();
    }

    private static File getProgressFile() {
        return new File(SaveManager.getSavesDirectory(), PROGRESS_FILE_NAME);
    }

    private static File getLogFile() {
        return new File(SaveManager.getSavesDirectory(), LOG_FILE_NAME);
    }

    /**
     * Loads saved progress from disk. Safe to call multiple times.
     */
    public static synchronized void loadProgress() {
        completedStories.clear();
        storyCompletionCounts.clear();
        completionHistory.clear();
        totalCompletions = 0;

        File file = getProgressFile();
        if (file.exists()) {
            Properties props = new Properties();
            try (InputStream in = new FileInputStream(file)) {
                props.load(new InputStreamReader(in, StandardCharsets.UTF_8));

                totalCompletions = Integer.parseInt(props.getProperty("total_completions", "0"));

                for (String key : props.stringPropertyNames()) {
                    if (key.startsWith("story.") && key.endsWith(".completed")) {
                        if ("true".equalsIgnoreCase(props.getProperty(key))) {
                            String idStr = key.substring("story.".length(), key.length() - ".completed".length());
                            try {
                                completedStories.add(Integer.parseInt(idStr));
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    } else if (key.startsWith("story.") && key.endsWith(".count")) {
                        String idStr = key.substring("story.".length(), key.length() - ".count".length());
                        try {
                            int storyId = Integer.parseInt(idStr);
                            int count = Integer.parseInt(props.getProperty(key, "0"));
                            storyCompletionCounts.put(storyId, count);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("[ProgressManager] Failed to load progress file: " + e.getMessage());
            }
        }

        // Load recent log entries if the log file exists
        File logFile = getLogFile();
        if (logFile.exists()) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(logFile), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        completionHistory.add(line);
                    }
                }
            } catch (Exception e) {
                System.err.println("[ProgressManager] Failed to load completion log: " + e.getMessage());
            }
        }

        initialized = true;
    }

    /**
     * Saves current progress properties to disk.
     */
    private static synchronized void saveProgress() {
        File file = getProgressFile();
        Properties props = new Properties();

        props.setProperty("total_completions", String.valueOf(totalCompletions));
        props.setProperty("all_core_stories_completed", String.valueOf(areAllStoriesCompleted()));

        for (int storyId = 1; storyId <= 3; storyId++) {
            props.setProperty("story." + storyId + ".completed", String.valueOf(completedStories.contains(storyId)));
            props.setProperty("story." + storyId + ".count",
                    String.valueOf(storyCompletionCounts.getOrDefault(storyId, 0)));
        }

        // Include any other completed stories (e.g., secret 4th story in the future)
        for (int storyId : completedStories) {
            props.setProperty("story." + storyId + ".completed", "true");
            props.setProperty("story." + storyId + ".count",
                    String.valueOf(storyCompletionCounts.getOrDefault(storyId, 0)));
        }

        props.setProperty("last_saved_timestamp", LocalDateTime.now().format(TIMESTAMP_FORMATTER));

        try (OutputStream out = new FileOutputStream(file)) {
            props.store(new OutputStreamWriter(out, StandardCharsets.UTF_8),
                    "Project Crossroads - Persistent Player Progress");
        } catch (Exception e) {
            System.err.println("[ProgressManager] Failed to save progress: " + e.getMessage());
        }
    }

    /**
     * Records completion of a story, updates counters, appends to the log, and
     * persists changes.
     *
     * @param storyId the completed story ID (1 = Jules, 2 = Maya, 3 = Nora, etc.)
     */
    public static synchronized void recordStoryCompletion(int storyId) {
        String storyName;
        JSCParser.StoryMetadata meta = JSCParser.getStoryMetadata(storyId);
        if (meta != null && meta.getTitle() != null) {
            storyName = meta.getTitle() + " (" + meta.getCharacterName() + ")";
        } else {
            storyName = switch (storyId) {
                case 1 -> "Jules's Story (Julian \"Jules\" Rivera)";
                case 2 -> "Maya's Story (Maya Sterling)";
                case 3 -> "Nora's Story (Nora Vance)";
                default -> "Story #" + storyId;
            };
        }
        recordStoryCompletion(storyId, storyName);
    }

    /**
     * Records completion of a story with explicit story title.
     *
     * @param storyId    the completed story ID
     * @param storyTitle description/title for logging
     */
    public static synchronized void recordStoryCompletion(int storyId, String storyTitle) {
        if (!initialized) {
            loadProgress();
        }

        totalCompletions++;
        int countForStory = storyCompletionCounts.getOrDefault(storyId, 0) + 1;
        storyCompletionCounts.put(storyId, countForStory);
        completedStories.add(storyId);

        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String logEntry = String.format("[%s] Completed Story #%d: %s | Run #%d | Total Game Completions: %d",
                timestamp, storyId, storyTitle, countForStory, totalCompletions);

        completionHistory.add(logEntry);

        // Append to persistent log file
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(getLogFile(), true), StandardCharsets.UTF_8))) {
            writer.println(logEntry);
        } catch (Exception e) {
            System.err.println("[ProgressManager] Failed to append to completion log: " + e.getMessage());
        }

        saveProgress();

        System.out.println("[ProgressManager] " + logEntry);
        if (areAllStoriesCompleted()) {
            System.out.println("[ProgressManager] ★ All core stories completed! Secret 4th story requirements met.");
        }
    }

    /**
     * Checks if a specific story has been completed at least once.
     */
    public static synchronized boolean isStoryCompleted(int storyId) {
        return completedStories.contains(storyId);
    }

    /**
     * Returns how many times a specific story has been completed.
     */
    public static synchronized int getStoryCompletionCount(int storyId) {
        return storyCompletionCounts.getOrDefault(storyId, 0);
    }

    /**
     * Returns total number of story completions across all playthroughs.
     */
    public static synchronized int getTotalCompletions() {
        return totalCompletions;
    }

    /**
     * Checks if all three core stories (1: Jules, 2: Maya, 3: Nora) have been
     * completed.
     * This is the requirement for unlocking the upcoming secret 4th story.
     */
    public static synchronized boolean areAllStoriesCompleted() {
        return completedStories.contains(1) &&
                completedStories.contains(2) &&
                completedStories.contains(3);
    }

    /**
     * Convenience alias for areAllStoriesCompleted() to make unlock conditions
     * clear.
     */
    public static synchronized boolean canUnlockSecretStory() {
        return areAllStoriesCompleted();
    }

    /**
     * Returns an unmodifiable set of completed story IDs.
     */
    public static synchronized Set<Integer> getCompletedStoryIds() {
        return Collections.unmodifiableSet(new HashSet<>(completedStories));
    }

    /**
     * Returns an unmodifiable list of all completion log entries.
     */
    public static synchronized List<String> getCompletionHistoryLog() {
        return Collections.unmodifiableList(new ArrayList<>(completionHistory));
    }

    /**
     * Resets all progress and clears persistent logs.
     */
    public static synchronized void resetProgress() {
        completedStories.clear();
        storyCompletionCounts.clear();
        completionHistory.clear();
        totalCompletions = 0;

        File propFile = getProgressFile();
        if (propFile.exists()) {
            propFile.delete();
        }

        File logFile = getLogFile();
        if (logFile.exists()) {
            logFile.delete();
        }

        System.out.println("[ProgressManager] Progress reset.");
    }
}
