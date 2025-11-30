package de.relaxogames.exceptions;

import de.relaxogames.Prefixes;
import de.relaxogames.languages.Locale;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Exception thrown when a requested language or locale is not available
 * in the RelaxoGames language system.
 * <p>
 * This typically occurs when attempting to load translations for a locale
 * that does not exist in the language configuration or has not been registered.
 * The missing {@link Locale} is stored for debugging and logging.
 * </p>
 * <p>
 * In addition to the normal stack trace, this exception logs a warning message
 * via the application logger whenever the stack trace is printed.
 * </p>
 */
public class LanguageNotFound extends RuntimeException {

    /** The locale that could not be found. */
    private final Locale locale;

    /**
     * Creates a new {@code LanguageNotFound} exception.
     *
     * @param locale  the missing locale
     * @param message a descriptive error message
     */
    public LanguageNotFound(Locale locale, String message) {
        super(message);
        this.locale = locale;
    }

    /**
     * Prints the exception stack trace and logs a warning describing which
     * locale was missing.
     * <p>
     * The message is logged using the logger identified by
     * {@link Prefixes#getLingoPrefix()}.
     * </p>
     */
    @Override
    public void printStackTrace() {
        super.printStackTrace();
        Logger.getLogger(Prefixes.getLingoPrefix()).log(
                Level.WARNING,
                "Language " + getMissingLocale() + " not found"
        );
    }

    /**
     * Returns the locale that was requested but not found.
     *
     * @return the missing {@link Locale}
     */
    public Locale getMissingLocale() {
        return locale;
    }
}
