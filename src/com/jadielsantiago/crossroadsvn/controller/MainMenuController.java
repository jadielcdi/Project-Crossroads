/*
 * Copyright (c) 2026 Jadiel Santiago. All rights reserved.
 *
 * This software is licensed under the MIT License.
 * See COPYRIGHT.txt in the project root for full license details.
 */
package com.jadielsantiago.crossroadsvn.controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.function.Consumer;

/**
 * Controller and layout builder for the modern Glassmorphism Main Menu.
 * Features a floating rounded translucent glass card, frosted borders,
 * micro-animations (slide on hover), ambient background zoom loop integration,
 * and handles routing for Start, Load, Settings, and Quit.
 */
public class MainMenuController {

    private final Consumer<Integer> startStoryHandler;
    private final Runnable loadGameHandler;
    private final Runnable quitGameHandler;
    private final Background_Manager backgroundManager;
    private final MusicPlayer musicPlayer;

    private StackPane rootPane;
    private VBox mainCard;
    private VBox mainActionsBox;
    private VBox routeSelectionBox;
    private StackPane modalOverlay;

    public MainMenuController(Consumer<Integer> startStoryHandler,
                              Runnable loadGameHandler,
                              Runnable quitGameHandler,
                              Background_Manager backgroundManager,
                              MusicPlayer musicPlayer) {
        this.startStoryHandler = startStoryHandler;
        this.loadGameHandler = loadGameHandler;
        this.quitGameHandler = quitGameHandler;
        this.backgroundManager = backgroundManager;
        this.musicPlayer = musicPlayer;

        buildUI();
    }

    /**
     * Builds the complete Main Menu view with floating glass card and modals.
     */
    private void buildUI() {
        rootPane = new StackPane();
        rootPane.setPickOnBounds(false);

        // 1. Floating Glass Card Container
        mainCard = new VBox(18);
        mainCard.getStyleClass().add("glass-menu-card");
        mainCard.setAlignment(Pos.CENTER);
        mainCard.setMaxWidth(430);
        mainCard.setMinWidth(400);
        StackPane.setAlignment(mainCard, Pos.CENTER);

        // Header Section
        Label badgeLabel = new Label("✦  CROSSROADS VN ENGINE  ✦");
        badgeLabel.getStyleClass().add("glass-badge");

        Label titleLabel = new Label("Project Crossroads");
        titleLabel.getStyleClass().add("glass-title");

        Label subtitleLabel = new Label("Where every choice carves a destiny");
        subtitleLabel.getStyleClass().add("glass-subtitle");

        Region frostedDivider = new Region();
        frostedDivider.getStyleClass().add("glass-divider");

        VBox headerBox = new VBox(6);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().addAll(badgeLabel, titleLabel, subtitleLabel, frostedDivider);
        VBox.setMargin(frostedDivider, new Insets(8, 0, 4, 0));

        // 2. Action Boxes (Main actions vs. Route selection switcher)
        mainActionsBox = buildMainActionsBox();
        routeSelectionBox = buildRouteSelectionBox();
        routeSelectionBox.setVisible(false);
        routeSelectionBox.setManaged(false);

        StackPane contentSwitcher = new StackPane(mainActionsBox, routeSelectionBox);

        // Footer Section
        Label footerLabel = new Label("v1.2.0 • Powered by Crossroads VN Engine");
        footerLabel.getStyleClass().add("glass-footer-text");

        mainCard.getChildren().addAll(headerBox, contentSwitcher, footerLabel);

        // 3. Modal Overlay for Settings and Quit confirmation
        modalOverlay = new StackPane();
        modalOverlay.getStyleClass().add("glass-modal-overlay");
        modalOverlay.setVisible(false);
        modalOverlay.setOnMouseClicked(e -> {
            if (e.getTarget() == modalOverlay) {
                hideModal();
            }
        });

        rootPane.getChildren().addAll(mainCard, modalOverlay);
    }

    /**
     * Builds the primary navigation buttons (Start, Load, Settings, Quit).
     */
    private VBox buildMainActionsBox() {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);

        // Start Story (Primary action with vibrant emerald glass glow)
        Button startBtn = new Button("▶  Start Story");
        startBtn.getStyleClass().add("glass-button-primary");
        startBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(startBtn, 8);
        startBtn.setOnAction(e -> showRouteSelection());

        // Load Game
        Button loadBtn = new Button("📂  Load Game");
        loadBtn.getStyleClass().add("glass-button");
        loadBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(loadBtn, 8);
        loadBtn.setOnAction(e -> {
            if (loadGameHandler != null) {
                loadGameHandler.run();
            }
        });

        // Settings
        Button settingsBtn = new Button("⚙  Settings");
        settingsBtn.getStyleClass().add("glass-button");
        settingsBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(settingsBtn, 8);
        settingsBtn.setOnAction(e -> showSettingsDialog());

