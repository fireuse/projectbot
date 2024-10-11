package data;

import discord4j.common.util.Snowflake;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class ProjectManager {
    public static void deleteProject(Snowflake category) {
        try {
            Connection conn = SQLConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement("delete from projects where category = ?");
            stmt.setLong(1, category.asLong());
            stmt.execute();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void addProject(Project project) throws SQLException {
        Connection conn = SQLConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement("insert into projects (name, creator, description, category) values (?, ?, ?, ?)");
        stmt.setString(1, project.getName());
        stmt.setLong(2, project.getAuthor().asLong());
        stmt.setString(3, project.getDescription());
        stmt.setLong(4, project.getCategoryId().asLong());
        stmt.execute();
        PreparedStatement stmtr = conn.prepareStatement(" insert into allowed_roles (projectId, role) values (LAST_INSERT_ID(), ?);");
        stmtr.setLong(1, project.getRoleId().asLong());
        stmtr.execute();
        conn.close();
    }

    public static boolean checkCategory(Snowflake category) {
        try {
            Connection conn = SQLConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement("select id from projects where category = ?");
            stmt.setLong(1, category.asLong());
            ResultSet rs = stmt.executeQuery();
            boolean ok = rs.next();
            conn.close();
            return ok;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<Project> getProjects() throws SQLException {
        Connection conn = SQLConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select creator, name, description, allowed_roles.role, category from projects INNER JOIN allowed_roles ON allowed_roles.projectId = projects.id");
        List<Project> projects = new LinkedList<>();
        while (rs.next()) {
            projects.add(new Project(rs.getString(2), Snowflake.of(rs.getLong(1)), rs.getString(3), Snowflake.of(rs.getLong(4)), Snowflake.of(rs.getLong(5))));
        }
        conn.close();
        return projects;
    }

}
