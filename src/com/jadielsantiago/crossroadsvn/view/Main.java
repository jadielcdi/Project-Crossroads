/*
 * Copyright (c) 2026 Jadiel Santiago. All rights reserved.
 *
 * This software is licensed under the MIT License.
 * See COPYRIGHT.txt in the project root for full license details.
 */
package com.jadielsantiago.crossroadsvn.view;


import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import com.jadielsantiago.crossroadsvn.controller.Background_Manager;
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
import java.util.Queue;

public class Main extends Application {
    // Pantone 347 C and Crossroads blue-adjacent/emerald palette
    private static final String COLOR_PANTONE_347C = "#009A44";
    private static final String COLOR_PANTONE_HOVER = "#00ba52";

    private Stage primaryStage;
    private GameManager gameManager;
    private MusicPlayer musicPlayer;
    private Background_Manager backgroundManager;
    private MainMenuController mainMenuController;
    private VBox dialogueBox;
    private Region dialogueHoverZone;
    private boolean isDialogueHidden = false;
    private PauseTransition hoverUnhideTimer;
    private Label speakerNameLabel;
    private Label dialogueTextLabel;
    private VBox choiceBoxContainer; // Holds choice buttons
    private StackPane root;
    private StackPane uiLayer; // UI container layered on top of the background

    // Active gameplay state tracking
    private int currentStoryId = 0; // 1 = Jules, 2 = Maya, 3 = Nora
    private String currentMusicTrack = null;
    private DialogueLine currentLine = null;
    private String currentSceneHeading = "";
    private SaveLoadView activeSaveLoadView = null;
    private boolean isMenuOpen = false;

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
        gameManager = new GameManager();
        musicPlayer = new MusicPlayer();
        backgroundManager = new Background_Manager();

        SettingsManager.loadSettings();
        musicPlayer.setVolume(SettingsManager.getMusicVolume());
        musicPlayer.setMuted(SettingsManager.isMusicMuted());

        root = new StackPane();
        root.setStyle("-fx-background-color: #2b2b2b;");

        uiLayer = new StackPane();
        uiLayer.setPickOnBounds(false);

        // Layer stack: base background layer -> UI layer (future sprite layer will sit between them)
        root.getChildren().addAll(backgroundManager.getBackgroundPane(), uiLayer);

        hoverUnhideTimer = new PauseTransition(Duration.seconds(2));
        hoverUnhideTimer.setOnFinished(e -> setDialogueHidden(false));

        mainMenuController = new MainMenuController(
                this::startStory,
                () -> openSaveLoadMenu(SaveLoadView.Mode.LOAD),
                Platform::exit,
                backgroundManager,
                musicPlayer
        );

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

