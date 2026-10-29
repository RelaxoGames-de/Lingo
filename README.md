
# Lingo
Lingo is a lightweight and extensible localization (i18n) library for Minecraft plugins, developed by the [SnorlaxLabs Team](https://snorlaxlabs.de) for [RelaxoGamesDE](https://relaxogames.de).

It provides a centralized language management system by resolving player locale settings through a database-backed lookup. This allows plugins to dynamically deliver translated messages based on individual player preferences.
## Features

- Database-driven locale resolution per player
- Fast, lightweight, and dependency-friendly design
- API-oriented and easy to integrate into existing plugins
- Supports runtime language switching
- Thread-safe access for asynchronous operations and efficient pool management [(HikariCP)](https://www.baeldung.com/hikaricp)



## Compatibility

Lingo works regardless of the platform you are using. We use Lingo as a Minecraft-Plugin library on those platforms:
- Bukkit / Spigot / Paper
- Velocity
- Proxy- and Standalone-Software
## Support

If you encounter any issues or have questions regarding Lingo, please feel free to reach out to our support team via email: [support@snorlaxlabs.de](mailto:support@snorlaxlabs.de). We are happy to assist you!

## Autoren

- [@SELTEX](https://github.com/Seltexx) (SnorlaxLabs)
- [@jotrorox](https://github.com/Jotrorox) (SnorlaxLabs)

## Player locales and shutdown

`LingoPlayer.getLanguage()` never waits for the database. It returns the shared cached locale, or `Locale.system_default` while the first lookup is pending. Players constructed with the same UUID share that cache. Use `getLanguageAsync()` when a result must reflect the database lookup; its future reports database errors and its callbacks may run on the database worker, so schedule Minecraft API calls on the server thread. A failed lookup can be retried by calling again.

`setLanguage()` updates the cache immediately and queues the database write. Use `LingoPlayer.setLanguageAsync()` to observe whether the write succeeded. Writes are processed in order. Call `Lingo.close()` on plugin shutdown to drain pending work and close the connection pool. Constructing a new `Lingo` replaces and closes the previous instance.
