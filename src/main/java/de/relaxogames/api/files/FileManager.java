package de.relaxogames.api.files;

import de.relaxogames.Lingo;
import de.relaxogames.snorlaxLOG.SnorlaxLOGConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public class FileManager {

    private Properties prop;

    public void generateFiles(){
        File lingoConfig = new File(Lingo.getLibrary().getApiHandledFolder(), "lingo-cfg.properties");
        try {
            if (!lingoConfig.exists()) {
                InputStream in = Lingo.getLibrary().getClass().getResourceAsStream("config.properties");
                if (in != null) Files.copy(in, lingoConfig.toPath());
                prop = new Properties();
                prop.load(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public SnorlaxLOGConfig getSlcConfig(){
        return new SnorlaxLOGConfig(prop.getProperty("slc-url", "http://rgdb.relaxogames.de"), prop.getProperty("slc-user"), prop.getProperty("slc-pw"));
    }

}
