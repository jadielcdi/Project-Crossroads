/*
 * Copyright (c) 2026 Jadiel Santiago. All rights reserved.
 *
 * This software is licensed under the MIT License.
 * See COPYRIGHT.txt in the project root for full license details.
 */
package com.jadielsantiago.crossroadsvn.view;

import com.jadielsantiago.crossroadsvn.controller.MusicPlayer;
import com.jadielsantiago.crossroadsvn.controller.SaveManager;
import com.jadielsantiago.crossroadsvn.model.DialogueLine;
import com.jadielsantiago.crossroadsvn.model.SaveState;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Modern Glassmorphic Save & Load Archive View for Project Crossroads.
 * Replaces the legacy flat DDLC menu with floating frosted glass cards,
 * ambient crossroads lighting, interactive slot cards, and micro-animations.
 */
public class SaveLoadView extends StackPane {

    public enum Mode {
        SAVE,
        LOAD
    }

    private Mode currentMode;
    private int currentPage = 1;
    private static final int SLOTS_PER_PAGE = 6;
    private static final int TOTAL_PAGES = 9;

    private final Consumer<Integer> saveHandler;
    private final Consumer<Integer> loadHandler;
    private final Runnable returnHandler;
    private final Runnable newGameHandler;
    private final Runnable quitHandler;
    private final MusicPlayer musicPlayer;
    private final Supplier<Queue<DialogueLine>> historySupplier;

    private Label titleLabel;
    private Label pageIndicatorLabel;
    private GridPane slotsGrid;
    private HBox pageSelectorBox;
    private Button saveNavBtn;
    private Button loadNavBtn;
    private StackPane modalOverlay;
    private Label toastLabel;

    public SaveLoadView(Mode initialMode,
                        Consumer<Integer> saveHandler,
                        Consumer<Integer> loadHandler,
                        Runnable returnHandler,
                        Runnable newGameHandler,
                        Runnable quitHandler) {
        this(initialMode, saveHandler, loadHandler, returnHandler, newGameHandler, quitHandler, null, (Supplier<Queue<DialogueLine>>) null);
    }

    public SaveLoadView(Mode initialMode,
                        Consumer<Integer> saveHandler,
                        Consumer<Integer> loadHandler,
                        Runnable returnHandler,
                        Runnable newGameHandler,
                        Runnable quitHandler,
                        MusicPlayer musicPlayer) {
        this(initialMode, saveHandler, loadHandler, returnHandler, newGameHandler, quitHandler, musicPlayer, (Supplier<Queue<DialogueLine>>) null);
    }

    public SaveLoadView(Mode initialMode,
                        Consumer<Integer> saveHandler,
                        Consumer<Integer> loadHandler,
                        Runnable returnHandler,
                        Runnable newGameHandler,
                        Runnable quitHandler,
                        MusicPlayer musicPlayer,
                        Queue<DialogueLine> historyQueue) {
        this(initialMode, saveHandler, loadHandler, returnHandler, newGameHandler, quitHandler, musicPlayer, () -> historyQueue);
    }

    public SaveLoadView(Mode initialMode,
                        Consumer<Integer> saveHandler,
                        Consumer<Integer> loadHandler,
                        Runnable returnHandler,
                        Runnable newGameHandler,
                        Runnable quitHandler,
                        MusicPlayer musicPlayer,
                        Supplier<Queue<DialogueLine>> historySupplier) {
        this.currentMode = initialMode;
        this.saveHandler = saveHandler;
        this.loadHandler = loadHandler;
        this.returnHandler = returnHandler;
        this.newGameHandler = newGameHandler;
        this.quitHandler = quitHandler;
        this.musicPlayer = musicPlayer;
        this.historySupplier = historySupplier;

        buildUI();
        refreshSlots();
        playEntranceAnimation();
    }

    private void buildUI() {
        this.getChildren().clear();
        this.setCache(true);
        this.setCacheHint(CacheHint.SPEED);

        // 1. Ambient Frosted Background with Crossroads Geometric Accents
        Pane backgroundPane = createAmbientGlassBackground();

        // 2. Main Console: Floating Left Sidebar Card + Floating Right Content Card
        HBox mainContainer = new HBox(16);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(18, 22, 18, 22));

