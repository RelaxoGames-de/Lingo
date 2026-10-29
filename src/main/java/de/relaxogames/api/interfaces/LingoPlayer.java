package de.relaxogames.api.interfaces;

import de.relaxogames.api.Lingo;
import de.relaxogames.api.LocaleService;
import de.relaxogames.languages.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LingoPlayer implements LingoUser {
    private final UUID uuid;
    private final LocaleService locales;

    public LingoPlayer(UUID uuid) {
        this.uuid = Objects.requireNonNull(uuid);
        this.locales = Objects.requireNonNull(Lingo.getLibrary(), "Lingo is not initialized").getLocaleService();
    }

    @Override public UUID getUUID() { return uuid; }

    /** Returns the shared cached locale immediately, or system_default while it loads. */
    @Override public Locale getLanguage() { return locales.get(uuid); }

    /** Completes with the loaded locale, or exceptionally on database failure. */
    @Override public CompletableFuture<Locale> getLanguageAsync() { return locales.getAsync(uuid); }

    /** Updates the cache immediately and saves asynchronously. */
    @Override public void setLanguage(Locale value) { locales.set(uuid, value); }

    /** Updates the cache immediately and reports when the database write finishes. */
    public CompletableFuture<Void> setLanguageAsync(Locale value) { return locales.set(uuid, value); }
}
