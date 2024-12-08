package Model.DAO;

import java.sql.ResultSet;

public class Authentication_DAO {
    public boolean checkLogin(String username, String password) {
        Database database = Database.getInstance();
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (ResultSet rs = database.executeQuery(query, username, password)) {
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addUser(String username, String password, String email) {
        Database database = Database.getInstance();
        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try {
            database.executeUpdate(query, username, password, email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExistUser(String username) {
        Database database = Database.getInstance();
        String query = "SELECT * FROM users WHERE username = ?";
        try (ResultSet rs = database.executeQuery(query, username)) {
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isExistEmail(String email) {
        Database database = Database.getInstance();
        String query = "SELECT * FROM users WHERE email = ?";
        try (ResultSet rs = database.executeQuery(query, email)) {
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public int getUserId(String username) {
        Database database = Database.getInstance();
        String query = "SELECT id FROM users WHERE username = ?";
        try (ResultSet rs = database.executeQuery(query, username)) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
