package Model.DAO;

import java.sql.*;

public class Database {
	private static final String URL = "jdbc:mysql://localhost:3306/LTM";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";
	private Connection connection;
	private PreparedStatement statement; // Khai báo statement ở cấp độ lớp
	private static Database instance;

	private Database() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("MySQL Driver not found: " + e.getMessage(), e);
		} catch (SQLException e) {
			throw new RuntimeException("Error connecting to database: " + e.getMessage(), e);
		}
	}

	// Đảm bảo Singleton thread-safe
	public static synchronized Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}

	// Thực thi câu lệnh SELECT
	public ResultSet executeQuery(String sql, Object... params) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = prepareStatement(sql, params);
			resultSet = statement.executeQuery();
			return resultSet;
		} catch (SQLException e) {
			throw new SQLException("Error executing query: " + e.getMessage(), e);
		}
	}
//    public ResultSet getGeneratedKeys() throws SQLException {
//        return statement.getGeneratedKeys();
//    }

	// Thực thi câu lệnh INSERT, UPDATE, DELETE
	public int executeUpdate(String sql, Object... params) throws SQLException {
		try (PreparedStatement statement = prepareStatement(sql, params)) {
			return statement.executeUpdate();
		}
	}

	// Chuẩn bị câu lệnh SQL với tham số
	PreparedStatement prepareStatement(String sql, Object... params) throws SQLException {
		statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // Gán giá trị cho statement ở
																						// cấp độ lớp
		for (int i = 0; i < params.length; i++) {
			statement.setObject(i + 1, params[i]);
		}
		return statement;
	}

	// Đóng kết nối
	public void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.err.println("Failed to close connection: " + e.getMessage());
			}
		}
	}

	public Connection getConnection() {
		return connection;
	}
}