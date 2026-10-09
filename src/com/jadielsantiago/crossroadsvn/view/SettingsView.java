package com.jadielsantiago.crossroadsvn.view;

import com.jadielsantiago.crossroadsvn.controller.MusicPlayer;
import com.jadielsantiago.crossroadsvn.controller.SettingsManager;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Modern Glassmorphic Settings Dialog View for Project Crossroads.
 * Provides organized subcategories:
 * 1. Audio: Volume slider, presets, and turn off music (full silence)
 * 2. Video: Fullscreen toggle, match display resolution, 1280x720 fallback, and presets
 * 3. Controls: Gameplay shortcuts and navigation guide
 */
public class SettingsView {

    private enum Tab {
        AUDIO,
        VIDEO,
        CONTROLS
    }

    /**
     * Creates the complete frosted glass settings modal card.
     *
     * @param musicPlayer the active MusicPlayer controller
     * @param stage       the primary game window Stage (can be null if not yet shown)
     * @param onDone      callback executed when the user closes the modal
     * @return the assembled Node ready to display in modal overlays
     */
    public static Node createSettingsCard(MusicPlayer musicPlayer, Stage stage, Runnable onDone) {
        VBox dialogCard = new VBox(10);
        dialogCard.getStyleClass().add("glass-modal-card");
        dialogCard.setAlignment(Pos.CENTER);
        dialogCard.setPrefWidth(520);
        dialogCard.setMaxWidth(540);
        dialogCard.setStyle("-fx-padding: 20 26;");

        // Header Section
        Label badgeLabel = new Label("✦ SYSTEM PREFERENCES ✦");
        badgeLabel.getStyleClass().add("glass-badge");

        Label titleLabel = new Label("Game Settings");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.WHITE);

        Label subLabel = new Label("Configure sound output, display scaling, and controls");
        subLabel.setFont(Font.font("Segoe UI", 11.5));
        subLabel.setTextFill(Color.web("#a2bdb4"));

        Region divider = new Region();
        divider.getStyleClass().add("glass-divider");

        VBox headerBox = new VBox(3);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().addAll(badgeLabel, titleLabel, subLabel, divider);
        VBox.setMargin(divider, new Insets(3, 0, 2, 0));

        // Subcategory Navigation Tabs
        HBox tabsBox = new HBox(8);
        tabsBox.setAlignment(Pos.CENTER);

        Button audioTabBtn = new Button("🔊  Audio");
        Button videoTabBtn = new Button("🖥  Video");
        Button controlsTabBtn = new Button("⌨  How to Play");

        tabsBox.getChildren().addAll(audioTabBtn, videoTabBtn, controlsTabBtn);

        // Content Area Container for Subcategories
        StackPane contentContainer = new StackPane();
        contentContainer.setPrefHeight(230);
        contentContainer.setMinHeight(210);
        contentContainer.setAlignment(Pos.TOP_CENTER);

        // Build individual panels
        VBox audioPanel = buildAudioPanel(musicPlayer);
        VBox videoPanel = buildVideoPanel(stage);
        VBox controlsPanel = buildControlsPanel();

        // Tab selection logic
        Runnable[] updateTabs = new Runnable[1];
        updateTabs[0] = () -> {
            // Default select Audio
            setTabActive(audioTabBtn, true);
            setTabActive(videoTabBtn, false);
            setTabActive(controlsTabBtn, false);
            contentContainer.getChildren().setAll(audioPanel);
        };

        audioTabBtn.setOnAction(e -> {
            setTabActive(audioTabBtn, true);
            setTabActive(videoTabBtn, false);
            setTabActive(controlsTabBtn, false);
            switchContent(contentContainer, audioPanel);
        });

        videoTabBtn.setOnAction(e -> {
            setTabActive(audioTabBtn, false);
            setTabActive(videoTabBtn, true);
            setTabActive(controlsTabBtn, false);
            switchContent(contentContainer, videoPanel);
        });

        controlsTabBtn.setOnAction(e -> {
            setTabActive(audioTabBtn, false);
            setTabActive(videoTabBtn, false);
            setTabActive(controlsTabBtn, true);
            switchContent(contentContainer, controlsPanel);
        });

