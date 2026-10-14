/*
 * Copyright (c) 2026 Jadiel Santiago. All rights reserved.
 *
 * This software is licensed under the MIT License.
 * See COPYRIGHT.txt in the project root for full license details.
 */
package com.jadielsantiago.crossroadsvn.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Serializable state representation of a character sprite rendered on stage,
 * capturing the character identifier, expression/pose name, and screen position slot.
 */
public class CharacterSlotState implements Serializable {
    private static final long serialVersionUID = 1L;

    private String characterId;
    private String expression;
    private CharacterPosition position;

    public CharacterSlotState() {
    }

    public CharacterSlotState(String characterId, String expression, CharacterPosition position) {
        this.characterId = characterId;
        this.expression = expression != null && !expression.trim().isEmpty() ? expression.trim() : "Reference";
        this.position = position != null ? position : CharacterPosition.MIDDLE;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression != null && !expression.trim().isEmpty() ? expression.trim() : "Reference";
    }

    public CharacterPosition getPosition() {
        return position;
    }

    public void setPosition(CharacterPosition position) {
        this.position = position != null ? position : CharacterPosition.MIDDLE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterSlotState that = (CharacterSlotState) o;
        return Objects.equals(characterId, that.characterId) &&
               Objects.equals(expression, that.expression) &&
               position == that.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId, expression, position);
    }

    @Override
    public String toString() {
        return "CharacterSlotState{" +
                "characterId='" + characterId + '\'' +
                ", expression='" + expression + '\'' +
                ", position=" + position +
                '}';
    }
}
