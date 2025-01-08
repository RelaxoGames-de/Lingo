package de.relaxogames.api.interfaces;

import de.relaxogames.languages.Locale;

import java.util.UUID;

public interface LingoPlayer {

    UUID getUUID();
    Object getPlayerStorage();
    Locale getLanguage();
    void setLanguage(Locale value);

}
