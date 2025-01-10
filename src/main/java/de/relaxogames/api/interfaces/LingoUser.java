package de.relaxogames.api.interfaces;

import de.relaxogames.languages.Locale;

import java.util.UUID;

public interface LingoUser {

    UUID getUUID();
    Locale getLanguage();
    void setLanguage(Locale value);

}
