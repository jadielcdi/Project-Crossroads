/*
 * Copyright (c) 2026 Jadiel Santiago. All rights reserved.
 *
 * This software is licensed under the MIT License.
 * See COPYRIGHT.txt in the project root for full license details.
 */
package com.jadielsantiago.crossroadsvn.view;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.media.AudioClip;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.jadielsantiago.crossroadsvn.controller.Background_Manager;
import com.jadielsantiago.crossroadsvn.controller.CharacterManager;
import com.jadielsantiago.crossroadsvn.controller.GameManager;
import com.jadielsantiago.crossroadsvn.controller.JSCParser;
import com.jadielsantiago.crossroadsvn.controller.MainMenuController;
import com.jadielsantiago.crossroadsvn.controller.MusicPlayer;
import com.jadielsantiago.crossroadsvn.controller.ProgressManager;
import com.jadielsantiago.crossroadsvn.controller.SaveManager;
import com.jadielsantiago.crossroadsvn.controller.SettingsManager;
import com.jadielsantiago.crossroadsvn.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Main extends Application {
    // Pantone 347 C and Crossroads blue-adjacent/emerald palette
    private static final String COLOR_PANTONE_347C = "#009A44";
    private static final String COLOR_PANTONE_HOVER = "#00ba52";

    public static final double VIRTUAL_WIDTH = 1280.0;
    public static final double VIRTUAL_HEIGHT = 720.0;

    private Stage primaryStage;
    private GameManager gameManager;
    private MusicPlayer musicPlayer;
    private Background_Manager backgroundManager;
    private CharacterManager characterManager;
    private MainMenuController mainMenuController;
    private VBox dialogueBox;
    private StackPane dialogueContentStack;
    private Region dialogueHoverZone;
    private Text textMeasureProbe;
    private boolean isDialogueHidden = false;
    private PauseTransition hoverUnhideTimer;
    private Label speakerNameLabel;
    private Label dialogueTextLabel;
    private VBox choiceBoxContainer; // Holds choice buttons
    private StackPane appRoot; // Outer full-window viewport container
    private StackPane ambientBackdropPane; // Ambient blurred backdrop container
    private ImageView ambientBackdropView; // Blurred image view reflecting active scene colors
    private StackPane root; // Fixed 16:9 game canvas (1280x720)
    private StackPane characterLayer; // Dedicated 1280x720 character sprite layer (sits between background and UI)
    private StackPane uiLayer; // UI container layered on top of the background

    // Active gameplay state tracking
    private int currentStoryId = 0; // 1 = Jules, 2 = Maya, 3 = Nora
    private String currentMusicTrack = null;
    private DialogueLine currentLine = null;
    private String currentSceneHeading = "";
    private SaveLoadView activeSaveLoadView = null;
    private boolean isMenuOpen = false;
    private boolean isAutoMode = false;
    private Button autoChip = null;
    private final PauseTransition autoAdvanceTimer = new PauseTransition(Duration.seconds(5));

    // Typewriter dialogue animator and visual novel prompt
    private TextAnimator textAnimator;
    private Label continuePromptLabel;
    private FadeTransition continuePromptPulse;
    private AudioClip charTickClip;

    // Sound effect resource paths
    private static final String SOUND_DIR = "/com/jadielsantiago/crossroadsvn/media/sounds/";
    private static final String TICK_SOUND = SOUND_DIR + "typewriter_tick.wav";

    // Music resource paths (loaded from the classpath)
    private static final String MUSIC_DIR = "/com/jadielsantiago/crossroadsvn/media/music/";
    private static final String MENU_MUSIC = MUSIC_DIR + "MainMenu_Test.mp3";
    private static final String JULES_MUSIC = MUSIC_DIR + "Jules_Test.mp3";
    private static final String MAYA_MUSIC = MUSIC_DIR + "Maya_Test.mp3";
    private static final String NORA_MUSIC = MUSIC_DIR + "Nora_Test.mp3";

    // Background resource paths
    private static final String MENU_BACKGROUND = "Crossroads Title Screen.jpg";
    private static final String STORY_PLACEHOLDER_BACKGROUND = "Test_Background.jpg";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(360);
        gameManager = new GameManager();
        musicPlayer = new MusicPlayer();
        backgroundManager = new Background_Manager();

        SettingsManager.loadSettings();
        musicPlayer.setVolume(SettingsManager.getMusicVolume());
        musicPlayer.setMuted(SettingsManager.isMusicMuted());

        try {
            java.net.URL tickUrl = getClass().getResource(TICK_SOUND);
            if (tickUrl != null) {
                charTickClip = new AudioClip(tickUrl.toExternalForm());
            }
        } catch (Exception e) {
            System.err.println("Note: Typewriter sound clip could not be loaded: " + e.getMessage());
        }

        // Fixed 16:9 game canvas container
        root = new StackPane();
        root.setStyle("-fx-background-color: #070e0b;");
        root.setPrefSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        root.setMinSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        root.setMaxSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        // Dynamic clip ensures scaling and ambient zoom stays cleanly bounded to the
        // 16:9 canvas
        javafx.scene.shape.Rectangle gameClip = new javafx.scene.shape.Rectangle(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        root.setClip(gameClip);

        uiLayer = new StackPane();
        uiLayer.setPickOnBounds(false);

        // Dedicated character sprite layer locked to the 1280x720 virtual coordinate
        // space
        characterLayer = new StackPane();
        characterLayer.setPrefSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        characterLayer.setMinSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        characterLayer.setMaxSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        characterLayer.setPickOnBounds(false);

        // Layer stack: base background layer -> character sprite layer -> UI layer
        root.getChildren().addAll(backgroundManager.getBackgroundPane(), characterLayer, uiLayer);

        characterManager = new CharacterManager(characterLayer);

        // Ambient blurred backdrop: takes active scene/title artwork colors and creates
        // a soft glowing letterbox
        ambientBackdropView = new ImageView();
        ambientBackdropView.setSmooth(true);
        ambientBackdropView.setPreserveRatio(true);
        ambientBackdropView.setCache(true);
        ambientBackdropView.setCacheHint(CacheHint.SPEED);

        GaussianBlur ambientBlur = new GaussianBlur(75);
        ambientBackdropView.setEffect(ambientBlur);

        // Mirror active background image
        ambientBackdropView.imageProperty().bind(backgroundManager.getImageView().imageProperty());
        ambientBackdropView.imageProperty().addListener((obs, oldImg, newImg) -> {
            if (newImg != null && newImg.getProgress() < 1.0) {
                newImg.progressProperty().addListener((o, op, np) -> {
                    if (np.doubleValue() >= 1.0) {
                        updateAmbientBackdropScaling();
                    }
                });
            }
            updateAmbientBackdropScaling();
        });

        // Ambient tint overlay for subtle, cinematic darkening
        StackPane ambientTintOverlay = new StackPane();
        ambientTintOverlay.setStyle("-fx-background-color: rgba(7, 14, 11, 0.48);");

        ambientBackdropPane = new StackPane(ambientBackdropView, ambientTintOverlay);
        ambientBackdropPane.setStyle("-fx-background-color: #070e0b;");

        javafx.scene.shape.Rectangle ambientClip = new javafx.scene.shape.Rectangle();
        ambientClip.widthProperty().bind(ambientBackdropPane.widthProperty());
        ambientClip.heightProperty().bind(ambientBackdropPane.heightProperty());
        ambientBackdropPane.setClip(ambientClip);

        // Outer viewport container housing ambient blurred glow and framed 16:9 game
        // canvas
        appRoot = new StackPane(ambientBackdropPane, root);
        StackPane.setAlignment(root, Pos.CENTER);

        // Letterboxing and proportional 16:9 viewport scaling
        javafx.beans.value.ChangeListener<Number> resizeListener = (obs, oldVal, newVal) -> updateViewportScale();
        appRoot.widthProperty().addListener(resizeListener);
        appRoot.heightProperty().addListener(resizeListener);

        primaryStage.fullScreenProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(this::updateViewportScale);
        });

        hoverUnhideTimer = new PauseTransition(Duration.seconds(2));
        hoverUnhideTimer.setOnFinished(e -> setDialogueHidden(false));

        mainMenuController = new MainMenuController(
                this::startStory,
                () -> openSaveLoadMenu(SaveLoadView.Mode.LOAD),
                Platform::exit,
                backgroundManager,
                musicPlayer);

        showMainMenu();

        double initWidth = SettingsManager.getLogicalWidth();
        double initHeight = SettingsManager.getLogicalHeight();
        try {
            Screen primaryScreen = Screen.getPrimary();
            if (primaryScreen != null) {
                initWidth = Math.min(initWidth, primaryScreen.getVisualBounds().getWidth());
                initHeight = Math.min(initHeight, primaryScreen.getVisualBounds().getHeight());
            }
        } catch (Throwable ignored) {
        }

        Scene scene = new Scene(appRoot, initWidth, initHeight);
        java.net.URL cssUrl = getClass().getResource("/com/jadielsantiago/crossroadsvn/style/main_menu.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (mainMenuController != null) {
                    mainMenuController.handleEscape();
                }
                event.consume();
            }
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F11) {
                if (primaryStage != null) {
                    boolean nextFs = !primaryStage.isFullScreen();
                    primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                    primaryStage.setFullScreen(nextFs);
                    primaryStage.setFullScreenExitHint("");
                    SettingsManager.setFullScreen(nextFs);
                    SettingsManager.saveSettings();
                    Platform.runLater(this::updateViewportScale);
                    event.consume();
                }
            }
        });

        primaryStage.setTitle("Project Crossroads - VN Engine");
        primaryStage.setScene(scene);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        if (SettingsManager.isFullScreen()) {
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
        }
        primaryStage.show();
        primaryStage.centerOnScreen();
        Platform.runLater(this::updateViewportScale);
    }

    /**
     * Dynamically scales the 16:9 game viewport to fit any window/display
     * resolution
     * while strictly maintaining the 16:9 aspect ratio and
     * letterboxing/pillarboxing outer margins.
     */
    private void updateViewportScale() {
        if (appRoot == null || root == null) {
            return;
        }
        double w = appRoot.getWidth();
        double h = appRoot.getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }
        double scale = Math.min(w / VIRTUAL_WIDTH, h / VIRTUAL_HEIGHT);
        if (scale > 0) {
            root.setScaleX(scale);
            root.setScaleY(scale);
        }
        updateAmbientBackdropScaling();
    }

    /**
     * Dynamically scales the ambient blurred backdrop image to completely cover
     * the full window with a slight bleed margin for seamless edge blending.
     */
    private void updateAmbientBackdropScaling() {
        if (ambientBackdropView == null || appRoot == null) {
            return;
        }
        Image img = ambientBackdropView.getImage();
        if (img == null) {
            return;
        }
        double w = appRoot.getWidth();
        double h = appRoot.getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }
        double iw = img.getWidth();
        double ih = img.getHeight();
        if (iw <= 0 || ih <= 0) {
            return;
        }

        // Cover scale with 12% bleed margin so blurred edges don't fade to black at
        // screen borders
        double scale = Math.max(w / iw, h / ih) * 1.12;
        ambientBackdropView.setFitWidth(iw * scale);
        ambientBackdropView.setFitHeight(ih * scale);
    }

    private void showMainMenu() {
        isMenuOpen = false;
        activeSaveLoadView = null;
        currentStoryId = 0;
        setAutoMode(false);
        if (autoAdvanceTimer != null) {
            autoAdvanceTimer.stop();
        }
        if (textAnimator != null) {
            textAnimator.stop();
        }
        if (continuePromptLabel != null) {
            continuePromptLabel.setVisible(false);
        }
        uiLayer.getChildren().clear();
        if (characterManager != null) {
            characterManager.clearAllCharacters();
        } else if (characterLayer != null) {
            characterLayer.getChildren().clear();
        }
        root.setOnMouseClicked(null);
        if (appRoot != null) {
            appRoot.setOnMouseClicked(null);
        }
        if (root.getScene() != null) {
            root.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    if (mainMenuController != null) {
                        mainMenuController.handleEscape();
                    }
                    event.consume();
                }
            });
        }

        // Set main menu background, start ambient breathing zoom loop, and play music
        backgroundManager.setBackground(MENU_BACKGROUND);
        backgroundManager.startAmbientZoom();
        musicPlayer.play(MENU_MUSIC);

        if (mainMenuController != null) {
            mainMenuController.resetView();
            uiLayer.getChildren().add(mainMenuController.getView());
            mainMenuController.playEntranceAnimation();
        }
    }

    private void showDialogueScreen() {
        uiLayer.getChildren().clear();
        isMenuOpen = false;
        isDialogueHidden = false;
        activeSaveLoadView = null;
        if (hoverUnhideTimer != null) {
            hoverUnhideTimer.stop();
        }
        if (autoAdvanceTimer != null) {
            autoAdvanceTimer.stop();
            autoAdvanceTimer.setOnFinished(e -> {
                if (isAutoMode && !isMenuOpen && currentStoryId > 0) {
                    if (choiceBoxContainer != null && choiceBoxContainer.isVisible()) {
                        setAutoMode(false);
                        return;
                    }
                    advanceDialogue();
                }
            });
        }

        // Dialogue Box setup with M3 Expressive Dark Bento HUD
        dialogueBox = new VBox(8);
        dialogueBox.getStyleClass().add("m3-dialogue-card");
        dialogueBox.setPadding(new Insets(18, 28, 18, 28));
        dialogueBox.setMinWidth(1140);
        dialogueBox.setPrefWidth(1140);
        dialogueBox.setMaxWidth(1140);
        dialogueBox.setMinHeight(165);
        dialogueBox.setMaxHeight(Region.USE_PREF_SIZE);
        dialogueBox.setOpacity(1.0);
        dialogueBox.setVisible(true);
        StackPane.setAlignment(dialogueBox, Pos.BOTTOM_CENTER);
        StackPane.setMargin(dialogueBox, new Insets(0, 0, 22, 0));

        speakerNameLabel = new Label();
        updateSpeakerBadge(currentLine != null ? currentLine.getSpeaker() : "");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Quick-HUD Pill Bar
        HBox quickToolbar = new HBox(4);
        quickToolbar.getStyleClass().add("m3-quick-toolbar");
        quickToolbar.setAlignment(Pos.CENTER_RIGHT);

        autoChip = createQuickChip("Auto", this::toggleAutoMode);
        if (isAutoMode) {
            autoChip.getStyleClass().add("m3-quick-chip-active");
            autoChip.setStyle("-fx-text-fill: #7ADC9B; -fx-font-weight: 800;");
        }

        Button historyChip = createQuickChip("History", () -> {
            openSaveLoadMenu(SaveLoadView.Mode.SAVE);
            if (activeSaveLoadView != null) {
                activeSaveLoadView.showHistoryDialog();
            }
        });

        Button saveChip = createQuickChip("Save", () -> {
            openSaveLoadMenu(SaveLoadView.Mode.SAVE);
        });

        Button loadChip = createQuickChip("Load", () -> {
            openSaveLoadMenu(SaveLoadView.Mode.LOAD);
        });

        Button hideChip = createQuickChip("Hide", this::toggleDialogueHidden);

        Button menuChip = createQuickChip("Menu", () -> {
            openSaveLoadMenu(SaveLoadView.Mode.SAVE);
        });

        quickToolbar.getChildren().addAll(autoChip, historyChip, saveChip, loadChip, hideChip, menuChip);

        HBox headerRow = new HBox(8, speakerNameLabel, spacer, quickToolbar);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        // Dialogue Text
        dialogueTextLabel = new Label(currentLine != null ? currentLine.getText() : "");
        dialogueTextLabel.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 17.5));
        dialogueTextLabel.setTextFill(Color.web("#E6ECE8"));
        dialogueTextLabel.setLineSpacing(4.0);
        dialogueTextLabel.setWrapText(true);
        dialogueTextLabel.setMinWidth(1084);
        dialogueTextLabel.setPrefWidth(1084);
        dialogueTextLabel.setMaxWidth(1084);

        // Blinking Continue Indicator (▾)
        continuePromptLabel = new Label("▾");
        continuePromptLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        continuePromptLabel.setTextFill(Color.web("#7ADC9B"));
        continuePromptLabel.setVisible(false);

        continuePromptPulse = new FadeTransition(Duration.millis(550), continuePromptLabel);
        continuePromptPulse.setFromValue(1.0);
        continuePromptPulse.setToValue(0.2);
        continuePromptPulse.setCycleCount(FadeTransition.INDEFINITE);
        continuePromptPulse.setAutoReverse(true);
        continuePromptPulse.play();

        dialogueContentStack = new StackPane();
        dialogueContentStack.setAlignment(Pos.TOP_LEFT);
        dialogueContentStack.getChildren().addAll(dialogueTextLabel, continuePromptLabel);
        StackPane.setAlignment(continuePromptLabel, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(continuePromptLabel, new Insets(0, 4, 0, 0));

        dialogueBox.getChildren().setAll(headerRow, dialogueContentStack);

        if (textMeasureProbe == null) {
            textMeasureProbe = new Text();
            textMeasureProbe.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 17.5));
            textMeasureProbe.setLineSpacing(4.0);
            textMeasureProbe.setWrappingWidth(1084);
        }
        updateDialogueBoxDimensions(currentLine != null ? currentLine.getText() : "");

        // Initialize TextAnimator
        textAnimator = new TextAnimator(dialogueTextLabel, charTickClip);
        textAnimator.setIndicatorNode(continuePromptLabel);
        textAnimator.setDelayNanos(SettingsManager.getTextDelayNanos());
        textAnimator.setSoundEnabled(SettingsManager.isTextSoundEnabled() && !SettingsManager.isMusicMuted());

        // Dialogue box click handler
        dialogueBox.setOnMouseClicked(event -> {
            if (isDialogueHidden) {
                setDialogueHidden(false);
                event.consume();
                return;
            }
            if (!isMenuOpen) {
                handleDialogueInteraction();
                event.consume();
            }
        });

        // Invisible Hover Zone: Detects mouse hover over where the dialogue box is
        // supposed to be when hidden
        dialogueHoverZone = new Region();
        dialogueHoverZone.setMinWidth(1140);
        dialogueHoverZone.setPrefWidth(1140);
        dialogueHoverZone.setMaxWidth(1140);
        dialogueHoverZone.minHeightProperty().bind(dialogueBox.heightProperty());
        dialogueHoverZone.prefHeightProperty().bind(dialogueBox.heightProperty());
        dialogueHoverZone.maxHeightProperty().bind(dialogueBox.heightProperty());
        dialogueHoverZone.setPickOnBounds(true);
        dialogueHoverZone.setVisible(false); // Only active while dialogue is hidden
        StackPane.setAlignment(dialogueHoverZone, Pos.BOTTOM_CENTER);
        StackPane.setMargin(dialogueHoverZone, new Insets(0, 0, 22, 0));

        dialogueHoverZone.setOnMouseEntered(e -> {
            if (isDialogueHidden) {
                hoverUnhideTimer.playFromStart();
            }
        });

        dialogueHoverZone.setOnMouseExited(e -> {
            hoverUnhideTimer.stop();
        });

        // Choice overlay container (centered on screen)
        choiceBoxContainer = new VBox(15);
        choiceBoxContainer.setAlignment(Pos.CENTER);
        choiceBoxContainer.setVisible(false);

        // Top-Right Menu Area: Transparent until hovering over that part of the screen
        StackPane menuHitArea = new StackPane();
        menuHitArea.setPrefSize(140, 65);
        menuHitArea.setMaxSize(140, 65);
        StackPane.setAlignment(menuHitArea, Pos.TOP_RIGHT);

        Button topMenuBtn = new Button("☰ Menu");
        topMenuBtn.setFocusTraversable(false);
        topMenuBtn.setOpacity(0.0); // Transparent until hover

        String normalStyle = "-fx-background-color: rgba(14, 38, 30, 0.85); -fx-text-fill: #cbf0de; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 7 18; -fx-background-radius: 18; -fx-cursor: hand; -fx-border-color: "
                + COLOR_PANTONE_347C + "; -fx-border-radius: 18; -fx-border-width: 1.5;";
        String hoverStyle = "-fx-background-color: " + COLOR_PANTONE_HOVER
                + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 7 18; -fx-background-radius: 18; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, "
                + COLOR_PANTONE_HOVER + ", 10, 0.5, 0, 0);";

        topMenuBtn.setStyle(normalStyle);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), topMenuBtn);
        fadeIn.setToValue(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), topMenuBtn);
        fadeOut.setToValue(0.0);

        menuHitArea.setOnMouseEntered(e -> {
            fadeOut.stop();
            fadeIn.playFromStart();
        });

        menuHitArea.setOnMouseExited(e -> {
            fadeIn.stop();
            fadeOut.playFromStart();
        });

        topMenuBtn.setOnMouseEntered(e -> topMenuBtn.setStyle(hoverStyle));
        topMenuBtn.setOnMouseExited(e -> topMenuBtn.setStyle(normalStyle));
        topMenuBtn.setOnAction(e -> openSaveLoadMenu(SaveLoadView.Mode.SAVE));

        menuHitArea.getChildren().add(topMenuBtn);
        StackPane.setMargin(topMenuBtn, new Insets(16, 20, 0, 0));

        uiLayer.getChildren().addAll(dialogueHoverZone, dialogueBox, choiceBoxContainer, menuHitArea);

        // Click on background or letterbox margins: if dialogue is hidden, unhide it;
        // otherwise advance dialogue
        root.setOnMouseClicked(event -> {
            if (isDialogueHidden) {
                setDialogueHidden(false);
                return;
            }
            if (!isMenuOpen) {
                handleDialogueInteraction();
            }
        });

        if (appRoot != null) {
            appRoot.setOnMouseClicked(event -> {
                if (isDialogueHidden) {
                    setDialogueHidden(false);
                    return;
                }
                if (!isMenuOpen && currentStoryId > 0) {
                    handleDialogueInteraction();
                }
            });
        }

        // Key bindings: SPACE advances dialogue, ESCAPE toggles menu, V toggles
        // dialogue visibility
        if (root.getScene() != null) {
            root.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    if (isMenuOpen) {
                        if (activeSaveLoadView != null && activeSaveLoadView.isModalOpen()) {
                            activeSaveLoadView.closeModal();
                        } else {
                            closeSaveLoadMenu();
                        }
                    } else {
                        if (isDialogueHidden) {
                            setDialogueHidden(false);
                        }
                        openSaveLoadMenu(SaveLoadView.Mode.SAVE);
                    }
                    event.consume();
                } else if (event.getCode() == KeyCode.H) {
                    if (!isMenuOpen) {
                        openSaveLoadMenu(SaveLoadView.Mode.SAVE);
                        if (activeSaveLoadView != null) {
                            activeSaveLoadView.showHistoryDialog();
                        }
                    }
                    event.consume();
                } else if (event.getCode() == KeyCode.F11) {
                    if (primaryStage != null) {
                        boolean nextFs = !primaryStage.isFullScreen();
                        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                        primaryStage.setFullScreen(nextFs);
                        primaryStage.setFullScreenExitHint("");
                        SettingsManager.setFullScreen(nextFs);
                        SettingsManager.saveSettings();
                    }
                    event.consume();
                } else if (event.getCode() == KeyCode.V) {
                    if (!isMenuOpen) {
                        toggleDialogueHidden();
                    }
                    event.consume();
                } else if (event.getCode() == KeyCode.A) {
                    if (!isMenuOpen) {
                        toggleAutoMode();
                    }
                    event.consume();
                } else if (event.getCode() == KeyCode.SPACE) {
                    if (isDialogueHidden) {
                        setDialogueHidden(false);
                    } else if (!isMenuOpen) {
                        handleDialogueInteraction();
                    }
                    event.consume();
                }
            });
        }
    }

    /**
     * Handles player click or spacebar input during story playback.
     * If dialogue text is currently actively typing out, immediately finishes the
     * line.
     * If dialogue text is already complete, advances to the next dialogue line.
     */
    private void handleDialogueInteraction() {
        // Prevent advancing while choice options are waiting on screen
        if (choiceBoxContainer != null && choiceBoxContainer.isVisible()) {
            return;
        }

        // If typewriter is actively typing characters, reveal full line immediately
        if (textAnimator != null && textAnimator.isRunning()) {
            textAnimator.finishImmediately();
            return;
        }

        // Advance to next dialogue line
        advanceDialogue();
    }

    private void setDialogueHidden(boolean hidden) {
        this.isDialogueHidden = hidden;
        if (hoverUnhideTimer != null) {
            hoverUnhideTimer.stop();
        }

        if (dialogueBox != null) {
            FadeTransition ft = new FadeTransition(Duration.millis(200), dialogueBox);
            ft.setToValue(hidden ? 0.0 : 1.0);
            ft.setOnFinished(e -> {
                if (hidden) {
                    dialogueBox.setVisible(false);
                }
            });
            if (!hidden) {
                dialogueBox.setVisible(true);
            }
            ft.play();
        }

        if (choiceBoxContainer != null && choiceBoxContainer.isVisible()) {
            choiceBoxContainer.setOpacity(hidden ? 0.0 : 1.0);
        }

        if (dialogueHoverZone != null) {
            dialogueHoverZone.setVisible(hidden);
        }
    }

    private void toggleDialogueHidden() {
        setDialogueHidden(!isDialogueHidden);
    }

    private void openSaveLoadMenu(SaveLoadView.Mode mode) {
        if (autoAdvanceTimer != null) {
            autoAdvanceTimer.stop();
        }
        if (textAnimator != null && textAnimator.isRunning()) {
            textAnimator.finishImmediately();
        }
        if (isMenuOpen && activeSaveLoadView != null) {
            activeSaveLoadView.setMode(mode);
            return;
        }

        isMenuOpen = true;

        // Capture screenshot of the game frame for visual thumbnail preview
        WritableImage frameSnapshot = null;
        try {
            frameSnapshot = root.snapshot(null, null);
        } catch (Exception e) {
            System.err.println("Snapshot capture error: " + e.getMessage());
        }

        final WritableImage snapshotToSave = frameSnapshot;

        activeSaveLoadView = new SaveLoadView(
                mode,
                slotIndex -> handleSaveSlot(slotIndex, snapshotToSave),
                this::handleLoadSlot,
                this::closeSaveLoadMenu,
                this::showMainMenu,
                Platform::exit,
                musicPlayer,
                () -> gameManager.getDialogueHistoryQueue());

        uiLayer.getChildren().add(activeSaveLoadView);

        if (currentStoryId == 0 && root.getScene() != null) {
            root.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    if (isMenuOpen) {
                        if (activeSaveLoadView != null && activeSaveLoadView.isModalOpen()) {
                            activeSaveLoadView.closeModal();
                        } else {
                            closeSaveLoadMenu();
                        }
                    }
                    event.consume();
                }
            });
        }
    }

    private void closeSaveLoadMenu() {
        if (activeSaveLoadView != null) {
            uiLayer.getChildren().remove(activeSaveLoadView);
            activeSaveLoadView = null;
        }
        isMenuOpen = false;

        if (textAnimator != null) {
            textAnimator.setDelayNanos(SettingsManager.getTextDelayNanos());
            textAnimator.setSoundEnabled(SettingsManager.isTextSoundEnabled() && !SettingsManager.isMusicMuted());
        }

        // If returned while in main menu and nothing else was loaded
        if (currentStoryId == 0) {
            showMainMenu();
        } else if (isAutoMode && (choiceBoxContainer == null || !choiceBoxContainer.isVisible())) {
            scheduleAutoAdvance();
        }
    }

    private void handleSaveSlot(int slotIndex, WritableImage screenshot) {
        if (currentStoryId == 0) {
            System.err.println("[Main] Cannot save game: No story currently active.");
            return;
        }

        String storyName = switch (currentStoryId) {
            case 1 -> "Jules's Story";
            case 2 -> "Maya's Story";
            case 3 -> "Nora's Story";
            case 4 -> "Bonus Story: Saturday Off-Campus";
            default -> "Project Crossroads";
        };

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        boolean isChoice = choiceBoxContainer != null && choiceBoxContainer.isVisible();

        Map<CharacterPosition, CharacterSlotState> activeChars = characterManager != null
                ? characterManager.getActiveCharacters()
                : new HashMap<>();

        SaveState state = new SaveState(
                slotIndex,
                currentStoryId,
                storyName,
                time,
                currentSceneHeading,
                speakerNameLabel != null ? speakerNameLabel.getText() : "Speaker",
                dialogueTextLabel != null ? dialogueTextLabel.getText() : "",
                currentMusicTrack,
                backgroundManager.getCurrentBackgroundPath(),
                currentLine,
                gameManager.getCurrentSceneQueue(),
                isChoice,
                gameManager.getHistoryLinkedList(),
                activeChars);

        SaveManager.saveGame(slotIndex, state, screenshot);
    }

    private void handleLoadSlot(int slotIndex) {
        SaveState state = SaveManager.loadGame(slotIndex);
        if (state == null) {
            return;
        }

        closeSaveLoadMenu();
        backgroundManager.stopAmbientZoom();

        // Restore game state
        this.currentStoryId = state.getStoryId();
        this.currentMusicTrack = state.getCurrentMusic();
        this.currentSceneHeading = state.getSceneHeading();
        this.currentLine = state.getCurrentLine();

        if (currentMusicTrack != null) {
            musicPlayer.play(currentMusicTrack);
        }

        if (state.getCurrentBackground() != null && !state.getCurrentBackground().isEmpty()) {
            backgroundManager.setBackground(state.getCurrentBackground());
        } else {
            backgroundManager.setBackground(STORY_PLACEHOLDER_BACKGROUND);
        }

        gameManager.setCurrentSceneQueue(state.getRemainingQueue());
        gameManager.setDialogueHistoryQueue(state.getHistoryQueue());

        showDialogueScreen();

        if (characterManager != null) {
            characterManager.restoreCharacters(state.getActiveCharacters());
            characterManager.highlightSpeaker(state.getCurrentSpeaker());
        }

        updateSpeakerBadge(state.getCurrentSpeaker());
        updateDialogueBoxDimensions(state.getCurrentText());
        dialogueTextLabel.setText(state.getCurrentText());

        if (textAnimator != null) {
            textAnimator.stop();
        }

        if (gameManager.getDialogueHistoryQueue().isEmpty() && currentLine != null) {
            gameManager.recordDialogue(currentLine);
        }

        if (state.isChoiceActive() && currentLine instanceof Choice) {
            presentChoices((Choice) currentLine);
            if (continuePromptLabel != null) {
                continuePromptLabel.setVisible(false);
            }
        } else {
            choiceBoxContainer.setVisible(false);
            if (continuePromptLabel != null) {
                continuePromptLabel.setVisible(true);
            }
        }
    }

    private void startStory(int storyId) {
        backgroundManager.stopAmbientZoom();
        this.currentStoryId = storyId;
        this.currentSceneHeading = "";
        gameManager.clearDialogueHistory();
        JSCParser.StoryMetadata meta = JSCParser.getStoryMetadata(storyId);
        String initialBg = (meta != null && meta.getDefaultBackground() != null)
                ? meta.getDefaultBackground()
                : STORY_PLACEHOLDER_BACKGROUND;
        backgroundManager.setBackground(initialBg);
        Queue<DialogueLine> scene = JSCParser.loadStory(storyId);

        if (meta != null && meta.getDefaultMusic() != null) {
            currentMusicTrack = meta.getDefaultMusic();
        } else {
            if (storyId == 1) {
                currentMusicTrack = JULES_MUSIC;
            } else if (storyId == 2) {
                currentMusicTrack = MAYA_MUSIC;
            } else if (storyId == 3) {
                currentMusicTrack = NORA_MUSIC;
            } else if (storyId == 4) {
                currentMusicTrack = MENU_MUSIC;
            }
        }

        if (scene != null && !scene.isEmpty()) {
            gameManager.loadScene(scene);
            if (currentMusicTrack != null) {
                musicPlayer.play(currentMusicTrack);
            }
            if (characterManager != null) {
                characterManager.clearAllCharacters();
            }
            showDialogueScreen();
            advanceDialogue();
        } else {
            System.err.println("[Main] Failed to load story script for ID: " + storyId);
        }
    }

    private void advanceDialogue() {
        if (autoAdvanceTimer != null) {
            autoAdvanceTimer.stop();
        }

        // Prevent advancing by clicking while a choice is pending on screen
        if (choiceBoxContainer != null && choiceBoxContainer.isVisible()) {
            return;
        }

        DialogueLine nextLine = gameManager.getNextLine();
        this.currentLine = nextLine;

        if (nextLine != null) {
            // Process sprite directives from speaker tags (e.g. Jules_Stressed(left)) and
            // inline text tags
            ParsedDialogue parsed = processSpriteDirectives(nextLine);

            // Track scene headings for save state labels
            String text = parsed.cleanText();
            if (text != null && text.startsWith("[Scene")) {
                int closingBracket = text.indexOf(']');
                if (closingBracket != -1) {
                    currentSceneHeading = text.substring(1, closingBracket);
                }
            }

            updateSpeakerBadge(parsed.cleanSpeaker());
            updateDialogueBoxDimensions(parsed.cleanText());

            if (characterManager != null) {
                characterManager.highlightSpeaker(parsed.cleanSpeaker());
            }

            // Save screenbox dialogue text to history queue with clean speaker and text
            gameManager.recordDialogue(parsed.cleanSpeaker(), parsed.cleanText());

            // Animate dialogue text with typewriter effect
            if (nextLine instanceof Choice choice) {
                setAutoMode(false);
                choiceBoxContainer.setVisible(false);
                if (textAnimator != null) {
                    textAnimator.setDelayNanos(SettingsManager.getTextDelayNanos());
                    textAnimator
                            .setSoundEnabled(SettingsManager.isTextSoundEnabled() && !SettingsManager.isMusicMuted());
                    textAnimator.animate(parsed.cleanText(), () -> presentChoices(choice));
                } else {
                    dialogueTextLabel.setText(parsed.cleanText());
                    presentChoices(choice);
                }
            } else {
                if (textAnimator != null) {
                    textAnimator.setDelayNanos(SettingsManager.getTextDelayNanos());
                    textAnimator
                            .setSoundEnabled(SettingsManager.isTextSoundEnabled() && !SettingsManager.isMusicMuted());
                    textAnimator.animate(parsed.cleanText(), () -> {
                        if (isAutoMode) {
                            scheduleAutoAdvance();
                        }
                    });
                } else {
                    dialogueTextLabel.setText(parsed.cleanText());
                    if (isAutoMode) {
                        scheduleAutoAdvance();
                    }
                }
            }
        } else {
            setAutoMode(false);
            if (currentStoryId > 0) {
                ProgressManager.recordStoryCompletion(currentStoryId);
            }
            showMainMenu();
        }
    }

    private void toggleAutoMode() {
        setAutoMode(!isAutoMode);
    }

    private void setAutoMode(boolean active) {
        this.isAutoMode = active;
        if (autoChip != null) {
            autoChip.getStyleClass().remove("m3-quick-chip-active");
            if (active) {
                autoChip.getStyleClass().add("m3-quick-chip-active");
                autoChip.setStyle("-fx-text-fill: #7ADC9B; -fx-font-weight: 800;");
            } else {
                autoChip.setStyle("");
            }
        }
        if (!active) {
            if (autoAdvanceTimer != null) {
                autoAdvanceTimer.stop();
            }
        } else {
            if (choiceBoxContainer != null && choiceBoxContainer.isVisible()) {
                setAutoMode(false);
                return;
            }
            if (textAnimator != null && !textAnimator.isRunning()) {
                scheduleAutoAdvance();
            }
        }
    }

    private void scheduleAutoAdvance() {
        if (!isAutoMode || isMenuOpen) {
            return;
        }
        if (choiceBoxContainer != null && choiceBoxContainer.isVisible()) {
            setAutoMode(false);
            return;
        }
        if (autoAdvanceTimer != null) {
            autoAdvanceTimer.stop();
            autoAdvanceTimer.playFromStart();
        }
    }

    private Button createQuickChip(String text, Runnable action) {
        Button chip = new Button(text);
        chip.getStyleClass().add("m3-quick-chip");
        chip.setFocusTraversable(false);
        chip.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, javafx.event.Event::consume);
        chip.setOnAction(e -> {
            e.consume();
            action.run();
        });
        return chip;
    }

    private void updateSpeakerBadge(String speaker) {
        if (speakerNameLabel == null) {
            return;
        }
        speakerNameLabel.getStyleClass().removeAll("m3-speaker-pill", "m3-speaker-pill-narrator");
        if (speaker == null || speaker.trim().isEmpty() || "Narrator".equalsIgnoreCase(speaker.trim())) {
            speakerNameLabel.getStyleClass().add("m3-speaker-pill-narrator");
            speakerNameLabel.setText(speaker != null && !speaker.trim().isEmpty() ? speaker : "Narrator");
        } else {
            speakerNameLabel.getStyleClass().add("m3-speaker-pill");
            speakerNameLabel.setText(speaker);
        }
    }

    /**
     * Dynamically sizes the dialogue content container and outer box to accommodate
     * extensive dialogue without text clipping, preserving a clean baseline height
     * for standard-length dialogue lines.
     */
    private void updateDialogueBoxDimensions(String text) {
        if (textMeasureProbe == null || dialogueContentStack == null) {
            return;
        }
        textMeasureProbe.setText(text != null ? text : "");
        double measuredHeight = textMeasureProbe.getLayoutBounds().getHeight();

        // Base content height for standard 1-3 line dialogue: 84px (gives total box height ~165px).
        // For extensive dialogue (e.g. 4-6 lines), dynamically grow contentStack to fit all lines + buffer for prompt.
        double contentHeight = Math.max(84.0, measuredHeight + 16.0);
        dialogueContentStack.setMinHeight(contentHeight);
        dialogueContentStack.setPrefHeight(contentHeight);
    }

    private void presentChoices(Choice choice) {
        setAutoMode(false);
        choiceBoxContainer.getChildren().clear();
        choiceBoxContainer.setMaxWidth(680);
        choiceBoxContainer.setMaxHeight(Region.USE_PREF_SIZE);
        StackPane.setMargin(choiceBoxContainer, new Insets(0, 0, 140, 0));

        for (ChoiceOption option : choice.getOptions()) {
            Button optionBtn = new Button(option.getOptionText());
            optionBtn.getStyleClass().add("m3-choice-card");
            optionBtn.setMaxWidth(640);
            optionBtn.setFocusTraversable(false); // Prevents accidental spacebar selection

            ScaleTransition stIn = new ScaleTransition(Duration.millis(120), optionBtn);
            stIn.setToX(1.02);
            stIn.setToY(1.02);
            ScaleTransition stOut = new ScaleTransition(Duration.millis(120), optionBtn);
            stOut.setToX(1.0);
            stOut.setToY(1.0);

            optionBtn.setOnMouseEntered(ev -> {
                stOut.stop();
                stIn.playFromStart();
            });
            optionBtn.setOnMouseExited(ev -> {
                stIn.stop();
                stOut.playFromStart();
            });

            optionBtn.setOnAction(e -> {
                // Hide choice box and inject the selected branch
                choiceBoxContainer.setVisible(false);
                gameManager.recordDialogue("Decision", "► Chosen: \"" + option.getOptionText() + "\"");
                gameManager.branchScene(option.getResultingBranch());
                advanceDialogue();
            });

            choiceBoxContainer.getChildren().add(optionBtn);
        }

        // Hide continue prompt while choice is pending player selection
        if (continuePromptLabel != null) {
            continuePromptLabel.setVisible(false);
        }

        choiceBoxContainer.setVisible(true);
    }

    /**
     * Returns the dedicated 1280x720 character sprite layer positioned between
     * the background layer and the UI layer.
     */
    public StackPane getCharacterLayer() {
        return characterLayer;
    }

    /**
     * Returns the active CharacterManager instance.
     */
    public CharacterManager getCharacterManager() {
        return characterManager;
    }

    /**
     * Displays a character sprite at the specified position.
     */
    public void showCharacter(String characterId, String expressionOrPose, CharacterPosition position) {
        if (characterManager != null) {
            characterManager.showCharacter(characterId, expressionOrPose, position);
        }
    }

    /**
     * Hides the character sprite at the specified position.
     */
    public void hideCharacter(CharacterPosition position) {
        if (characterManager != null) {
            characterManager.hideCharacter(position);
        }
    }

    /**
     * Clears all character sprites from the stage.
     */
    public void clearAllCharacters() {
        if (characterManager != null) {
            characterManager.clearAllCharacters();
        }
    }

    /**
     * Sets the opacity of a character position slot (useful for dimming inactive
     * speakers).
     */
    public void setCharacterPositionOpacity(CharacterPosition position, double opacity) {
        if (characterManager != null) {
            characterManager.setPositionOpacity(position, opacity);
        }
    }

    /**
     * Data record containing sanitized speaker name and cleaned dialogue text.
     */
    private record ParsedDialogue(String cleanSpeaker, String cleanText) {
    }

    /**
     * Parses speaker strings (e.g. "Jules_Stressed(left)", "Jules(Stressed, left)")
     * and inline dialogue text tags (e.g. "[SPRITE: Jules_Stressed, left]", "[HIDE:
     * left]")
     * to dynamically invoke the CharacterManager while displaying a clean speaker
     * name on screen.
     */
    private ParsedDialogue processSpriteDirectives(DialogueLine line) {
        if (line == null) {
            return new ParsedDialogue("Speaker", "");
        }

        String rawSpeaker = line.getSpeaker() != null ? line.getSpeaker() : "";
        String rawText = line.getText() != null ? line.getText() : "";
        String cleanSpeaker = rawSpeaker;
        String cleanText = rawText;

        // 1. Check for speaker tag syntax: e.g. "Jules_Stressed(left)" or
        // "Jules(Stressed, left)"
        // Pattern A: Character_Expression(position) -> e.g. Jules_Stressed(left)
        java.util.regex.Pattern patA = java.util.regex.Pattern
                .compile("^([A-Za-z0-9]+)_([A-Za-z0-9_]+)\\s*\\(\\s*([A-Za-z]+)\\s*\\)$");
        java.util.regex.Matcher matA = patA.matcher(rawSpeaker.trim());
        if (matA.matches()) {
            String charId = matA.group(1);
            String expression = matA.group(2);
            CharacterPosition pos = CharacterPosition.fromString(matA.group(3));
            cleanSpeaker = charId;
            if (characterManager != null) {
                characterManager.showCharacter(charId, expression, pos);
            }
        } else {
            // Pattern B: Character(expression, position) -> e.g. Jules(Stressed, left)
            java.util.regex.Pattern patB = java.util.regex.Pattern
                    .compile("^([A-Za-z0-9_]+)\\s*\\(\\s*([A-Za-z0-9_]+)\\s*,\\s*([A-Za-z]+)\\s*\\)$");
            java.util.regex.Matcher matB = patB.matcher(rawSpeaker.trim());
            if (matB.matches()) {
                String charId = matB.group(1);
                String expression = matB.group(2);
                CharacterPosition pos = CharacterPosition.fromString(matB.group(3));
                cleanSpeaker = charId;
                if (characterManager != null) {
                    characterManager.showCharacter(charId, expression, pos);
                }
            } else {
                // Pattern C: Character_Expression -> e.g. Jules_Stressed
                java.util.regex.Pattern patC = java.util.regex.Pattern.compile("^([A-Za-z0-9]+)_([A-Za-z0-9_]+)$");
                java.util.regex.Matcher matC = patC.matcher(rawSpeaker.trim());
                if (matC.matches()) {
                    String charId = matC.group(1);
                    String expression = matC.group(2);
                    cleanSpeaker = charId;
                    if (characterManager != null) {
                        CharacterPosition pos = findCharacterPositionOrMiddle(charId);
                        characterManager.showCharacter(charId, expression, pos);
                    }
                }
            }
        }

        // 2. Check for inline bracket tags in dialogue text: e.g. [SPRITE:
        // Jules_Stressed, left] or [HIDE: left]
        if (cleanText.contains("[SPRITE:") || cleanText.contains("[sprite:")) {
            java.util.regex.Pattern spriteTagPat = java.util.regex.Pattern.compile(
                    "\\[(?i)SPRITE:\\s*([A-Za-z0-9_]+)(?:[_,\\s]+([A-Za-z0-9_]+))?(?:[_,\\s]+([A-Za-z]+))?\\s*\\]");
            java.util.regex.Matcher m = spriteTagPat.matcher(cleanText);
            while (m.find()) {
                String p1 = m.group(1);
                String p2 = m.group(2);
                String p3 = m.group(3);
                String charId = p1;
                String expr = p2 != null ? p2 : "Reference";
                CharacterPosition pos = p3 != null ? CharacterPosition.fromString(p3) : CharacterPosition.MIDDLE;

                if (p1.contains("_") && p2 != null) {
                    String[] parts = p1.split("_", 2);
                    charId = parts[0];
                    expr = parts[1];
                    pos = CharacterPosition.fromString(p2);
                } else if (p3 == null && p2 != null && (p2.equalsIgnoreCase("left") || p2.equalsIgnoreCase("middle")
                        || p2.equalsIgnoreCase("center") || p2.equalsIgnoreCase("right"))) {
                    expr = "Reference";
                    pos = CharacterPosition.fromString(p2);
                }

                if (characterManager != null) {
                    characterManager.showCharacter(charId, expr, pos);
                }
            }
            cleanText = spriteTagPat.matcher(cleanText).replaceAll("").trim();
        }

        if (cleanText.contains("[HIDE:") || cleanText.contains("[hide:")) {
            java.util.regex.Pattern hideTagPat = java.util.regex.Pattern
                    .compile("\\[(?i)HIDE:\\s*([A-Za-z0-9_]+)\\s*\\]");
            java.util.regex.Matcher m = hideTagPat.matcher(cleanText);
            while (m.find()) {
                String target = m.group(1).toLowerCase();
                if (characterManager != null) {
                    if (target.contains("all")) {
                        characterManager.clearAllCharacters();
                    } else if (target.equals("left") || target.equals("right") || target.equals("middle")
                            || target.equals("center")) {
                        characterManager.hideCharacter(CharacterPosition.fromString(target));
                    } else {
                        String charName = target.contains("_") ? target.split("_")[0] : target;
                        CharacterPosition charPos = null;
                        for (CharacterPosition pos : CharacterPosition.values()) {
                            if (charName.equalsIgnoreCase(characterManager.getActiveCharacter(pos))) {
                                charPos = pos;
                                break;
                            }
                        }
                        if (charPos != null) {
                            characterManager.hideCharacter(charPos);
                        } else {
                            characterManager.hideCharacter(CharacterPosition.fromString(target));
                        }
                    }
                }
            }
            cleanText = hideTagPat.matcher(cleanText).replaceAll("").trim();
        }

        // 3. Check for background directives: e.g. [BG: Campus_Quad.jpg] or
        // [BACKGROUND: Campus_Quad.jpg]
        if (cleanText.contains("[BG:") || cleanText.contains("[bg:") ||
                cleanText.contains("[BACKGROUND:") || cleanText.contains("[background:")) {
            java.util.regex.Pattern bgTagPat = java.util.regex.Pattern
                    .compile("\\[(?i)(?:BG|BACKGROUND):\\s*([^\\]]+)\\s*\\]");
            java.util.regex.Matcher m = bgTagPat.matcher(cleanText);
            StringBuilder sb = new StringBuilder();
            while (m.find()) {
                String fullTag = m.group(0);
                String target = m.group(1).trim();
                if ((target.startsWith("\"") && target.endsWith("\""))
                        || (target.startsWith("'") && target.endsWith("'"))) {
                    target = target.substring(1, target.length() - 1).trim();
                }

                if (isBackgroundImageDirective(fullTag, target)) {
                    if (backgroundManager != null) {
                        backgroundManager.changeBackground(target);
                    }
                    m.appendReplacement(sb, "");
                } else {
                    m.appendReplacement(sb, java.util.regex.Matcher.quoteReplacement(fullTag));
                }
            }
            m.appendTail(sb);
            cleanText = sb.toString().trim();
        }

        return new ParsedDialogue(cleanSpeaker, cleanText);
    }

    private boolean isBackgroundImageDirective(String fullTag, String target) {
        if (target == null || target.isEmpty()) {
            return false;
        }
        // Explicit [BG: ...] tag is always a background change directive
        if (fullTag.regionMatches(true, 0, "[bg:", 0, 4)) {
            return true;
        }
        // Image extension present (.jpg, .jpeg, .png, .webp, .gif)
        if (target.toLowerCase().matches(".*\\.(jpg|jpeg|png|webp|gif)$")) {
            return true;
        }
        // Exists in background assets
        if (backgroundManager != null && backgroundManager.hasBackground(target)) {
            return true;
        }
        // Short identifier without sentence punctuation/commas (under 40 chars)
        if (target.length() <= 40 && !target.contains(",") && !target.contains(".") && !target.contains("!")) {
            return true;
        }
        return false;
    }

    private CharacterPosition findCharacterPositionOrMiddle(String characterId) {
        if (characterManager != null) {
            for (CharacterPosition pos : CharacterPosition.values()) {
                if (characterId.equalsIgnoreCase(characterManager.getActiveCharacter(pos))) {
                    return pos;
                }
            }
        }
        return CharacterPosition.MIDDLE;
    }
}