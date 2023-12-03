package dao;

import com.mysql.jdbc.DatabaseMetaData;
import entity.User;

import java.sql.*;

/**
 * 数据访问对象（DAO）用于与数据库交互，执行用户相关的操作。
 */
public class UserDAO {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    private Connection connection;

    // 构造函数中初始化数据库连接
    public UserDAO() {
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 初始化数据库连接
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            System.out.println("Database connection established.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    private DatabaseMetaData DatabaseConnector;


    /**
     * 向数据库插入用户记录。
     * @param user 要插入的用户对象
     */
    public void insertUser(User user) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "INSERT INTO UserTable (UserlD, Username, Password, Email, IsAdmin, Description) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getUserId());
                preparedStatement.setString(2, user.getUsername());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setString(4, user.getEmail());
                preparedStatement.setBoolean(5, user.isAdmin());
                preparedStatement.setString(6, user.getDescription());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将 ResultSet 映射到 User 对象。
     * @param resultSet 包含用户信息的 ResultSet
     * @return 映射后的 User 对象
     * @throws SQLException 如果在映射过程中发生 SQL 异常
     */
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getString("UserlD"));
        user.setUsername(resultSet.getString("Username"));
        user.setPassword(resultSet.getString("Password"));
        user.setEmail(resultSet.getString("Email"));
        user.setAdmin(resultSet.getBoolean("IsAdmin"));
        user.setDescription(resultSet.getString("Description"));
        return user;
    }

    /**
     * 根据用户ID从数据库中检索用户信息。
     * @param userId 要检索的用户ID
     * @return 匹配用户ID的用户对象，如果未找到则为null
     */
    public User getUserById(String userId) {
        User user = null;

        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "SELECT * FROM UserTable WHERE UserlD = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        user = new User();
                        user.setUserId(resultSet.getString("UserlD"));
                        user.setUsername(resultSet.getString("Username"));
                        user.setPassword(resultSet.getString("Password"));
                        user.setEmail(resultSet.getString("Email"));
                        user.setAdmin(resultSet.getBoolean("IsAdmin"));
                        user.setDescription(resultSet.getString("Description"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 更新数据库中的用户信息。
     * @param user 包含更新信息的用户对象
     */
    public void updateUser(User user) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "UPDATE UserTable SET Username=?, Password=?, Email=?, IsAdmin=?, Description=? WHERE UserlD=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setBoolean(4, user.isAdmin());
                preparedStatement.setString(5, user.getDescription());
                preparedStatement.setString(6, user.getUserId());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库中删除指定用户。
     * @param userId 要删除的用户ID
     */
    public void deleteUser(String userId) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "DELETE FROM UserTable WHERE UserlD=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, userId);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新用户的用户名。
     * @param userId 要更新的用户ID
     * @param newUsername 新的用户名
     */
    public void updateUsername(String userId, String newUsername) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "UPDATE UserTable SET Username=? WHERE UserlD=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, userId);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新用户的密码。
     * @param userId 要更新的用户ID
     * @param newPassword 新的密码
     */
    public void updatePassword(String userId, String newPassword) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "UPDATE UserTable SET Password=? WHERE UserlD=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newPassword);
                preparedStatement.setString(2, userId);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新用户的邮箱。
     * @param userId 要更新的用户ID
     * @param newEmail 新的邮箱
     */
    public void updateEmail(String userId, String newEmail) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "UPDATE UserTable SET Email=? WHERE UserlD=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newEmail);
                preparedStatement.setString(2, userId);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭数据库连接。
     */
    public void closeConnection() {
        try {
            // 在这里关闭数据库连接
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        // 插入用户记录
        User newUser = new User("user001", "john_doe", "password123", "john@example.com", false, "A regular user");
        userDAO.insertUser(newUser);

        // 根据用户ID查询用户
        User retrievedUser = userDAO.getUserById("user001");
        System.out.println("Retrieved User: " + retrievedUser);

        // 更新用户的用户名
        userDAO.updateUsername("user001", "john_doe_updated");

        // 更新用户的密码
        userDAO.updatePassword("user001", "new_password123");

        // 更新用户的邮箱
        userDAO.updateEmail("user001", "john_updated@example.com");

        // 查询更新后的用户信息
        User updatedUser = userDAO.getUserById("user001");
        System.out.println("Updated User: " + updatedUser);

        // 删除用户
        userDAO.deleteUser("user001");

        // 关闭数据库连接
        userDAO.closeConnection();
    }


}
