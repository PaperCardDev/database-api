package cn.paper_card.database.api;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("unused")
public interface DatabaseApi {

    interface MySqlConnection {
        long getLastUseTime();

        void setLastUseTime();

        @NotNull Connection getRawConnection() throws SQLException;

        int getConnectCount();

        void testConnection() throws SQLException;

        void close() throws SQLException;

        void handleException(@NotNull SQLException e) throws SQLException;
    }

    interface RemoteMySQL {

        @NotNull MySqlConnection getConnectionImportant();

        @NotNull MySqlConnection getConnectionNormal();

        @NotNull MySqlConnection getConnectionUnimportant();

    }

    interface LocalSQLite {
        @NotNull Connection connectImportant() throws SQLException;

        @NotNull Connection connectNormal() throws SQLException;

        @NotNull Connection connectUnimportant() throws SQLException;
    }

    @NotNull RemoteMySQL getRemoteMySQL();

    @NotNull LocalSQLite getLocalSQLite();

    @NotNull MySqlConnection createMySqlConnection(@NotNull String address, @NotNull String user, @NotNull String password);
}
