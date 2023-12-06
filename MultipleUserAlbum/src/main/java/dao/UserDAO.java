package dao;

import entity.User;

import java.sql.*;

public class UserDAO {

    private static final String JDBC_URL = "jdbc:mysql://223.2.20.14:3306/albumdb?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "album";
    private static final String PASSWORD = "StrongPassword123!";

    private Connection connection;
    // 修改表名
    private static final String TABLE_NAME = "`User`";

    // 修改列名
    private static final String COLUMN_USER_ID = "`UserID` INT AUTO_INCREMENT PRIMARY KEY";
    private static final String COLUMN_USERNAME = "`Username`";
    private static final String COLUMN_PASSWORD = "`Password`";
    private static final String COLUMN_EMAIL = "`Email`";
    private static final String COLUMN_IS_ADMIN = "`IsAdmin`";
    private static final String COLUMN_DESCRIPTION = "`Description`";


    public UserDAO() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            connection.setAutoCommit(false);
            System.out.println("数据库连接已建立。");
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录连接异常日志
            System.err.println("无法连接到数据库: " + e.getMessage());
        }
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUsername(resultSet.getString("Username"));
        user.setPassword(resultSet.getString("Password"));
        user.setEmail(resultSet.getString("Email"));
        user.setAdmin(resultSet.getBoolean("IsAdmin"));
        user.setDescription(resultSet.getString("Description"));
        return user;
    }


    public void insertUser(User user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + ", " + COLUMN_EMAIL + ", " + COLUMN_IS_ADMIN + ", " + COLUMN_DESCRIPTION + ") VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setBoolean(4, user.isAdmin());
            preparedStatement.setString(5, user.getDescription());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedUserId = generatedKeys.getInt(1);
                        // 将生成的主键值设置到User对象中
                        user.setUserId(generatedUserId);
                    }
                }
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public User getUserById(String userId) {
        User user = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM User WHERE UserID = ?")) {
            preparedStatement.setString(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = mapResultSetToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }


    public void updateUser(User user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE User SET Username=?, Password=?, Email=?, IsAdmin=?, Description=? WHERE UserID=?")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setBoolean(4, user.isAdmin());
            preparedStatement.setString(5, user.getDescription());

            // 使用 setInt 设置整数类型的 UserID
            preparedStatement.setInt(6, user.getUserId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteUser(String userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM User WHERE UserID=?")) {
            preparedStatement.setString(1, userId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUsername(String userId, String newUsername) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE User SET Username=? WHERE UserID=?")) {
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, userId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(String userId, String newPassword) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE User SET Password=? WHERE UserID=?")) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, userId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmail(String userId, String newEmail) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE User SET Email=? WHERE UserID=?")) {
            preparedStatement.setString(1, newEmail);
            preparedStatement.setString(2, userId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("数据库连接已关闭。");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        // 插入用户记录
        User newUser = new User("john_doe", "password123", "john@example.com", false, "normal user");
        userDAO.insertUser(newUser);

        // 获取生成的自增主键值，如果需要
        int generatedUserID = newUser.getUserId();


// 根据用户ID查询用户
        User retrievedUser = userDAO.getUserById(String.valueOf(generatedUserID));
        System.out.println("查询到的用户：" + retrievedUser);

// 更新用户的用户名
        userDAO.updateUsername(String.valueOf(generatedUserID), "john_doe_updated");

// 更新用户的密码
        userDAO.updatePassword(String.valueOf(generatedUserID), "new_password123");

// 更新用户的邮箱
        userDAO.updateEmail(String.valueOf(generatedUserID), "john_updated@example.com");

// 查询更新后的用户信息
        User updatedUser = userDAO.getUserById(String.valueOf(generatedUserID));
        System.out.println("更新后的用户：" + updatedUser);

// 删除用户
        userDAO.deleteUser(String.valueOf(generatedUserID));


        // 关闭数据库连接
        userDAO.closeConnection();
    }

}
