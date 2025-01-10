package de.relaxogames.exceptions;

import de.relaxogames.Prefixes;
import de.relaxogames.languages.Locale;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageNotFound extends RuntimeException {
    Locale locale;
    String key;
    public MessageNotFound(Locale locale, String key) {
        this.locale = locale;
        this.key = key;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        Logger.getLogger(Prefixes.getLingoPrefix()).log(Level.WARNING, "Message " + missingMessage() + " for " + missingLocale() + " not found");
    }

    /**
     * @return the missing message key.
     */
    public String missingMessage(){
        return key;
    }

    /**
     * @return the missing locale
     */
    public Locale missingLocale(){
        return locale;
    }
}