        // Quit Game
        Button quitBtn = new Button("✕  Quit Game");
        quitBtn.getStyleClass().add("glass-button");
        quitBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(quitBtn, 8);
        quitBtn.setOnAction(e -> showQuitConfirmation());

        box.getChildren().addAll(startBtn, loadBtn, settingsBtn, quitBtn);
        return box;
    }

    /**
     * Builds the route selection sub-view for choosing Jules, Maya, or Nora.
     */
    private VBox buildRouteSelectionBox() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        Label routeTitle = new Label("Select Your Route");
        routeTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        routeTitle.setTextFill(Color.web("#e6f7ef"));

        Button julesBtn = createRouteButton("Jules's Story", "The Weight of Expectations", "Route 01", 1);
        Button mayaBtn = createRouteButton("Maya's Story", "The Rhythm of Choice", "Route 02", 2);
        Button noraBtn = createRouteButton("Nora's Story", "Canvas of Tomorrow", "Route 03", 3);

        Button backBtn = new Button("←  Back to Main Menu");
        backBtn.getStyleClass().add("glass-button");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(backBtn, -6);
        backBtn.setOnAction(e -> showMainActions());

        box.getChildren().addAll(routeTitle, julesBtn, mayaBtn, noraBtn, backBtn);
        return box;
    }

    private Button createRouteButton(String name, String subtitle, String routeTag, int storyId) {
        Button btn = new Button();
        btn.getStyleClass().add("glass-route-button");
        btn.setMaxWidth(Double.MAX_VALUE);

        HBox content = new HBox(12);
        content.setAlignment(Pos.CENTER_LEFT);

        Label tagLabel = new Label(routeTag);
        tagLabel.setStyle("-fx-background-color: rgba(0, 154, 68, 0.3); -fx-text-fill: #61f2a4; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2 6; -fx-background-radius: 6;");

        VBox textCol = new VBox(2);
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.WHITE);

        Label descLabel = new Label(subtitle);
        descLabel.setFont(Font.font("Segoe UI", 11.5));
        descLabel.setTextFill(Color.web("#a0bfb5"));

        textCol.getChildren().addAll(nameLabel, descLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label arrow = new Label("›");
        arrow.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        arrow.setTextFill(Color.web("#80d8b4"));

        content.getChildren().addAll(tagLabel, textCol, spacer, arrow);
        btn.setGraphic(content);

        attachHoverSlideAnimation(btn, 8);
        btn.setOnAction(e -> {
            if (startStoryHandler != null) {
                stopAmbientZoom();
                startStoryHandler.accept(storyId);
            }
        });

        return btn;
    }

    private void showRouteSelection() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(160), mainActionsBox);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            mainActionsBox.setVisible(false);
            mainActionsBox.setManaged(false);

            routeSelectionBox.setOpacity(0.0);
            routeSelectionBox.setVisible(true);
            routeSelectionBox.setManaged(true);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(160), routeSelectionBox);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    private void showMainActions() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(160), routeSelectionBox);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            routeSelectionBox.setVisible(false);
            routeSelectionBox.setManaged(false);

            mainActionsBox.setOpacity(0.0);
            mainActionsBox.setVisible(true);
            mainActionsBox.setManaged(true);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(160), mainActionsBox);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    /**
     * Attaches a subtle hover slide micro-animation to a button.
     */
    public void attachHoverSlideAnimation(Button button, double slideDistance) {
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(170), button);
        slideIn.setToX(slideDistance);
        slideIn.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(170), button);
        slideOut.setToX(0);
        slideOut.setInterpolator(Interpolator.EASE_OUT);

        button.setOnMouseEntered(e -> {
            slideOut.stop();
            slideIn.playFromStart();
        });

        button.setOnMouseExited(e -> {
            slideIn.stop();
            slideOut.playFromStart();
        });
    }

    /**
     * Plays the entrance micro-animation on the floating card.
     */
    public void playEntranceAnimation() {
        mainCard.setOpacity(0.0);
        mainCard.setTranslateY(18);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(450), mainCard);
        fadeIn.setToValue(1.0);
        fadeIn.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition floatUp = new TranslateTransition(Duration.millis(450), mainCard);
        floatUp.setToY(0);
        floatUp.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition entrance = new ParallelTransition(fadeIn, floatUp);
        entrance.play();
    }

    /**
     * Displays a frosted glass Settings dialog modal.
     */
    private void showSettingsDialog() {
        VBox dialogCard = new VBox(16);
        dialogCard.getStyleClass().add("glass-modal-card");
        dialogCard.setAlignment(Pos.CENTER);
        dialogCard.setMaxWidth(380);

        Label title = new Label("Settings & Audio");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.WHITE);

        // Music volume row
        VBox volumeBox = new VBox(6);
        volumeBox.setAlignment(Pos.CENTER_LEFT);

        double currentVol = musicPlayer != null ? musicPlayer.getVolume() * 100 : 80;
        Label volLabel = new Label("Music Volume: " + (int) currentVol + "%");
        volLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        volLabel.setTextFill(Color.web("#ccebe0"));

        Slider volumeSlider = new Slider(0, 100, currentVol);
        volumeSlider.getStyleClass().add("glass-slider");
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int intVal = newVal.intValue();
            volLabel.setText("Music Volume: " + intVal + "%");
            if (musicPlayer != null) {
                musicPlayer.setVolume(intVal / 100.0);
            }
        });
        volumeBox.getChildren().addAll(volLabel, volumeSlider);

        // Controls info section
        VBox infoBox = new VBox(4);
        infoBox.getStyleClass().add("glass-sub-panel");
        Label infoTitle = new Label("Gameplay Keybinds");
        infoTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        infoTitle.setTextFill(Color.web("#75e4ab"));

        Label infoKeys = new Label(
                "• Space / Left Click: Advance dialogue\n" +
                "• [Esc]: Open In-Game Save/Load Menu\n" +
                "• [V]: Toggle Dialogue Box Visibility"
        );
        infoKeys.setFont(Font.font("Segoe UI", 11.5));
        infoKeys.setTextFill(Color.web("#d1e3dc"));
        infoBox.getChildren().addAll(infoTitle, infoKeys);

        Button closeBtn = new Button("✓  Save & Close");
        closeBtn.getStyleClass().add("glass-button-primary");
        closeBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(closeBtn, 6);
        closeBtn.setOnAction(e -> hideModal());

        dialogCard.getChildren().addAll(title, volumeBox, infoBox, closeBtn);

        showModal(dialogCard);
    }

    /**
     * Displays a frosted glass Quit confirmation dialog modal.
     */
    private void showQuitConfirmation() {
        VBox dialogCard = new VBox(16);
        dialogCard.getStyleClass().add("glass-modal-card");
        dialogCard.setAlignment(Pos.CENTER);
        dialogCard.setMaxWidth(360);

        Label title = new Label("Quit Project Crossroads?");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.WHITE);

        Label message = new Label("Are you sure you want to exit the visual novel?");
        message.setFont(Font.font("Segoe UI", 13));
        message.setTextFill(Color.web("#b4d1c6"));
        message.setWrapText(true);

        HBox btnBox = new HBox(12);
        btnBox.setAlignment(Pos.CENTER);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("glass-button");
        HBox.setHgrow(cancelBtn, Priority.ALWAYS);
        cancelBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(cancelBtn, -4);
        cancelBtn.setOnAction(e -> hideModal());

        Button confirmQuitBtn = new Button("Exit Game");
        confirmQuitBtn.getStyleClass().add("glass-button-primary");
        HBox.setHgrow(confirmQuitBtn, Priority.ALWAYS);
        confirmQuitBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(confirmQuitBtn, 4);
        confirmQuitBtn.setOnAction(e -> {
            if (quitGameHandler != null) {
                quitGameHandler.run();
            }
        });

        btnBox.getChildren().addAll(cancelBtn, confirmQuitBtn);
        dialogCard.getChildren().addAll(title, message, btnBox);

        showModal(dialogCard);
    }

    private void showModal(Node content) {
        modalOverlay.getChildren().setAll(content);
        modalOverlay.setOpacity(0.0);
        modalOverlay.setVisible(true);

        FadeTransition ft = new FadeTransition(Duration.millis(180), modalOverlay);
        ft.setToValue(1.0);
        ft.play();
    }

    private void hideModal() {
        FadeTransition ft = new FadeTransition(Duration.millis(150), modalOverlay);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> {
            modalOverlay.setVisible(false);
            modalOverlay.getChildren().clear();
        });
        ft.play();
    }

    /**
     * Starts ambient background zoom via Background_Manager.
     */
    public void startAmbientZoom() {
        if (backgroundManager != null) {
            backgroundManager.startAmbientZoom();
        }
    }

    /**
     * Stops ambient background zoom.
     */
    public void stopAmbientZoom() {
        if (backgroundManager != null) {
            backgroundManager.stopAmbientZoom();
        }
    }

    /**
     * Resets the view to the primary main actions card.
     */
    public void resetView() {
        mainActionsBox.setOpacity(1.0);
        mainActionsBox.setVisible(true);
        mainActionsBox.setManaged(true);
        routeSelectionBox.setVisible(false);
        routeSelectionBox.setManaged(false);
        hideModal();
    }

    /**
     * Returns the root node of the Main Menu layout.
     */
    public StackPane getView() {
        return rootPane;
    }
}
