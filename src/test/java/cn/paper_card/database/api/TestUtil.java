package cn.paper_card.database.api;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestUtil {

    private PreparedStatement statement = null;

    @Test
    public void test1() throws SQLException {
        final File file = new File("test.db");
        final Connection connection = Util.connectSQLite(file);
        connection.close();
        final boolean delete = file.delete();
        Assert.assertTrue(delete);
    }

    @Test
    public void test2() throws SQLException {
        final Connection connection = Util.connectMySQL("localhost/test", "root", "qwer4321");
        connection.close();
    }

    @Test
    public void test3() throws SQLException {
        final File file = new File("test.db");
        final Connection connection = Util.connectSQLite(file);

        Util.executeSQL(connection, "DROP TABLE IF EXISTS test");
        final int i = Util.executeSQL(connection, "CREATE TABLE IF NOT EXISTS test (qq INTEGER NOT NULL)");
        Assert.assertEquals(0, i);
        Util.executeSQL(connection, "DROP TABLE test");

        connection.close();
        final boolean delete = file.delete();
        Assert.assertTrue(delete);
    }

    @Test
    public void test4() throws SQLException {
        final File file = new File("test.db");
        final Connection connection = Util.connectSQLite(file);

        this.statement = connection.prepareStatement("SELECT 1");
        Util.closeAllStatements(this.getClass(), this);
        Assert.assertNull(this.statement);

        connection.close();
        final boolean delete = file.delete();
        Assert.assertTrue(delete);
    }

    @Test
    public void test5() throws SQLException {

        final Connection connection = Util.connectMySQL("localhost/test", "root", "qwer4321");

        this.statement = connection.prepareStatement("SELECT 1");
        Util.closeAllStatements(this.getClass(), this);
        Assert.assertNull(this.statement);

        connection.close();
    }
}
