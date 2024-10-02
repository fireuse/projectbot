package data;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class RoleManager {
    public static List<Long> getLeader() throws SQLException {
        List<Long> out = new LinkedList<>();
        Connection conn = SQLConnector.getConnection();
        Statement ps = conn.createStatement();
        ResultSet rs = ps.executeQuery("select * from allowed_roles");
        while (rs.next()) {
            out.add(rs.getLong(1));
        }
        conn.close();
        return out;
    }

    public static void createLeader(long id) throws SQLException {
        Connection conn = SQLConnector.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into allowed_roles values(?)");
        ps.setLong(1, id);
        ps.execute();
        conn.close();
    }

    public static void deleteLeader(long id) {
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
