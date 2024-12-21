package de.relaxogames.bukkit.manager;

import de.relaxogames.bukkit.BukkitLingo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BukkitFiles {

    private static Properties rgdbProp;
    private static File datafolder;

    public BukkitFiles(boolean loadFiles){
        if (loadFiles)load();
    }

    public BukkitFiles() {
    }

    private void load(){
        datafolder = new File(BukkitLingo.getInstance().getDataFolder().getPath());
        if (!datafolder.exists() || datafolder == null){
            datafolder.mkdir();
        }

        File rgProp = new File(BukkitLingo.getInstance().getDataFolder(), "config.properties");
        if (!rgProp.exists()){
            rgProp.mkdir();
        }
        rgdbProp = new Properties();
        rgdbProps();
    }

    private Properties rgdbProps(){
        if (rgdbProp != null)return rgdbProp;
        try {
            rgdbProp.load(new FileInputStream(BukkitLingo.getInstance().getDataFolder() + "config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rgdbProp;
    }

    public String rgdbURL(){
        Properties props = rgdbProps();
        return props.getProperty("rgdb.url");
    }

    public String rgdbPassword(){
        Properties props = rgdbProps();
        return props.getProperty("rgdb.username");
    }

    public String rgdbUsername(){
        Properties props = rgdbProps();
        return props.getProperty("rgdb.password");
    }

}
