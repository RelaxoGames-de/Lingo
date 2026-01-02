package de.relaxogames.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.relaxogames.api.FileManager;
import de.relaxogames.api.Lingo;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides a centralized and thread-safe way to manage the application's
 * connection pool to a MariaDB database using HikariCP.
 * <p>
 * The connector initializes a {@link HikariDataSource} on demand and exposes
 * methods to obtain database connections from the pool. Configuration values
 * (host, database, credentials) are loaded through {@link FileManager}.
 * </p>
 */
public class SQLConnector {

    private static HikariDataSource dataSource;
    private static final FileManager fileManagement = Lingo.getLibrary().getFileManager();

    /**
     * Initializes the connection pool if it has not been created yet.
     * <p>
     * This method loads the MariaDB JDBC driver explicitly and configures
     * HikariCP with database credentials and pool settings retrieved from
     * {@link FileManager}. If the pool is already initialized, the method
     * returns immediately without modifying it.
     * </p>
     *
     * @throws IllegalStateException if required JDBC driver classes are missing
     */
    public static void connect() {
        if (dataSource != null) return;

        try {
            Class.forName("org.mariadb.jdbc.Driver"); // Required for some environments
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MariaDB JDBC Driver could not be loaded.", e);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mariadb://" + fileManagement.host() + ":3306/" + fileManagement.database());
        config.setUsername(fileManagement.user());
        config.setPassword(fileManagement.password());
        config.setPoolName("SnorlaxLingo Helper");
        config.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(config);
    }

    /**
     * Retrieves a connection from the HikariCP connection pool.
     * <p>
     * The caller is responsible for closing the returned {@link Connection} when
     * finished, which returns it back to the pool instead of closing the
     * physical connection.
     * </p>
     *
     * @return an active {@link Connection} from the pool
     * @throws SQLException           if a connection cannot be retrieved
     * @throws IllegalStateException  if the connection pool has not been initialized via {@link #connect()}
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) throw new IllegalStateException("Database not initialized!");
        return dataSource.getConnection();
    }

    /**
     * Checks whether the connection pool has been initialized.
     *
     * @return {@code true} if the data source is active and ready to use,
     *         {@code false} otherwise
     */
    public static boolean conIsActive() {
        return dataSource != null;
    }
}
