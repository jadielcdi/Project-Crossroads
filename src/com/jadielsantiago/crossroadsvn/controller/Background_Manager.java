package com.jadielsantiago.crossroadsvn.controller;

import com.jadielsantiago.crossroadsvn.view.Main;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;

/**
 * Manages background images for the VN engine.
 * Handles loading landscape backgrounds from media/images/backgrounds,
 * dynamic virtual container (1280x720) cover scaling, smooth scene transitions,
 * and graceful fallback for placeholder backgrounds.
 */
public class Background_Manager {
    public static final String BACKGROUNDS_DIR = "/com/jadielsantiago/crossroadsvn/media/images/backgrounds/";
    public static final String DEFAULT_PLACEHOLDER = "Test_Background.jpg";
    private static final String[] EXTENSIONS = { "", ".jpg", ".png", ".webp", ".jpeg" };

    // Single source of truth virtual container dimensions (1280 x 720 canvas)
    public static final double VIRTUAL_WIDTH = Main.VIRTUAL_WIDTH;
    public static final double VIRTUAL_HEIGHT = Main.VIRTUAL_HEIGHT;

    private final ImageView backgroundImageView;
    private final StackPane backgroundPane;
    private String currentBackgroundPath;
    private ScaleTransition ambientZoomTransition;
    private FadeTransition activeTransition;
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
     * Resolves a filename or path to a valid classpath resource or file path.
     * Supports full classpath resource paths, simple filenames, extension-less names,
     * and workspace source folders during development.
     */
    public String resolveBackgroundPath(String filenameOrPath) {
        if (filenameOrPath == null || filenameOrPath.trim().isEmpty()) {
            return null;
        }

        String raw = filenameOrPath.trim();
        if ((raw.startsWith("\"") && raw.endsWith("\"")) || (raw.startsWith("'") && raw.endsWith("'"))) {
            raw = raw.substring(1, raw.length() - 1).trim();
        }

        // 1. Direct classpath path (starts with "/")
        if (raw.startsWith("/")) {
            if (getClass().getResource(raw) != null) {
                return raw;
            }
            for (String ext : EXTENSIONS) {
                if (!ext.isEmpty() && getClass().getResource(raw + ext) != null) {
                    return raw + ext;
                }
            }
        }

        // 2. Classpath backgrounds directory
        String baseName = raw.startsWith("/") ? raw.substring(1) : raw;
        for (String ext : EXTENSIONS) {
            String candidate = BACKGROUNDS_DIR + baseName + ext;
            if (getClass().getResource(candidate) != null) {
                return candidate;
            }
        }

        // 3. Direct filesystem check for active IDE/development runs
        String srcPath = "src/com/jadielsantiago/crossroadsvn/media/images/backgrounds/" + baseName;
        for (String ext : EXTENSIONS) {
            File f = new File(srcPath + ext);
            if (f.exists() && f.isFile()) {
                return BACKGROUNDS_DIR + baseName + ext;
            }
        }

        return null;
    }

    /**
     * Checks if a background image exists in the backgrounds directory or classpath.
     */
    public boolean hasBackground(String filenameOrPath) {
        return resolveBackgroundPath(filenameOrPath) != null;
    }

