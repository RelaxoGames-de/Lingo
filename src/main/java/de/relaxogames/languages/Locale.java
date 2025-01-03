package de.relaxogames.languages;

public enum Locale {

    system_default("de_DE"),
    GERMAN("de_DE"),
    ENGLISH("en_US");

    String lngISO;

    Locale(String lngISO) {
        this.lngISO = lngISO;
    }

    public String getISO() {
        return lngISO;
    }

    /**
     * Converts the ISOShort to a {@link Locale}
     * @param ISOShort is the ISO-Short that is needed as a search index
     * @return the {@link Locale} -- if there is no language with the search index, the system_default language will be returned.
     */
    public static Locale convertStringToLanguage(String ISOShort){
        for (Locale languages : Locale.values()){
            if (languages.getISO().equalsIgnoreCase(ISOShort) && languages != system_default){
                return languages;
            }
        }
        return system_default;
    }
}
