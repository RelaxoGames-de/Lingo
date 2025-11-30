package de.relaxogames.exceptions;

import de.relaxogames.Prefixes;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Exception thrown when a required language file cannot be found or accessed.
 * <p>
 * This exception indicates that a localization file expected by the RelaxoGames
 * language system is missing, unavailable, or could not be loaded from the
 * configured path. It stores both the filename and the attempted lookup path
 * for easier debugging.
 * </p>
 * <p>
 * When the stack trace is printed, this exception additionally logs a warning
 * message through the application logger.
 * </p>
 */
public class LanguageFileNull extends RuntimeException {

    /** The name of the missing file. */
    private final String file;

    /** The path where the system attempted to find the file. */
    private final String path;

    /**
     * Constructs a new {@code LanguageFileNull} exception.
     *
     * @param fileName the name of the missing language file
     * @param path     the expected lookup path of the file
     * @param message  a descriptive error message
     */
    public LanguageFileNull(String fileName, String path, String message) {
        super(message);
        this.file = fileName;
        this.path = path;
    }

    /**
     * Prints the stack trace and logs a warning message indicating the missing file.
     * <p>
     * The warning is logged using the logger returned by
     * {@link Prefixes#getLingoPrefix()}.
     * </p>
     */
    @Override
    public void printStackTrace() {
        super.printStackTrace();
        Logger.getLogger(Prefixes.getLingoPrefix()).log(
                Level.WARNING,
                "Language file " + getFile() + " could not be found at " + getPath() + " !"
        );
    }

    /**
     * Returns the name of the missing file.
     *
     * @return the missing filename
     */
    public String getFile() {
        return file;
    }

    /**
     * Returns the path where the file was expected to be found.
     *
     * @return the file lookup path
     */
    public String getPath() {
        return path;
    }
}
