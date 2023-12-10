package dao;

import entity.Friendship;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDAO {

    private static final String URL = "jdbc:mysql://223.2.20.14:3306/albumdb?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "album";
    private static final String PASSWORD = "StrongPassword123!";
    static {
        try {
            // 注册 JDBC 驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load MySQL JDBC driver: com.mysql.cj.jdbc.Driver");
        }
    }
    private Connection connection;

    public FriendshipDAO() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            e.printStackTrace();
            // 记录连接异常日志
            System.err.println("无法连接到数据库: " + e.getMessage());
        }
    }

    public void insertFriendship(Friendship friendship) {
        try {
            // 检查是否已存在相同的好友关系记录
            if (!checkDuplicateFriendship(friendship)) {
                String query = "INSERT INTO Friendship (UserID1, UserID2, FriendshipStatus, CreatedAt) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setInt(1, friendship.getUserID1());
                    preparedStatement.setInt(2, friendship.getUserID2());
                    preparedStatement.setString(3, friendship.getFriendshipStatus());
                    preparedStatement.setTimestamp(4, friendship.getCreatedAt());

                    preparedStatement.executeUpdate();

                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            friendship.setFriendshipID(generatedKeys.getInt(1));
                        }
                    }
                }
            } else {
                System.out.println("Friendship record with the same UserID1 and UserID2 already exists.");
                // 可以选择抛出异常或进行其他处理，例如记录日志
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("插入好友关系记录时发生异常: " + e.getMessage());
        }
    }

    // 检查是否已存在相同的好友关系记录
    private boolean checkDuplicateFriendship(Friendship friendship) {
        boolean duplicateExists = false;
        try {
            String query = "SELECT COUNT(*) FROM Friendship WHERE (UserID1 = ? AND UserID2 = ?) OR (UserID1 = ? AND UserID2 = ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, friendship.getUserID1());
                preparedStatement.setInt(2, friendship.getUserID2());
                preparedStatement.setInt(3, friendship.getUserID2());
                preparedStatement.setInt(4, friendship.getUserID1());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        duplicateExists = count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("检查好友关系记录是否重复时发生异常: " + e.getMessage());
        }
        return duplicateExists;
    }

    // 将 ResultSet 映射到 entity.Friendship 对象
    private Friendship mapResultSetToFriendship(ResultSet resultSet) throws SQLException {
        Friendship friendship = new Friendship();
        friendship.setFriendshipID(resultSet.getInt("FriendshipID"));
        friendship.setUserID1(resultSet.getInt("UserID1"));
        friendship.setUserID2(resultSet.getInt("UserID2"));
        friendship.setFriendshipStatus(resultSet.getString("FriendshipStatus"));
        friendship.setCreatedAt(resultSet.getTimestamp("CreatedAt"));
        return friendship;
    }

    // 根据好友关系ID查询好友关系记录
    public Friendship getFriendshipByID(int friendshipID) {
        Friendship friendship = null;
        try {
            String query = "SELECT * FROM Friendship WHERE FriendshipID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, friendshipID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        friendship = mapResultSetToFriendship(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录查询异常日志
            System.err.println("根据好友关系ID查询好友关系记录时发生异常: " + e.getMessage());
        }
        return friendship;
    }

    // 关闭数据库连接
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录关闭连接异常日志
            System.err.println("关闭数据库连接时发生异常: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        FriendshipDAO friendshipDAO = new FriendshipDAO();

        // 插入好友关系记录
        Friendship newFriendship = new Friendship(1, 2, "Pending", Timestamp.valueOf("2023-01-01 12:00:00"));
        friendshipDAO.insertFriendship(newFriendship);

        // 根据好友关系ID查询好友关系记录
        Friendship retrievedFriendship = friendshipDAO.getFriendshipByID(newFriendship.getFriendshipID());
        System.out.println("Retrieved Friendship: " + retrievedFriendship);

        // 关闭数据库连接
        friendshipDAO.closeConnection();
    }
}
