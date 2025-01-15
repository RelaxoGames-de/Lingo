package de.relaxogames;

import de.relaxogames.api.Lingo;
import de.relaxogames.languages.ServerColors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Prefixes {

    private static Component lingoPrefix = Component.text("[")
            .color(NamedTextColor.DARK_GRAY)
            .append(Component.text("Lingo")
                    .color(ServerColors.DodgerBlue3.color()))
            .append(Component.text("]")
                    .color(NamedTextColor.DARK_GRAY))
            .append(Component.text(" §7"));

    public static String getLingoPrefix() {
        return Lingo.getLibrary().convertMessage(lingoPrefix);
    }
}
