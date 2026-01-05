package de.relaxogames.sql;

import de.relaxogames.api.FileManager;
import de.relaxogames.languages.Locale;

import java.sql.*;
import java.util.UUID;

/**
 * Provides database access for player locale operations in the RelaxoGames language system.
 * <p>
 * This class handles:
 * <ul>
 *     <li>Initializing the database table for storing player locales</li>
 *     <li>Loading a player's locale from the database</li>
 *     <li>Updating or inserting a player's locale in the database</li>
 *     <li>Checking whether a player entry exists</li>
 * </ul>
 * </p>
 * <p>
 * Note: This class uses a static {@link Connection}, which may not be ideal in all environments.
 * Connection pooling with per-operation connections is recommended for better reliability.
 * </p>
 */
public class LingoSQL {

    private static final FileManager FM = new FileManager();

    /**
     * Initializes the locale storage table in the database if it does not already exist.
     * <p>
     * Executes the SQL statement defined in {@link SQLingos#CREATE_LINGO_FIELD}.
     * Throws a {@link RuntimeException} if an error occurs during table creation.
     * </p>
     */
    public static void initialize() {
        try (Connection dbConnection = connection()){
            Statement st = dbConnection.createStatement();
            st.execute(SQLingos.CREATE_LINGO_FIELD.getSql());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize locale table", e);
        }
    }

    /**
     * Loads the stored locale for a specific player by their UUID.
     * <p>
     * If no locale is stored for the player, {@link Locale#system_default} is returned.
     * Otherwise, the stored locale string is converted to a {@link Locale} instance
     * using {@link Locale#convertStringToLanguage(String)}.
     * </p>
     *
     * @param uuid the unique identifier of the player
     * @return the stored {@link Locale}, or the system default if none exists
     * @throws RuntimeException if a database access error occurs or the connection is invalid
     */
    public Locale loadLocale(UUID uuid) {
        try {
            try (Connection dbConnection = connection(); PreparedStatement pst = dbConnection.prepareStatement(SQLingos.SELECT_LINGO_LOCALE.getSql())) {
                pst.setString(1, uuid.toString());
                ResultSet set = pst.executeQuery();
                if (!set.next()) return Locale.system_default;
                return Locale.convertStringToLanguage(set.getString("locale"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load locale for UUID: " + uuid, e);
        }
    }

    /**
     * Updates or sets the locale for a specific player in the database.
     * <p>
     * Executes an update statement using {@link SQLingos#UPDATE_LINGO_LOCALE}.
     * </p>
     *
     * @param uuid   the unique identifier of the player
     * @param locale the {@link Locale} to set for the player
     * @throws RuntimeException if a database access error occurs or the connection is invalid
     */
    public void setLocale(UUID uuid, Locale locale) {
        try {
            try (Connection dbConnection = connection(); PreparedStatement pst = dbConnection.prepareStatement(SQLingos.UPDATE_LINGO_LOCALE.getSql())) {
                pst.setString(1, locale.getISO());
                pst.setString(2, uuid.toString());
                pst.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set locale for UUID: " + uuid, e);
        }
    }

    /**
     * Checks whether a player entry exists in the database.
     * <p>
     * Queries the database using the player's UUID to determine if an entry is present.
     * </p>
     *
     * @param uuid the unique identifier of the player
     * @return {@code true} if the player has an entry in the database, {@code false} otherwise
     * @throws RuntimeException if a database access error occurs or the connection is invalid
     */
    public boolean hasEntry(UUID uuid) {
        try {
            try (Connection dbConnection = connection(); PreparedStatement pst = dbConnection.prepareStatement(SQLingos.SELECT_LINGO_LOCALE.getSql())) {
                pst.setString(1, uuid.toString());
                ResultSet set = pst.executeQuery();
                return set.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check existence of UUID: " + uuid, e);
        }
    }

    /**
     * Inserts a new player entry into the database with the default system locale.
     * <p>
     * Uses {@link SQLingos#INSERT_LINGO_LOCALE} to create a new record with the UUID,
     * the system default locale, and the current timestamp.
     * </p>
     *
     * @param uuid the unique identifier of the player
     * @throws RuntimeException if a database access error occurs or the connection is invalid
     */
    public void insertEntry(UUID uuid) {
        try {
            try (Connection dbConnection = connection(); PreparedStatement pst = dbConnection.prepareStatement(SQLingos.INSERT_LINGO_LOCALE.getSql())) {
                pst.setString(1, uuid.toString());
                pst.setString(2, Locale.system_default.getISO());
                pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                pst.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert locale entry for UUID: " + uuid, e);
        }
    }

    /**
     * Retrieves a database connection from the configured HikariCP connection pool.
     * <p>
     * Each invocation returns a <em>new pooled connection</em> which must be closed
     * after use. Closing the connection does <strong>not</strong> terminate the
     * physical database connection; instead, it returns the connection back to
     * the pool for reuse.
     * </p>
     *
     * <p>
     * This method does not perform any manual connection validation or reconnection
     * logic. Connection health checks and automatic recovery are fully handled by
     * HikariCP itself.
     * </p>
     *
     * @return an active {@link Connection} from the connection pool
     * @throws SQLException if a database access error occurs or the pool
     *                      cannot provide a connection
     * @throws IllegalStateException if the connection pool has not been initialized
     *                              prior to calling this method
     */
    private static Connection connection() throws SQLException {
        return SQLConnector.getConnection();
    }
}
