package com.jadielsantiago.crossroadsvn.model;

import static com.jadielsantiago.crossroadsvn.model.Choice.branch;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Noras_Story {
    public static Queue<DialogueLine> getScene() {
        Queue<DialogueLine> scene = new LinkedList<>();
        scene.add(new DialogueLine("Nora", "The composition is almost perfect, just needs more... red."));
        scene.add(new DialogueLine("Nora", "No, wait. Cobalt blue. That's the mood."));
        scene.add(new DialogueLine("Professor", "Fascinating choice, Nora. But does it fit the theme?"));
        scene.add(new DialogueLine("Nora", "It doesn't just fit the theme, Professor. It redefines it."));
        return scene;
    }
}