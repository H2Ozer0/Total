package dao;

import entity.Like;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LikeDAO {

    private static final String URL = "jdbc:mysql://223.2.20.14:3306/albumdb?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "album";
    private static final String PASSWORD = "StrongPassword123!";

    private Connection connection;

    public LikeDAO() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("无法连接到数据库: " + e.getMessage());
        }
    }
    //插入点赞
    public void insertLike(Like like) {
        try {

            if (!albumExists(like.getAlbumID())) {
                throw new IllegalArgumentException("相册不存在，无法插入点赞记录。AlbumID: " + like.getAlbumID());
            }

            // 检查用户是否已经点赞了相册
            if (userLikedAlbum(like.getUserID(), like.getAlbumID())) {
                throw new IllegalArgumentException("用户已经点赞了相册，无法重复点赞。UserID: " + like.getUserID() + ", AlbumID: " + like.getAlbumID());
            }
            String query = "INSERT INTO LikeTable (AlbumID, UserID, LikeTime) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, like.getAlbumID());
                preparedStatement.setInt(2, like.getUserID());
                preparedStatement.setTimestamp(3, like.getLikeTime());

                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        like.setLikeID(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("插入点赞记录时发生异常: " + e.getMessage());
        }
    }
    //获取一个相册的所有点赞数
    public int getLikeCountByAlbum(int albumID) {
        int likeCount = 0;
        try {
            // Check if AlbumID exists before querying
            if (!albumExists(albumID)) {
                throw new IllegalArgumentException("相册不存在，无法查询点赞记录。AlbumID: " + albumID);
            }

            String query = "SELECT COUNT(*) AS LikeCount FROM LikeTable WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        likeCount = resultSet.getInt("LikeCount");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("查询某相册的点赞数时发生异常: " + e.getMessage());
        }
        return likeCount;
    }


    //用于检查相册是否存在
    private boolean albumExists(int albumID) throws SQLException {
        String query = "SELECT COUNT(*) AS AlbumCount FROM Album WHERE AlbumID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, albumID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt("AlbumCount") > 0;
            }
        }
    }

    // 检查某用户是否已经点赞某相册
    public boolean userLikedAlbum(int userID, int albumID) {
        boolean liked = false;
        try {
            // 检查相册是否存在
            if (!albumExists(albumID)) {
                throw new IllegalArgumentException("相册不存在，无法查询用户点赞记录。AlbumID: " + albumID);
            }

            String query = "SELECT COUNT(*) AS LikeCount FROM LikeTable WHERE AlbumID = ? AND UserID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                preparedStatement.setInt(2, userID);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int likeCount = resultSet.getInt("LikeCount");
                        liked = likeCount > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("检查某用户是否已经点赞某相册时发生异常: " + e.getMessage());
        }
        return liked;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("关闭数据库连接时发生异常: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        LikeDAO likeDAO = new LikeDAO();

        // 插入点赞记录
        Like newLike = new Like(1, 1, Timestamp.valueOf("2023-01-02 14:30:00"));
        likeDAO.insertLike(newLike);

        // 查询某相册的点赞数
        int likeCount = likeDAO.getLikeCountByAlbum(newLike.getAlbumID());
        System.out.println("Album Like Count: " + likeCount);

        // 关闭数据库连接
        likeDAO.closeConnection();
    }
}
