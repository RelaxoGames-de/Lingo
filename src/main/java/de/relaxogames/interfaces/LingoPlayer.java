package de.relaxogames.interfaces;

import de.relaxogames.languages.Locale;
import de.relaxogames.snorlaxLOG.RGDBStorage;

import java.util.UUID;

public interface LingoPlayer {

    UUID getUUID();
    RGDBStorage getPlayerStorage();
    Locale getLanguage();
    void setLanguage(Locale value);

}
