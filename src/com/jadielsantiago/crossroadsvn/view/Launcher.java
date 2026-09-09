package com.jadielsantiago.crossroadsvn.view;

/**
 * Entry point wrapper that does not extend javafx.application.Application.
 * This prevents the JVM from enforcing JavaFX runtime component checks at class load time
 * and allows smooth execution across different IDE runners and classpath setups.
 */
public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
