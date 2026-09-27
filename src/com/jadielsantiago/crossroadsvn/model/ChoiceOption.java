package com.jadielsantiago.crossroadsvn.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class ChoiceOption implements Serializable {
    private static final long serialVersionUID = 1L;
    private String optionText;
    private Queue<DialogueLine> resultingBranch;

    public ChoiceOption(String optionText, Queue<DialogueLine> resultingBranch) {
        this.optionText = optionText;
        this.resultingBranch = resultingBranch != null ? new LinkedList<>(resultingBranch) : new LinkedList<>();
    }

    public String getOptionText() {
        return optionText;
    }

    public Queue<DialogueLine> getResultingBranch() {
        return resultingBranch;
    }
}