package com.jadielsantiago.crossroadsvn.view;

import com.jadielsantiago.crossroadsvn.controller.SaveManager;
import com.jadielsantiago.crossroadsvn.model.SaveState;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.Map;
import java.util.function.Consumer;

public class SaveLoadView extends StackPane {

    public enum Mode {
        SAVE,
        LOAD
    }

    // Pantone 347 C and Crossroads blue-adjacent/emerald palette
    private static final String COLOR_PANTONE_347C = "#009A44"; // Primary emerald accent
    private static final String COLOR_PANTONE_DARK = "#007A36"; // Deep emerald
    private static final String COLOR_BG_MINT = "#eef7f3";     // Soft cool mint background
    private static final String COLOR_SIDEBAR_BG = "#c8ecdc";  // Sleek mint-emerald sidebar
    private static final String COLOR_EMPTY_SLOT = "#a6d6c3";  // Empty slot preview block
    private static final String COLOR_EMPTY_BORDER = "#84c0a8";
    private static final String COLOR_NAV_TEXT = "#163b30";    // Deep slate-green
    private static final String COLOR_TITLE_DARK = "#143327";

    private Mode currentMode;
    private int currentPage = 1;
    private static final int SLOTS_PER_PAGE = 6;
    private static final int TOTAL_PAGES = 9;

    private final Consumer<Integer> saveHandler;
    private final Consumer<Integer> loadHandler;
    private final Runnable returnHandler;
    private final Runnable newGameHandler;
    private final Runnable quitHandler;

    private Label titleLabel;
    private Label pageTitleLabel;
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
        this.currentMode = initialMode;
        this.saveHandler = saveHandler;
        this.loadHandler = loadHandler;
        this.returnHandler = returnHandler;
        this.newGameHandler = newGameHandler;
        this.quitHandler = quitHandler;

