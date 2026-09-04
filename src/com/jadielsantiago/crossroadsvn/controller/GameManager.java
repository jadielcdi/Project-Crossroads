package com.jadielsantiago.crossroadsvn.controller;

import com.jadielsantiago.crossroadsvn.model.DialogueLine;
import java.util.LinkedList;
import java.util.Queue;

public class GameManager {
    private Queue<DialogueLine> currentSceneQueue;
    private String currentCharacter;
    private int currentDay;

    public GameManager() {
        currentSceneQueue = new LinkedList<>();
        currentDay = 1; // Default to Monday
    }

    public void loadScene(Queue<DialogueLine> newScene) {
        this.currentSceneQueue = newScene;
    }

    public DialogueLine getNextLine() {
        if (!currentSceneQueue.isEmpty()) {
            return currentSceneQueue.poll();
        }
        return null; // Scene is over
    }
}