        VBox sidebar = buildSidebar();
        VBox contentArea = buildContentArea();
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        mainContainer.getChildren().addAll(sidebar, contentArea);

        // 3. Modal Overlay for Dialogs and Confirmations
        modalOverlay = new StackPane();
        modalOverlay.getStyleClass().add("glass-modal-overlay");
        modalOverlay.setVisible(false);
        modalOverlay.setOnMouseClicked(e -> {
            if (e.getTarget() == modalOverlay) {
                hideModal();
            }
        });

        // 4. Toast Notification Label
        toastLabel = new Label();
        toastLabel.getStyleClass().add("glass-toast");
        toastLabel.setVisible(false);
        StackPane.setAlignment(toastLabel, Pos.TOP_CENTER);
        StackPane.setMargin(toastLabel, new Insets(20, 0, 0, 0));

        this.getChildren().addAll(backgroundPane, mainContainer, modalOverlay, toastLabel);
    }

    /**
     * Creates an ambient dark frosted backdrop featuring glowing crossroads geometric lines
     * and soft emerald light orbs.
     */
    private Pane createAmbientGlassBackground() {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: rgba(7, 16, 13, 0.82);");
        pane.setCache(true);
        pane.setCacheHint(CacheHint.SPEED);

        // Subtle glowing crossroads geometric lines
        Line lineH1 = new Line(0, 160, 900, 160);
        lineH1.setStroke(Color.web("#00ba52", 0.12));
        lineH1.setStrokeWidth(1.5);

        Line lineH2 = new Line(0, 440, 900, 440);
        lineH2.setStroke(Color.web("#00ba52", 0.12));
        lineH2.setStrokeWidth(1.5);

        Line lineV1 = new Line(490, 0, 490, 700);
        lineV1.setStroke(Color.web("#00ba52", 0.12));
        lineV1.setStrokeWidth(1.5);

        pane.getChildren().addAll(lineH1, lineH2, lineV1);

        // Soft ambient floating light orbs in emerald
        double[][] circles = {
                {110, 80, 65, 0.06}, {260, 120, 80, 0.04}, {690, 100, 90, 0.05},
                {150, 320, 70, 0.05}, {380, 270, 85, 0.06}, {760, 260, 65, 0.04},
                {220, 490, 80, 0.05}, {450, 480, 70, 0.05}, {660, 470, 85, 0.04}
        };

        for (double[] c : circles) {
            Circle circle = new Circle(c[0], c[1], c[2]);
            circle.setFill(Color.web("#00ba52", c[3]));
            pane.getChildren().add(circle);
        }

        return pane;
    }

    /**
     * Builds the left frosted glass sidebar card containing navigation & mode switching.
     */
    private VBox buildSidebar() {
        VBox sidebar = new VBox(12);
        sidebar.getStyleClass().add("glass-sidebar-card");
        sidebar.setPrefWidth(205);
        sidebar.setMinWidth(205);
        sidebar.setMaxWidth(205);
        sidebar.setAlignment(Pos.TOP_CENTER);

        // Header Section
        Label badgeLabel = new Label("✦ SAVED GAMES ✦");
        badgeLabel.getStyleClass().add("glass-badge");

        titleLabel = new Label(currentMode == Mode.SAVE ? "Save Game" : "Load Game");
        titleLabel.getStyleClass().add("glass-title");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));

        Label subtitle = new Label("Save your place or pick up where you left off");
        subtitle.getStyleClass().add("glass-subtitle");

        Region frostedDivider = new Region();
        frostedDivider.getStyleClass().add("glass-divider");

        VBox headerBox = new VBox(5);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().addAll(badgeLabel, titleLabel, subtitle, frostedDivider);
        VBox.setMargin(frostedDivider, new Insets(6, 0, 4, 0));

        // Mode switch buttons (Save vs Load)
        saveNavBtn = new Button("💾  Save Game");
        saveNavBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(saveNavBtn, 6);
        saveNavBtn.setOnAction(e -> setMode(Mode.SAVE));

        loadNavBtn = new Button("📂  Load Game");
        loadNavBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(loadNavBtn, 6);
        loadNavBtn.setOnAction(e -> setMode(Mode.LOAD));

        VBox modeBox = new VBox(8);
        modeBox.getChildren().addAll(saveNavBtn, loadNavBtn);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Action Navigation Links
        VBox navLinks = new VBox(7);
        navLinks.setAlignment(Pos.BOTTOM_CENTER);

        Button newGameBtn = createSidebarNavButton("✦  New Story", () -> {
            showConfirmationDialog("Start a New Story?", "Return to character selection? Make sure you have saved your game first!", () -> {
                if (newGameHandler != null) newGameHandler.run();
            });
        });

        Button historyBtn = createSidebarNavButton("📜  History", this::showHistoryDialog);
        Button settingsBtn = createSidebarNavButton("⚙  Settings", this::showSettingsDialog);
        Button helpBtn = createSidebarNavButton("?  How to Play", this::showHelpDialog);

        Button quitBtn = createSidebarNavButton("✕  Quit Game", () -> {
            showConfirmationDialog("Quit Game?", "Are you sure you want to quit the game?", () -> {
                if (quitHandler != null) quitHandler.run();
            });
        });

        Button returnBtn = new Button("←  Return");
        returnBtn.getStyleClass().add("glass-button-primary");
        returnBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(returnBtn, -6);
        returnBtn.setOnAction(e -> {
            if (returnHandler != null) returnHandler.run();
        });

        navLinks.getChildren().addAll(newGameBtn, historyBtn, settingsBtn, helpBtn, quitBtn, returnBtn);
        VBox.setMargin(returnBtn, new Insets(4, 0, 0, 0));

        updateNavActiveState();

        sidebar.getChildren().addAll(headerBox, modeBox, spacer, navLinks);
        return sidebar;
    }

    private Button createSidebarNavButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.getStyleClass().add("glass-nav-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(btn, 6);
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private void updateNavActiveState() {
        if (saveNavBtn != null && loadNavBtn != null) {
            if (currentMode == Mode.SAVE) {
                saveNavBtn.getStyleClass().setAll("button", "glass-button-primary");
                loadNavBtn.getStyleClass().setAll("button", "glass-button");
            } else {
                loadNavBtn.getStyleClass().setAll("button", "glass-button-primary");
                saveNavBtn.getStyleClass().setAll("button", "glass-button");
            }
        }
    }

    /**
     * Builds the right frosted glass card containing pagination and the 2x3 slots grid.
     */
    private VBox buildContentArea() {
        VBox contentArea = new VBox(12);
        contentArea.getStyleClass().add("glass-content-card");
        contentArea.setAlignment(Pos.TOP_CENTER);

        // Header bar with Page Title and Page Navigation Buttons
        HBox topBar = new HBox(12);
        topBar.setAlignment(Pos.CENTER_LEFT);

        pageIndicatorLabel = new Label("Page " + currentPage + " of " + TOTAL_PAGES);
        pageIndicatorLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        pageIndicatorLabel.setTextFill(Color.WHITE);

        Label slotRangeBadge = new Label("Saves " + ((currentPage - 1) * SLOTS_PER_PAGE + 1) + " - " + (currentPage * SLOTS_PER_PAGE));
        slotRangeBadge.setStyle("-fx-background-color: rgba(0, 154, 68, 0.22); -fx-text-fill: #5fe09a; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        pageSelectorBox = new HBox(6);
        pageSelectorBox.setAlignment(Pos.CENTER_RIGHT);
        buildPageSelector();

        topBar.getChildren().addAll(pageIndicatorLabel, slotRangeBadge, spacer, pageSelectorBox);

        Region frostedDivider = new Region();
        frostedDivider.getStyleClass().add("glass-divider");

        // 2x3 Save Slots Grid
        slotsGrid = new GridPane();
        slotsGrid.setAlignment(Pos.CENTER);
        slotsGrid.setHgap(16);
        slotsGrid.setVgap(14);
        VBox.setVgrow(slotsGrid, Priority.ALWAYS);

        contentArea.getChildren().addAll(topBar, frostedDivider, slotsGrid);
        return contentArea;
    }

    private void buildPageSelector() {
        pageSelectorBox.getChildren().clear();

        for (int p = 1; p <= TOTAL_PAGES; p++) {
            final int pageNum = p;
            Button pageBtn = new Button(String.valueOf(p));
            if (pageNum == currentPage) {
                pageBtn.getStyleClass().add("glass-page-btn-active");
            } else {
                pageBtn.getStyleClass().add("glass-page-btn");
            }

            pageBtn.setOnAction(e -> {
                currentPage = pageNum;
                pageIndicatorLabel.setText("Page " + currentPage + " of " + TOTAL_PAGES);
                buildPageSelector();
                refreshSlots();
            });

            pageSelectorBox.getChildren().add(pageBtn);
        }
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
        titleLabel.setText(currentMode == Mode.SAVE ? "Save Game" : "Load Game");
        updateNavActiveState();
        refreshSlots();
    }

    public void refreshSlots() {
        slotsGrid.getChildren().clear();
        Map<Integer, SaveState> states = SaveManager.loadSlotMetadataForPage(currentPage, SLOTS_PER_PAGE);

        int startSlot = (currentPage - 1) * SLOTS_PER_PAGE + 1;

        for (int i = 0; i < SLOTS_PER_PAGE; i++) {
            int slotNum = startSlot + i;
            int col = i % 3;
            int row = i / 3;

            Node slotNode = buildSlotCard(slotNum, states.get(slotNum));
            slotsGrid.add(slotNode, col, row);
        }
    }

    /**
     * Builds a single frosted glass save slot card with thumbnail, tags, and micro-animations.
     */
    private Node buildSlotCard(int slotNum, SaveState state) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(164, 134);
        card.setStyle("-fx-cursor: hand;");

        StackPane frameBox = new StackPane();
        frameBox.setPrefSize(158, 88);
        frameBox.setMinSize(158, 88);
        frameBox.setMaxSize(158, 88);

        VBox textInfoBox = new VBox(2);
        textInfoBox.setAlignment(Pos.CENTER);

        if (state == null) {
            // Empty slot card
            frameBox.getStyleClass().add("glass-slot-empty");

            VBox emptyContent = new VBox(3);
            emptyContent.setAlignment(Pos.CENTER);

            Label plusIcon = new Label("✦");
            plusIcon.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
            plusIcon.setTextFill(Color.web("#5fe09a"));

            Label emptyLabel = new Label("Slot " + slotNum + " (Empty)");
            emptyLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
            emptyLabel.setTextFill(Color.web("#80a89a"));

            emptyContent.getChildren().addAll(plusIcon, emptyLabel);
            frameBox.getChildren().add(emptyContent);

            Label subHint = new Label(currentMode == Mode.SAVE ? "Click to save here" : "Nothing saved here");
            subHint.setFont(Font.font("Segoe UI", 10));
            subHint.setTextFill(Color.web("#5a7a70"));
            textInfoBox.getChildren().add(subHint);
        } else {
            // Occupied slot card
            frameBox.getStyleClass().add("glass-slot-occupied");

            Image thumb = SaveManager.getThumbnail(slotNum);
            if (thumb != null) {
                ImageView thumbView = new ImageView(thumb);
                thumbView.setFitWidth(154);
                thumbView.setFitHeight(84);
                thumbView.setPreserveRatio(false);
                thumbView.setSmooth(true);

                // Clip corners of the thumbnail to fit cleanly
                Rectangle clip = new Rectangle(154, 84);
                clip.setArcWidth(10);
                clip.setArcHeight(10);
                thumbView.setClip(clip);

                frameBox.getChildren().add(thumbView);
            }

            // Top-left slot number badge
            Label slotBadge = new Label("Save #" + slotNum);
            slotBadge.setStyle("-fx-background-color: rgba(10, 24, 18, 0.85); -fx-text-fill: #5fe09a; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 2 5; -fx-background-radius: 5;");
            StackPane.setAlignment(slotBadge, Pos.TOP_LEFT);
            StackPane.setMargin(slotBadge, new Insets(4));
            frameBox.getChildren().add(slotBadge);

            // Delete button in top-right corner
            Button deleteBtn = new Button("✕");
            deleteBtn.getStyleClass().add("glass-delete-btn");
            StackPane.setAlignment(deleteBtn, Pos.TOP_RIGHT);
            StackPane.setMargin(deleteBtn, new Insets(4));
            deleteBtn.setOnAction(e -> {
                e.consume();
                showConfirmationDialog("Delete Save #" + slotNum + "?", "Are you sure you want to delete this saved game? This cannot be undone.", () -> {
                    SaveManager.deleteSave(slotNum);
                    showToast("Save #" + slotNum + " deleted.");
                    refreshSlots();
                });
            });
            frameBox.getChildren().add(deleteBtn);

            // Metadata below thumbnail
            String routeName = switch (state.getStoryId()) {
                case 1 -> "Jules's Story";
                case 2 -> "Maya's Story";
                case 3 -> "Nora's Story";
                default -> (state.getStoryTitle() != null && !state.getStoryTitle().isEmpty() && !state.getStoryTitle().equals("Route"))
                        ? state.getStoryTitle() : "Project Crossroads";
            };
            String sceneInfo = state.getSceneHeading() != null ? state.getSceneHeading() : "";
            Label headerLbl = new Label(routeName + (sceneInfo.isEmpty() ? "" : " • " + sceneInfo));
            headerLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
            headerLbl.setTextFill(Color.WHITE);
            headerLbl.setMaxWidth(160);
            headerLbl.setAlignment(Pos.CENTER);

            Label timeLbl = new Label(state.getTimestamp() != null ? state.getTimestamp() : "");
            timeLbl.setFont(Font.font("Segoe UI", 10));
            timeLbl.setTextFill(Color.web("#7fe2b0"));

            textInfoBox.getChildren().addAll(headerLbl, timeLbl);
        }

        card.getChildren().addAll(frameBox, textInfoBox);

        // Micro-animation: Smooth scale on hover
        card.setOnMouseEntered(e -> {
            card.setScaleX(1.035);
            card.setScaleY(1.035);
        });

        card.setOnMouseExited(e -> {
            card.setScaleX(1.0);
            card.setScaleY(1.0);
        });

        // Click handler based on mode
        card.setOnMouseClicked(e -> {
            if (currentMode == Mode.SAVE) {
                if (state == null) {
                    executeSave(slotNum);
                } else {
                    showConfirmationDialog("Save Over Slot #" + slotNum + "?", "A saved game already exists here. Do you want to replace it?", () -> {
                        executeSave(slotNum);
                    });
                }
            } else { // Mode.LOAD
                if (state == null) {
                    showToast("Slot #" + slotNum + " is empty! Pick a saved game to play.");
                } else {
                    showConfirmationDialog("Load Save #" + slotNum + "?", "Jump back to this point in the story?", () -> {
                        if (loadHandler != null) {
                            loadHandler.accept(slotNum);
                        }
                    });
                }
            }
        });

        return card;
    }

    private void executeSave(int slotNum) {
        if (saveHandler != null) {
            saveHandler.accept(slotNum);
            showToast("Game saved to Slot #" + slotNum + "!");
            refreshSlots();
        }
    }

    public void showToast(String message) {
        toastLabel.setText(message);
        toastLabel.setVisible(true);
        toastLabel.setOpacity(1.0);

        FadeTransition ft = new FadeTransition(Duration.millis(1800), toastLabel);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setDelay(Duration.millis(1200));
        ft.setOnFinished(e -> toastLabel.setVisible(false));
        ft.play();
    }

    private void showConfirmationDialog(String title, String message, Runnable onConfirm) {
        VBox dialogBox = new VBox(16);
        dialogBox.getStyleClass().add("glass-modal-card");
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setMaxWidth(360);

        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 19));
        titleLbl.setTextFill(Color.WHITE);

        Label msgLbl = new Label(message);
        msgLbl.setFont(Font.font("Segoe UI", 13));
        msgLbl.setTextFill(Color.web("#b4d1c6"));
        msgLbl.setWrapText(true);
        msgLbl.setAlignment(Pos.CENTER);

        HBox btnBox = new HBox(12);
        btnBox.setAlignment(Pos.CENTER);

        Button noBtn = new Button("Cancel");
        noBtn.getStyleClass().add("glass-button");
        HBox.setHgrow(noBtn, Priority.ALWAYS);
        noBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(noBtn, -4);
        noBtn.setOnAction(e -> hideModal());

        Button yesBtn = new Button("Yes, Continue");
        yesBtn.getStyleClass().add("glass-button-primary");
        HBox.setHgrow(yesBtn, Priority.ALWAYS);
        yesBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(yesBtn, 4);
        yesBtn.setOnAction(e -> {
            hideModal();
            if (onConfirm != null) onConfirm.run();
        });

        btnBox.getChildren().addAll(noBtn, yesBtn);
        dialogBox.getChildren().addAll(titleLbl, msgLbl, btnBox);

        showModal(dialogBox);
    }

    private void showSettingsDialog() {
        VBox dialogBox = new VBox(16);
        dialogBox.getStyleClass().add("glass-modal-card");
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setMaxWidth(380);

        Label titleLbl = new Label("Settings & Music");
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        titleLbl.setTextFill(Color.WHITE);

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

        // Gameplay Shortcuts
        VBox infoBox = new VBox(4);
        infoBox.getStyleClass().add("glass-sub-panel");
        Label infoTitle = new Label("How to Play");
        infoTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        infoTitle.setTextFill(Color.web("#75e4ab"));

        Label infoKeys = new Label(
                "• Spacebar or Left Click: Read the next line\n" +
                "• [Esc]: Open Save & Load menu\n" +
                "• [H]: Open Dialogue History\n" +
                "• [V]: Hide text to view the artwork"
        );
        infoKeys.setFont(Font.font("Segoe UI", 11.5));
        infoKeys.setTextFill(Color.web("#d1e3dc"));
        infoBox.getChildren().addAll(infoTitle, infoKeys);

        Button closeBtn = new Button("✓  Done");
        closeBtn.getStyleClass().add("glass-button-primary");
        closeBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(closeBtn, 6);
        closeBtn.setOnAction(e -> hideModal());

        dialogBox.getChildren().addAll(titleLbl, volumeBox, infoBox, closeBtn);
        showModal(dialogBox);
    }

    private void showHelpDialog() {
        VBox dialogBox = new VBox(16);
        dialogBox.getStyleClass().add("glass-modal-card");
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setMaxWidth(400);

        Label titleLbl = new Label("How to Play & Save");
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        titleLbl.setTextFill(Color.WHITE);

        VBox infoBox = new VBox(6);
        infoBox.getStyleClass().add("glass-sub-panel");

        Label descLbl = new Label(
                "• Saving: Click any slot to save your progress.\n" +
                "• Loading: Click a saved game to keep playing.\n" +
                "• History: Click 📜 History to review dialogue read so far.\n" +
                "• Deleting: Click the ✕ on a slot to remove it.\n" +
                "• Turn Pages: Click 1 to 9 at the top to see more slots.\n" +
                "• Menu: Press [Esc] during the story to open this menu anytime."
        );
        descLbl.setFont(Font.font("Segoe UI", 12));
        descLbl.setTextFill(Color.web("#d1e3dc"));
        infoBox.getChildren().add(descLbl);

        Button closeBtn = new Button("Got it!");
        closeBtn.getStyleClass().add("glass-button-primary");
        closeBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(closeBtn, 6);
        closeBtn.setOnAction(e -> hideModal());

        dialogBox.getChildren().addAll(titleLbl, infoBox, closeBtn);
        showModal(dialogBox);
    }

    /**
     * Displays a scrollable modal backlog showing all dialogue lines read from the screenbox,
     * fetched in FIFO order from the history Queue.
     */
    public void showHistoryDialog() {
        VBox dialogBox = new VBox(12);
        dialogBox.getStyleClass().add("glass-modal-card");
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setPrefWidth(540);
        dialogBox.setMaxWidth(560);
        dialogBox.setMaxHeight(490);

        // Header Section
        Label badgeLabel = new Label("✦ DIALOGUE ARCHIVE ✦");
        badgeLabel.getStyleClass().add("glass-badge");

        Label titleLbl = new Label("Dialogue History");
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        titleLbl.setTextFill(Color.WHITE);

        Label subLbl = new Label("Queue-based backlog of screenbox dialogue text read so far");
        subLbl.setFont(Font.font("Segoe UI", 12));
        subLbl.setTextFill(Color.web("#a2bdb4"));

        Region divider = new Region();
        divider.getStyleClass().add("glass-divider");

        VBox headerBox = new VBox(4);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().addAll(badgeLabel, titleLbl, subLbl, divider);
        VBox.setMargin(divider, new Insets(6, 0, 4, 0));

        // Fetch dialogue entries from the Queue
        Queue<DialogueLine> historyQueue = (historySupplier != null) ? historySupplier.get() : null;
        int count = (historyQueue != null) ? historyQueue.size() : 0;

        // Subheader bar with entry count badge and quick-scroll controls
        HBox controlBar = new HBox(10);
        controlBar.setAlignment(Pos.CENTER_LEFT);
        controlBar.setPadding(new Insets(0, 4, 2, 4));

        Label countBadge = new Label(count + (count == 1 ? " line read" : " lines read"));
        countBadge.setStyle("-fx-background-color: rgba(0, 154, 68, 0.22); -fx-text-fill: #5fe09a; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 9; -fx-background-radius: 6;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button topBtn = new Button("▲ Oldest");
        topBtn.getStyleClass().add("glass-page-btn");
        topBtn.setStyle("-fx-font-size: 11px; -fx-padding: 3 8;");

        Button bottomBtn = new Button("▼ Latest");
        bottomBtn.getStyleClass().add("glass-page-btn");
        bottomBtn.setStyle("-fx-font-size: 11px; -fx-padding: 3 8;");

        controlBar.getChildren().addAll(countBadge, spacer, topBtn, bottomBtn);

        // Content Area inside ScrollPane
        VBox entriesContainer = new VBox(6);
        entriesContainer.setPadding(new Insets(4, 8, 4, 2));

        if (historyQueue == null || historyQueue.isEmpty()) {
            VBox emptyBox = new VBox(8);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(36, 16, 36, 16));

            Label emptyIcon = new Label("📜");
            emptyIcon.setFont(Font.font(32));

            Label emptyTitle = new Label("No Dialogue History Yet");
            emptyTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
            emptyTitle.setTextFill(Color.web("#b4d1c6"));

            Label emptyDesc = new Label("Progress through the story to record screenbox dialogue into your backlog queue.");
            emptyDesc.setFont(Font.font("Segoe UI", 12));
            emptyDesc.setTextFill(Color.web("#6e8a80"));
            emptyDesc.setWrapText(true);
            emptyDesc.setAlignment(Pos.CENTER);

            emptyBox.getChildren().addAll(emptyIcon, emptyTitle, emptyDesc);
            entriesContainer.getChildren().add(emptyBox);
            topBtn.setDisable(true);
            bottomBtn.setDisable(true);
        } else {
            // Render all items in the Queue in FIFO order (earliest to latest)
            for (DialogueLine line : historyQueue) {
                Node itemNode = buildHistoryItem(line);
                if (itemNode != null) {
                    entriesContainer.getChildren().add(itemNode);
                }
            }
        }

        ScrollPane scrollPane = new ScrollPane(entriesContainer);
        scrollPane.getStyleClass().add("glass-scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefHeight(255);
        scrollPane.setMaxHeight(275);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        topBtn.setOnAction(e -> scrollPane.setVvalue(0.0));
        bottomBtn.setOnAction(e -> scrollPane.setVvalue(1.0));

        // Auto-scroll to bottom to view latest dialogue
        Platform.runLater(() -> scrollPane.setVvalue(1.0));

        // Close / Done button
        Button closeBtn = new Button("✓  Back to Menu");
        closeBtn.getStyleClass().add("glass-button-primary");
        closeBtn.setMaxWidth(Double.MAX_VALUE);
        attachHoverSlideAnimation(closeBtn, 6);
        closeBtn.setOnAction(e -> hideModal());

        dialogBox.getChildren().addAll(headerBox, controlBar, scrollPane, closeBtn);
        showModal(dialogBox);
    }

    private Node buildHistoryItem(DialogueLine line) {
        if (line == null) return null;

        String speaker = line.getSpeaker();
        String text = line.getText() != null ? line.getText() : "";

        // If it's a scene heading (e.g., [Scene 1: ...])
        if (text.startsWith("[Scene") && text.contains("]")) {
            HBox sceneBox = new HBox(8);
            sceneBox.setAlignment(Pos.CENTER_LEFT);
            sceneBox.setPadding(new Insets(6, 12, 6, 12));
            sceneBox.setStyle("-fx-background-color: rgba(0, 154, 68, 0.18); -fx-background-radius: 6; -fx-border-color: rgba(0, 186, 82, 0.35); -fx-border-radius: 6; -fx-border-width: 1;");

            Label icon = new Label("✦");
            icon.setTextFill(Color.web("#5fe09a"));
            icon.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));

            Label sceneLbl = new Label(text);
            sceneLbl.setTextFill(Color.web("#8bf7bf"));
            sceneLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

            sceneBox.getChildren().addAll(icon, sceneLbl);
            return sceneBox;
        }

        // Standard dialogue entry card
        VBox card = new VBox(3);
        card.setPadding(new Insets(8, 12, 8, 12));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.04); -fx-background-radius: 8; -fx-border-color: rgba(255, 255, 255, 0.07); -fx-border-radius: 8; -fx-border-width: 1;");

        // Character speaker badge/name
        if (speaker != null && !speaker.trim().isEmpty()) {
            Label spkLabel = new Label(speaker);
            spkLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12.5));

            String spkLower = speaker.toLowerCase();
            if (spkLower.contains("jules")) {
                spkLabel.setTextFill(Color.web("#38bdf8")); // Sky blue
            } else if (spkLower.contains("maya")) {
                spkLabel.setTextFill(Color.web("#f59e0b")); // Amber
            } else if (spkLower.contains("nora")) {
                spkLabel.setTextFill(Color.web("#10b981")); // Emerald
            } else if (spkLower.contains("decision") || spkLower.contains("choice")) {
                spkLabel.setTextFill(Color.web("#f43f5e")); // Coral
            } else if (spkLower.contains("narrator")) {
                spkLabel.setTextFill(Color.web("#80a89a")); // Sage
            } else {
                spkLabel.setTextFill(Color.web("#5fe09a")); // Mint green
            }

            card.getChildren().add(spkLabel);
        }

        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Segoe UI", 12.5));
        textLabel.setTextFill(Color.web("#e6f4ed"));
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(480);
        card.getChildren().add(textLabel);

        // Hover micro-animation
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: rgba(0, 154, 68, 0.12); -fx-background-radius: 8; -fx-border-color: rgba(0, 186, 82, 0.35); -fx-border-radius: 8; -fx-border-width: 1;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.04); -fx-background-radius: 8; -fx-border-color: rgba(255, 255, 255, 0.07); -fx-border-radius: 8; -fx-border-width: 1;"));

        return card;
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

    private void attachHoverSlideAnimation(Button btn, double slideX) {
        btn.setCache(true);
        btn.setCacheHint(CacheHint.SPEED);

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

    private void playEntranceAnimation() {
        this.setOpacity(0.0);
        FadeTransition ft = new FadeTransition(Duration.millis(250), this);
        ft.setToValue(1.0);
        ft.play();
    }
}
