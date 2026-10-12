package com.jadielsantiago.crossroadsvn.controller;

import com.jadielsantiago.crossroadsvn.view.Main;
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
 * dynamic virtual container (1280x720) cover scaling, and maintaining the base visual layer.
 */
public class Background_Manager {
    private static final String BACKGROUNDS_DIR = "/com/jadielsantiago/crossroadsvn/media/images/backgrounds/";

    // Single source of truth virtual container dimensions (1280 x 720 canvas)
    public static final double VIRTUAL_WIDTH = Main.VIRTUAL_WIDTH;
    public static final double VIRTUAL_HEIGHT = Main.VIRTUAL_HEIGHT;

    private final ImageView backgroundImageView;
    private final StackPane backgroundPane;
    private String currentBackgroundPath;
    private ScaleTransition ambientZoomTransition;
    private boolean preserveRatio = true;

    public Background_Manager() {
        this.backgroundImageView = new ImageView();
        this.backgroundPane = new StackPane(backgroundImageView);
        backgroundPane.setStyle("-fx-background-color: #070e0b;");

        // Explicitly lock backgroundPane to the 1280 x 720 virtual coordinate space
        backgroundPane.setPrefSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        backgroundPane.setMinSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        backgroundPane.setMaxSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        // Configure image rendering to strictly preserve aspect ratio without stretching or distortion
        backgroundImageView.setSmooth(true);
        backgroundImageView.setPreserveRatio(true);

        // High-DPI & Quality: Disable low-res bitmap caching so JavaFX Prism pipeline
        // renders full-resolution source assets crisply across 1080p, 1440p, 4K, and OS DPI scaling (125%/150%/200%)
        backgroundImageView.setCache(false);
        backgroundPane.setCache(false);

        // Strictly bound scaling and ambient zoom to the 1280x720 virtual canvas
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        backgroundPane.setClip(clip);
    }

    /**
     * Updates the ImageView fit dimensions so that the image cleanly covers
     * the 1280.0 x 720.0 virtual container without distortion or unwanted letterbox gaps
     * within the game canvas.
     */
    public void updateImageScaling() {
        Image image = backgroundImageView.getImage();
        if (image == null) {
            return;
        }

        double paneW = VIRTUAL_WIDTH;
        double paneH = VIRTUAL_HEIGHT;

        if (!preserveRatio) {
            backgroundImageView.setFitWidth(paneW);
            backgroundImageView.setFitHeight(paneH);
            return;
        }

        double imgW = image.getWidth();
        double imgH = image.getHeight();
        if (imgW <= 0 || imgH <= 0) {
            return;
        }

        // Cover scaling: scale up so both width >= paneW and height >= paneH,
        // preserving natural image aspect ratio without distortion or letterboxing inside the pane.
        double scale = Math.max(paneW / imgW, paneH / imgH);
        backgroundImageView.setFitWidth(imgW * scale);
        backgroundImageView.setFitHeight(imgH * scale);
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
            if (image.getProgress() < 1.0) {
                image.progressProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal.doubleValue() >= 1.0) {
                        updateImageScaling();
                    }
                });
            }
            backgroundImageView.setImage(image);
            this.currentBackgroundPath = path;
            updateImageScaling();
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
        if (image != null && image.getProgress() < 1.0) {
            image.progressProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.doubleValue() >= 1.0) {
                    updateImageScaling();
                }
            });
        }
        this.backgroundImageView.setImage(image);
        this.currentBackgroundPath = null;
        updateImageScaling();
    }

    /**
     * Clears the current background.
     */
    public void clearBackground() {
        this.backgroundImageView.setImage(null);
        this.currentBackgroundPath = null;
        this.backgroundImageView.setFitWidth(0);
        this.backgroundImageView.setFitHeight(0);
    }

    /**
     * Toggles whether the background should preserve aspect ratio or stretch to fill.
     */
    public void setPreserveRatio(boolean preserveRatio) {
        this.preserveRatio = preserveRatio;
        this.backgroundImageView.setPreserveRatio(preserveRatio);
        updateImageScaling();
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
            ambientZoomTransition = new ScaleTransition(Duration.seconds(18), backgroundImageView);
            ambientZoomTransition.setFromX(1.0);
            ambientZoomTransition.setFromY(1.0);
            ambientZoomTransition.setToX(1.025);
            ambientZoomTransition.setToY(1.025);
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
