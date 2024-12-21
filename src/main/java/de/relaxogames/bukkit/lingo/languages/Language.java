package de.relaxogames.bukkit.lingo.languages;

public enum Language {

    system_default("de_DE"),
    de_DE("de_DE"),
    en_US("en_US");

    String lngISO;

    Language(String lngISO) {
        this.lngISO = lngISO;
    }

    public String getISO() {
        return lngISO;
    }

    public static Language convertStringToLanguage(String ISOShort){
        for (Language languages : Language.values()){
            if (languages.getISO().equalsIgnoreCase(ISOShort) && languages != system_default){
                return languages;
            }
        }
        return system_default;
    }

}
