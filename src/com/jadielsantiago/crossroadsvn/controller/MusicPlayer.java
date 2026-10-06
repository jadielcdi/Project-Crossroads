package com.jadielsantiago.crossroadsvn.controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * Manages background music playback for the VN engine.
 * Handles loading, playing, stopping, and looping MP3 tracks.
 */
public class MusicPlayer {
    private MediaPlayer mediaPlayer;
    private double volume = 0.8;

    /**
     * Plays the specified music resource, stopping any currently playing track first.
     * The track loops indefinitely until stopped or replaced.
     *
     * @param resourcePath the classpath resource path (e.g., "/com/jadielsantiago/crossroadsvn/media/music/MainMenu_Test.mp3")
     */
    public void play(String resourcePath) {
        stop(); // Stop any currently playing track

        URL resourceUrl = getClass().getResource(resourcePath);
        if (resourceUrl == null) {
            System.err.println("Music resource not found: " + resourcePath);
            return;
        }

        Media media = new Media(resourceUrl.toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(this.volume);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the track
        mediaPlayer.play();
    }

    /**
     * Sets the playback volume (clamped between 0.0 and 1.0).
     */
    public void setVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(this.volume);
        }
    }

    /**
     * Returns the current volume level (0.0 to 1.0).
     */
    public double getVolume() {
        return volume;
    }

    /**
     * Stops the currently playing track and releases the MediaPlayer resources.
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
}
