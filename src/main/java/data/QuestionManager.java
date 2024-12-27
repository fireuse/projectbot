package data;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class QuestionManager {
    public record Category(String category, int id) {
    }

    public record Question(String question, String answer, int id) {
    }

    public static void createCategory(String category) throws SQLException {
        Connection conn = SQLConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO faq_categories (name) VALUES (?)");
        stmt.setString(1, category);
        stmt.execute();
        conn.close();
    }

    public static void createQuestion(String question, String answer, int category) throws SQLException {
        Connection conn = SQLConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO faq (question, answer, category) VALUES (?, ?, ?)");
        stmt.setString(1, question);
        stmt.setString(2, answer);
        stmt.setInt(3, category);
        stmt.execute();
        conn.close();
    }

    public static List<Category> getCategories() throws SQLException {
        Connection conn = SQLConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM faq_categories");
        List<Category> categories = new LinkedList<>();
        while (rs.next()) {
            categories.add(new Category(rs.getString("name"), rs.getInt("id")));
        }
        conn.close();
        return categories;
    }

    public static List<Question> getQuestions(int category) throws SQLException {
        Connection conn = SQLConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM faq WHERE category = ?");
        stmt.setInt(1, category);
        ResultSet rs = stmt.executeQuery();
        List<Question> questions = new LinkedList<>();
        while (rs.next()) {
            questions.add(new Question(rs.getString("question"), rs.getString("answer"), rs.getInt("id")));
        }
        conn.close();
        return questions;
    }

    public static void deleteCategory(int category) throws SQLException {
        Connection conn = SQLConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM faq_categories WHERE id = ?");
        stmt.setInt(1, category);
        stmt.execute();
        conn.close();
    }

    public static void deleteQuestion(int question) throws SQLException {
        Connection conn = SQLConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM faq WHERE id = ?");
        stmt.setInt(1, question);
        stmt.execute();
        conn.close();
    }
}
