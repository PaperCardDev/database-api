package cn.paper_card.database.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class Parser<T> {
    public abstract @NotNull T parseRow(@NotNull ResultSet resultSet) throws SQLException;

    public final @Nullable T parseOne(@NotNull ResultSet resultSet) throws SQLException {
        final T t;
        try {
            if (resultSet.next()) t = this.parseRow(resultSet);
            else t = null;
            if (resultSet.next()) throw new SQLException("不应该还有数据！");
        } catch (SQLException e) {
            try {
                resultSet.close();
            } catch (SQLException ignored) {
            }
            throw e;
        }
        resultSet.close();
        return t;
    }

    public final @NotNull List<T> parseAll(@NotNull ResultSet resultSet) throws SQLException {
        final LinkedList<T> list = new LinkedList<>();
        try {
            while (resultSet.next()) list.add(this.parseRow(resultSet));
        } catch (SQLException e) {
            try {
                resultSet.close();
            } catch (SQLException ignored) {
            }
            throw e;
        }
        resultSet.close();
        return list;
    }

    public static int parseOneInt(@NotNull ResultSet resultSet) throws SQLException {
        final int i;
        try {
            if (resultSet.next()) i = resultSet.getInt(1);
            else throw new SQLException("不应该没有数据！");
            if (resultSet.next()) throw new SQLException("不应该还有数据！");
        } catch (SQLException e) {
            try {
                resultSet.close();
            } catch (SQLException ignored) {
            }
            throw e;
        }
        resultSet.close();
        return i;
    }
}