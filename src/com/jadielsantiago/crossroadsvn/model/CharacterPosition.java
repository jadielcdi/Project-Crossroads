/*
 * Copyright (c) 2026 Jadiel Santiago. All rights reserved.
 *
 * This software is licensed under the MIT License.
 * See COPYRIGHT.txt in the project root for full license details.
 */
package com.jadielsantiago.crossroadsvn.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * Defines standard visual novel stage positions and margin offsets for character sprites
 * anchored within the 1280x720 virtual coordinate plane.
 */
public enum CharacterPosition {
    LEFT(Pos.BOTTOM_LEFT, new Insets(0, 0, 0, 100)),
    MIDDLE(Pos.BOTTOM_CENTER, new Insets(0, 0, 0, 0)),
    RIGHT(Pos.BOTTOM_RIGHT, new Insets(0, 100, 0, 0));

    private final Pos alignment;
    private final Insets margin;

    CharacterPosition(Pos alignment, Insets margin) {
        this.alignment = alignment;
        this.margin = margin;
    }

    /**
     * Returns the StackPane alignment for this position anchored to the bottom.
     */
    public Pos getAlignment() {
        return alignment;
    }

    /**
     * Returns the horizontal margin inset within the 1280x720 plane.
     */
    public Insets getMargin() {
        return margin;
    }

    /**
     * Parses a string representation into a CharacterPosition enum.
     * Supports "left", "middle", "center", "right", with a fallback to MIDDLE.
     */
    public static CharacterPosition fromString(String posStr) {
        if (posStr == null) {
            return MIDDLE;
        }
        String lower = posStr.trim().toLowerCase();
        if (lower.contains("left")) {
            return LEFT;
        }
        if (lower.contains("right")) {
            return RIGHT;
        }
        return MIDDLE;
    }
}
