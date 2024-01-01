package dao;

import entity.Album;
import entity.Photo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO {

    private static final String URL = "jdbc:mysql://223.2.20.14:3306/albumdb?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "album";
    private static final String PASSWORD = "StrongPassword123!";

    private Connection connection;


    public AlbumDAO() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            e.printStackTrace();
            // 记录连接异常日志
            System.err.println("无法连接到数据库: " + e.getMessage());
        }
    }

    // 插入相册记录
    public boolean insertAlbum(Album album) {
        try {
            String query = "INSERT INTO Album (AlbumName, Description, CreatedAt, IsPublic, IsDeleted, FavoritesCount, LikesCount, CreatorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, album.getAlbumName());
                preparedStatement.setString(2, album.getDescription());
                preparedStatement.setTimestamp(3, album.getCreatedAt());
                preparedStatement.setBoolean(4, album.isPublic());
                preparedStatement.setBoolean(5, album.isDeleted());
                preparedStatement.setInt(6, album.getFavoritesCount());
                preparedStatement.setInt(7, album.getLikesCount());
                preparedStatement.setInt(8, album.getCreatorID());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            album.setAlbumID(generatedKeys.getInt(1));
                            return true; // 插入成功
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("插入相册记录时发生异常: " + e.getMessage());
        }
        return false; // 插入失败
    }
    // 查询相册创建者ID根据AlbumID
    public int getCreatorIDByAlbumID(int albumID) {
        int creatorID = -1; // Default value indicating failure
        try {
            String query = "SELECT CreatorID FROM Album WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        creatorID = resultSet.getInt("CreatorID");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录查询异常日志
            System.err.println("根据相册ID查询创建者ID时发生异常: " + e.getMessage());
        }
        return creatorID;
    }

    // 将 ResultSet 映射到 entity.Album 对象
    private Album mapResultSetToAlbum(ResultSet resultSet) throws SQLException {
        Album album = new Album();
        album.setAlbumID(resultSet.getInt("AlbumID"));
        album.setAlbumName(resultSet.getString("AlbumName"));
        album.setDescription(resultSet.getString("Description"));
        album.setCreatedAt(resultSet.getTimestamp("CreatedAt"));
        album.setPublic(resultSet.getBoolean("IsPublic"));
        album.setDeleted(resultSet.getBoolean("IsDeleted"));
        album.setFavoritesCount(resultSet.getInt("FavoritesCount"));
        album.setLikesCount(resultSet.getInt("LikesCount"));
        album.setCreatorID(resultSet.getInt("CreatorID"));
        return album;
    }
    public boolean updateAlbumLikesCount(int albumID) {
        try {
            String query = "SELECT COUNT(*) AS LikeCount FROM LikeTable WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int likeCount = resultSet.getInt("LikeCount");

                        // Update LikesCount in the Album table
                        String updateQuery = "UPDATE Album SET LikesCount = ? WHERE AlbumID = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setInt(1, likeCount);
                            updateStatement.setInt(2, albumID);

                            int rowsAffected = updateStatement.executeUpdate();

                            return rowsAffected > 0; // Update successful if rows affected
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("更新相册点赞数时发生异常: " + e.getMessage());
        }
        return false; // Update failed
    }


    // 查询相册点赞数
    public int getLikesCount(int albumID) {
        int likesCount = 0;
        try {
            String query = "SELECT LikesCount FROM Album WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        likesCount = resultSet.getInt("LikesCount");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录查询异常日志
            System.err.println("查询相册点赞数时发生异常: " + e.getMessage());
        }
        return likesCount;
    }

    // 查询相册收藏数
    public int getFavoritesCount(int albumID) {
        int favoritesCount = 0;
        try {
            String query = "SELECT FavoritesCount FROM Album WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        favoritesCount = resultSet.getInt("FavoritesCount");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录查询异常日志
            System.err.println("查询相册收藏数时发生异常: " + e.getMessage());
        }
        return favoritesCount;
    }

    // 查询相册创建时间
    public Timestamp getCreatedAt(int albumID) {
        Timestamp createdAt = null;
        try {
            String query = "SELECT CreatedAt FROM Album WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        createdAt = resultSet.getTimestamp("CreatedAt");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录查询异常日志
            System.err.println("查询相册创建时间时发生异常: " + e.getMessage());
        }
        return createdAt;
    }

    // 通过相册名称查找公开相册
    public List<Album> getPublicAlbumsByName(String albumName) {
        List<Album> publicAlbums = new ArrayList<>();
        try {
            String query = "SELECT * FROM Album WHERE AlbumName = ? AND IsPublic = true";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, albumName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Album album = mapResultSetToAlbum(resultSet);
                        publicAlbums.add(album);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录查询异常日志
            System.err.println("通过相册名称查找公开相册时发生异常: " + e.getMessage());
        }
        return publicAlbums;
    }
    // 编辑相册名称
    public boolean updateAlbumName(int albumID, String newAlbumName) {
        try {
            String query = "UPDATE Album SET AlbumName = ? WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newAlbumName);
                preparedStatement.setInt(2, albumID);

                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0; // 更新成功返回true，否则返回false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("编辑相册名称时发生异常: " + e.getMessage());
        }
        return false; // 更新失败
    }

    // 编辑相册描述
    public boolean updateAlbumDescription(int albumID, String newDescription) {
        try {
            String query = "UPDATE Album SET Description = ? WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newDescription);
                preparedStatement.setInt(2, albumID);

                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0; // 更新成功返回true，否则返回false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("编辑相册描述时发生异常: " + e.getMessage());
        }
        return false; // 更新失败
    }

    // 查询相册记录根据AlbumID
    public Album getAlbumByID(int albumID) {
        Album album = null;
        try {
            String query = "SELECT * FROM Album WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        album = mapResultSetToAlbum(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录查询异常日志
            System.err.println("根据相册ID查询相册时发生异常: " + e.getMessage());
        }
        return album;
    }


    // 查询相册中的照片列表
    public List<Photo> getPhotosInAlbum(int albumID) {
        List<Photo> photosInAlbum = new ArrayList<>();
        try {
            String query = "SELECT * FROM Photo WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Photo photo = new Photo(
                                resultSet.getInt("AlbumID"),
                                resultSet.getString("Title"),
                                resultSet.getString("Path"),
                                resultSet.getTimestamp("UploadTime"),
                                resultSet.getBoolean("IsDeleted")
                        );
                        photo.setPhotoID(resultSet.getInt("PhotoID"));
                        photosInAlbum.add(photo);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录查询异常日志
            System.err.println("查询相册中的照片列表时发生异常: " + e.getMessage());
        }
        return photosInAlbum;
    }
    // 删除相册记录
    public boolean deleteAlbum(int albumID) {
        try {
            String query = "DELETE FROM Album WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);

                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0; // 删除成功返回true，否则返回false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("删除相册时发生异常: " + e.getMessage());
        }
        return false; // 删除失败
    }
    // 获取用户相册列表
    public List<Album> getAlbumsByUserID(int userID) {
        List<Album> userAlbums = new ArrayList<>();
        try {
            String query = "SELECT * FROM Album WHERE CreatorID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Album album = mapResultSetToAlbum(resultSet);
                        userAlbums.add(album);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录查询异常日志
            System.err.println("根据用户ID查询相册列表时发生异常: " + e.getMessage());
        }
        return userAlbums;
    }

    // 分享相册
    public boolean insertShareRecord(int albumID, int userID) {
        try {
            String query = "INSERT INTO ShareRecord (AlbumID, UserID) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                preparedStatement.setInt(2, userID);

                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0; // 插入成功返回true，否则返回false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("插入分享记录时发生异常: " + e.getMessage());
        }
        return false; // 插入失败
    }

    // 返回用户的好友分享的相册列表
    public List<Album> getFriendSharedAlbums(int userID) {
        List<Album> friendSharedAlbums = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT A.* FROM Album A " +
                    "JOIN ShareRecord S ON A.AlbumID = S.AlbumID " +
                    "JOIN Friendship F ON (A.CreatorID = F.UserID1 OR A.CreatorID = F.UserID2) " +
                    "WHERE (F.UserID1 = ? OR F.UserID2 = ?) AND F.FriendshipStatus = 'Accepted' AND A.IsPublic = true";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userID);
                preparedStatement.setInt(2, userID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Album album = mapResultSetToAlbum(resultSet);
                        friendSharedAlbums.add(album);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("获取好友分享的相册列表时发生异常: " + e.getMessage());
        }
        return friendSharedAlbums;
    }

    // 关闭数据库连接
    public boolean closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                return true; // 关闭成功返回true
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("关闭数据库连接时发生异常: " + e.getMessage());
        }
        return false; // 关闭失败
    }

