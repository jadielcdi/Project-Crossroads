package com.jadielsantiago.crossroadsvn.controller;

import com.jadielsantiago.crossroadsvn.model.DialogueLine;
import java.util.LinkedList;
import java.util.Queue;

public class GameManager {
    private LinkedList<DialogueLine> currentSceneQueue;
    private LinkedList<DialogueLine> dialogueHistoryQueue;
    private String currentCharacter;
    private int currentDay;

    public GameManager() {
        currentSceneQueue = new LinkedList<>();
        dialogueHistoryQueue = new LinkedList<>();
        currentDay = 1;
    }

    public void loadScene(Queue<DialogueLine> newScene) {
        this.currentSceneQueue = new LinkedList<>(newScene);
    }

    public DialogueLine getNextLine() {
        if (!currentSceneQueue.isEmpty()) {
            return currentSceneQueue.poll();
        }
        return null;
    }

    /**
     * Injects the chosen branch at the front of the queue so it plays next,
     * before resuming any remaining scene dialogue.
     */
    public void branchScene(Queue<DialogueLine> branchQueue) {
        if (branchQueue != null) {
            // Add existing lines after the branch lines
            branchQueue.addAll(this.currentSceneQueue);
            this.currentSceneQueue = new LinkedList<>(branchQueue);
        }
    }

    public LinkedList<DialogueLine> getCurrentSceneQueue() {
        return new LinkedList<>(currentSceneQueue);
    }

    public void setCurrentSceneQueue(LinkedList<DialogueLine> queue) {
        this.currentSceneQueue = queue != null ? new LinkedList<>(queue) : new LinkedList<>();
    }

    /**
     * Records a screenbox dialogue line into the FIFO history queue.
     * Prevents consecutive duplicates and safely handles null values.
     */
    public void recordDialogue(DialogueLine line) {
        if (line != null) {
            String spk = line.getSpeaker() != null ? line.getSpeaker() : "";
            String txt = line.getText() != null ? line.getText() : "";
            if (!dialogueHistoryQueue.isEmpty()) {
                DialogueLine last = dialogueHistoryQueue.getLast();
                if (last != null &&
                    java.util.Objects.equals(last.getSpeaker(), spk) &&
                    java.util.Objects.equals(last.getText(), txt)) {
                    return;
                }
            }
            dialogueHistoryQueue.add(new DialogueLine(spk, txt));
        }
    }

    public void recordDialogue(String speaker, String text) {
        recordDialogue(new DialogueLine(speaker, text));
    }

    public Queue<DialogueLine> getDialogueHistoryQueue() {
        return new LinkedList<>(dialogueHistoryQueue);
    }

    public LinkedList<DialogueLine> getHistoryLinkedList() {
        return new LinkedList<>(dialogueHistoryQueue);
    }

    public void setDialogueHistoryQueue(Queue<DialogueLine> queue) {
        this.dialogueHistoryQueue = queue != null ? new LinkedList<>(queue) : new LinkedList<>();
    }

    public void clearDialogueHistory() {
        this.dialogueHistoryQueue.clear();
    }

    public String getCurrentCharacter() {
        return currentCharacter;
    }

    public void setCurrentCharacter(String currentCharacter) {
        this.currentCharacter = currentCharacter;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public void clearQueue() {
        currentSceneQueue.clear();
    }
}