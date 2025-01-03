package de.relaxogames.api;

import de.relaxogames.Lingo;
import de.relaxogames.languages.Locale;
import de.relaxogames.snorlaxLOG.RGDBStorage;

import java.util.UUID;

public class LingoPlayer implements de.relaxogames.api.interfaces.LingoPlayer {

    UUID uuid;

    public LingoPlayer(UUID uuid){
        this.uuid = uuid;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public RGDBStorage getPlayerStorage() {
        return Lingo.getLibrary().getSnorlaxLOG().syncGetStorage(uuid.toString());
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }

    @Override
    public void setLanguage(Locale value) {

    }
}
