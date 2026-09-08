package com.jadielsantiago.crossroadsvn.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Choice extends DialogueLine {
    private List<ChoiceOption> options;

    public Choice(String promptSpeaker, String promptText, List<ChoiceOption> options) {
        super(promptSpeaker, promptText);
        this.options = options;
    }

    public List<ChoiceOption> getOptions() {
        return options;
    }

    // Available to Jules_Story, Mayas_Story, and Noras_Story
    public static Queue<DialogueLine> branch(DialogueLine... lines) {
        return new LinkedList<>(Arrays.asList(lines));
    }
}