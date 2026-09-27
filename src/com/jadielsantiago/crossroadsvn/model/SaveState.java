package com.jadielsantiago.crossroadsvn.model;

import java.io.Serializable;
import java.util.LinkedList;

public class SaveState implements Serializable {
    private static final long serialVersionUID = 1L;

    private int slotIndex;
    private int storyId;
    private String storyTitle;
    private String timestamp;
    private String sceneHeading;
    private String currentSpeaker;
    private String currentText;
    private String currentMusic;
    private String currentBackground;
    private DialogueLine currentLine;
    private LinkedList<DialogueLine> remainingQueue;
    private boolean isChoiceActive;

    public SaveState() {
        this.remainingQueue = new LinkedList<>();
    }

    public SaveState(int slotIndex, int storyId, String storyTitle, String timestamp,
                     String sceneHeading, String currentSpeaker, String currentText,
                     String currentMusic, String currentBackground, DialogueLine currentLine,
                     LinkedList<DialogueLine> remainingQueue, boolean isChoiceActive) {
        this.slotIndex = slotIndex;
        this.storyId = storyId;
        this.storyTitle = storyTitle;
        this.timestamp = timestamp;
        this.sceneHeading = sceneHeading;
        this.currentSpeaker = currentSpeaker;
        this.currentText = currentText;
        this.currentMusic = currentMusic;
        this.currentBackground = currentBackground;
        this.currentLine = currentLine;
        this.remainingQueue = remainingQueue != null ? new LinkedList<>(remainingQueue) : new LinkedList<>();
        this.isChoiceActive = isChoiceActive;
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSceneHeading() {
        return sceneHeading;
    }

    public void setSceneHeading(String sceneHeading) {
        this.sceneHeading = sceneHeading;
    }

    public String getCurrentSpeaker() {
        return currentSpeaker;
    }

    public void setCurrentSpeaker(String currentSpeaker) {
        this.currentSpeaker = currentSpeaker;
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }

    public String getCurrentMusic() {
        return currentMusic;
    }

    public void setCurrentMusic(String currentMusic) {
        this.currentMusic = currentMusic;
    }

    public String getCurrentBackground() {
        return currentBackground;
    }

    public void setCurrentBackground(String currentBackground) {
        this.currentBackground = currentBackground;
    }

    public DialogueLine getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(DialogueLine currentLine) {
        this.currentLine = currentLine;
    }

    public LinkedList<DialogueLine> getRemainingQueue() {
        return remainingQueue;
    }

    public void setRemainingQueue(LinkedList<DialogueLine> remainingQueue) {
        this.remainingQueue = remainingQueue != null ? new LinkedList<>(remainingQueue) : new LinkedList<>();
    }

    public boolean isChoiceActive() {
        return isChoiceActive;
    }

    public void setChoiceActive(boolean choiceActive) {
        isChoiceActive = choiceActive;
    }
}
