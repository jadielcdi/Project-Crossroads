/*
 * Copyright (c) 2026 Jadiel Santiago. All rights reserved.
 *
 * This software is licensed under the MIT License.
 * See COPYRIGHT.txt in the project root for full license details.
 */
package com.jadielsantiago.crossroadsvn.controller;

import com.jadielsantiago.crossroadsvn.model.CharacterPosition;
import com.jadielsantiago.crossroadsvn.model.CharacterSlotState;
import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller managing visual novel character sprites on stage.
 * Handles sprite positioning across LEFT, MIDDLE, and RIGHT slots,
 * proportional 720p height scaling, expression cross-fading, speaker dimming,
 * and high-DPI texture preservation within the 1280x720 coordinate system.
 */
public class CharacterManager {

    /** Base directory path for character assets on the classpath */
    public static final String CHARACTERS_DIR = "/com/jadielsantiago/crossroadsvn/media/images/characters/";

    /** Default standing sprite height relative to the 720p virtual canvas (roughly 86% height) */
    public static final double DEFAULT_FIT_HEIGHT = 620.0;

    /** Cross-fade duration in milliseconds when switching expressions or poses */
    public static final double CROSS_FADE_DURATION_MS = 150.0;

    private final StackPane characterLayer;
    private final Map<CharacterPosition, StackPane> slotContainers = new EnumMap<>(CharacterPosition.class);
    private final Map<CharacterPosition, ImageView> activeViews = new EnumMap<>(CharacterPosition.class);
    private final Map<CharacterPosition, String> activeCharacterIds = new EnumMap<>(CharacterPosition.class);
    private final Map<CharacterPosition, String> activeExpressions = new EnumMap<>(CharacterPosition.class);
    private final Map<CharacterPosition, Double> positionOpacities = new EnumMap<>(CharacterPosition.class);
    private final Map<String, Image> imageCache = new ConcurrentHashMap<>();

    /**
     * Initializes the character manager and binds it to the dedicated 1280x720 character layer.
     *
     * @param characterLayer the StackPane residing between background and UI layers
     */
    public CharacterManager(StackPane characterLayer) {
        if (characterLayer == null) {
            throw new IllegalArgumentException("characterLayer cannot be null");
        }
        this.characterLayer = characterLayer;
        this.characterLayer.setPickOnBounds(false);

        // Pre-create and anchor the three fixed stage slots (LEFT, MIDDLE, RIGHT)
        for (CharacterPosition pos : CharacterPosition.values()) {
            StackPane slot = new StackPane();
            slot.setPickOnBounds(false);
            slot.setAlignment(pos.getAlignment());
            slot.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            StackPane.setAlignment(slot, pos.getAlignment());
            StackPane.setMargin(slot, pos.getMargin());

            this.characterLayer.getChildren().add(slot);
            this.slotContainers.put(pos, slot);
            this.positionOpacities.put(pos, 1.0);
        }
    }

    /**
     * Displays a character sprite at the specified position with expression cross-fading.
     *
     * @param characterId        the identifier or name of the character (e.g. "Jules", "Maya", "Nora")
     * @param expressionOrPose  the expression, pose, or filename (e.g. "Reference", "happy", or null for default)
     * @param position          the stage position slot (LEFT, MIDDLE, RIGHT)
     */
    public void showCharacter(String characterId, String expressionOrPose, CharacterPosition position) {
        showCharacter(characterId, expressionOrPose, position, false);
    }