        buildUI();
        refreshSlots();
    }

    private void buildUI() {
        this.getChildren().clear();

        // 1. Crossroads custom background (soft mint with crossroads lines & soft ambient circles)
        Pane backgroundPane = createCrossroadsBackground();

        // 2. Main layout: Left sidebar + Right grid area
        HBox mainContainer = new HBox();
        mainContainer.setAlignment(Pos.CENTER_LEFT);

        VBox sidebar = buildSidebar();
        VBox contentArea = buildContentArea();
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        mainContainer.getChildren().addAll(sidebar, contentArea);

        // 3. Modal overlay for dialogs and toasts
        modalOverlay = new StackPane();
        modalOverlay.setVisible(false);
        modalOverlay.setStyle("-fx-background-color: rgba(10, 25, 20, 0.7);");

        // 4. Toast notification label (Pantone 347 C)
        toastLabel = new Label();
        toastLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        toastLabel.setTextFill(Color.WHITE);
        toastLabel.setStyle("-fx-background-color: " + COLOR_PANTONE_347C + "; -fx-padding: 8 22; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);");
        toastLabel.setVisible(false);
        StackPane.setAlignment(toastLabel, Pos.TOP_CENTER);
        StackPane.setMargin(toastLabel, new Insets(20, 0, 0, 0));

        this.getChildren().addAll(backgroundPane, mainContainer, modalOverlay, toastLabel);
    }

    private Pane createCrossroadsBackground() {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: " + COLOR_BG_MINT + ";");

        // Subtle intersecting "crossroads" geometric lines
        Line lineH1 = new Line(0, 180, 900, 180);
        lineH1.setStroke(Color.web("#d5ede2", 0.7));
        lineH1.setStrokeWidth(2);

        Line lineH2 = new Line(0, 420, 900, 420);
        lineH2.setStroke(Color.web("#d5ede2", 0.7));
        lineH2.setStrokeWidth(2);

        Line lineV1 = new Line(480, 0, 480, 700);
        lineV1.setStroke(Color.web("#d5ede2", 0.7));
        lineV1.setStrokeWidth(2);

        pane.getChildren().addAll(lineH1, lineH2, lineV1);

        // Soft ambient floating circles in fresh emerald mint
        double[][] circles = {
                {100, 80, 55}, {260, 120, 75}, {460, 60, 60}, {680, 110, 80},
                {150, 310, 65}, {360, 270, 85}, {570, 320, 70}, {760, 250, 60},
                {220, 490, 80}, {430, 480, 65}, {650, 470, 85}, {70, 520, 50}
        };

        for (double[] cData : circles) {
            Circle circle = new Circle(cData[0], cData[1], cData[2]);
            circle.setFill(Color.web("#c8ecdc", 0.55));
            pane.getChildren().add(circle);
        }

        return pane;
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(215);
        sidebar.setMinWidth(215);
        sidebar.setMaxWidth(215);
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setPadding(new Insets(25, 20, 30, 25));
        sidebar.setStyle("-fx-background-color: " + COLOR_SIDEBAR_BG + "; -fx-background-radius: 0 45 45 0; -fx-effect: dropshadow(gaussian, rgba(0, 154, 68, 0.22), 18, 0, 4, 0);");

        // Top Header: "Save" or "Load" in vibrant Pantone 347 C
        titleLabel = new Label(currentMode == Mode.SAVE ? "Save" : "Load");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 42));
        titleLabel.setTextFill(Color.web(COLOR_PANTONE_347C));
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian, white, 7, 0.8, 0, 0);");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Navigation Menu Buttons
        VBox navLinks = new VBox(10);
        navLinks.setAlignment(Pos.BOTTOM_LEFT);

        Button newGameBtn = createSidebarNavButton("New Game", () -> {
            showConfirmationDialog("Start a New Game?", "Return to character selection?", () -> {
                if (newGameHandler != null) newGameHandler.run();
            });
        });

        saveNavBtn = createSidebarNavButton("Save Game", () -> setMode(Mode.SAVE));
        loadNavBtn = createSidebarNavButton("Load Game", () -> setMode(Mode.LOAD));

        Button settingsBtn = createSidebarNavButton("Settings", this::showSettingsDialog);
        Button helpBtn = createSidebarNavButton("Help", this::showHelpDialog);
        Button quitBtn = createSidebarNavButton("Quit", () -> {
            showConfirmationDialog("Quit Project Crossroads?", "Are you sure you want to exit?", () -> {
                if (quitHandler != null) quitHandler.run();
            });
        });

        Button returnBtn = createSidebarNavButton("Return", () -> {
            if (returnHandler != null) returnHandler.run();
        });
        returnBtn.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_PANTONE_DARK + "; -fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 6 0;");

        updateNavActiveState();

        navLinks.getChildren().addAll(newGameBtn, saveNavBtn, loadNavBtn, settingsBtn, helpBtn, quitBtn, returnBtn);
        sidebar.getChildren().addAll(titleLabel, spacer, navLinks);

        return sidebar;
    }

    private Button createSidebarNavButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        btn.setTextFill(Color.web(COLOR_NAV_TEXT));
        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 3 0;");

        btn.setOnMouseEntered(e -> {
            btn.setTextFill(Color.web(COLOR_PANTONE_347C));
            btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 3 0; -fx-underline: true;");
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 3 0; -fx-underline: false;");
            updateNavActiveState();
        });

        btn.setOnAction(e -> action.run());
        return btn;
    }

    private void updateNavActiveState() {
        if (saveNavBtn != null && loadNavBtn != null) {
            if (currentMode == Mode.SAVE) {
                saveNavBtn.setTextFill(Color.web(COLOR_PANTONE_347C));
                saveNavBtn.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 19));
                loadNavBtn.setTextFill(Color.web(COLOR_NAV_TEXT));
                loadNavBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
            } else {
                loadNavBtn.setTextFill(Color.web(COLOR_PANTONE_347C));
                loadNavBtn.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 19));
                saveNavBtn.setTextFill(Color.web(COLOR_NAV_TEXT));
                saveNavBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
            }
        }
    }

    private VBox buildContentArea() {
        VBox contentArea = new VBox(15);
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setPadding(new Insets(20, 30, 20, 30));

        // Subheader: "Page 1"
        pageTitleLabel = new Label("Page " + currentPage);
        pageTitleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        pageTitleLabel.setTextFill(Color.web(COLOR_TITLE_DARK));

        // 2x3 Save Slots Grid
        slotsGrid = new GridPane();
        slotsGrid.setAlignment(Pos.CENTER);
        slotsGrid.setHgap(22);
        slotsGrid.setVgap(18);

        // Page selector buttons: 1 2 3 4 5 6 7 8 9
        pageSelectorBox = new HBox(12);
        pageSelectorBox.setAlignment(Pos.CENTER);
        buildPageSelector();

        contentArea.getChildren().addAll(pageTitleLabel, slotsGrid, pageSelectorBox);
        return contentArea;
    }

    private void buildPageSelector() {
        pageSelectorBox.getChildren().clear();

        for (int p = 1; p <= TOTAL_PAGES; p++) {
            final int pageNum = p;
            Label pageLbl = new Label(String.valueOf(p));
            pageLbl.setCursor(javafx.scene.Cursor.HAND);

            if (pageNum == currentPage) {
                pageLbl.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 18));
                pageLbl.setTextFill(Color.web(COLOR_PANTONE_347C));
                pageLbl.setStyle("-fx-underline: true;");
            } else {
                pageLbl.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
                pageLbl.setTextFill(Color.web("#507c6c"));
            }

            pageLbl.setOnMouseClicked(e -> {
                currentPage = pageNum;
                pageTitleLabel.setText("Page " + currentPage);
                buildPageSelector();
                refreshSlots();
            });

            pageSelectorBox.getChildren().add(pageLbl);
        }
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
        titleLabel.setText(currentMode == Mode.SAVE ? "Save" : "Load");
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

    private Node buildSlotCard(int slotNum, SaveState state) {
        VBox card = new VBox(6);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(180, 135);
        card.setStyle("-fx-cursor: hand;");

        StackPane frameBox = new StackPane();
        frameBox.setPrefSize(176, 99);
        frameBox.setMinSize(176, 99);
        frameBox.setMaxSize(176, 99);

        VBox textInfoBox = new VBox(2);
        textInfoBox.setAlignment(Pos.CENTER);

        if (state == null) {
            // Empty slot placeholder (Crossroads cool mint-sage block)
            frameBox.setStyle("-fx-background-color: " + COLOR_EMPTY_SLOT + "; -fx-background-radius: 6; -fx-border-color: " + COLOR_EMPTY_BORDER + "; -fx-border-radius: 6; -fx-border-width: 1;");

            Label emptyLabel = new Label("empty slot");
            emptyLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
            emptyLabel.setTextFill(Color.web("#285242"));
            textInfoBox.getChildren().add(emptyLabel);
        } else {
            // Occupied slot with screenshot thumbnail
            frameBox.setStyle("-fx-background-color: #122820; -fx-background-radius: 6; -fx-border-color: " + COLOR_PANTONE_347C + "; -fx-border-radius: 6; -fx-border-width: 2;");

            Image thumb = SaveManager.getThumbnail(slotNum);
            if (thumb != null) {
                ImageView thumbView = new ImageView(thumb);
                thumbView.setFitWidth(172);
                thumbView.setFitHeight(95);
                thumbView.setPreserveRatio(false);
                thumbView.setSmooth(true);
                thumbView.setStyle("-fx-background-radius: 4;");
                frameBox.getChildren().add(thumbView);
            }

            // Delete button in the top-right corner of thumbnail
            Button deleteBtn = new Button("✕");
            deleteBtn.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            deleteBtn.setTextFill(Color.WHITE);
            deleteBtn.setStyle("-fx-background-color: rgba(14, 55, 38, 0.85); -fx-background-radius: 10; -fx-padding: 2 6; -fx-cursor: hand;");
            StackPane.setAlignment(deleteBtn, Pos.TOP_RIGHT);
            StackPane.setMargin(deleteBtn, new Insets(4));
            deleteBtn.setOnAction(e -> {
                e.consume();
                showConfirmationDialog("Delete Save Slot " + slotNum + "?", "This save file will be permanently removed.", () -> {
                    SaveManager.deleteSave(slotNum);
                    showToast("Slot " + slotNum + " deleted.");
                    refreshSlots();
                });
            });
            frameBox.getChildren().add(deleteBtn);

            // Metadata below thumbnail
            String routeName = state.getStoryTitle() != null ? state.getStoryTitle() : "Route";
            String sceneInfo = state.getSceneHeading() != null ? state.getSceneHeading() : "";
            Label headerLbl = new Label(routeName + (sceneInfo.isEmpty() ? "" : " - " + sceneInfo));
            headerLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
            headerLbl.setTextFill(Color.web("#103a27"));
            headerLbl.setMaxWidth(176);
            headerLbl.setAlignment(Pos.CENTER);

            Label timeLbl = new Label(state.getTimestamp() != null ? state.getTimestamp() : "");
            timeLbl.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 10));
            timeLbl.setTextFill(Color.web("#426958"));

            textInfoBox.getChildren().addAll(headerLbl, timeLbl);
        }

        card.getChildren().addAll(frameBox, textInfoBox);

        // Hover animation & styling with Pantone 347 C emerald glow
        card.setOnMouseEntered(e -> {
            card.setScaleX(1.04);
            card.setScaleY(1.04);
            frameBox.setStyle(frameBox.getStyle() + "; -fx-effect: dropshadow(gaussian, " + COLOR_PANTONE_347C + ", 11, 0.55, 0, 0);");
        });

        card.setOnMouseExited(e -> {
            card.setScaleX(1.0);
            card.setScaleY(1.0);
            if (state == null) {
                frameBox.setStyle("-fx-background-color: " + COLOR_EMPTY_SLOT + "; -fx-background-radius: 6; -fx-border-color: " + COLOR_EMPTY_BORDER + "; -fx-border-radius: 6; -fx-border-width: 1;");
            } else {
                frameBox.setStyle("-fx-background-color: #122820; -fx-background-radius: 6; -fx-border-color: " + COLOR_PANTONE_347C + "; -fx-border-radius: 6; -fx-border-width: 2;");
            }
        });

        // Click handler based on mode
        card.setOnMouseClicked(e -> {
            if (currentMode == Mode.SAVE) {
                if (state == null) {
                    executeSave(slotNum);
                } else {
                    showConfirmationDialog("Overwrite Slot " + slotNum + "?", "Previous save data will be overwritten.", () -> {
                        executeSave(slotNum);
                    });
                }
            } else { // Mode.LOAD
                if (state == null) {
                    showToast("Slot " + slotNum + " is empty.");
                } else {
                    showConfirmationDialog("Load Slot " + slotNum + "?", "Load this save state and resume playing?", () -> {
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
            showToast("Game saved to Slot " + slotNum + "!");
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
        modalOverlay.getChildren().clear();

        VBox dialogBox = new VBox(15);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setMaxSize(360, 200);
        dialogBox.setStyle("-fx-background-color: #f0f9f5; -fx-background-radius: 14; -fx-border-color: " + COLOR_PANTONE_347C + "; -fx-border-width: 2; -fx-border-radius: 14; -fx-padding: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 16, 0, 0, 4);");

        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLbl.setTextFill(Color.web(COLOR_PANTONE_DARK));

        Label msgLbl = new Label(message);
        msgLbl.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        msgLbl.setTextFill(Color.web("#1c3a2f"));
        msgLbl.setWrapText(true);
        msgLbl.setAlignment(Pos.CENTER);

        HBox btnBox = new HBox(15);
        btnBox.setAlignment(Pos.CENTER);

        Button yesBtn = new Button("Yes");
        yesBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        yesBtn.setStyle("-fx-background-color: " + COLOR_PANTONE_347C + "; -fx-text-fill: white; -fx-padding: 8 22; -fx-background-radius: 8; -fx-cursor: hand;");
        yesBtn.setOnAction(e -> {
            modalOverlay.setVisible(false);
            if (onConfirm != null) onConfirm.run();
        });

        Button noBtn = new Button("Cancel");
        noBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        noBtn.setStyle("-fx-background-color: #c4e5d7; -fx-text-fill: #1c3a2f; -fx-padding: 8 20; -fx-background-radius: 8; -fx-cursor: hand;");
        noBtn.setOnAction(e -> modalOverlay.setVisible(false));

        btnBox.getChildren().addAll(yesBtn, noBtn);
        dialogBox.getChildren().addAll(titleLbl, msgLbl, btnBox);

        modalOverlay.getChildren().add(dialogBox);
        modalOverlay.setVisible(true);
    }

    private void showSettingsDialog() {
        modalOverlay.getChildren().clear();

        VBox dialogBox = new VBox(15);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setMaxSize(380, 240);
        dialogBox.setStyle("-fx-background-color: #f0f9f5; -fx-background-radius: 14; -fx-border-color: " + COLOR_PANTONE_347C + "; -fx-border-width: 2; -fx-border-radius: 14; -fx-padding: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 16, 0, 0, 4);");

        Label titleLbl = new Label("Settings");
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        titleLbl.setTextFill(Color.web(COLOR_PANTONE_DARK));

        Label textLbl = new Label("Audio & Game Settings\n(Music volume: Active | Dialogue Speed: Instant)");
        textLbl.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        textLbl.setTextFill(Color.web("#1c3a2f"));
        textLbl.setAlignment(Pos.CENTER);

        Button closeBtn = new Button("Close");
        closeBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        closeBtn.setStyle("-fx-background-color: " + COLOR_PANTONE_347C + "; -fx-text-fill: white; -fx-padding: 8 25; -fx-background-radius: 8; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> modalOverlay.setVisible(false));

        dialogBox.getChildren().addAll(titleLbl, textLbl, closeBtn);
        modalOverlay.getChildren().add(dialogBox);
        modalOverlay.setVisible(true);
    }

    private void showHelpDialog() {
        modalOverlay.getChildren().clear();

        VBox dialogBox = new VBox(15);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setMaxSize(400, 260);
        dialogBox.setStyle("-fx-background-color: #f0f9f5; -fx-background-radius: 14; -fx-border-color: " + COLOR_PANTONE_347C + "; -fx-border-width: 2; -fx-border-radius: 14; -fx-padding: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 16, 0, 0, 4);");

        Label titleLbl = new Label("Controls & Help");
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        titleLbl.setTextFill(Color.web(COLOR_PANTONE_DARK));

        Label textLbl = new Label("• Spacebar or Left Click: Advance dialogue\n• Choices: Click on your chosen decision\n• Menu Button or [Esc]: Open Save/Load Menu\n• Save Slots: Click a slot to save or load");
        textLbl.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        textLbl.setTextFill(Color.web("#1c3a2f"));

        Button closeBtn = new Button("Got it");
        closeBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        closeBtn.setStyle("-fx-background-color: " + COLOR_PANTONE_347C + "; -fx-text-fill: white; -fx-padding: 8 25; -fx-background-radius: 8; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> modalOverlay.setVisible(false));

        dialogBox.getChildren().addAll(titleLbl, textLbl, closeBtn);
        modalOverlay.getChildren().add(dialogBox);
        modalOverlay.setVisible(true);
    }
}
