package de.relaxogames.api;

import de.relaxogames.languages.Locale;
import de.relaxogames.sql.LingoSQL;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletableFuture;
import static org.junit.jupiter.api.Assertions.*;

class LocaleServiceTest {
    @Test void readsAreNonBlockingSharedAndWritesWinOverInFlightLoad() throws Exception {
        CountDownLatch started = new CountDownLatch(1), release = new CountDownLatch(1);
        LingoSQL sql = new LingoSQL() {
            @Override public void insertEntry(UUID id) { }
            @Override public Locale loadLocale(UUID id) {
                started.countDown();
                try { assertTrue(release.await(3, TimeUnit.SECONDS)); } catch (InterruptedException e) { throw new RuntimeException(e); }
                return Locale.ENGLISH;
            }
            @Override public void setLocale(UUID id, Locale locale) { }
        };
        try (LocaleService service = new LocaleService(sql)) {
            UUID id = UUID.randomUUID();
            assertEquals(Locale.system_default, service.get(id));
            assertTrue(started.await(1, TimeUnit.SECONDS));
            CompletableFuture<Locale> load = service.getAsync(id);
            CompletableFuture<Void> write = service.set(id, Locale.GERMAN);
            assertEquals(Locale.GERMAN, service.get(id));
            release.countDown();
            assertEquals(Locale.ENGLISH, load.get(2, TimeUnit.SECONDS));
            write.get(2, TimeUnit.SECONDS);
            assertEquals(Locale.GERMAN, service.getAsync(id).get(2, TimeUnit.SECONDS));
        } finally { release.countDown(); }
    }
}
