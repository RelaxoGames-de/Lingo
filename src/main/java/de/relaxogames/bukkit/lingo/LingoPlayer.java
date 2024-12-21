package de.relaxogames.bukkit.lingo;

import de.relaxogames.bukkit.BukkitLingo;
import de.relaxogames.bukkit.lingo.languages.Language;
import de.relaxogames.snorlaxLOG.RGDBStorage;

import java.util.UUID;

public class LingoPlayer implements de.relaxogames.bukkit.lingo.interfaces.LingoPlayer {

    UUID uuid;
    RGDBStorage playerStorage;

    public LingoPlayer(UUID uuid) {
        this.uuid = uuid;
        playerStorage = (RGDBStorage) BukkitLingo.getLibary().getStorage(uuid.toString(), null);
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public RGDBStorage getPlayerStorage() {
        return playerStorage;
    }

    @Override
    public Language getLanguage() {
        return Language.convertStringToLanguage((String) BukkitLingo.getLibary().getSharedEntry(uuid.toString(), "language", null));
    }

    @Override
    public void setLanguage(Language value) {

    }
}
