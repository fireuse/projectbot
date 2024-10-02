package data;

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
        conn.close();
    }

    public static List<Project> getProjects() throws SQLException {
        Connection conn = SQLConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select creator, name, description from projects");
        List<Project> projects = new LinkedList<>();
        while (rs.next()) {
            projects.add(new Project(rs.getString(2), rs.getLong(1), rs.getString(3)));
        }
        conn.close();
        return projects;
    }

}
