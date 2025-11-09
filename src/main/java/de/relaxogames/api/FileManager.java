package de.relaxogames.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public class FileManager {

    private Properties props;

    /**
     * This method generates all files needed to run lingo.
     * @apiNote The files will be created in YOUR plugin folder. CHANGE ONLY THE SETTINGS IN THIS SPECIFIC FILE!
     */
    protected void generateFiles(){
        File propF = new File(Lingo.getLibrary().getApiHandledFolder().getAbsolutePath(), "snorlaxlabs.properties");
        System.out.println("FILE ERSTELLT BEI :" + propF.getAbsolutePath());
        try {
            if (!propF.exists()) {
                InputStream in = getClass().getClassLoader().getResourceAsStream("snorlaxlabs.properties");
                if (in != null) Files.copy(in, propF.toPath());
            }
            InputStream in = new FileInputStream(propF);
            props = new Properties();
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @return true -- if the debug has been enabled
     */
    protected boolean isDebugging(){
        return Boolean.parseBoolean(props.getProperty("debug-mode", "false"));
    }

}
