package de.relaxogames.exceptions;

import de.relaxogames.Prefixes;
import de.relaxogames.languages.Locale;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Exception thrown when a specific translation key is not found for a given
 * locale in the RelaxoGames language system.
 * <p>
 * This exception indicates that the requested message key is missing for the
 * provided {@link Locale}. Both the missing key and locale are stored to
 * facilitate debugging and logging.
 * </p>
 * <p>
 * When the stack trace is printed, a warning is also logged using the
 * logger provided by {@link Prefixes#getLingoPrefix()}.
 * </p>
 */
public class MessageNotFound extends RuntimeException {

    /** The locale for which the message key was requested. */
    private final Locale locale;

    /** The missing message key. */
    private final String key;

    /**
     * Constructs a new {@code MessageNotFound} exception.
     *
     * @param locale the locale for which the message is missing
     * @param key    the missing message key
     */
    public MessageNotFound(Locale locale, String key) {
        this.locale = locale;
        this.key = key;
    }

    /**
     * Prints the exception stack trace and logs a warning about the missing
     * message.
     * <p>
     * The warning includes both the missing message key and the affected locale.
     * </p>
     */
    @Override
    public void printStackTrace() {
        Logger.getLogger(Prefixes.getLingoPrefix()).log(
                Level.WARNING,
                "Message " + missingMessage() + " for " + missingLocale() + " not found"
        );
        super.printStackTrace();
    }

    /**
     * Returns the missing message key.
     *
     * @return the missing message key
     */
    public String missingMessage() {
        return key;
    }

    /**
     * Returns the locale for which the message key was not found.
     *
     * @return the missing {@link Locale}
     */
    public Locale missingLocale() {
        return locale;
    }
}
