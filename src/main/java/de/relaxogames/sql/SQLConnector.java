package de.relaxogames.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.relaxogames.api.FileManager;
import de.relaxogames.exceptions.DriverLostConnection;

import java.sql.Connection;
import java.sql.SQLException;

/** Owns the shared connection pool. Borrowed connections must be closed by their callers. */
public class SQLConnector {
    private static volatile HikariDataSource dataSource;

    /** Initialize the pool without borrowing a connection. Repeated calls reuse the active pool. */
    public static synchronized void initialize(FileManager fileManagement) {
        if (conIsActive()) return;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DriverLostConnection("MariaDB JDBC Driver could not be loaded.", e);
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mariadb://" + fileManagement.host() + ":3306/" + fileManagement.database());
        config.setUsername(fileManagement.user());
        config.setPassword(fileManagement.password());
        config.setPoolName("SnorlaxLingo Helper");
        config.setMaximumPoolSize(10);
        initialize(config);
    }

    static synchronized void initialize(HikariConfig config) {
        if (!conIsActive()) dataSource = new HikariDataSource(config);
    }

    /** @deprecated Initialize the pool separately and close the returned connection after use. */
    @Deprecated
    public static Connection connect() {
        initialize(de.relaxogames.api.Lingo.getLibrary().getFileManager());
        try {
            return getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        HikariDataSource pool = dataSource;
        if (pool == null || pool.isClosed()) throw new IllegalStateException("Database not initialized!");
        return pool.getConnection();
    }

    public static boolean conIsActive() {
        HikariDataSource pool = dataSource;
        return pool != null && !pool.isClosed();
    }

    /** Close the pool; a later initialization creates a fresh pool. */
    public static synchronized void close() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }
}
