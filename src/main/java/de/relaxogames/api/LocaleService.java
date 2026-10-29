package de.relaxogames.api;

import de.relaxogames.languages.Locale;
import de.relaxogames.sql.LingoSQL;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Shared locale cache and ordered database worker for a Lingo instance. */
public final class LocaleService implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(LocaleService.class.getName());
    private final ExecutorService worker = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "Lingo database worker");
        thread.setDaemon(true);
        return thread;
    });
    private final LingoSQL sql;
    private final Map<UUID, Entry> entries = new HashMap<>();
    private boolean closed;
    private final Set<CompletableFuture<Locale>> operations = ConcurrentHashMap.newKeySet();

    private static final class Entry {
        Locale locale = Locale.system_default;
        CompletableFuture<Locale> pending;
    }

    public LocaleService(LingoSQL sql) { this.sql = Objects.requireNonNull(sql); }

    public synchronized Locale get(UUID uuid) {
        Entry entry = entry(uuid);
        load(uuid, entry);
        return entry.locale;
    }

    /** Completes on the database worker; callers must schedule game API work on its required thread. */
    public synchronized CompletableFuture<Locale> getAsync(UUID uuid) {
        Entry entry = entry(uuid);
        return load(uuid, entry).thenApply(locale -> locale);
    }

    private Entry entry(UUID uuid) {
        if (closed) throw new IllegalStateException("Lingo is closed");
        return entries.computeIfAbsent(Objects.requireNonNull(uuid), ignored -> new Entry());
    }

    private CompletableFuture<Locale> load(UUID uuid, Entry entry) {
        if (entry.pending != null) return entry.pending;
        CompletableFuture<Locale> result = new CompletableFuture<>();
        operations.add(result);
        result.whenComplete((ignored, error) -> operations.remove(result));
        entry.pending = result;
        worker.execute(() -> {
            try {
                sql.insertEntry(uuid);
                Locale loaded = sql.loadLocale(uuid);
                synchronized (this) { if (entry.pending == result) entry.locale = loaded; }
                result.complete(loaded);
            } catch (Throwable error) {
                synchronized (this) { if (entry.pending == result) entry.pending = null; }
                result.completeExceptionally(error);
                LOG.log(Level.WARNING, "Failed to load locale for " + uuid, error);
            }
        });
        return result;
    }

    public synchronized CompletableFuture<Void> set(UUID uuid, Locale value) {
        Objects.requireNonNull(value);
        Entry entry = entry(uuid);
        // Queue after the initial load, so an older load cannot overwrite the new value.
        load(uuid, entry);
        entry.locale = value;
        CompletableFuture<Locale> result = new CompletableFuture<>();
        operations.add(result);
        result.whenComplete((ignored, error) -> operations.remove(result));
        entry.pending = result;
        worker.execute(() -> {
            try {
                sql.insertEntry(uuid);
                sql.setLocale(uuid, value);
                synchronized (this) { if (entry.pending == result) entry.locale = value; }
                result.complete(value);
            } catch (Throwable error) {
                synchronized (this) { if (entry.pending == result) entry.pending = null; }
                result.completeExceptionally(error);
                LOG.log(Level.WARNING, "Failed to save locale for " + uuid, error);
            }
        });
        return result.thenApply(ignored -> null);
    }

    private void failPending() {
        operations.forEach(future -> future.completeExceptionally(new IllegalStateException("Lingo shut down before the operation completed")));
    }

    @Override public void close() {
        synchronized (this) { if (closed) return; closed = true; worker.shutdown(); }
        try {
            if (!worker.awaitTermination(30, TimeUnit.SECONDS)) {
                worker.shutdownNow();
                failPending();
                if (!worker.awaitTermination(5, TimeUnit.SECONDS)) LOG.warning("Lingo database worker did not stop");
            }
        } catch (InterruptedException e) {
            worker.shutdownNow();
            failPending();
            Thread.currentThread().interrupt();
        }
    }
}
