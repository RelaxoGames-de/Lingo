package de.relaxogames.bukkit;

import de.relaxogames.bukkit.manager.BukkitFiles;
import de.relaxogames.snorlaxLOG.SnorlaxLOG;
import de.relaxogames.snorlaxLOG.SnorlaxLOGConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLingo extends JavaPlugin {

    static BukkitLingo instance;
    static SnorlaxLOG snorlaxLOG;
    static BukkitFiles fileManager;

    @Override
    public void onLoad() {
        if (instance == null) instance = this;
    }

    @Override
    public void onEnable() {
        fileManager = new BukkitFiles(true);
        SnorlaxLOGConfig logConfig = new SnorlaxLOGConfig(fileManager.rgdbURL(), fileManager.rgdbUsername(), fileManager.rgdbPassword());
        snorlaxLOG = new SnorlaxLOG(logConfig, false);
    }

    @Override
    public void onDisable() {

    }

    public static BukkitLingo getInstance() {
        return instance;
    }

    public static SnorlaxLOG getLibary() {
        return snorlaxLOG;
    }
}
