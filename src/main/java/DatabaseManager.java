import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private final String url = "jdbc:sqlite:vault.db";

    public DatabaseManager() {
        createTableIfNotExists();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS credentials (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "site TEXT NOT NULL," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "notes TEXT);";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("DB Initialization Error: " + e.getMessage());
        }
    }

    public void addCredential(Credential c) throws SQLException {
        String sql = "INSERT INTO credentials(site, username, password, notes) VALUES(?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getSite());
            pstmt.setString(2, c.getUsername());
            pstmt.setString(3, c.getPassword());
            pstmt.setString(4, c.getNotes());
            pstmt.executeUpdate();
        }
    }

    public List<Credential> getAllCredentials() throws SQLException {
        List<Credential> list = new ArrayList<>();
        String sql = "SELECT * FROM credentials";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Credential(
                        rs.getInt("id"),
                        rs.getString("site"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("notes")
                ));
            }
        }
        return list;
    }

    public void updateCredential(Credential c) throws SQLException {
        String sql = "UPDATE credentials SET site=?, username=?, password=?, notes=? WHERE id=?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getSite());
            pstmt.setString(2, c.getUsername());
            pstmt.setString(3, c.getPassword());
            pstmt.setString(4, c.getNotes());
            pstmt.setInt(5, c.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteCredential(int id) throws SQLException {
        String sql = "DELETE FROM credentials WHERE id=?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}