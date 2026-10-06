/*
 * Copyright (c) 2026 Jadiel Santiago. All rights reserved.
 *
 * This software is licensed under the MIT License.
 * See COPYRIGHT.txt in the project root for full license details.
 */
package com.jadielsantiago.crossroadsvn.controller;

import javafx.animation.Animation;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Controller and layout builder for the Persona / ZZZ / NieR inspired Main Menu.
 * Features a minimalist left-bordered navigation dock anchored to the lower-left,
 * micro-typography, vertical character route cards, and staggered entrance micro-interactions.
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

    private final List<Button> menuButtons = new ArrayList<>();
    private Animation activeEntranceAnimation;

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
     * Builds the complete Main Menu view with lower-left docked navigation and modals.
     */
    private void buildUI() {
        rootPane = new StackPane();
        rootPane.setPickOnBounds(false);

        // 1. Lower-Left Anchored Navigation Dock (Persona / ZZZ / NieR aesthetic)
        mainCard = new VBox(14);
        mainCard.getStyleClass().add("glass-nav-dock");
        mainCard.setAlignment(Pos.CENTER_LEFT);
        mainCard.setPrefWidth(300);
        mainCard.setMaxWidth(310);
        mainCard.setMinWidth(285);

        StackPane.setAlignment(mainCard, Pos.BOTTOM_LEFT);
        StackPane.setMargin(mainCard, new Insets(0, 0, 50, 50));

        // Header Section with sci-fi / modern VN micro-typography
        Label badgeLabel = new Label("// SYSTEM ONLINE");
        badgeLabel.getStyleClass().add("glass-badge");

        Label titleLabel = new Label("PROJECT CROSSROADS");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 20));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 154, 68, 0.6), 10, 0.3, 0, 0);");

        Label subtitleLabel = new Label("A CONTEMPORARY VISUAL NOVEL");
        subtitleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 9.5));
        subtitleLabel.setTextFill(Color.web("#82a59a"));

        Region frostedDivider = new Region();
        frostedDivider.getStyleClass().add("glass-divider");

        VBox headerBox = new VBox(4);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.getChildren().addAll(badgeLabel, titleLabel, subtitleLabel, frostedDivider);
        VBox.setMargin(frostedDivider, new Insets(6, 0, 2, 0));

        // 2. Action Boxes (Main actions vs. Route selection switcher)
        mainActionsBox = buildMainActionsBox();
        routeSelectionBox = buildRouteSelectionBox();
        routeSelectionBox.setVisible(false);
        routeSelectionBox.setManaged(false);

        StackPane contentSwitcher = new StackPane(mainActionsBox, routeSelectionBox);

        // Footer Section with build micro-tag
        Label footerLabel = new Label("BUILD // 2026.10 • CROSSROADS ENGINE");
        footerLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 9));
        footerLabel.setTextFill(Color.web("#5e7a70"));

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
     * Builds the primary navigation buttons with micro-typography and left-border styling.
     */
    private VBox buildMainActionsBox() {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER_LEFT);
        menuButtons.clear();

        // 01 // START STORY (Primary Action)
        Button startBtn = createMenuNavButton("01 //", "START STORY", "INITIALIZE ROUTE", true, this::showRouteSelection);

        // 02 // LOAD ARCHIVE
        Button loadBtn = createMenuNavButton("02 //", "LOAD ARCHIVE", "RESTORE TIMELINE", false, () -> {
            if (loadGameHandler != null) {
                loadGameHandler.run();
            }
        });

        // 03 // SYSTEM CONFIG
        Button settingsBtn = createMenuNavButton("03 //", "SYSTEM CONFIG", "AUDIO & KEYBINDS", false, this::showSettingsDialog);

        // 04 // TERMINATE
        Button quitBtn = createMenuNavButton("04 //", "TERMINATE", "EXIT APPLICATION", false, this::showQuitConfirmation);

        menuButtons.add(startBtn);
        menuButtons.add(loadBtn);
        menuButtons.add(settingsBtn);
        menuButtons.add(quitBtn);

        box.getChildren().addAll(startBtn, loadBtn, settingsBtn, quitBtn);
        return box;
    }

    /**
     * Helper to create sleek, minimalist left-bordered menu items with micro-typography.
     */
    private Button createMenuNavButton(String indexTag, String title, String subtext, boolean isPrimary, Runnable action) {
        Button btn = new Button();
        btn.getStyleClass().add(isPrimary ? "glass-button-primary" : "glass-button");
        btn.setMaxWidth(Double.MAX_VALUE);

        HBox content = new HBox(10);
        content.setAlignment(Pos.CENTER_LEFT);

        Label indexLabel = new Label(indexTag);
        indexLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        indexLabel.setTextFill(isPrimary ? Color.web("#5fe09a") : Color.web("#7aa896"));

        VBox textCol = new VBox(1);
        textCol.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        titleLabel.setTextFill(Color.WHITE);

        Label subLabel = new Label(subtext);
        subLabel.setFont(Font.font("Segoe UI", 9));
        subLabel.setTextFill(Color.web("#9cbab0"));

        textCol.getChildren().addAll(titleLabel, subLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label arrow = new Label("›");
        arrow.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        arrow.setTextFill(Color.web("#669a88"));

        content.getChildren().addAll(indexLabel, textCol, spacer, arrow);
        btn.setGraphic(content);

        attachHoverSlideAnimation(btn, 8);
        btn.setOnAction(e -> action.run());

        return btn;
    }

    /**
     * Builds the route selection sub-view with vertical character cards and distinct accents.
     */
    private VBox buildRouteSelectionBox() {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER_LEFT);

        Label routeTitle = new Label("// SELECT TIMELINE ROUTE");
        routeTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        routeTitle.setTextFill(Color.web("#80d8b4"));

        Button julesBtn = createRouteCard(
                "01 //", "JULES RIVERA", "The Weight of Expectations",
                "DUTY", "route-card-jules", 1, Color.web("#38bdf8")
        );

        Button mayaBtn = createRouteCard(
                "02 //", "MAYA STERLING", "The Rhythm of Choice",
                "BEAT", "route-card-maya", 2, Color.web("#f59e0b")
        );

        Button noraBtn = createRouteCard(
                "03 //", "NORA CHEN", "Canvas of Tomorrow",
                "VISION", "route-card-nora", 3, Color.web("#10b981")
        );

        Button backBtn = new Button("←  RETURN // MAIN DOCK");
        backBtn.getStyleClass().add("glass-button");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(backBtn, -6);
        backBtn.setOnAction(e -> showMainActions());

        box.getChildren().addAll(routeTitle, julesBtn, mayaBtn, noraBtn, backBtn);
        return box;
    }

    /**
     * Builds an individual vertical character route card with distinct accent color and micro-details.
     */
    private Button createRouteCard(String routeNum, String name, String subtitle, String genreTag,
                                   String styleClass, int storyId, Color accentColor) {
        Button btn = new Button();
        btn.getStyleClass().add(styleClass);
        btn.setMaxWidth(Double.MAX_VALUE);

        HBox content = new HBox(8);
        content.setAlignment(Pos.CENTER_LEFT);

        Label numLabel = new Label(routeNum);
        numLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        numLabel.setTextFill(accentColor);

        VBox textCol = new VBox(1);
        textCol.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12.5));
        nameLabel.setTextFill(Color.WHITE);

        Label descLabel = new Label(subtitle);
        descLabel.setFont(Font.font("Segoe UI", 9.5));
        descLabel.setTextFill(Color.web("#a0bfb5"));

        textCol.getChildren().addAll(nameLabel, descLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label tagLabel = new Label(genreTag);
        tagLabel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); -fx-text-fill: #e0f2ea; -fx-font-size: 8.5px; -fx-font-weight: bold; -fx-padding: 2 5; -fx-background-radius: 3;");

        content.getChildren().addAll(numLabel, textCol, spacer, tagLabel);
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
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), mainActionsBox);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            mainActionsBox.setVisible(false);
            mainActionsBox.setManaged(false);

            routeSelectionBox.setOpacity(0.0);
            routeSelectionBox.setVisible(true);
            routeSelectionBox.setManaged(true);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(150), routeSelectionBox);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    private void showMainActions() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), routeSelectionBox);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            routeSelectionBox.setVisible(false);
            routeSelectionBox.setManaged(false);

            mainActionsBox.setOpacity(0.0);
            mainActionsBox.setVisible(true);
            mainActionsBox.setManaged(true);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(150), mainActionsBox);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    /**
     * Attaches an interactive micro-slide (6-8px X-shift on hover) to a button.
     */
    public void attachHoverSlideAnimation(Button button, double slideDistance) {
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(160), button);
        slideIn.setToX(slideDistance);
        slideIn.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(160), button);
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
     * Plays a staggered entrance sequence: each button slides in from the left
     * and fades in sequentially, delayed by 65ms per item.
     */
    public void playEntranceAnimation() {
        if (activeEntranceAnimation != null) {
            activeEntranceAnimation.stop();
        }

        ParallelTransition pt = new ParallelTransition();

        // Dock container smooth fade-in
        mainCard.setOpacity(0.0);
        mainCard.setTranslateX(-15);

        FadeTransition dockFade = new FadeTransition(Duration.millis(260), mainCard);
        dockFade.setToValue(1.0);

        TranslateTransition dockSlide = new TranslateTransition(Duration.millis(260), mainCard);
        dockSlide.setToX(0);
        dockSlide.setInterpolator(Interpolator.EASE_OUT);

        pt.getChildren().addAll(dockFade, dockSlide);

        // Stagger entrance of each button
        for (int i = 0; i < menuButtons.size(); i++) {
            Button btn = menuButtons.get(i);
            btn.setOpacity(0.0);
            btn.setTranslateX(-20);

            Duration delay = Duration.millis(60 + (i * 65));

            FadeTransition ft = new FadeTransition(Duration.millis(300), btn);
            ft.setToValue(1.0);
            ft.setDelay(delay);
            ft.setInterpolator(Interpolator.EASE_OUT);

            TranslateTransition tt = new TranslateTransition(Duration.millis(300), btn);
            tt.setToX(0);
            tt.setDelay(delay);
            tt.setInterpolator(Interpolator.EASE_OUT);

            pt.getChildren().addAll(ft, tt);
        }

        activeEntranceAnimation = pt;
        pt.play();
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
     * Resets the view to the primary main actions card, safely stopping active
     * transitions and restoring full opacities and transformations.
     */
    public void resetView() {
        if (activeEntranceAnimation != null) {
            activeEntranceAnimation.stop();
            activeEntranceAnimation = null;
        }

        mainCard.setOpacity(1.0);
        mainCard.setTranslateX(0);
        mainCard.setTranslateY(0);

        for (Button btn : menuButtons) {
            btn.setOpacity(1.0);
            btn.setTranslateX(0);
            btn.setTranslateY(0);
        }

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
