package com.jadielsantiago.crossroadsvn.model;

import static com.jadielsantiago.crossroadsvn.model.Choice.branch;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Mayas_Story {
    public static Queue<DialogueLine> getScene() {
        Queue<DialogueLine> scene = new LinkedList<>();
        scene.add(new DialogueLine("Maya", "The campus is so quiet at this hour."));
        scene.add(new DialogueLine("Maya", "I wonder if Nora is still at the studio."));
        scene.add(new DialogueLine("Nora", "(From a distance) Hey! Over here!"));
        scene.add(new DialogueLine("Maya", "Ah, speak of the devil."));
        return scene;
    }
}