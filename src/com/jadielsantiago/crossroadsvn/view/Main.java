package com.jadielsantiago.crossroadsvn.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import com.jadielsantiago.crossroadsvn.controller.GameManager;
import com.jadielsantiago.crossroadsvn.controller.MusicPlayer;
import com.jadielsantiago.crossroadsvn.model.Choice;
import com.jadielsantiago.crossroadsvn.model.ChoiceOption;
import com.jadielsantiago.crossroadsvn.model.DialogueLine;
import com.jadielsantiago.crossroadsvn.model.Jules_Story;
import com.jadielsantiago.crossroadsvn.model.Mayas_Story;
import com.jadielsantiago.crossroadsvn.model.Noras_Story;

import java.util.Queue;

public class Main extends Application {
    private GameManager gameManager;
    private MusicPlayer musicPlayer;
    private Label speakerNameLabel;
    private Label dialogueTextLabel;
    private VBox choiceBoxContainer; // Holds choice buttons
    private StackPane root;

    // Music resource paths (loaded from the classpath)
    private static final String MUSIC_DIR = "/com/jadielsantiago/crossroadsvn/media/music/";
    private static final String MENU_MUSIC = MUSIC_DIR + "MainMenu_Test.mp3";
    private static final String JULES_MUSIC = MUSIC_DIR + "Jules_Test.mp3";
    private static final String MAYA_MUSIC = MUSIC_DIR + "Maya_Test.mp3";
    private static final String NORA_MUSIC = MUSIC_DIR + "Nora_Test.mp3";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gameManager = new GameManager();
        musicPlayer = new MusicPlayer();

        root = new StackPane();
        root.setStyle("-fx-background-color: #2b2b2b;");

        showMainMenu();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Project Crossroads - VN Engine");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMainMenu() {
        root.getChildren().clear();
        root.setOnMouseClicked(null);
        if (root.getScene() != null) {
            root.getScene().setOnKeyPressed(null);
        }

        // Play main menu music
        musicPlayer.play(MENU_MUSIC);

        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Project Crossroads");
        titleLabel.setFont(new Font("Arial Bold", 36));
        titleLabel.setTextFill(Color.WHITE);

        Button julesBtn = new Button("Play Jules's Story");
        Button mayaBtn = new Button("Play Maya's Story");
        Button noraBtn = new Button("Play Nora's Story");

        String btnStyle = "-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;";
        julesBtn.setStyle(btnStyle);
        mayaBtn.setStyle(btnStyle);
        noraBtn.setStyle(btnStyle);

        julesBtn.setOnAction(e -> startStory(1));
        mayaBtn.setOnAction(e -> startStory(2));
        noraBtn.setOnAction(e -> startStory(3));

        menuBox.getChildren().addAll(titleLabel, julesBtn, mayaBtn, noraBtn);
        root.getChildren().add(menuBox);
    }

    private void showDialogueScreen() {
        root.getChildren().clear();

        // Dialogue Box setup
        VBox dialogueBox = new VBox(10);
        dialogueBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 10;");
        dialogueBox.setPadding(new Insets(20));
        dialogueBox.setMaxHeight(150);
        StackPane.setAlignment(dialogueBox, Pos.BOTTOM_CENTER);
        StackPane.setMargin(dialogueBox, new Insets(20));

        speakerNameLabel = new Label("Speaker");
        speakerNameLabel.setFont(new Font("Arial Bold", 18));
        speakerNameLabel.setTextFill(Color.DARKBLUE);

        dialogueTextLabel = new Label("Text...");
        dialogueTextLabel.setFont(new Font("Arial", 16));
        dialogueTextLabel.setWrapText(true);

        dialogueBox.getChildren().addAll(speakerNameLabel, dialogueTextLabel);

        // Choice overlay container (centered on screen)
        choiceBoxContainer = new VBox(15);
        choiceBoxContainer.setAlignment(Pos.CENTER);
        choiceBoxContainer.setVisible(false);

        root.getChildren().addAll(dialogueBox, choiceBoxContainer);

        root.setOnMouseClicked(event -> advanceDialogue());
        if (root.getScene() != null) {
            root.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
                    advanceDialogue();
                }
            });
        }
    }

    private void startStory(int storyId) {
        Queue<DialogueLine> scene = null;

        if (storyId == 1) {
            scene = Jules_Story.getScene();
        } else if (storyId == 2) {
            scene = Mayas_Story.getScene();
        } else if (storyId == 3) {
            scene = Noras_Story.getScene();
        }

        if (scene != null) {
            gameManager.loadScene(scene);

            // Play the character's designated music track
            if (storyId == 1) {
                musicPlayer.play(JULES_MUSIC);
            } else if (storyId == 2) {
                musicPlayer.play(MAYA_MUSIC);
            } else if (storyId == 3) {
                musicPlayer.play(NORA_MUSIC);
            }

            showDialogueScreen();
            advanceDialogue();
        }
    }

    private void advanceDialogue() {
        // Prevent advancing by clicking while a choice is pending on screen
        if (choiceBoxContainer != null && choiceBoxContainer.isVisible()) {
            return;
        }

        DialogueLine nextLine = gameManager.getNextLine();

        if (nextLine != null) {
            speakerNameLabel.setText(nextLine.getSpeaker());
            dialogueTextLabel.setText(nextLine.getText());

            // If this line contains player choices, render them
            if (nextLine instanceof Choice) {
                presentChoices((Choice) nextLine);
            }
        } else {
            showMainMenu();
        }
    }

    private void presentChoices(Choice choice) {
        choiceBoxContainer.getChildren().clear();

        for (ChoiceOption option : choice.getOptions()) {
            Button optionBtn = new Button(option.getOptionText());
            optionBtn.setStyle("-fx-font-size: 15px; -fx-padding: 10 25; -fx-background-color: #1e3d59; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");

            optionBtn.setOnAction(e -> {
                // Hide choice box and inject the selected branch
                choiceBoxContainer.setVisible(false);
                gameManager.branchScene(option.getResultingBranch());
                advanceDialogue();
            });

            choiceBoxContainer.getChildren().add(optionBtn);
        }

        choiceBoxContainer.setVisible(true);
    }
}