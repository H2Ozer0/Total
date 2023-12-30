package dao;

import entity.Friendship;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FriendshipDAO {

    private static final String URL = "jdbc:mysql://223.2.20.14:3306/albumdb?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "album";
    private static final String PASSWORD = "StrongPassword123!";

    private Connection connection;
    private Timestamp getCurrentTimestamp() {
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
    }
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
    public List<Integer> getAllFriendUserIDs(int userID) {
        List<Integer> friendUserIDs = new ArrayList<>();
        try {
            String query = "SELECT * FROM Friendship WHERE (UserID1 = ? OR UserID2 = ?) AND FriendshipStatus = 'Accepted'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userID);
                preparedStatement.setInt(2, userID);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // 根据情况选择UserID1或UserID2
                        int friendUserID = (resultSet.getInt("UserID1") == userID) ? resultSet.getInt("UserID2") : resultSet.getInt("UserID1");
                        friendUserIDs.add(friendUserID);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录查询异常日志
            System.err.println("查询用户的所有好友时发生异常: " + e.getMessage());
        }
        return friendUserIDs;
    }

    public boolean deleteFriendship(int userID1, int userID2) {
        try {
            String query = "DELETE FROM Friendship WHERE " +
                    "((UserID1 = ? AND UserID2 = ?) OR (UserID1 = ? AND UserID2 = ?)) " +
                    "AND FriendshipStatus = 'Accepted'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userID1);
                preparedStatement.setInt(2, userID2);
                preparedStatement.setInt(3, userID2);
                preparedStatement.setInt(4, userID1);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录删除异常日志
            System.err.println("删除好友关系时发生异常: " + e.getMessage());
            return false;
        }
    }
    //发送好友申请
    public boolean sendFriendRequest(int senderID, int receiverID) {
        try {
            // 检查是否已存在相同的好友关系记录
            if (!checkDuplicateFriendship(new Friendship(senderID, receiverID, "Pending", getCurrentTimestamp()))) {
                String query = "INSERT INTO Friendship (UserID1, UserID2, FriendshipStatus, CreatedAt) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setInt(1, senderID);
                    preparedStatement.setInt(2, receiverID);
                    preparedStatement.setString(3, "Pending");
                    preparedStatement.setTimestamp(4, getCurrentTimestamp());

                    preparedStatement.executeUpdate();
                    return true;
                }
            } else {
                System.out.println("Friendship record with the same UserID1 and UserID2 already exists.");
                // 可以选择抛出异常或进行其他处理，例如记录日志
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录异常日志
            System.err.println("发送好友请求时发生异常: " + e.getMessage());
            return false;
        }
    }
//拒绝好友申请并直接删除这条数据
    public boolean rejectFriendRequest(int senderID,int receiverID) {
        try {
            String query = "DELETE FROM Friendship WHERE " +
                    "UserID1 = ? AND UserID2 = ? AND FriendshipStatus = 'Pending'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, senderID);
                preparedStatement.setInt(2, receiverID);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录异常日志
            System.err.println("拒绝好友请求时发生异常: " + e.getMessage());
            return false;
        }
    }
//获得好友待接受列表
    public List<Friendship> getFriendRequestsToUser(int userID) {
        List<Friendship> friendRequests = new ArrayList<>();

        try {
            String query = "SELECT * FROM Friendship WHERE UserID2 = ? AND FriendshipStatus = 'Pending'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userID);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Friendship friendship = mapResultSetToFriendship(resultSet);
                        friendRequests.add(friendship);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("查询待接受好友请求时发生异常: " + e.getMessage());
        }

        return friendRequests;
    }
    // 检查是否已经是好友
    public boolean areFriends(int userID1, int userID2) {
        try {
            String query = "SELECT COUNT(*) FROM Friendship WHERE (UserID1 = ? AND UserID2 = ? AND FriendshipStatus = 'Accepted') OR (UserID1 = ? AND UserID2 = ? AND FriendshipStatus = 'Accepted')";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userID1);
                preparedStatement.setInt(2, userID2);
                preparedStatement.setInt(3, userID2);
                preparedStatement.setInt(4, userID1);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("检查是否已经是好友时发生异常: " + e.getMessage());
        }
        return false;
    }

    // 检查是否已经有未处理的好友申请
    public boolean hasPendingFriendRequest(int userID1, int userID2) {
        try {
            String query = "SELECT COUNT(*) FROM Friendship WHERE (UserID1 = ? AND UserID2 = ? AND FriendshipStatus = 'Pending') OR (UserID1 = ? AND UserID2 = ? AND FriendshipStatus = 'Pending')";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userID1);
                preparedStatement.setInt(2, userID2);
                preparedStatement.setInt(3, userID2);
                preparedStatement.setInt(4, userID1);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("检查是否已经有未处理的好友申请时发生异常: " + e.getMessage());
        }
        return false;
    }
    // 获取所有好友的用户ID
    public List<Integer> getAllFriends(int userId) {
        List<Integer> friendIds = new ArrayList<>();

        try {
            String query = "SELECT UserID2 FROM Friendship WHERE UserID1 = ? AND FriendshipStatus = 'Accepted' " +
                    "UNION " +
                    "SELECT UserID1 FROM Friendship WHERE UserID2 = ? AND FriendshipStatus = 'Accepted'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        friendIds.add(resultSet.getInt("UserID2"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("获取好友列表时发生异常: " + e.getMessage());
        }

        return friendIds;
    }
    public static void main(String[] args) {
        FriendshipDAO friendshipDAO = new FriendshipDAO();
        // 发送好友请求
        boolean sent = friendshipDAO.sendFriendRequest(2, 1);
        if (sent) {
            System.out.println("好友请求已发送");
        } else {
            System.out.println("发送好友请求失败");
        }
        boolean sent2 = friendshipDAO.sendFriendRequest(2, 3);
        if (sent) {
            System.out.println("好友请求已发送");
        } else {
            System.out.println("发送好友请求失败");
        }
        // 假设用户ID为1，查询该用户的待接受好友请求
        int userID = 1;
        List<Friendship> friendRequests = friendshipDAO.getFriendRequestsToUser(userID);

        // 输出当前待接受好友请求
        System.out.println("当前待接受好友请求:");
        for (Friendship request : friendRequests) {
            System.out.println(request);
        }




        // 插入好友关系记录
        Friendship newFriendship = new Friendship(1, 3, "Pending", Timestamp.valueOf("2023-01-01 12:00:00"));
        friendshipDAO.insertFriendship(newFriendship);

        // 根据好友关系ID查询好友关系记录
        Friendship retrievedFriendship = friendshipDAO.getFriendshipByID(newFriendship.getFriendshipID());
        System.out.println("Retrieved Friendship: " + retrievedFriendship);

        // 关闭数据库连接
        friendshipDAO.closeConnection();
    }
//    @Bean
//    public FriendshipDAO friendshipDAO() {
//        return new FriendshipDAO();
//    }
}
