/*
 * Copyright (c) 2026 Jadiel Santiago. All rights reserved.
 *
 * This software is licensed under the MIT License.
 * See COPYRIGHT.txt in the project root for full license details.
 */
package com.jadielsantiago.crossroadsvn.view;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;

import java.util.function.Consumer;

/**
 * Modern visual novel typewriter text animator.
 *
 * Dynamically streams dialogue character-by-character with:
 * - Natural punctuation pacing (pauses at commas, periods, etc.)
 * - Subtle typewriter audio ticks on alphanumeric characters
 * - Click-to-finish / Space-to-finish instant reveal support
 * - Visual continue prompt indicator (e.g. pulsing ▼ indicator)
 * - Configurable character delay / instant display mode
 */
public class TextAnimator {
    public static final long DEFAULT_DELAY_NANOS = 25_000_000L; // ~40 chars/sec
    public static final long FAST_DELAY_NANOS = 12_000_000L;    // ~80 chars/sec
    public static final long SLOW_DELAY_NANOS = 45_000_000L;    // ~22 chars/sec
    public static final long INSTANT_DELAY_NANOS = 0L;          // Instant reveal

    private final Consumer<String> textConsumer;
    private AudioClip charTick;
    private String fullText = "";
    private int charIndex = 0;
    private long lastUpdate = 0;
    private long delayNanos = DEFAULT_DELAY_NANOS;
    private long currentStepDelay = DEFAULT_DELAY_NANOS;
    private AnimationTimer timer;
    private Runnable onComplete;
    private Node indicatorNode;
    private boolean timerRunning = false;
    private boolean soundEnabled = true;
    private double soundVolume = 0.15;
    private boolean punctuationPausesEnabled = true;

    /**
     * Constructs a TextAnimator targeting a JavaFX Label.
     */
    public TextAnimator(Label labelNode, AudioClip charTick) {
        this(labelNode != null ? labelNode::setText : str -> {}, charTick);
    }

    /**
     * Constructs a TextAnimator targeting a JavaFX Text shape.
     */
    public TextAnimator(Text textNode, AudioClip charTick) {
        this(textNode != null ? textNode::setText : str -> {}, charTick);
    }

    /**
     * Constructs a TextAnimator targeting an arbitrary string consumer.
     */
    public TextAnimator(Consumer<String> textConsumer, AudioClip charTick) {
        this.textConsumer = textConsumer != null ? textConsumer : str -> {};
        this.charTick = charTick;
    }

    /**
     * Animates dialogue text with typewriter effect.
     *
     * @param text       The full text string to display.
     * @param onComplete Callback invoked when the text completes animating or finishes immediately.
     */
    public void animate(String text, Runnable onComplete) {
        if (timer != null) {
            timer.stop();
            timerRunning = false;
        }

        this.fullText = text != null ? text : "";
        this.charIndex = 0;
        this.onComplete = onComplete;
        this.lastUpdate = 0;
        this.currentStepDelay = delayNanos;

        // Hide continue indicator while text is animating
        if (indicatorNode != null) {
            indicatorNode.setVisible(false);
        }

        // If delay is 0 or text is empty, display immediately without starting a timer
        if (delayNanos <= 0 || fullText.isEmpty()) {
            finishImmediately();
            return;
        }

        textConsumer.accept("");

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                if (now - lastUpdate >= currentStepDelay) {
                    if (charIndex < fullText.length()) {
                        char c = fullText.charAt(charIndex);
                        charIndex++;
                        textConsumer.accept(fullText.substring(0, charIndex));

                        // Play audio tick on alphanumeric characters (skipping spaces/punctuation)
                        if (soundEnabled && charTick != null && Character.isLetterOrDigit(c) && (charIndex % 2 == 0)) {
                            try {
                                charTick.play(soundVolume);
                            } catch (Exception ignored) {
                            }
                        }

                        // Calculate delay for next character, factoring in natural punctuation pauses
                        currentStepDelay = calculateNextDelay(c, charIndex);
                    } else {
                        stop();
                        timerRunning = false;
                        if (indicatorNode != null) {
                            indicatorNode.setVisible(true);
                        }
                        if (onComplete != null) {
                            onComplete.run();
                        }
                    }
                    lastUpdate = now;
                }
            }
        };

        timerRunning = true;
        timer.start();
    }

    /**
     * Calculates delay in nanoseconds before typing the next character.
     * Adds subtle pauses for commas, periods, etc., to emulate spoken dialogue.
     */
    private long calculateNextDelay(char currentCharacter, int nextIndex) {
        long baseDelay = delayNanos;
        if (!punctuationPausesEnabled || baseDelay <= 0) {
            return baseDelay;
        }

        if (currentCharacter == ',' || currentCharacter == ';' || currentCharacter == ':' ||
                currentCharacter == '—' || currentCharacter == '-') {
            return baseDelay + 85_000_000L; // +85ms pause on clause breaks
        }

        if (currentCharacter == '.' || currentCharacter == '!' || currentCharacter == '?') {
            // Check if next character is also punctuation (e.g. ellipses '...')
            if (nextIndex < fullText.length()) {
                char nextChar = fullText.charAt(nextIndex);
                if (nextChar == '.' || nextChar == '!' || nextChar == '?') {
                    return baseDelay + 30_000_000L; // Shorter pause between consecutive dots
                }
            }
            return baseDelay + 180_000_000L; // +180ms pause on sentence completion
        }

        return baseDelay;
    }

    /**
     * Returns true if text is currently actively typing out.
     */
    public boolean isRunning() {
        return timerRunning && charIndex < fullText.length();
    }

    /**
     * Immediately finishes typing the line, displaying the entire text and invoking onComplete.
     */
    public void finishImmediately() {
        if (timer != null) {
            timer.stop();
        }
        timerRunning = false;
        charIndex = fullText.length();
        textConsumer.accept(fullText);

        if (indicatorNode != null) {
            indicatorNode.setVisible(true);
        }

        if (onComplete != null) {
            Runnable cb = onComplete;
            onComplete = null; // Prevent duplicate invocation
            cb.run();
        }
    }

    /**
     * Stops the animation timer without completing the line or triggering callbacks.
     */
    public void stop() {
        if (timer != null) {
            timer.stop();
        }
        timerRunning = false;
    }

    /**
     * Sets the prompt indicator node (e.g., pulsing ▼ arrow) to display when typing finishes.
     */
    public void setIndicatorNode(Node indicatorNode) {
        this.indicatorNode = indicatorNode;
        if (indicatorNode != null) {
            indicatorNode.setVisible(!isRunning() && !fullText.isEmpty());
        }
    }

    public Node getIndicatorNode() {
        return indicatorNode;
    }

    public long getDelayNanos() {
        return delayNanos;
    }

    public void setDelayNanos(long delayNanos) {
        this.delayNanos = Math.max(0, delayNanos);
        this.currentStepDelay = this.delayNanos;
    }

    public void setCharTick(AudioClip charTick) {
        this.charTick = charTick;
    }

    public AudioClip getCharTick() {
        return charTick;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public double getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(double soundVolume) {
        this.soundVolume = Math.max(0.0, Math.min(1.0, soundVolume));
    }

    public boolean isPunctuationPausesEnabled() {
        return punctuationPausesEnabled;
    }

    public void setPunctuationPausesEnabled(boolean punctuationPausesEnabled) {
        this.punctuationPausesEnabled = punctuationPausesEnabled;
    }

    public String getFullText() {
        return fullText;
    }
}
