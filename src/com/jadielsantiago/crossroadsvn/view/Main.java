package com.jadielsantiago.crossroadsvn.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gameManager = new GameManager();
        
        // Build the scene and hand it to the GameManager
        loadTestScene(); 

        // --- UI SETUP ---
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #2b2b2b;"); 

        VBox dialogueBox = new VBox(10); 
        dialogueBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 10;");
        dialogueBox.setPadding(new Insets(20));
        dialogueBox.setMaxHeight(150);
        StackPane.setAlignment(dialogueBox, Pos.BOTTOM_CENTER);
        StackPane.setMargin(dialogueBox, new Insets(20)); 

        speakerNameLabel = new Label("Speaker Name");
        speakerNameLabel.setFont(new Font("Arial Bold", 18));
        speakerNameLabel.setTextFill(Color.DARKBLUE);

        dialogueTextLabel = new Label("Dialogue text goes here...");
        dialogueTextLabel.setFont(new Font("Arial", 16));
        dialogueTextLabel.setWrapText(true); 

        dialogueBox.getChildren().addAll(speakerNameLabel, dialogueTextLabel);
        root.getChildren().add(dialogueBox);

        // --- LOGIC SETUP ---
        root.setOnMouseClicked(event -> advanceDialogue());
        
        // Show the very first line before waiting for a click
        advanceDialogue();

        // --- WINDOW SETUP ---
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Weight and Horizon - VN Engine");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void advanceDialogue() {
        // Ask the GameManager for the next line instead of touching the Queue directly
        DialogueLine nextLine = gameManager.getNextLine();
        
        if (nextLine != null) {
            speakerNameLabel.setText(nextLine.getSpeaker());
            dialogueTextLabel.setText(nextLine.getText());
        } else {
            speakerNameLabel.setText("");
            dialogueTextLabel.setText("--- End of Day ---");
        }
    }

    private void loadTestScene() {
        // Temporary hardcoded scene for testing purposes. Next, we will load this from a text file!
        Queue<DialogueLine> scene = new LinkedList<>();
        scene.add(new DialogueLine("Jules", "I've got three assignments due by midnight."));
        scene.add(new DialogueLine("Maya", "You need to take a break, Jules. You're going to burn out."));
        scene.add(new DialogueLine("Jules", "If I stop now, I lose my momentum. I just need coffee."));
        scene.add(new DialogueLine("Maya", "Coffee isn't a substitute for sleep..."));
        
        gameManager.loadScene(scene);
    }
}