    private Image loadResourceImage(String path) {
        URL resourceUrl = getClass().getResource(path);
        if (resourceUrl == null) {
            File f = new File("src" + path);
            if (f.exists()) {
                return new Image(f.toURI().toString());
            }
            return null;
        }
        Image image = new Image(resourceUrl.toExternalForm());
        if (image.getProgress() < 1.0) {
            image.progressProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.doubleValue() >= 1.0) {
                    updateImageScaling();
                }
            });
        }
        return image;
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
     * Smoothly transitions to a new background image using a cinematic dissolve animation.
     * If the requested asset has not been created yet, retains the current background
     * (or loads the default placeholder) and logs a friendly developer notification.
     *
     * @param filenameOrPath filename (e.g. "Campus_Quad.jpg") or classpath path
     * @return true if successfully loaded and transitioning, false if falling back
     */
    public boolean changeBackground(String filenameOrPath) {
        return changeBackground(filenameOrPath, true);
    }

    /**
     * Transitions or immediately switches to a new background image.
     */
    public boolean changeBackground(String filenameOrPath, boolean animated) {
        if (filenameOrPath == null || filenameOrPath.trim().isEmpty()) {
            clearBackground();
            return true;
        }

        String resolved = resolveBackgroundPath(filenameOrPath);
        if (resolved == null) {
            System.out.println("[Background_Manager] Background asset requested: '" + filenameOrPath
                    + "', but not found in " + BACKGROUNDS_DIR + ". Retaining current background until asset is added.");
            this.currentBackgroundPath = filenameOrPath;
            if (backgroundImageView.getImage() == null) {
                setBackground(DEFAULT_PLACEHOLDER);
            }
            return false;
        }

        if (!animated || backgroundImageView.getImage() == null) {
            return setBackground(resolved);
        }

        if (activeTransition != null) {
            activeTransition.stop();
        }

        try {
            Image newImage = loadResourceImage(resolved);
            if (newImage == null) {
                return false;
            }

            activeTransition = new FadeTransition(Duration.millis(200), backgroundImageView);
            activeTransition.setFromValue(backgroundImageView.getOpacity());
            activeTransition.setToValue(0.15);
            activeTransition.setOnFinished(e -> {
                backgroundImageView.setImage(newImage);
                this.currentBackgroundPath = resolved;
                updateImageScaling();
                FadeTransition fadeIn = new FadeTransition(Duration.millis(250), backgroundImageView);
                fadeIn.setFromValue(0.15);
                fadeIn.setToValue(1.0);
                fadeIn.setOnFinished(evt -> activeTransition = null);
                activeTransition = fadeIn;
                fadeIn.play();
            });
            activeTransition.play();
            return true;
        } catch (Exception e) {
            System.err.println("Error in animated background change: " + e.getMessage());
            return setBackground(resolved);
        }
    }

    /**
     * Loads and immediately displays a background image without animation.
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

        String resolved = resolveBackgroundPath(filenameOrPath);
        if (resolved == null) {
            System.out.println("[Background_Manager] Background asset requested: '" + filenameOrPath
                    + "', but not found in " + BACKGROUNDS_DIR + ". Retaining current background until asset is added.");
            this.currentBackgroundPath = filenameOrPath;
            if (backgroundImageView.getImage() == null && !filenameOrPath.equals(DEFAULT_PLACEHOLDER)) {
                setBackground(DEFAULT_PLACEHOLDER);
            }
            return false;
        }

        try {
            Image image = loadResourceImage(resolved);
            if (image == null) {
                return false;
            }

            if (activeTransition != null) {
                activeTransition.stop();
                activeTransition = null;
            }
            backgroundImageView.setOpacity(1.0);
            backgroundImageView.setImage(image);
            this.currentBackgroundPath = resolved;
            updateImageScaling();
            return true;
        } catch (Exception e) {
            System.err.println("Error loading background image " + resolved + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Directly sets an existing JavaFX Image instance.
     */
    public void setBackgroundImage(Image image) {
        if (activeTransition != null) {
            activeTransition.stop();
            activeTransition = null;
        }
        if (image != null && image.getProgress() < 1.0) {
            image.progressProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.doubleValue() >= 1.0) {
                    updateImageScaling();
                }
            });
        }
        this.backgroundImageView.setOpacity(1.0);
        this.backgroundImageView.setImage(image);
        this.currentBackgroundPath = null;
        updateImageScaling();
    }

    /**
     * Clears the current background.
     */
    public void clearBackground() {
        if (activeTransition != null) {
            activeTransition.stop();
            activeTransition = null;
        }
        this.backgroundImageView.setOpacity(1.0);
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
