package com.jadielsantiago.crossroadsvn.model;

import static com.jadielsantiago.crossroadsvn.model.Choice.branch;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Jules_Story {

    public static Queue<DialogueLine> getScene() {
        Queue<DialogueLine> scene = new LinkedList<>();

        // 1. Lines leading up to the choice
        scene.add(new DialogueLine("Jules", "I've been staring at this terminal for three hours."));
        scene.add(new DialogueLine("Nora", "Hey Jules, wanna go get some ice cream?"));

        // 2. The Choice created right in between the lines!
        scene.add(new Choice("Nora", "Hey Jules, wanna go get some ice cream?", List.of(
            new ChoiceOption("Accept: Go get ice cream", branch(
                new DialogueLine("Jules", "Yeah sure, let's go get some ice cream!"),
                new DialogueLine("Nora", "Awesome! My treat.")
            )),
            new ChoiceOption("Decline: Stay and code", branch(
                new DialogueLine("Jules", "Can't right now, I'm so close to finishing my coding assignment."),
                new DialogueLine("Nora", "Aw, don't overwork yourself. Next time then!")
            ))
        )));

        // 3. Dialogue that continues after either branch completes
        scene.add(new DialogueLine("Narrator", "Later that evening, campus quieted down..."));

        return scene;
    }
}

//public class Jules_Story {
//    public static Queue<DialogueLine> getScene() {
//        Queue<DialogueLine> scene = new LinkedList<>();
//        scene.add(new DialogueLine("Jules", "I've got three assignments due by midnight."));
//        scene.add(new DialogueLine("Maya", "You need to take a break, Jules. You're going to burn out."));
//        scene.add(new DialogueLine("Jules", "If I stop now, I lose my momentum. I just need coffee."));
//        scene.add(new DialogueLine("Maya", "Coffee isn't a substitute for sleep..."));
//        return scene;
//    }
//}