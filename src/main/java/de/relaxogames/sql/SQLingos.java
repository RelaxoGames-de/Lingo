package de.relaxogames.sql;

/**
 * Defines predefined SQL statements used by the RelaxoGames language (Lingo) system.
 * <p>
 * Each enum constant corresponds to a specific SQL query or DDL command for managing
 * player locale information. Use {@link #getSql()} to retrieve the raw SQL string.
 * </p>
 */
public enum SQLingos {

    /**
     * SQL statement to create the {@code general} table if it does not already exist.
     * <p>
     * The table stores the following columns:
     * <ul>
     *   <li>{@code uuid} – the unique player identifier (primary key)</li>
     *   <li>{@code name} – the player name (optional)</li>
     *   <li>{@code locale} – the player's language/locale (optional)</li>
     *   <li>{@code created_at} – timestamp of the first insertion (defaults to current time)</li>
     * </ul>
     * </p>
     */
    CREATE_LINGO_FIELD(
            "CREATE TABLE IF NOT EXISTS `general` (\n" +
                    "    uuid       VARCHAR(36) NOT NULL PRIMARY KEY,\n" +
                    "    name       VARCHAR(32) NULL,\n" +
                    "    locale     VARCHAR(8) NULL,\n" +
                    "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL\n" +
                    ");"
    ),

    /**
     * SQL statement for selecting a player's locale entry from the {@code general} table
     * based on their UUID.
     * <p>
     * The UUID is passed as a parameter in a prepared statement.
     * </p>
     */
    SELECT_LINGO_LOCALE("SELECT * FROM `general` WHERE uuid = ?;"),

    /**
     * SQL statement for updating a player's locale in the {@code general} table.
     * <p>
     * The locale and UUID are provided as parameters in a prepared statement.
     * </p>
     */
    UPDATE_LINGO_LOCALE("UPDATE `general` SET locale = ? WHERE uuid = ?;"),
    INSERT_LINGO_LOCALE("INSERT INTO `general` (uuid, name, locale, created_at)\n" +
            "VALUES (?, '', ?, ?);");

    /** The raw SQL string associated with this enum constant. */
    private final String sql;

    /**
     * Constructs an enum constant containing a SQL statement.
     *
     * @param sql the SQL string assigned to this enum constant
     */
    SQLingos(String sql) {
        this.sql = sql;
    }

    /**
     * Returns the SQL statement associated with this enum constant.
     *
     * @return the SQL query or command as a string
     */
    public String getSql() {
        return sql;
    }
}
