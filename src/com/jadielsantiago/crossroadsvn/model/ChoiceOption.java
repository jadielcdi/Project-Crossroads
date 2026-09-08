package com.jadielsantiago.crossroadsvn.model;

import java.util.Queue;

public class ChoiceOption {
    private String optionText;
    private Queue<DialogueLine> resultingBranch;

    public ChoiceOption(String optionText, Queue<DialogueLine> resultingBranch) {
        this.optionText = optionText;
        this.resultingBranch = resultingBranch;
    }

    public String getOptionText() {
        return optionText;
    }

    public Queue<DialogueLine> getResultingBranch() {
        return resultingBranch;
    }
}