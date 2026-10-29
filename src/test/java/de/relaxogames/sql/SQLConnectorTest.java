package de.relaxogames.sql;

import com.zaxxer.hikari.HikariConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

class SQLConnectorTest {
    @AfterEach void cleanup() { SQLConnector.close(); }

    @Test void repeatedInitializationDoesNotBorrowConnectionsAndCanRestart() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:lingo;DB_CLOSE_DELAY=-1");
        config.setMaximumPoolSize(2);
        config.setConnectionTimeout(250);
        for (int i = 0; i < 20; i++) SQLConnector.initialize(config);
        try (Connection first = SQLConnector.getConnection(); Connection second = SQLConnector.getConnection()) {
            assertFalse(first.isClosed());
            assertFalse(second.isClosed());
        }
        SQLConnector.close();
        assertFalse(SQLConnector.conIsActive());
        SQLConnector.initialize(config);
        try (Connection connection = SQLConnector.getConnection()) { assertFalse(connection.isClosed()); }
    }
}
