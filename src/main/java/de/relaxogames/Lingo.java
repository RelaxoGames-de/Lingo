package de.relaxogames;

import de.relaxogames.api.files.FileManager;
import de.relaxogames.languages.Locale;
import de.relaxogames.snorlaxLOG.SnorlaxLOG;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lingo {

    private static volatile Lingo instance;
    private FileManager fileManager;

    private SnorlaxLOG snorlaxLOG;
    private File apiHandledFolder;

    List<File> fileList;

    private static HashMap<Locale, File> lingoList;
    private static HashMap<String, String> messages;
    private static HashMap<Locale, HashMap<String , String>> messageList;

    /**
     * Use this constructor to create your LingoAPI instance
     * @param pluginFolder is your pluginfolder. This is needed for the Lingo configuration file.
     */
    public Lingo(File pluginFolder) {
        apiHandledFolder = pluginFolder;
        instance = this;
        fileManager = new FileManager();
        fileManager.generateFiles();
        snorlaxLOG = new SnorlaxLOG(fileManager.getSlcConfig(), true);
        messageList = new HashMap<>();
        lingoList = new HashMap<>();
    }

    /**
     * @return This Library
     */
    public static Lingo getLibrary() {
        return instance;
    }

    /**
     * This method is called if you load your message files on server start
     * @param fileList are all language files combined in an {@link java.util.ArrayList}
     */
    public void loadMessages(List<File> fileList) {
        this.fileList = fileList;
        for(File langFile : fileList){
            Locale lng = Locale.convertStringToLanguage(langFile.getName().replace(".yml", ""));
            lingoList.put(lng, langFile);
            Yaml cfg = new Yaml();
            InputStream is = null;
            try {
                is = new FileInputStream(langFile.getAbsolutePath());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            HashMap<String, Object> valuesMap = cfg.load(is);
            messages = new HashMap<>();
            for (String key : valuesMap.keySet()){
                messages.put(key, String.valueOf(valuesMap.get(key)).replace("&", "§"));
                System.out.println(lng.getISO() + "   /   " + key + "   /   " + String.valueOf(valuesMap.get(key)));
            }
            System.out.println("***** ADDED " + langFile.getName() + " (" + lng + ") " + "  ["+ cfg.getName() + "]   " + " *****");
            messageList.put(lng, messages);
        }
    }

    /**
     * @return true if Lingo is ready to use.
     */
    public boolean isReady(){
        return messageList != null && fileList != null && getSnorlaxLOG().syncTestConnection();
    }

    /**
     * Call this method everytime you want to reload the message files.
     * @param fileList can be used if you want to edit the language file that should be loaded.
     */
    public void reloadMessages(List<File> fileList){
        messageList = new HashMap<>();
        lingoList = new HashMap<>();
        loadMessages(fileList);
    }

    /**
     * Call this method everytime you want to reload the message files. You are not able to edit the language files!
     */
    public void reloadMessages(){
        messageList = new HashMap<>();
        lingoList = new HashMap<>();
        loadMessages(fileList);
    }

    /**
     * This method is used to get a specific message from a local language file
     * @param locale {@link Locale} is the language of the message
     * @param message is the message key: <message-key> <message-content>
     * @return The translated message as a {@link String}
     * @apiNote This method won't replace any placeholders or color codes!
     */
    public String getMessage(Locale locale, String message) {
        File localeFile = getLocaleFile(locale);
        if (localeFile == null) {
            return "Language " + locale.getISO() + " is not supported!";
        }

        Map<String, String> localeMessages = messageList.get(locale);
        if (localeMessages == null) {
            return "This message has not been set up for your language! (" + message + " / " + localeFile.getName() + ")";
        }
        return localeMessages.getOrDefault(message, "This message has not been set up for your language! (" + message + " / " + localeFile.getName() + ")");
    }

    /**
     * @param locale {@link Locale} is the language of the message
     * @return the full language file
     */
    public File getLocaleFile(Locale locale) {
        for (File file : lingoList.values()) {
            if (file.getName().replace(".yml", "").equalsIgnoreCase(locale.getISO())) return file;
        }
        return null;
    }

    public SnorlaxLOG getSnorlaxLOG() {
        return snorlaxLOG;
    }

    public File getApiHandledFolder() {
        return apiHandledFolder;
    }
}
