package data;

import discord4j.common.util.Snowflake;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleManager {

    public static Snowflake getRoleByCategory(Snowflake category) {
        try {
            Connection conn = SQLConnector.getConnection();
            PreparedStatement ps = conn.prepareStatement("select allowed_roles.role from projects inner join allowed_roles ON projects.id = allowed_roles.projectId where category=?");
            ps.setLong(1, category.asLong());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()){
                return null;
            }
            Snowflake res = Snowflake.of(rs.getLong(1));
            conn.close();
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void deleteRole(long id) {
        try {
            Connection conn = SQLConnector.getConnection();
            PreparedStatement ps = conn.prepareStatement("delete from allowed_roles where role = ?");
            ps.setLong(1, id);
            ps.execute();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