        Scene scene = new Scene(root, initWidth, initHeight);
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
    }

    private void showMainMenu() {
        isMenuOpen = false;
        activeSaveLoadView = null;
        currentStoryId = 0;
        uiLayer.getChildren().clear();
        root.setOnMouseClicked(null);
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

        // Dialogue Box setup with subtle Pantone 347 C border
        dialogueBox = new VBox(8);
        dialogueBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.94); -fx-background-radius: 12; -fx-border-color: rgba(0, 154, 68, 0.35); -fx-border-width: 1.5; -fx-border-radius: 12; -fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 3);");
        dialogueBox.setCache(true);
        dialogueBox.setCacheHint(CacheHint.SPEED);
        dialogueBox.setPadding(new Insets(16, 20, 16, 20));
        dialogueBox.setMaxHeight(160);
        dialogueBox.setOpacity(1.0);
        dialogueBox.setVisible(true);
        StackPane.setAlignment(dialogueBox, Pos.BOTTOM_CENTER);
        StackPane.setMargin(dialogueBox, new Insets(20));

        speakerNameLabel = new Label(currentLine != null ? currentLine.getSpeaker() : "Speaker");
        speakerNameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 19));
        speakerNameLabel.setTextFill(Color.web("#14384a"));

        // Dialogue Text
        dialogueTextLabel = new Label(currentLine != null ? currentLine.getText() : "Text...");
        dialogueTextLabel.setFont(Font.font("Segoe UI", 16));
        dialogueTextLabel.setWrapText(true);

        dialogueBox.getChildren().addAll(speakerNameLabel, dialogueTextLabel);

        // Invisible Hover Zone: Detects mouse hover over where the dialogue box is supposed to be when hidden
        dialogueHoverZone = new Region();
        dialogueHoverZone.setMaxHeight(160);
        dialogueHoverZone.setPrefHeight(160);
        dialogueHoverZone.setPickOnBounds(true);
        dialogueHoverZone.setVisible(false); // Only active while dialogue is hidden
        StackPane.setAlignment(dialogueHoverZone, Pos.BOTTOM_CENTER);
        StackPane.setMargin(dialogueHoverZone, new Insets(20));

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

        String normalStyle = "-fx-background-color: rgba(14, 38, 30, 0.85); -fx-text-fill: #cbf0de; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 7 18; -fx-background-radius: 18; -fx-cursor: hand; -fx-border-color: " + COLOR_PANTONE_347C + "; -fx-border-radius: 18; -fx-border-width: 1.5;";
        String hoverStyle = "-fx-background-color: " + COLOR_PANTONE_HOVER + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 7 18; -fx-background-radius: 18; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, " + COLOR_PANTONE_HOVER + ", 10, 0.5, 0, 0);";

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

        // Click on background: if dialogue is hidden, unhide it; otherwise advance dialogue
        root.setOnMouseClicked(event -> {
            if (isDialogueHidden) {
                setDialogueHidden(false);
                return;
            }
            if (!isMenuOpen) {
                advanceDialogue();
            }
        });

        // Key bindings: SPACE advances dialogue, ESCAPE toggles menu, V toggles dialogue visibility
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
                } else if (event.getCode() == KeyCode.SPACE) {
                    if (isDialogueHidden) {
                        setDialogueHidden(false);
                    } else if (!isMenuOpen) {
                        advanceDialogue();
                    }
                    event.consume();
                }
            });
        }
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
                () -> gameManager.getDialogueHistoryQueue()
        );

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

        // If returned while in main menu and nothing else was loaded
        if (currentStoryId == 0) {
            showMainMenu();
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
            default -> "Project Crossroads";
        };

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        boolean isChoice = choiceBoxContainer != null && choiceBoxContainer.isVisible();

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
                gameManager.getHistoryLinkedList()
        );

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

        speakerNameLabel.setText(state.getCurrentSpeaker());
        dialogueTextLabel.setText(state.getCurrentText());

        if (gameManager.getDialogueHistoryQueue().isEmpty() && currentLine != null) {
            gameManager.recordDialogue(currentLine);
        }

        if (state.isChoiceActive() && currentLine instanceof Choice) {
            presentChoices((Choice) currentLine);
        } else {
            choiceBoxContainer.setVisible(false);
        }
    }

    private void startStory(int storyId) {
        backgroundManager.stopAmbientZoom();
        backgroundManager.setBackground(STORY_PLACEHOLDER_BACKGROUND);
        this.currentStoryId = storyId;
        this.currentSceneHeading = "";
        gameManager.clearDialogueHistory();
        JSCParser.StoryMetadata meta = JSCParser.getStoryMetadata(storyId);
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
            }
        }

        if (scene != null && !scene.isEmpty()) {
            gameManager.loadScene(scene);
            if (currentMusicTrack != null) {
                musicPlayer.play(currentMusicTrack);
            }
            showDialogueScreen();
            advanceDialogue();
        } else {
            System.err.println("[Main] Failed to load story script for ID: " + storyId);
        }
    }

    private void advanceDialogue() {
        // Prevent advancing by clicking while a choice is pending on screen
        if (choiceBoxContainer != null && choiceBoxContainer.isVisible()) {
            return;
        }

        DialogueLine nextLine = gameManager.getNextLine();
        this.currentLine = nextLine;

        if (nextLine != null) {
            // Track scene headings for save state labels
            String text = nextLine.getText();
            if (text != null && text.startsWith("[Scene")) {
                int closingBracket = text.indexOf(']');
                if (closingBracket != -1) {
                    currentSceneHeading = text.substring(1, closingBracket);
                }
            }

            speakerNameLabel.setText(nextLine.getSpeaker());
            dialogueTextLabel.setText(nextLine.getText());

            // Save screenbox dialogue text to history queue
            gameManager.recordDialogue(nextLine);

            // If this line contains player choices, render them
            if (nextLine instanceof Choice) {
                presentChoices((Choice) nextLine);
            }
        } else {
            if (currentStoryId > 0) {
                ProgressManager.recordStoryCompletion(currentStoryId);
            }
            showMainMenu();
        }
    }

    private void presentChoices(Choice choice) {
        choiceBoxContainer.getChildren().clear();

        for (ChoiceOption option : choice.getOptions()) {
            Button optionBtn = new Button(option.getOptionText());
            optionBtn.setStyle("-fx-font-size: 15px; -fx-padding: 10 25; -fx-background-color: #1e3d59; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
            optionBtn.setFocusTraversable(false); // Prevents accidental spacebar selection

            optionBtn.setOnAction(e -> {
                // Hide choice box and inject the selected branch
                choiceBoxContainer.setVisible(false);
                gameManager.recordDialogue("Decision", "► Chosen: \"" + option.getOptionText() + "\"");
                gameManager.branchScene(option.getResultingBranch());
                advanceDialogue();
            });

            choiceBoxContainer.getChildren().add(optionBtn);
        }

        choiceBoxContainer.setVisible(true);
    }
}