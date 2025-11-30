package de.relaxogames.sql;

import de.relaxogames.exceptions.DriverLostConnection;
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
 *     <li>Updating a player's locale in the database</li>
 * </ul>
 * </p>
 * <p>
 * Note: This class uses a static {@link Connection}, which may not be ideal in all environments.
 * Connection pooling with per-operation connections is recommended for better reliability.
 * </p>
 */
public class LingoSQL {

    /** Shared static database connection obtained from {@link SQLConnector}. */
    static Connection con;

    static {
        try {
            con = SQLConnector.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish initial database connection", e);
        }
    }

    /**
     * Initializes the locale storage table in the database if it does not already exist.
     * <p>
     * Executes the SQL statement defined in {@link SQLingos#CREATE_LINGO_FIELD}.
     * Throws a {@link RuntimeException} if an error occurs during table creation.
     * </p>
     */
    public static void initialize() {
        try {
            checkConnection();
            Statement st = con.createStatement();
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
            checkConnection();
            try (PreparedStatement pst = con.prepareStatement(SQLingos.SELECT_LINGO_LOCALE.getSql())) {
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
            checkConnection();
            try (PreparedStatement pst = con.prepareStatement(SQLingos.UPDATE_LINGO_LOCALE.getSql())) {
                pst.setString(1, locale.getISO());
                pst.setString(2, uuid.toString());
                pst.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set locale for UUID: " + uuid, e);
        }
    }

    /**
     * Checks whether the current database connection is active and valid.
     * <p>
     * If the connection is {@code null} or closed, a {@link DriverLostConnection} exception
     * is thrown to indicate that database operations cannot proceed.
     * </p>
     *
     * @return {@code true} if the connection is active
     * @throws SQLException         if an I/O error occurs while checking the connection
     * @throws DriverLostConnection if the connection is null or closed
     */
    private static boolean checkConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            throw new DriverLostConnection(
                    "Connection to RelaxoGames database is closed! " +
                            "Canceled current action! Is connection alive?"
            );
        }
        return true;
    }
}
