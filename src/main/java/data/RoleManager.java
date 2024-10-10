package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RoleManager {

    public static void createLeader(long id) throws SQLException {
        Connection conn = SQLConnector.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into allowed_roles values(?)");
        ps.setLong(1, id);
        ps.execute();
        conn.close();
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
