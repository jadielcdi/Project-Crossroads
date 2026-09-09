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
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the track
        mediaPlayer.play();
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
