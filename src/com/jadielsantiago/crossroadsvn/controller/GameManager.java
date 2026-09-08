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
}