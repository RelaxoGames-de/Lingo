package de.relaxogames.api.interfaces;

import de.relaxogames.api.Lingo;
import de.relaxogames.languages.Locale;

import java.util.UUID;

public class LingoPlayer implements LingoUser {

    UUID uuid;
    Locale lng;

    /**
     * @param uuid the UUID of the needed player
     */
    public LingoPlayer(UUID uuid){
        this.uuid = uuid;
    }

    /**
     * @return the players UUID
     */
    @Override
    public UUID getUUID() {
        return uuid;
    }

    /**
     * @return the {@link Locale} of a player
     */
    @Override
    public Locale getLanguage() {
        if (lng == null) {
            new Thread(() -> {
                lng = Locale.GERMAN;
                //lng = Locale.convertStringToLanguage(Lingo.getLibrary().getSnorlaxLOG().syncGetSharedEntry(uuid.toString(), "player_locale"));
            }).run();
        }
        return lng;
    }

    /**
     * This method sets the value of a player locale
     * @param value is the new language that should be used
     */
    @Override
    public void setLanguage(Locale value) {
        new Thread(() -> {
            //Lingo.getLibrary().getSnorlaxLOG().syncSetSharedEntry(uuid.toString(), "player_locale", value.getISO());
        }).start();
    }
}