//    public static void main(String[] args) {
//        AlbumDAO albumDAO = new AlbumDAO();
//
//
//        // 插入相册记录
//        Album newAlbum = new Album("Summer Vacation", "A trip to the beach", Timestamp.valueOf("2023-01-01 12:00:00"), true, false, 0, 0, 1);
//        albumDAO.insertAlbum(newAlbum);
//
//        // 根据相册ID查询相册
//        Album retrievedAlbum = albumDAO.getAlbumByID(newAlbum.getAlbumID());
//        System.out.println("Retrieved entity.Album: " + retrievedAlbum);
//
//        // 查询相册点赞数
//        int likesCount = albumDAO.getLikesCount(newAlbum.getAlbumID());
//        System.out.println("Likes Count: " + likesCount);
//
//        // 查询相册收藏数
//        int favoritesCount = albumDAO.getFavoritesCount(newAlbum.getAlbumID());
//        System.out.println("Favorites Count: " + favoritesCount);
//
//        // 查询相册创建时间
//        Timestamp createdAt = albumDAO.getCreatedAt(newAlbum.getAlbumID());
//        System.out.println("Created At: " + createdAt);
//
//        // 通过相册名称查找公开相册
//        List<Album> publicAlbums = albumDAO.getPublicAlbumsByName("Summer Vacation");
//        System.out.println("Public Albums: " + publicAlbums);
//
//        // 编辑相册名称
//        albumDAO.updateAlbumName(newAlbum.getAlbumID(), "Vacation Memories");
//
//        // 编辑相册描述
//        albumDAO.updateAlbumDescription(newAlbum.getAlbumID(), "Memories from our amazing vacation");
//
//        // 关闭数据库连接
//        albumDAO.closeConnection();
//    }
//    @Bean
//    public AlbumDAO albumDAO() {
//        return new AlbumDAO();
//    }
}


