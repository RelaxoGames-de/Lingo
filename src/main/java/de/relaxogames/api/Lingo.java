package de.relaxogames.api;

import de.relaxogames.exceptions.LanguageFileNull;
import de.relaxogames.exceptions.LanguageNotFound;
import de.relaxogames.exceptions.MessageNotFound;
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

/**
 * Lingo is the language API for the RelaxoGamesDE Minigame Network.
 * Please consider before you import the library to your plugin, that you
 * are using at least {@link SnorlaxLOG} v1.7!
 * @author Simon (DevTex) Stier
 * @see Locale
 * @see de.relaxogames.api.interfaces.LingoPlayer
 * @see FileManager
 */
public class Lingo {

    private static volatile Lingo instance;
    private SnorlaxLOG snorlaxLOG;
    private FileManager fileManager;

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
        snorlaxLOG = new SnorlaxLOG(fileManager.getSlcConfig(), false);
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
        boolean debug = fileManager.isDebugging();
        for(File langFile : fileList){
            Locale lng = Locale.convertStringToLanguage(langFile.getName().replace(".yml", ""));
            lingoList.put(lng, langFile);
            Yaml cfg = new Yaml();
            InputStream is = null;
            try {
                is = new FileInputStream(langFile.getAbsolutePath());
            } catch (FileNotFoundException e) {
                throw new LanguageFileNull(langFile.getName(), langFile.getPath(), "Language file not found!");
            }
            HashMap<String, Object> valuesMap = cfg.load(is);
            messages = new HashMap<>();
            if (debug){
                System.out.println("############################ [LINGO-DEBUG] ############################");
                System.out.println("File: [" + langFile.getName() + "] \t / \tPath: [" + langFile.getPath() + "]");
            }
            for (String key : valuesMap.keySet()){
                messages.put(key, String.valueOf(valuesMap.get(key)).replace("&", "§"));
                if (debug)System.out.println(key + "\t » \t" + String.valueOf(valuesMap.get(key)).replace("&", "§"));
            }
            if (debug)System.out.println("############################ " + langFile.getName() + " finished ############################");
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
        if (localeFile == null)throw new LanguageNotFound(locale, "Language file is null");

        Map<String, String> localeMessages = messageList.get(locale);
        if (localeMessages == null) throw new MessageNotFound(locale, message);
        String lngMessage = localeMessages.getOrDefault(message,"This message has not been set up for your language! (" + message + " / " + localeFile.getName() + ")");
        return lngMessage;
    }

    /**
     * @param locale {@link Locale} is the language of the message
     * @return the full language file
     */
    public File getLocaleFile(Locale locale) {
        for (File file : lingoList.values()) {
            if (file.getName().replace(".yml", "").equalsIgnoreCase(locale.getISO())) return file;
        }
        throw new LanguageNotFound(locale,"Could not find any language file with that locale!");
    }

    /**
     * Reloads all Lingo-messages on the server
     * and the whole library, this includes the
     * SnorlaxLOG connection.
     */
    public void reload(){
        fileManager.generateFiles();
        snorlaxLOG = new SnorlaxLOG(fileManager.getSlcConfig(), false);
        messageList = new HashMap<>();
        lingoList = new HashMap<>();
        loadMessages(fileList);
    }

    public SnorlaxLOG getSnorlaxLOG() {
        return snorlaxLOG;
    }

    /**
     * @return the plugin-folder of the API handled plugin
     */
    public File getApiHandledFolder() {
        return apiHandledFolder;
    }
}
