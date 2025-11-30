package de.relaxogames.sql;

/**
 * Contains predefined SQL statements used by the RelaxoGames language
 * (Lingo) system.
 * <p>
 * Each enum constant represents a specific SQL query or DDL command used for
 * managing player locale information. The SQL can be retrieved using
 * {@link #getSql()}.
 * </p>
 */
public enum SQLingos {

    /**
     * SQL statement for creating the {@code general} table if it does not
     * already exist.
     * <p>
     * This table stores:
     * <ul>
     *   <li>{@code uuid} – the unique player identifier (primary key)</li>
     *   <li>{@code name} – the player name (optional)</li>
     *   <li>{@code locale} – the stored language/locale (optional)</li>
     *   <li>{@code created_at} – the timestamp of first insertion</li>
     * </ul>
     * </p>
     */
    CREATE_LINGO_FIELD(
            "create table IF NOT EXISTS `general`\n" +
                    "(\n" +
                    "    uuid       varchar(36)                           not null\n" +
                    "        primary key,\n" +
                    "    name       varchar(32)                           null,\n" +
                    "    locale     varchar(8)                            null,\n" +
                    "    created_at timestamp default current_timestamp() null\n" +
                    ");\n"
    ),

    /**
     * SQL statement for selecting a player's locale entry from the
     * {@code general} table based on their UUID.
     */
    SELECT_LINGO_LOCALE("SELECT * FROM `general` WHERE uuid = ?;");

    private final String sql;

    /**
     * Constructs an enum constant containing a SQL statement.
     *
     * @param sql the SQL string assigned to the enum constant
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