    /**
     * Displays a character sprite at the specified position, optionally skipping the cross-fade animation.
     *
     * @param characterId        the identifier or name of the character
     * @param expressionOrPose  the expression, pose, or filename
     * @param position          the stage position slot (LEFT, MIDDLE, RIGHT)
     * @param immediate         if true, renders the sprite immediately without a fade-in animation (ideal for save loading)
     */
    public void showCharacter(String characterId, String expressionOrPose, CharacterPosition position, boolean immediate) {
        if (position == null) {
            position = CharacterPosition.MIDDLE;
        }

        URL resourceUrl = resolveImageResource(characterId, expressionOrPose);
        if (resourceUrl == null) {
            System.err.println("[CharacterManager] Character sprite not found for character: '"
                    + characterId + "', expression: '" + expressionOrPose + "'");
            return;
        }

        Image image = loadImage(resourceUrl);
        if (image == null) {
            return;
        }

        StackPane slot = slotContainers.get(position);
        if (slot == null) {
            return;
        }

        // Ensure character is not duplicated across multiple slots simultaneously
        String normalizedTarget = resolveCharacterFolder(characterId);
        for (CharacterPosition otherPos : CharacterPosition.values()) {
            if (otherPos != position) {
                String existingChar = activeCharacterIds.get(otherPos);
                if (existingChar != null && resolveCharacterFolder(existingChar).equalsIgnoreCase(normalizedTarget)) {
                    hideCharacter(otherPos);
                }
            }
        }

        ImageView oldView = activeViews.get(position);
        String expr = (expressionOrPose != null && !expressionOrPose.trim().isEmpty())
                ? expressionOrPose.trim()
                : "Reference";

        // If the identical image is already active and visible at this slot, update opacity and return
        if (oldView != null && oldView.getImage() == image && oldView.getOpacity() > 0.0) {
            double targetOpacity = positionOpacities.getOrDefault(position, 1.0);
            setPositionOpacity(position, targetOpacity);
            activeCharacterIds.put(position, characterId);
            activeExpressions.put(position, expr);
            return;
        }

        // Configure new sprite view for crisp high-DPI scaling
        ImageView newView = new ImageView(image);
        newView.setFitHeight(DEFAULT_FIT_HEIGHT);
        newView.setPreserveRatio(true);
        newView.setSmooth(true);

        // Disable low-resolution texture caching to prevent downsampling blur on 1440p/4K panels
        newView.setCache(false);
        newView.setPickOnBounds(false);
        newView.setMouseTransparent(true);

        double targetOpacity = positionOpacities.getOrDefault(position, 1.0);
        StackPane.setAlignment(newView, position.getAlignment());

        slot.getChildren().add(newView);
        activeViews.put(position, newView);
        activeCharacterIds.put(position, characterId);
        activeExpressions.put(position, expr);

        if (immediate) {
            if (oldView != null) {
                slot.getChildren().remove(oldView);
            }
            newView.setOpacity(targetOpacity);
        } else {
            newView.setOpacity(0.0);
            // Smooth cross-fade between old and new expression
            if (oldView != null) {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(CROSS_FADE_DURATION_MS), oldView);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(e -> slot.getChildren().remove(oldView));
                fadeOut.play();
            }

            FadeTransition fadeIn = new FadeTransition(Duration.millis(CROSS_FADE_DURATION_MS), newView);
            fadeIn.setToValue(targetOpacity);
            fadeIn.play();
        }
    }

    /**
     * Convenience method to show a character using their default standing reference pose.
     */
    public void showCharacter(String characterId, CharacterPosition position) {
        showCharacter(characterId, "Reference", position);
    }

    /**
     * Hides the character sprite at the specified position with a smooth fade-out.
     *
     * @param position the stage position slot to clear
     */
    public void hideCharacter(CharacterPosition position) {
        if (position == null) {
            return;
        }

        ImageView currentView = activeViews.remove(position);
        activeCharacterIds.remove(position);
        activeExpressions.remove(position);

        if (currentView != null) {
            StackPane slot = slotContainers.get(position);
            FadeTransition ft = new FadeTransition(Duration.millis(CROSS_FADE_DURATION_MS), currentView);
            ft.setToValue(0.0);
            ft.setOnFinished(e -> {
                if (slot != null) {
                    slot.getChildren().remove(currentView);
                }
            });
            ft.play();
        }
    }

    /**
     * Clears all character sprites from all position slots immediately.
     */
    public void clearAllCharacters() {
        for (CharacterPosition pos : CharacterPosition.values()) {
            activeViews.remove(pos);
            activeCharacterIds.remove(pos);
            activeExpressions.remove(pos);
            StackPane slot = slotContainers.get(pos);
            if (slot != null) {
                slot.getChildren().clear();
            }
        }
    }

    /**
     * Adjusts the opacity of a position slot with a smooth transition.
     * Useful for dimming non-speaking characters on screen during dialogues.
     *
     * @param position the target slot
     * @param opacity  opacity value between 0.0 and 1.0
     */
    public void setPositionOpacity(CharacterPosition position, double opacity) {
        if (position == null) {
            return;
        }
        double clamped = Math.max(0.0, Math.min(1.0, opacity));
        positionOpacities.put(position, clamped);

        ImageView currentView = activeViews.get(position);
        if (currentView != null) {
            FadeTransition ft = new FadeTransition(Duration.millis(CROSS_FADE_DURATION_MS), currentView);
            ft.setToValue(clamped);
            ft.play();
        }
    }

    /**
     * Updates speaker state across all stage positions.
     * In accordance with Project Crossroads visual novel design, all characters on stage
     * maintain full 1.0 opacity regardless of who is currently speaking.
     *
     * @param speakerName the name of the character currently speaking
     */
    public void highlightSpeaker(String speakerName) {
        resetSpeakerOpacities();
    }

    /**
     * Resets all active characters to full opacity (1.0).
     */
    public void resetSpeakerOpacities() {
        for (CharacterPosition pos : CharacterPosition.values()) {
            setPositionOpacity(pos, 1.0);
        }
    }

    /**
     * Checks if a character sprite is currently rendered at the given position.
     */
    public boolean hasCharacter(CharacterPosition position) {
        return activeViews.containsKey(position);
    }

    /**
     * Returns the character ID currently occupying the given position, or null if empty.
     */
    public String getActiveCharacter(CharacterPosition position) {
        return activeCharacterIds.get(position);
    }

    /**
     * Returns the active expression or pose name currently occupying the given position, or null if empty.
     */
    public String getActiveExpression(CharacterPosition position) {
        return activeExpressions.get(position);
    }

    /**
     * Returns a snapshot map of all currently rendered characters on stage with their poses and positions.
     */
    public Map<CharacterPosition, CharacterSlotState> getActiveCharacters() {
        Map<CharacterPosition, CharacterSlotState> snapshot = new HashMap<>();
        for (CharacterPosition pos : CharacterPosition.values()) {
            String charId = activeCharacterIds.get(pos);
            if (charId != null) {
                String expr = activeExpressions.getOrDefault(pos, "Reference");
                snapshot.put(pos, new CharacterSlotState(charId, expr, pos));
            }
        }
        return snapshot;
    }

    /**
     * Restores characters onto the stage from saved state, immediately displaying them
     * at their preserved stage positions and expressions.
     *
     * @param savedCharacters the map of character slot states to restore
     */
    public void restoreCharacters(Map<CharacterPosition, CharacterSlotState> savedCharacters) {
        clearAllCharacters();
        if (savedCharacters == null || savedCharacters.isEmpty()) {
            return;
        }
        for (Map.Entry<CharacterPosition, CharacterSlotState> entry : savedCharacters.entrySet()) {
            CharacterSlotState slotState = entry.getValue();
            if (slotState != null && slotState.getCharacterId() != null) {
                CharacterPosition pos = slotState.getPosition() != null ? slotState.getPosition() : entry.getKey();
                showCharacter(slotState.getCharacterId(), slotState.getExpression(), pos, true);
            }
        }
    }

    /**
     * Returns the underlying character layer StackPane.
     */
    public StackPane getCharacterLayer() {
        return characterLayer;
    }

    /**
     * Resolves the resource URL for a given character and pose/expression.
     */
    private URL resolveImageResource(String characterId, String expressionOrPose) {
        if (characterId == null || characterId.trim().isEmpty()) {
            return null;
        }

        // Direct classpath path provided
        if (characterId.startsWith("/")) {
            return getClass().getResource(characterId);
        }
        if (expressionOrPose != null && expressionOrPose.startsWith("/")) {
            return getClass().getResource(expressionOrPose);
        }

        String folder = resolveCharacterFolder(characterId);
        String expr = (expressionOrPose != null && !expressionOrPose.trim().isEmpty())
                ? expressionOrPose.trim()
                : "Reference";
        String capitalizedExpr = expr.length() > 1
                ? Character.toUpperCase(expr.charAt(0)) + expr.substring(1)
                : expr.toUpperCase();

        // Candidate paths checked in order of specificity (PNG transparent sprites prioritized)
        List<String> candidates = new ArrayList<>();
        candidates.add(CHARACTERS_DIR + folder + "/" + expr);
        candidates.add(CHARACTERS_DIR + folder + "/" + folder + "_" + expr + ".png");
        candidates.add(CHARACTERS_DIR + folder + "/" + folder + "_" + capitalizedExpr + ".png");
        candidates.add(CHARACTERS_DIR + folder + "/" + folder + "_" + expr + ".jpeg");
        candidates.add(CHARACTERS_DIR + folder + "/" + folder + "_" + capitalizedExpr + ".jpeg");
        candidates.add(CHARACTERS_DIR + folder + "/" + folder + "_" + expr + ".jpg");
        candidates.add(CHARACTERS_DIR + folder + "/" + folder + "_" + capitalizedExpr + ".jpg");
        candidates.add(CHARACTERS_DIR + folder + "/" + expr + ".png");
        candidates.add(CHARACTERS_DIR + folder + "/" + capitalizedExpr + ".png");
        candidates.add(CHARACTERS_DIR + folder + "/" + expr + ".jpeg");
        candidates.add(CHARACTERS_DIR + folder + "/" + capitalizedExpr + ".jpeg");
        candidates.add(CHARACTERS_DIR + folder + "/" + expr + ".jpg");
        candidates.add(CHARACTERS_DIR + folder + "/" + capitalizedExpr + ".jpg");

        // Standard neutral / reference image fallbacks
        candidates.add(CHARACTERS_DIR + folder + "/" + folder + "_Neutral.png");
        candidates.add(CHARACTERS_DIR + folder + "/" + folder + "_Reference.png");
        candidates.add(CHARACTERS_DIR + folder + "/" + folder + "_Reference.jpeg");
        candidates.add(CHARACTERS_DIR + folder + "/" + folder + "_Reference.jpg");

        for (String path : candidates) {
            URL url = getClass().getResource(path);
            if (url != null) {
                return url;
            }
        }
        return null;
    }

    /**
     * Maps screenplay speaker names (e.g. "Julian \"Jules\" Rivera", "Jules", "Maya Sterling")
     * to the corresponding canonical asset folder name ("Jules", "Maya", "Nora").
     */
    private String resolveCharacterFolder(String characterId) {
        if (characterId == null) {
            return "";
        }
        String lower = characterId.toLowerCase().trim();
        if (lower.contains("jules") || lower.contains("julian")) {
            return "Jules";
        }
        if (lower.contains("maya")) {
            return "Maya";
        }
        if (lower.contains("nora")) {
            return "Nora";
        }
        if (characterId.length() > 0) {
            return Character.toUpperCase(characterId.charAt(0)) + characterId.substring(1).trim();
        }
        return characterId.trim();
    }

    /**
     * Loads and caches an Image instance to prevent repetitive disk reads.
     */
    private Image loadImage(URL resourceUrl) {
        if (resourceUrl == null) {
            return null;
        }
        String key = resourceUrl.toExternalForm();
        return imageCache.computeIfAbsent(key, k -> {
            try {
                return new Image(k);
            } catch (Exception e) {
                System.err.println("[CharacterManager] Failed to load sprite from: " + k + ": " + e.getMessage());
                return null;
            }
        });
    }
}
