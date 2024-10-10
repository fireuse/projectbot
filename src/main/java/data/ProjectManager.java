package data;

import discord4j.common.util.Snowflake;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ProjectManager {
    public static void addProject(Project project) throws SQLException {
        Connection conn = SQLConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement("insert into projects (name, creator, description) values (?, ?, ?)");
        stmt.setString(1, project.getName());
        stmt.setLong(2, project.getAuthor());
        stmt.setString(3, project.getDescription());
        stmt.execute();
        PreparedStatement stmtr = conn.prepareStatement(" insert into allowed_roles (projectId, role) values (LAST_INSERT_ID(), ?);");
        stmtr.setLong(1, project.getRoleId().asLong());
        stmtr.execute();
        conn.close();
    }

    public static List<Project> getProjects() throws SQLException {
        Connection conn = SQLConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select creator, name, description, allowed_roles.role from projects INNER JOIN allowed_roles ON allowed_roles.projectId = projects.id");
        List<Project> projects = new LinkedList<>();
        while (rs.next()) {
            projects.add(new Project(rs.getString(2), rs.getLong(1), rs.getString(3), Snowflake.of(rs.getLong(4))));
        }
        conn.close();
        return projects;
    }

}
