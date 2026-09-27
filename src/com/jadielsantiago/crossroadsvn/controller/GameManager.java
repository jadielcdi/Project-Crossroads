package com.jadielsantiago.crossroadsvn.controller;

import com.jadielsantiago.crossroadsvn.model.DialogueLine;
import java.util.LinkedList;
import java.util.Queue;

public class GameManager {
    private LinkedList<DialogueLine> currentSceneQueue;
    private String currentCharacter;
    private int currentDay;

    public GameManager() {
        currentSceneQueue = new LinkedList<>();
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