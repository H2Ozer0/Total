package dao;

import entity.User;
import server.UserServer;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {

    private static final String JDBC_URL = "jdbc:mysql://223.2.20.14:3306/albumdb?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "album";
    private static final String PASSWORD = "StrongPassword123!";

    private Connection connection;
    private static final String TABLE_NAME = "`User`";
    private static final String COLUMN_USER_ID = "`UserID`";
    private static final String COLUMN_USERNAME = "`Username`";
    private static final String COLUMN_PASSWORD = "`Password`";
    private static final String COLUMN_EMAIL = "`Email`";
    private static final String COLUMN_IS_ADMIN = "`IsAdmin`";
    private static final String COLUMN_DESCRIPTION = "`Description`";


    public UserDAO() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            //connection.setAutoCommit(false);
            System.out.println("数据库连接已建立。");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("无法连接到数据库: " + e.getMessage());
        }
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getInt("UserID"));
        user.setUsername(resultSet.getString("Username"));
        user.setPassword(resultSet.getString("Password"));
        user.setEmail(resultSet.getString("Email"));
        user.setAdmin(resultSet.getBoolean("IsAdmin"));
        user.setDescription(resultSet.getString("Description"));
        return user;
    }


    public void insertUser(User user) {
        try {
            if (isUsernameUnique(user.getUsername())) {
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
                                user.setUserId(generatedUserId);
                            }
                        }
                    }

                    connection.commit();
                }
            } else {
                System.out.println("用户名已存在，无法插入用户。");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateUsername(int userId, String newUsername) {
        try {
            if (isUsernameUnique(newUsername)) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + TABLE_NAME + " SET " + COLUMN_USERNAME + " = ? WHERE " + COLUMN_USER_ID + " = ?")) {
                    preparedStatement.setString(1, newUsername);
                    preparedStatement.setInt(2, userId);

                    preparedStatement.executeUpdate();
                    return true;
                }
            } else {
                System.out.println("新用户名已存在，无法更新用户名。");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 新增方法，检查用户名是否唯一，若用户名不存在返回1，已经存在返回0
    public boolean isUsernameUnique(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ?")) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public User getUserById(int userId) {
        User user = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ?")) {
            preparedStatement.setInt(1, userId);

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

    public User getUserByUsername(String username) {
        User user = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ?")) {
            preparedStatement.setString(1, username);

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
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + TABLE_NAME + " SET " + COLUMN_USERNAME + " = ?, " + COLUMN_PASSWORD + " = ?, " + COLUMN_EMAIL + " = ?, " + COLUMN_IS_ADMIN + " = ?, " + COLUMN_DESCRIPTION + " = ? WHERE " + COLUMN_USER_ID + " = ?")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setBoolean(4, user.isAdmin());
            preparedStatement.setString(5, user.getDescription());
            preparedStatement.setInt(6, user.getUserId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteUserWithRelatedData(int userId) {
        try {
            // 开启事务
            connection.setAutoCommit(false);

            // 删除用户的喜欢记录
            deleteLikesByUserId(userId);

            // 删除用户的收藏记录
            deleteFavoritesByUserId(userId);

            // 删除用户的评论记录
            deleteCommentsByUserId(userId);

            // 删除用户创建的相册及相关数据
            deleteAlbumsByCreatorId(userId);

            // 删除用户的好友关系
            deleteFriendshipsByUserId(userId);

            // 最后删除用户本身
            if (deleteUser(userId)) {
                // 提交事务
                connection.commit();
                return true;
            } else {
                // 回滚事务
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // 出现异常时回滚事务
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            return false;
        } finally {
            try {
                // 恢复自动提交状态
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 删除用户创建的相册及相关数据
    private void deleteAlbumsByCreatorId(int userId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT AlbumID FROM Album WHERE CreatorID = ?")) {
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int albumId = resultSet.getInt("AlbumID");
                    // 删除相册的喜欢记录
                    deleteLikesByAlbumId(albumId);
                    // 删除相册的收藏记录
                    deleteFavoritesByAlbumId(albumId);
                    // 删除相册的评论记录
                    deleteCommentsByAlbumId(albumId);
                    // 最后删除相册本身
                    deleteAlbum(albumId);
                }
            }
        }
    }

    // 删除用户
    private boolean deleteUser(int userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ?")) {
            preparedStatement.setInt(1, userId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除用户的喜欢记录
    private void deleteLikesByUserId(int userId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM LikeTable WHERE UserID = ?")) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        }
    }

    // 删除用户的收藏记录
    private void deleteFavoritesByUserId(int userId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Favorite WHERE UserID = ?")) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        }
    }

    // 删除用户的评论记录
    private void deleteCommentsByUserId(int userId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Comment WHERE CommenterID = ?")) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        }
    }

    // 删除用户的好友关系
    private void deleteFriendshipsByUserId(int userId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Friendship WHERE UserID1 = ? OR UserID2 = ?")) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        }
    }

    // 删除相册
    private void deleteAlbum(int albumId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Album WHERE AlbumID = ?")) {
            preparedStatement.setInt(1, albumId);
            preparedStatement.executeUpdate();
        }
    }

    // 删除相册的喜欢记录
    private void deleteLikesByAlbumId(int albumId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM LikeTable WHERE AlbumID = ?")) {
            preparedStatement.setInt(1, albumId);
            preparedStatement.executeUpdate();
        }
    }

    // 删除相册的收藏记录
    private void deleteFavoritesByAlbumId(int albumId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Favorite WHERE AlbumID = ?")) {
            preparedStatement.setInt(1, albumId);
            preparedStatement.executeUpdate();
        }
    }

    // 删除相册的评论记录
    private void deleteCommentsByAlbumId(int albumId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Comment WHERE AlbumID = ?")) {
            preparedStatement.setInt(1, albumId);
            preparedStatement.executeUpdate();
        }
    }


    public boolean updatePassword(int userId, String newPassword) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + TABLE_NAME + " SET " + COLUMN_PASSWORD + " = ? WHERE " + COLUMN_USER_ID + " = ?")) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userId);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEmail(int userId, String newEmail) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + TABLE_NAME + " SET " + COLUMN_EMAIL + " = ? WHERE " + COLUMN_USER_ID + " = ?")) {
            preparedStatement.setString(1, newEmail);
            preparedStatement.setInt(2, userId);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateDescription(int userId, String newDescription) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + TABLE_NAME + " SET " + COLUMN_DESCRIPTION + " = ? WHERE " + COLUMN_USER_ID + " = ?")) {
            preparedStatement.setString(1, newDescription);
            preparedStatement.setInt(2, userId);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //获取除自己以外所有用户
    public List<User> getAllUsersExceptCurrentUser(int currentUserId) {
        List<User> users = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " != ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, currentUserId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        User user = mapResultSetToUser(resultSet);
                        users.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录查询异常日志
            System.err.println("获取除当前用户以外所有用户时发生异常: " + e.getMessage());
        }
        return users;
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
//        UserDAO userDAO = new UserDAO();
//
//        // 插入用户记录
//        User newUser = new User("john111", "password123", "john@example.com", false, "normal user");
//        userDAO.insertUser(newUser);
//
//        // 获取生成的自增主键值，如果需要
//        int generatedUserID = newUser.getUserId();
//
//        // 根据用户ID查询用户
//        User retrievedUser = userDAO.getUserById(generatedUserID);
//        System.out.println("查询到的用户：" + retrievedUser);
//
//        // 通过用户名查询用户
//        User retrievedUserByUsername = userDAO.getUserByUsername("john_doe");
//        System.out.println("通过用户名查询到的用户：" + retrievedUserByUsername);
//
//        // 更新用户的用户名
//        userDAO.updateUsername(generatedUserID, "john_doe_updated");
//
//        // 更新用户的密码
//        userDAO.updatePassword(generatedUserID, "new_password123");
//
//        // 更新用户的邮箱
//        userDAO.updateEmail(generatedUserID, "john_updated@example.com");
//
//        // 查询更新后的用户信息
//        User updatedUser = userDAO.getUserById(generatedUserID);
//        System.out.println("更新后的用户：" + updatedUser);
//
//        // 删除用户
//        userDAO.deleteUser(generatedUserID);
//
//        // 关闭数据库连接
//        userDAO.closeConnection();

        UserServer u=new UserServer();
       // u.login("kkk","123456");
        u.editEmail(1,"njnu.com");
        u.editPassword(2,"123123");
    }
}
