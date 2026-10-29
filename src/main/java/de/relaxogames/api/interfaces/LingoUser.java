package de.relaxogames.api.interfaces;

import de.relaxogames.languages.Locale;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface LingoUser {

    UUID getUUID();
    Locale getLanguage();
    default CompletableFuture<Locale> getLanguageAsync() { return CompletableFuture.completedFuture(getLanguage()); }
    void setLanguage(Locale value);

}
