package com.jadielsantiago.crossroadsvn.controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

/**
 * Manages background music playback for the VN engine.
 * Handles loading, playing, stopping, and looping MP3 tracks.
 */
public class MusicPlayer {
    private MediaPlayer mediaPlayer;

    /**
     * Plays the specified music file, stopping any currently playing track first.
     * The track loops indefinitely until stopped or replaced.
     *
     * @param filePath the absolute or relative path to the MP3 file
     */
    public void play(String filePath) {
        stop(); // Stop any currently playing track

        File musicFile = new File(filePath);
        if (!musicFile.exists()) {
            System.err.println("Music file not found: " + filePath);
            return;
        }

        Media media = new Media(musicFile.toURI().toString());
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
