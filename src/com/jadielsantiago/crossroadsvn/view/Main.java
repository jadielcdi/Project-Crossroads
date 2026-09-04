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
import com.jadielsantiago.crossroadsvn.model.DialogueLine;

import java.util.LinkedList;
import java.util.Queue;

public class Main extends Application {
    
    private GameManager gameManager; // The Controller
    private Label speakerNameLabel;
    private Label dialogueTextLabel;
    private StackPane root; // Made class-level to allow screen swapping

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gameManager = new GameManager();
        
        // Root container that will hold either the Menu or the Dialogue UI
        root = new StackPane();
        root.setStyle("-fx-background-color: #2b2b2b;");

        // Load the Main Menu initially
        showMainMenu();

        // --- WINDOW SETUP ---
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Project Crossroads - VN Engine");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMainMenu() {
        // Clear out any existing UI (like the dialogue box)
        root.getChildren().clear();
        
        // Remove dialogue click listener if coming back from a story
        root.setOnMouseClicked(null); 

        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Project Crossroads");
        titleLabel.setFont(new Font("Arial Bold", 36));
        titleLabel.setTextFill(Color.WHITE);

        Button julesBtn = new Button("Play Jules's Story");
        Button mayaBtn = new Button("Play Maya's Story");
        Button noraBtn = new Button("Play Nora's Story");

        // Simple inline styling for the buttons
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
        root.getChildren().add(dialogueBox);

        // Click anywhere on screen to advance
        root.setOnMouseClicked(event -> advanceDialogue());
    }

    private void startStory(int storyId) {
        // 1. Load the requested scene data into the GameManager
        if (storyId == 1) {
            loadJulesScene();
        } else if (storyId == 2) {
            loadMayaScene();
        } else if (storyId == 3) {
            loadNoraScene();
        }

        // 2. Swap the UI to the dialogue screen
        showDialogueScreen();

        // 3. Kick off the first line of dialogue
        advanceDialogue();
    }

    private void advanceDialogue() {
        DialogueLine nextLine = gameManager.getNextLine();
        
        if (nextLine != null) {
            speakerNameLabel.setText(nextLine.getSpeaker());
            dialogueTextLabel.setText(nextLine.getText());
        } else {
            // Scene is over, return to main menu
            showMainMenu();
        }
    }

    private void loadJulesScene() {
        Queue<DialogueLine> scene = new LinkedList<>();
        scene.add(new DialogueLine("Jules", "I've got three assignments due by midnight."));
        scene.add(new DialogueLine("Maya", "You need to take a break, Jules. You're going to burn out."));
        scene.add(new DialogueLine("Jules", "If I stop now, I lose my momentum. I just need coffee."));
        scene.add(new DialogueLine("Maya", "Coffee isn't a substitute for sleep..."));
        
        gameManager.loadScene(scene);
    }

    private void loadMayaScene() {
        Queue<DialogueLine> scene = new LinkedList<>();
        scene.add(new DialogueLine("Maya", "The campus is so quiet at this hour."));
        scene.add(new DialogueLine("Maya", "I wonder if Nora is still at the studio."));
        scene.add(new DialogueLine("Nora", "(From a distance) Hey! Over here!"));
        scene.add(new DialogueLine("Maya", "Ah, speak of the devil."));
        
        gameManager.loadScene(scene);
    }

    private void loadNoraScene() {
        Queue<DialogueLine> scene = new LinkedList<>();
        scene.add(new DialogueLine("Nora", "The composition is almost perfect, just needs more... red."));
        scene.add(new DialogueLine("Nora", "No, wait. Cobalt blue. That's the mood."));
        scene.add(new DialogueLine("Professor", "Fascinating choice, Nora. But does it fit the theme?"));
        scene.add(new DialogueLine("Nora", "It doesn't just fit the theme, Professor. It redefines it."));
        
        gameManager.loadScene(scene);
    }
}