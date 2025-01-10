package de.relaxogames.exceptions;

import de.relaxogames.Prefixes;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LanguageFileNull extends RuntimeException {
    String file;
    String path;
    public LanguageFileNull(String fileName, String path, String message) {
        super(message);
        this.file = fileName;
        this.path = path;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        Logger.getLogger(Prefixes.getLingoPrefix()).log(Level.WARNING, "Language file " + getFile() + " could not be found at " + getPath() + " !");
    }

    /**
     * @return the missing filename
     */
    public String getFile() {
        return file;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }
}
