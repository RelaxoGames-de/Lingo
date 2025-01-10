package de.relaxogames.exceptions;

import de.relaxogames.Prefixes;
import de.relaxogames.languages.Locale;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LanguageNotFound extends RuntimeException
{
    Locale locale;
    public LanguageNotFound(Locale locale, String message) {
        super(message);
        this.locale = locale;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        Logger.getLogger(Prefixes.getLingoPrefix()).log(Level.WARNING, "Language " + getMissingLocale() + " not found");
    }

    /**
     * @return the missing locale
     */
    public Locale getMissingLocale() {
        return locale;
    }
}
