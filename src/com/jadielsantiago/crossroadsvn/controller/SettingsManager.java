package com.jadielsantiago.crossroadsvn.controller;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Manages persistent user preferences for Project Crossroads.
 * Handles audio levels, mute state, resolution, and fullscreen toggles,
 * with persistence to saves/game_settings.properties.
 */
public class SettingsManager {
    private static final String SETTINGS_FILE_NAME = "game_settings.properties";
    public static final int DEFAULT_WIDTH = 1280;
    public static final int DEFAULT_HEIGHT = 720;

    private static double musicVolume = 0.8;
    private static boolean musicMuted = false;
    private static boolean fullScreen = false;
    private static int windowWidth = DEFAULT_WIDTH;
    private static int windowHeight = DEFAULT_HEIGHT;
    private static boolean initialized = false;

    static {
        loadSettings();
    }

    private static File getSettingsFile() {
        return new File(SaveManager.getSavesDirectory(), SETTINGS_FILE_NAME);
    }

    /**
     * Loads saved settings from disk, applying defaults if file is not found.
     */
    public static synchronized void loadSettings() {
        File file = getSettingsFile();
        if (file.exists()) {
            Properties props = new Properties();
            try (InputStream in = new FileInputStream(file)) {
                props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
                musicVolume = Double.parseDouble(props.getProperty("audio.volume", "0.8"));
                musicMuted = Boolean.parseBoolean(props.getProperty("audio.muted", "false"));
                fullScreen = Boolean.parseBoolean(props.getProperty("video.fullscreen", "false"));
                windowWidth = Integer.parseInt(props.getProperty("video.width", String.valueOf(DEFAULT_WIDTH)));
                windowHeight = Integer.parseInt(props.getProperty("video.height", String.valueOf(DEFAULT_HEIGHT)));
            } catch (Exception e) {
                System.err.println("[SettingsManager] Failed to load settings: " + e.getMessage());
            }
        }
        initialized = true;
    }

    /**
     * Persists current settings to disk.
     */
    public static synchronized void saveSettings() {
        File file = getSettingsFile();
        Properties props = new Properties();
        props.setProperty("audio.volume", String.valueOf(musicVolume));
        props.setProperty("audio.muted", String.valueOf(musicMuted));
        props.setProperty("video.fullscreen", String.valueOf(fullScreen));
        props.setProperty("video.width", String.valueOf(windowWidth));
        props.setProperty("video.height", String.valueOf(windowHeight));

        try (OutputStream out = new FileOutputStream(file)) {
            props.store(new OutputStreamWriter(out, StandardCharsets.UTF_8), "Project Crossroads - Settings Configuration");
        } catch (Exception e) {
            System.err.println("[SettingsManager] Failed to save settings: " + e.getMessage());
        }
    }

    /**
     * Applies loaded settings to the given MusicPlayer and Stage.
     */
    public static void applySettings(MusicPlayer musicPlayer, Stage stage) {
        if (!initialized) {
            loadSettings();
        }
        if (musicPlayer != null) {
            musicPlayer.setVolume(musicVolume);
            musicPlayer.setMuted(musicMuted);
        }
        if (stage != null) {
            double logicalW = physicalToLogicalWidth(windowWidth);
            double logicalH = physicalToLogicalHeight(windowHeight);
            try {
                Screen screen = Screen.getPrimary();
                if (screen != null) {
                    logicalW = Math.min(logicalW, screen.getVisualBounds().getWidth());
                    logicalH = Math.min(logicalH, screen.getVisualBounds().getHeight());
                }
            } catch (Throwable ignored) {
            }
            stage.setWidth(logicalW);
            stage.setHeight(logicalH);
            if (fullScreen) {
                stage.setFullScreen(true);
                stage.setFullScreenExitHint("");
            } else {
                stage.setFullScreen(false);
            }
            stage.centerOnScreen();
        }
    }

    /**
     * Converts the stored physical window width to JavaFX logical units.
     */
    public static double getLogicalWidth() {
        return physicalToLogicalWidth(windowWidth);
    }

    /**
     * Converts the stored physical window height to JavaFX logical units.
     */
    public static double getLogicalHeight() {
        return physicalToLogicalHeight(windowHeight);
    }

    public static double getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(double volume) {
        musicVolume = Math.max(0.0, Math.min(1.0, volume));
    }

    public static boolean isMusicMuted() {
        return musicMuted;
    }

    public static void setMusicMuted(boolean muted) {
        musicMuted = muted;
    }

    public static boolean isFullScreen() {
        return fullScreen;
    }

    public static void setFullScreen(boolean fs) {
        fullScreen = fs;
    }

    public static int getWindowWidth() {
        return windowWidth;
    }

    public static void setWindowWidth(int width) {
        windowWidth = width;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }

    public static void setWindowHeight(int height) {
        windowHeight = height;
    }

    /**
     * Returns the true physical width of the primary monitor display (e.g. 1920),
     * taking into account Windows DPI scaling.
     */
    public static int getDisplayWidth() {
        try {
            java.awt.DisplayMode dm = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
            if (dm != null && dm.getWidth() > 0) {
                return dm.getWidth();
            }
        } catch (Throwable ignored) {
        }
        try {
            Screen primary = Screen.getPrimary();
            if (primary != null) {
                return (int) Math.round(primary.getBounds().getWidth() * primary.getOutputScaleX());
            }
        } catch (Throwable ignored) {
        }
        return DEFAULT_WIDTH;
    }

    /**
     * Returns the true physical height of the primary monitor display (e.g. 1200),
     * taking into account Windows DPI scaling.
     */
    public static int getDisplayHeight() {
        try {
            java.awt.DisplayMode dm = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
            if (dm != null && dm.getHeight() > 0) {
                return dm.getHeight();
            }
        } catch (Throwable ignored) {
        }
        try {
            Screen primary = Screen.getPrimary();
            if (primary != null) {
                return (int) Math.round(primary.getBounds().getHeight() * primary.getOutputScaleY());
            }
        } catch (Throwable ignored) {
        }
        return DEFAULT_HEIGHT;
    }

    /**
     * Returns the horizontal display scale factor (e.g. 1.5 for 150% scaling).
     */
    public static double getScaleX() {
        try {
            Screen primary = Screen.getPrimary();
            if (primary != null) {
                return primary.getOutputScaleX();
            }
        } catch (Throwable ignored) {
        }
        return 1.0;
    }

    /**
     * Returns the vertical display scale factor (e.g. 1.5 for 150% scaling).
     */
    public static double getScaleY() {
        try {
            Screen primary = Screen.getPrimary();
            if (primary != null) {
                return primary.getOutputScaleY();
            }
        } catch (Throwable ignored) {
        }
        return 1.0;
    }

    /**
     * Converts a target physical pixel width into JavaFX logical coordinate units.
     */
    public static double physicalToLogicalWidth(int physicalW) {
        double sx = getScaleX();
        return sx > 0 ? (physicalW / sx) : physicalW;
    }

    /**
     * Converts a target physical pixel height into JavaFX logical coordinate units.
     */
    public static double physicalToLogicalHeight(int physicalH) {
        double sy = getScaleY();
        return sy > 0 ? (physicalH / sy) : physicalH;
    }

    /**
     * Converts a JavaFX logical coordinate width into physical screen pixels.
     */
    public static int logicalToPhysicalWidth(double logicalW) {
        return (int) Math.round(logicalW * getScaleX());
    }

    /**
     * Converts a JavaFX logical coordinate height into physical screen pixels.
     */
    public static int logicalToPhysicalHeight(double logicalH) {
        return (int) Math.round(logicalH * getScaleY());
    }
}
