package de.relaxogames.bukkit.lingo.interfaces;

import de.relaxogames.bukkit.lingo.languages.Language;
import de.relaxogames.snorlaxLOG.RGDBStorage;

import java.util.UUID;

public interface LingoPlayer {

    UUID getUUID();
    RGDBStorage getPlayerStorage();
    Language getLanguage();
    void setLanguage(Language value);

}
