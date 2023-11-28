package cn.paper_card.database.api;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.*;

public class Util {

    public static int executeSQL(@NotNull Connection connection, @NotNull String sql) throws SQLException {
        final Statement statement = connection.createStatement();
        final int i;
        try {
            i = statement.executeUpdate(sql);
        } catch (SQLException e) {
            try {
                statement.close();
            } catch (SQLException ignored) {
            }
            throw e;
        }
        statement.close();
        return i;
    }

    public static void closeAllStatements(@NotNull Class<?> klass, @NotNull Object obj) throws SQLException {
        SQLException exception = null;
        for (final Field f : klass.getDeclaredFields()) {

            final Object o;

            f.setAccessible(true);

            try {
                o = f.get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (o instanceof final PreparedStatement ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    exception = e;
                }

                try {
                    f.set(obj, null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (exception != null) throw exception;
    }

    public static Connection connectSQLite(@NotNull File file) throws SQLException {
        // 数据库文件
        final File parentFile = file.getParentFile();

        if (parentFile != null && !parentFile.isDirectory()) {
            if (!parentFile.mkdir()) {
                throw new SQLException("创建父目录[%s]失败！".formatted(parentFile.getAbsolutePath()));
            }
        }

        // 驱动类
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("无法加载SQLITE驱动类", e);
        }

        // 数据库连接
        return DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
    }

    public static Connection connectMySQL(String address, String user, String password) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("无法加载MySQL驱动类", e);
        }

        if (address.isEmpty()) throw new SQLException("未指定MySQL数据库地址！");

        return DriverManager.getConnection("jdbc:mysql://" + address,
                user, password);

    }
}
