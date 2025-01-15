package de.relaxogames.languages;

import net.kyori.adventure.text.format.TextColor;

/**
 * Represents a collection of predefined server colors.
 * Each color is defined by its RGB values and a corresponding {@link TextColor}.
 *
 * @author Seltex
 * @see TextColor
 */
public enum ServerColors {

    /**
     * A shade of {@code Dodger Blue (3rd variant)} with RGB values (24, 116, 205).
     */
    DodgerBlue3(24, 116, 205, TextColor.color(24, 116, 205)),

    /**
     * A darker shade of {@code Dodger Blue (4th variant)} with RGB values (16, 78, 139).
     */
    DodgerBlue4(16, 78, 139, TextColor.color(16, 78, 139)),

    /**
     * A shade of {@code Sky Blue} with RGB values (45, 167, 204).
     */
    SkyBlue(45, 167, 204, TextColor.color(45, 167, 204)),

    /**
     * A bright shade of {@code Red (2nd variant)} with RGB values (238, 0, 0).
     */
    Red2(238, 0, 0, TextColor.color(238, 0, 0)),

    /**
     * A slightly darker shade of {@code Red (3rd variant)} with RGB values (205, 0, 0).
     */
    Red3(205, 0, 0, TextColor.color(205, 0, 0)),

    /**
     * A shade of {@code Dark Orange} with RGB values (255, 140, 0).
     */
    DarkOrange(255, 140, 0, TextColor.color(255, 140, 0)),

    /**
     * A green shade {@code (3rd variant)} with RGB values (0, 205, 0).
     */
    Green3(0, 205, 0, TextColor.color(0, 205, 0)),

    /**
     * A shade of {@code Chartreuse (2nd variant)} with RGB values (118, 238, 0).
     */
    Chartreuse2(118, 238, 0, TextColor.color(118, 238, 0));

    /**
     * The red component of the RGB color.
     */
    int r;

    /**
     * The green component of the RGB color.
     */
    int g;

    /**
     * The blue component of the RGB color.
     */
    int b;

    /**
     * The serialized {@link TextColor} representation of the color.
     */
    TextColor serializedColor;

    /**
     * Constructs a {@code ServerColors} enum constant with the given RGB values and {@link TextColor}.
     *
     * @param r               The red component of the RGB color.
     * @param g               The green component of the RGB color.
     * @param b               The blue component of the RGB color.
     * @param serializedColor The serialized {@link TextColor} representation of the color.
     */
    ServerColors(int r, int g, int b, TextColor serializedColor) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.serializedColor = serializedColor;
    }

    /**
     * Gets the serialized {@link TextColor} representation of the color.
     *
     * @return The serialized {@link TextColor}.
     */
    public TextColor color() {
        return serializedColor;
    }

    /**
     * Gets the blue component of the RGB color.
     *
     * @return The blue component of the color.
     */
    public int getB() {
        return b;
    }

    /**
     * Gets the green component of the RGB color.
     *
     * @return The green component of the color.
     */
    public int getG() {
        return g;
    }

    /**
     * Gets the red component of the RGB color.
     *
     * @return The red component of the color.
     */
    public int getR() {
        return r;
    }
}