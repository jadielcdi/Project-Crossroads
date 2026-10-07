package com.jadielsantiago.crossroadsvn.controller;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;

/**
 * Manages background images for the VN engine.
 * Handles loading landscape backgrounds from media/images/backgrounds,
 * dynamic window/16:9 scaling, and maintaining the base visual layer.
 */
public class Background_Manager {
    private static final String BACKGROUNDS_DIR = "/com/jadielsantiago/crossroadsvn/media/images/backgrounds/";

    private final ImageView backgroundImageView;
    private final StackPane backgroundPane;
    private String currentBackgroundPath;
    private ScaleTransition ambientZoomTransition;

    public Background_Manager() {
        this.backgroundImageView = new ImageView();
        this.backgroundPane = new StackPane(backgroundImageView);

        // Configure image rendering for landscape backgrounds
        backgroundImageView.setSmooth(true);
        backgroundImageView.setPreserveRatio(false);

        // Hardware texture caching for high-framerate ambient scaling
        backgroundImageView.setCache(true);
        backgroundImageView.setCacheHint(CacheHint.SPEED);
        backgroundPane.setCache(true);
        backgroundPane.setCacheHint(CacheHint.SPEED);

        // Bind image dimensions to container dimensions so it scales with the window
        backgroundImageView.fitWidthProperty().bind(backgroundPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(backgroundPane.heightProperty());
    }

    /**
     * Loads and displays a background image.
     *
     * @param filenameOrPath either the file name (e.g., "Test_Background.jpg")
     *                       or a full classpath resource path
     * @return true if successfully loaded, false otherwise
     */
    public boolean setBackground(String filenameOrPath) {
        if (filenameOrPath == null || filenameOrPath.trim().isEmpty()) {
            clearBackground();
            return true;
        }

        String path = filenameOrPath.startsWith("/")
                ? filenameOrPath
                : BACKGROUNDS_DIR + filenameOrPath;

        URL resourceUrl = getClass().getResource(path);
        if (resourceUrl == null) {
            System.err.println("Background image not found: " + path);
            return false;
        }

        try {
            Image image = new Image(resourceUrl.toExternalForm());
            backgroundImageView.setImage(image);
            this.currentBackgroundPath = path;
            return true;
        } catch (Exception e) {
            System.err.println("Error loading background image " + path + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Directly sets an existing JavaFX Image instance.
     */
    public void setBackgroundImage(Image image) {
        this.backgroundImageView.setImage(image);
        this.currentBackgroundPath = null;
    }

    /**
     * Clears the current background.
     */
    public void clearBackground() {
        this.backgroundImageView.setImage(null);
        this.currentBackgroundPath = null;
    }

    /**
     * Toggles whether the background should preserve aspect ratio or stretch to fill.
     */
    public void setPreserveRatio(boolean preserveRatio) {
        this.backgroundImageView.setPreserveRatio(preserveRatio);
    }

    /**
     * Returns the underlying ImageView.
     */
    public ImageView getImageView() {
        return backgroundImageView;
    }

    /**
     * Returns the StackPane container housing the background ImageView.
     */
    public StackPane getBackgroundPane() {
        return backgroundPane;
    }

    /**
     * Returns the currently displayed Image, or null if none.
     */
    public Image getCurrentBackground() {
        return backgroundImageView.getImage();
    }

    /**
     * Returns the path of the currently loaded background.
     */
    public String getCurrentBackgroundPath() {
        return currentBackgroundPath;
    }

    /**
     * Starts a slow, ambient breathing zoom loop on the background image.
     */
    public void startAmbientZoom() {
        if (ambientZoomTransition == null) {
            ambientZoomTransition = new ScaleTransition(Duration.seconds(16), backgroundImageView);
            ambientZoomTransition.setFromX(1.0);
            ambientZoomTransition.setFromY(1.0);
            ambientZoomTransition.setToX(1.07);
            ambientZoomTransition.setToY(1.07);
            ambientZoomTransition.setCycleCount(Animation.INDEFINITE);
            ambientZoomTransition.setAutoReverse(true);
            ambientZoomTransition.setInterpolator(Interpolator.EASE_BOTH);
        }
        ambientZoomTransition.playFromStart();
    }

    /**
     * Stops the ambient zoom animation and resets scale back to 1.0.
     */
    public void stopAmbientZoom() {
        if (ambientZoomTransition != null) {
            ambientZoomTransition.stop();
        }
        backgroundImageView.setScaleX(1.0);
        backgroundImageView.setScaleY(1.0);
    }
}
