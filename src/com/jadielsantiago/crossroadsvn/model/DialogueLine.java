package com.jadielsantiago.crossroadsvn.model;

import java.io.Serializable;

public class DialogueLine implements Serializable {
    private static final long serialVersionUID = 1L;
    private String speaker;
    private String text;

    public DialogueLine(String speaker, String text) {
        this.speaker = speaker;
        this.text = text;
    }

    public String getSpeaker() { return speaker; }
    public String getText() { return text; }
}