package com.jadielsantiago.crossroadsvn.model;

public class DialogueLine {
    private String speaker;
    private String text;

    public DialogueLine(String speaker, String text) {
        this.speaker = speaker;
        this.text = text;
    }

    public String getSpeaker() { return speaker; }
    public String getText() { return text; }
}