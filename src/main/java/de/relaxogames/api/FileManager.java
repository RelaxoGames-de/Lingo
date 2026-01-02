package de.relaxogames.api;

import de.relaxogames.Prefixes;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;

public class FileManager {

    private File propFile;
    private Properties props;

    private int CONFIG_VERSION = 1;

    enum PROPERTIES{
        PROP_HOST("slc-host", "HOST"),
        PROP_DB("slc-database", "DATABASE"),
        PROP_USER("slc-user", "USER"),
        PROP_PASSWORD("slc-password", "PASSWORD"),
        PROP_TIMEOUT("connection-timeout-try", "3"),
        PROP_VERSION("cfg-version", null);

        String field;
        String defaultValue;

        PROPERTIES(String field, String defaultValue) {
            this.field = field;
            this.defaultValue = defaultValue;
        }

        public String getField() {
            return field;
        }

        public String getDefault() {
            return defaultValue;
        }
    }

    /**
     * This method generates all files needed to run lingo.
     * @apiNote The files will be created in YOUR plugin folder. CHANGE ONLY THE SETTINGS IN THIS SPECIFIC FILE!
     */
    protected void generateFiles(){
        propFile = new File(Lingo.getLibrary().getApiHandledFolder().getPath(), "snorlaxlabs.properties");
        try {
            if (!propFile.exists()) {
                InputStream in = getClass().getClassLoader().getResourceAsStream("snorlaxlabs.properties");
                if (in != null) Files.copy(in, propFile.toPath());

            }
            props = new Properties();

            try (FileInputStream fis = new FileInputStream(propFile)) {
                props.load(fis);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            CONFIG_VERSION = Integer.parseInt(props.getProperty(PROPERTIES.PROP_VERSION.getField()));
            if (CONFIG_VERSION < Lingo.getVersion()) updateProperties();
            if (isDebugging())System.out.println("FILE ERSTELLT BEI :" + propFile.getAbsolutePath());
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

    /**
     * @return the host from the .properties file
     */
    public String host(){
        return props.getProperty(PROPERTIES.PROP_HOST.getField());
    }

    /**
     * @return the database name form the .properties
     */
    public String database(){
        return props.getProperty(PROPERTIES.PROP_DB.getField());
    }

    /**
     * @return the database user form the .properties
     */
    public String user(){
        return props.getProperty(PROPERTIES.PROP_USER.getField());
    }

    /**
     * @return the password for the database form the .properties
     */
    public String password(){
        return props.getProperty(PROPERTIES.PROP_PASSWORD.getField());
    }

    /**
     * @return the amount the library tries to reconnect to the database
     */
    public int timeoutTryAmount(){
        return Integer.parseInt(props.getProperty(PROPERTIES.PROP_TIMEOUT.getField()));
    }

    /**
     * @return the current version of the properties file
     */
    public int cfgVersion(){
        return Integer.parseInt(props.getProperty(PROPERTIES.PROP_VERSION.getField()));
    }


    public void updateProperties(){
        if (props == null){
            props = new Properties();

            try (FileInputStream fis = new FileInputStream(propFile)) {
                props.load(fis);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        boolean changed = false;

        for (PROPERTIES key : PROPERTIES.values()){
            if (!props.containsKey(key.getField())){
                if (key.getDefault() == null)continue;
                props.setProperty(key.getField(), key.getDefault());
                changed = true;
            }
        }

        props.setProperty(PROPERTIES.PROP_VERSION.getField(), String.valueOf(Lingo.getVersion()));

        if (changed){
            try (FileOutputStream fOS = new FileOutputStream(propFile)){
                props.store(fOS, "Updated to snorlax.properties version: " + cfgVersion());
                System.getLogger(Prefixes.logPrefix()).log(System.Logger.Level.INFO, "Updated snorlaxlabs.properties to version: " + cfgVersion());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