        // Initialize default tab (Audio)
        updateTabs[0].run();

        // Footer Done / Save Button
        Button doneBtn = new Button("✓  Done & Save");
        doneBtn.getStyleClass().add("glass-button-primary");
        doneBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(doneBtn, 6);
        doneBtn.setOnAction(e -> {
            SettingsManager.saveSettings();
            if (onDone != null) {
                onDone.run();
            }
        });

        dialogCard.getChildren().addAll(headerBox, tabsBox, contentContainer, doneBtn);
        return dialogCard;
    }

    private static void setTabActive(Button btn, boolean active) {
        btn.getStyleClass().removeAll("glass-tab-btn", "glass-tab-btn-active", "glass-page-btn", "glass-page-btn-active");
        if (active) {
            btn.getStyleClass().add("glass-tab-btn-active");
        } else {
            btn.getStyleClass().add("glass-tab-btn");
        }
    }

    private static void switchContent(StackPane container, Node newContent) {
        newContent.setOpacity(0.0);
        container.getChildren().setAll(newContent);
        FadeTransition ft = new FadeTransition(Duration.millis(140), newContent);
        ft.setToValue(1.0);
        ft.play();
    }

    // =========================================================================
    // AUDIO SUBCATEGORY
    // =========================================================================

    private static VBox buildAudioPanel(MusicPlayer musicPlayer) {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.TOP_CENTER);

        double initialVol = musicPlayer != null ? musicPlayer.getVolume() : SettingsManager.getMusicVolume();
        boolean initialMuted = musicPlayer != null ? musicPlayer.isMuted() : SettingsManager.isMusicMuted();

        // 1. Music Volume Slider Card
        VBox volumeCard = new VBox(8);
        volumeCard.getStyleClass().add("glass-sub-panel");

        HBox volTopRow = new HBox(8);
        volTopRow.setAlignment(Pos.CENTER_LEFT);

        Label volTitle = new Label("🎵  Background Music Volume");
        volTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        volTitle.setTextFill(Color.WHITE);

        Region spacerVol = new Region();
        HBox.setHgrow(spacerVol, Priority.ALWAYS);

        Label volBadge = new Label((int) (initialVol * 100) + "%");
        volBadge.setStyle("-fx-background-color: rgba(0, 154, 68, 0.25); -fx-text-fill: #5fe09a; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 2 8; -fx-background-radius: 6;");

        volTopRow.getChildren().addAll(volTitle, spacerVol, volBadge);

        Slider volumeSlider = new Slider(0, 100, initialVol * 100);
        volumeSlider.getStyleClass().add("glass-slider");

        // Quick volume preset buttons
        HBox presetBox = new HBox(6);
        presetBox.setAlignment(Pos.CENTER_LEFT);

        Label presetLbl = new Label("Presets:");
        presetLbl.setFont(Font.font("Segoe UI", 10.5));
        presetLbl.setTextFill(Color.web("#80a89a"));

        presetBox.getChildren().add(presetLbl);
        int[] presets = {0, 25, 50, 75, 100};
        for (int p : presets) {
            Button pBtn = new Button(p + "%");
            pBtn.getStyleClass().add("glass-pill-btn");
            final int presetVal = p;
            pBtn.setOnAction(e -> volumeSlider.setValue(presetVal));
            presetBox.getChildren().add(pBtn);
        }

        volumeCard.getChildren().addAll(volTopRow, volumeSlider, presetBox);

        // 2. Turn Off Music / Mute Card
        VBox muteCard = new VBox(6);
        muteCard.getStyleClass().add("glass-sub-panel");

        HBox muteRow = new HBox(12);
        muteRow.setAlignment(Pos.CENTER_LEFT);

        VBox muteText = new VBox(2);
        Label muteTitle = new Label("🔇  Music Playback");
        muteTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        muteTitle.setTextFill(Color.WHITE);

        Label muteDesc = new Label("Turn off all music to play the game in complete silence");
        muteDesc.setFont(Font.font("Segoe UI", 11));
        muteDesc.setTextFill(Color.web("#95b8ab"));
        muteText.getChildren().addAll(muteTitle, muteDesc);

        Region spacerMute = new Region();
        HBox.setHgrow(spacerMute, Priority.ALWAYS);

        Button muteToggleBtn = new Button();
        updateMuteButtonState(muteToggleBtn, initialMuted, volumeSlider, volBadge, initialVol);

        muteToggleBtn.setOnAction(e -> {
            boolean currentlyMuted = musicPlayer != null ? musicPlayer.isMuted() : SettingsManager.isMusicMuted();
            boolean newMuted = !currentlyMuted;

            if (musicPlayer != null) {
                musicPlayer.setMuted(newMuted);
            }
            SettingsManager.setMusicMuted(newMuted);
            SettingsManager.saveSettings();

            double currentSliderVal = volumeSlider.getValue() / 100.0;
            updateMuteButtonState(muteToggleBtn, newMuted, volumeSlider, volBadge, currentSliderVal);
        });

        muteRow.getChildren().addAll(muteText, spacerMute, muteToggleBtn);
        muteCard.getChildren().add(muteRow);

        // Slider listener
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int intVal = newVal.intValue();
            double doubleVal = intVal / 100.0;

            if (musicPlayer != null) {
                musicPlayer.setVolume(doubleVal);
            }
            SettingsManager.setMusicVolume(doubleVal);

            boolean isMuted = musicPlayer != null ? musicPlayer.isMuted() : SettingsManager.isMusicMuted();
            if (isMuted) {
                volBadge.setText(intVal + "% (Muted)");
            } else {
                volBadge.setText(intVal + "%");
            }
        });

        panel.getChildren().addAll(volumeCard, muteCard);
        return panel;
    }

    private static void updateMuteButtonState(Button btn, boolean muted, Slider slider, Label badge, double currentVol) {
        btn.getStyleClass().removeAll("glass-toggle-on", "glass-toggle-off", "glass-button-primary", "glass-button");
        if (muted) {
            btn.setText("🔇  Music: OFF");
            btn.getStyleClass().add("glass-toggle-off");
            slider.setOpacity(0.5);
            badge.setText((int) (currentVol * 100) + "% (Muted)");
        } else {
            btn.setText("🔊  Music: ON");
            btn.getStyleClass().add("glass-toggle-on");
            slider.setOpacity(1.0);
            badge.setText((int) (currentVol * 100) + "%");
        }
    }

    // =========================================================================
    // VIDEO SUBCATEGORY
    // =========================================================================

    private static VBox buildVideoPanel(Stage stage) {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.TOP_CENTER);

        int displayW = SettingsManager.getDisplayWidth();
        int displayH = SettingsManager.getDisplayHeight();

        // 1. Full Screen Mode Card
        VBox fsCard = new VBox(6);
        fsCard.getStyleClass().add("glass-sub-panel");

        HBox fsRow = new HBox(12);
        fsRow.setAlignment(Pos.CENTER_LEFT);

        VBox fsText = new VBox(2);
        Label fsTitle = new Label("⛶  Full Screen Mode");
        fsTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        fsTitle.setTextFill(Color.WHITE);

        Label fsDesc = new Label("Expand game view to cover the entire monitor display");
        fsDesc.setFont(Font.font("Segoe UI", 11));
        fsDesc.setTextFill(Color.web("#95b8ab"));
        fsText.getChildren().addAll(fsTitle, fsDesc);

        Region spacerFs = new Region();
        HBox.setHgrow(spacerFs, Priority.ALWAYS);

        Button fsToggleBtn = new Button();
        boolean isFs = (stage != null) ? stage.isFullScreen() : SettingsManager.isFullScreen();
        updateFullScreenButton(fsToggleBtn, isFs);

        int curW = (stage != null && stage.getWidth() > 0)
                ? SettingsManager.logicalToPhysicalWidth(stage.getWidth())
                : SettingsManager.getWindowWidth();
        int curH = (stage != null && stage.getHeight() > 0)
                ? SettingsManager.logicalToPhysicalHeight(stage.getHeight())
                : SettingsManager.getWindowHeight();
        Label curResBadge = new Label("Current: " + curW + " × " + curH);
        curResBadge.setStyle("-fx-background-color: rgba(0, 154, 68, 0.25); -fx-text-fill: #5fe09a; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 2 8; -fx-background-radius: 6;");

        fsToggleBtn.setOnAction(e -> {
            if (stage != null) {
                stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                boolean nextFs = !stage.isFullScreen();
                stage.setFullScreen(nextFs);
                stage.setFullScreenExitHint("");
                SettingsManager.setFullScreen(nextFs);
                SettingsManager.saveSettings();
                updateFullScreenButton(fsToggleBtn, nextFs);
                int badgeW = nextFs ? displayW : SettingsManager.getWindowWidth();
                int badgeH = nextFs ? displayH : SettingsManager.getWindowHeight();
                curResBadge.setText("Current: " + badgeW + " × " + badgeH);
            }
        });

        if (stage != null) {
            javafx.beans.value.ChangeListener<Boolean> fsListener = (obs, oldVal, newVal) -> {
                updateFullScreenButton(fsToggleBtn, newVal);
                SettingsManager.setFullScreen(newVal);
                int badgeW = newVal ? displayW : SettingsManager.getWindowWidth();
                int badgeH = newVal ? displayH : SettingsManager.getWindowHeight();
                curResBadge.setText("Current: " + badgeW + " × " + badgeH);
            };
            stage.fullScreenProperty().addListener(fsListener);
            panel.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene == null) {
                    stage.fullScreenProperty().removeListener(fsListener);
                }
            });
        }

        fsRow.getChildren().addAll(fsText, spacerFs, fsToggleBtn);
        fsCard.getChildren().add(fsRow);

        // 2. Window Resolution Card
        VBox resCard = new VBox(8);
        resCard.getStyleClass().add("glass-sub-panel");

        HBox resHeaderRow = new HBox(8);
        resHeaderRow.setAlignment(Pos.CENTER_LEFT);

        Label resTitle = new Label("🖥  Resolution");
        resTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        resTitle.setTextFill(Color.WHITE);

        Region spacerRes = new Region();
        HBox.setHgrow(spacerRes, Priority.ALWAYS);

        resHeaderRow.getChildren().addAll(resTitle, spacerRes, curResBadge);

        // Action Buttons: Match Display & Fallback 1280x720
        HBox quickActionRow = new HBox(8);
        quickActionRow.setAlignment(Pos.CENTER);

        Button matchDisplayBtn = new Button("🖥  Match Display (" + displayW + "×" + displayH + ")");
        matchDisplayBtn.getStyleClass().add("glass-button-primary");
        matchDisplayBtn.setStyle("-fx-font-size: 11.5px; -fx-padding: 6 12;");
        HBox.setHgrow(matchDisplayBtn, Priority.ALWAYS);
        matchDisplayBtn.setMaxWidth(Double.MAX_VALUE);

        matchDisplayBtn.setOnAction(e -> {
            applyResolution(stage, displayW, displayH, curResBadge, fsToggleBtn);
        });

        Button fallbackBtn = new Button("↺  1280 × 720 (Fallback)");
        fallbackBtn.getStyleClass().add("glass-button");
        fallbackBtn.setStyle("-fx-font-size: 11.5px; -fx-padding: 6 12;");
        HBox.setHgrow(fallbackBtn, Priority.ALWAYS);
        fallbackBtn.setMaxWidth(Double.MAX_VALUE);

        fallbackBtn.setOnAction(e -> {
            applyResolution(stage, SettingsManager.DEFAULT_WIDTH, SettingsManager.DEFAULT_HEIGHT, curResBadge, fsToggleBtn);
        });

        quickActionRow.getChildren().addAll(matchDisplayBtn, fallbackBtn);

        // Preset Resolution Pills
        HBox presetPillsRow = new HBox(6);
        presetPillsRow.setAlignment(Pos.CENTER_LEFT);

        Label presetsLabel = new Label("Presets:");
        presetsLabel.setFont(Font.font("Segoe UI", 10.5));
        presetsLabel.setTextFill(Color.web("#80a89a"));
        presetPillsRow.getChildren().add(presetsLabel);

        int[][] commonResolutions = {
                {1920, 1200},
                {1920, 1080},
                {1600, 900},
                {1280, 720},
                {800, 600}
        };

        for (int[] r : commonResolutions) {
            int w = r[0];
            int h = r[1];
            if (w <= displayW && h <= displayH) {
                Button pillBtn = new Button(w + "×" + h);
                pillBtn.getStyleClass().add("glass-pill-btn");
                pillBtn.setOnAction(e -> applyResolution(stage, w, h, curResBadge, fsToggleBtn));
                presetPillsRow.getChildren().add(pillBtn);
            }
        }

        // Display Info with Scaling
        double scale = SettingsManager.getScaleX();
        String scaleInfo = (scale != 1.0) ? " (Scale: " + (int) (scale * 100) + "%)" : "";
        Label displayInfo = new Label("Detected Display: " + displayW + " × " + displayH + scaleInfo);
        displayInfo.setFont(Font.font("Segoe UI", 10.5));
        displayInfo.setTextFill(Color.web("#80a89a"));

        resCard.getChildren().addAll(resHeaderRow, quickActionRow, presetPillsRow, displayInfo);

        panel.getChildren().addAll(fsCard, resCard);
        return panel;
    }

    private static void applyResolution(Stage stage, int targetPhysW, int targetPhysH, Label curResBadge, Button fsToggleBtn) {
        if (stage != null) {
            if (stage.isFullScreen()) {
                stage.setFullScreen(false);
            }
            double logicalW = SettingsManager.physicalToLogicalWidth(targetPhysW);
            double logicalH = SettingsManager.physicalToLogicalHeight(targetPhysH);

            try {
                javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
                if (screen != null) {
                    logicalW = Math.min(logicalW, screen.getVisualBounds().getWidth());
                    logicalH = Math.min(logicalH, screen.getVisualBounds().getHeight());
                }
            } catch (Throwable ignored) {
            }

            stage.setWidth(logicalW);
            stage.setHeight(logicalH);
            stage.centerOnScreen();
            updateFullScreenButton(fsToggleBtn, false);
        }
        SettingsManager.setWindowWidth(targetPhysW);
        SettingsManager.setWindowHeight(targetPhysH);
        SettingsManager.setFullScreen(false);
        SettingsManager.saveSettings();

        curResBadge.setText("Current: " + targetPhysW + " × " + targetPhysH);
    }

    private static void updateFullScreenButton(Button btn, boolean isFullScreen) {
        btn.getStyleClass().removeAll("glass-toggle-on", "glass-toggle-off", "glass-button-primary", "glass-button");
        if (isFullScreen) {
            btn.setText("⛶  Fullscreen: ON");
            btn.getStyleClass().add("glass-toggle-on");
        } else {
            btn.setText("🗖  Windowed");
            btn.getStyleClass().add("glass-button");
        }
    }

    // =========================================================================
    // CONTROLS SUBCATEGORY
    // =========================================================================

    private static VBox buildControlsPanel() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.TOP_CENTER);

        VBox infoBox = new VBox(6);
        infoBox.getStyleClass().add("glass-sub-panel");

        Label infoTitle = new Label("⌨  How to Play & Gameplay Shortcuts");
        infoTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        infoTitle.setTextFill(Color.web("#75e4ab"));

        Label infoKeys = new Label(
                "• Spacebar or Left Click: Read the next line of story dialogue\n" +
                "• [Esc]: Open Save & Load menu (includes Settings & History)\n" +
                "• [H]: Open Dialogue History backlog queue\n" +
                "• [V]: Hide dialogue text box to view the background artwork\n" +
                "• [F11]: Quick toggle Full Screen mode\n" +
                "• Choices: Click choice buttons to steer branch narrative decisions"
        );
        infoKeys.setFont(Font.font("Segoe UI", 12));
        infoKeys.setTextFill(Color.web("#d1e3dc"));

        infoBox.getChildren().addAll(infoTitle, infoKeys);
        panel.getChildren().add(infoBox);
        return panel;
    }

    private static void attachHoverSlideAnimation(Button btn, double slideX) {
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(160), btn);
        slideIn.setToX(slideX);
        slideIn.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(160), btn);
        slideOut.setToX(0);
        slideOut.setInterpolator(Interpolator.EASE_OUT);

        btn.setOnMouseEntered(e -> {
            slideOut.stop();
            slideIn.playFromStart();
        });

        btn.setOnMouseExited(e -> {
            slideIn.stop();
            slideOut.playFromStart();
        });
    }
}
