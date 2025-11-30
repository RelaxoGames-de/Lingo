package de.relaxogames.sql;

import de.relaxogames.exceptions.DriverLostConnection;
import de.relaxogames.languages.Locale;

import java.sql.*;
import java.util.UUID;

/**
 * Provides access to the locale-related database operations used by the
 * RelaxoGames language system.
 * <p>
 * This class manages queries for loading a player's locale and initializing
 * the required database table. A single shared {@link Connection} is obtained
 * from {@link SQLConnector} during class initialization.
 * </p>
 * <p>
 * Note: This class relies on a static {@link Connection}, which may not be
 * suitable for all environments. Consider using connection pooling with
 * per-operation connections for improved safety and reliability.
 * </p>
 */
public class LingoSQL {

    static Connection con;

    static {
        try {
            con = SQLConnector.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the required database table for locale storage if it does not
     * already exist.
     * <p>
     * This method ensures a valid connection and executes the SQL statement
     * defined in {@link SQLingos#CREATE_LINGO_FIELD}. If initialization fails,
     * a {@link RuntimeException} is thrown.
     * </p>
     */
    public static void initialize() {
        try {
            checkConnection();
            Statement st = con.createStatement();
            st.execute(SQLingos.CREATE_LINGO_FIELD.getSql());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the stored locale for a specific player identified by their UUID.
     * <p>
     * If the player has no stored locale entry, {@link Locale#system_default}
     * is returned. Otherwise, the locale string is converted to a
     * {@link Locale} instance via
     * {@link Locale#convertStringToLanguage(String)}.
     * </p>
     *
     * @param uuid the unique identifier of the player
     * @return the stored {@link Locale}, or the system default if none exists
     * @throws RuntimeException if a database access error occurs or the
     *                          connection is invalid
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
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies that the current database connection is active.
     * <p>
     * If the connection is closed or {@code null}, a
     * {@link DriverLostConnection} exception is thrown, indicating that the
     * operation cannot proceed.
     * </p>
     *
     * @return {@code true} if the connection is valid
     * @throws SQLException           if an I/O error occurs while checking the connection
     * @throws DriverLostConnection   if the connection is no longer active
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
