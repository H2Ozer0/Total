package dao;

import entity.Album;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    private Connection connection;

    public AlbumDAO() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理连接异常
        }
    }

    // 插入相册记录
    public void insertAlbum(Album album) {
        try {
            String query = "INSERT INTO album (AlbumID, AlbumName, Description, CreatedAt, IsPublic, IsDeleted, FavoritesCount, LikesCount, CreatorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, album.getAlbumID());
            preparedStatement.setString(2, album.getAlbumName());
            preparedStatement.setString(3, album.getDescription());
            preparedStatement.setTimestamp(4, album.getCreatedAt());
            preparedStatement.setBoolean(5, album.isPublic());
            preparedStatement.setBoolean(6, album.isDeleted());
            preparedStatement.setInt(7, album.getFavoritesCount());
            preparedStatement.setInt(8, album.getLikesCount());
            preparedStatement.setString(9, album.getCreatorID());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理插入异常
        }
    }

    // 根据相册ID查询相册
    public Album getAlbumByID(String albumID) {
        Album album = null;
        try {
            String query = "SELECT * FROM album WHERE AlbumID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, albumID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                album = mapResultSetToAlbum(resultSet);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理查询异常
        }
        return album;
    }

    // 查询所有相册
    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();
        try {
            String query = "SELECT * FROM album";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Album album = mapResultSetToAlbum(resultSet);
                albums.add(album);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理查询异常
        }
        return albums;
    }

    // 将 ResultSet 映射到 entity.Album 对象
    private Album mapResultSetToAlbum(ResultSet resultSet) throws SQLException {
        Album album = new Album();
        album.setAlbumID(resultSet.getString("AlbumID"));
        album.setAlbumName(resultSet.getString("AlbumName"));
        album.setDescription(resultSet.getString("Description"));
        album.setCreatedAt(resultSet.getTimestamp("CreatedAt"));
        album.setPublic(resultSet.getBoolean("IsPublic"));
        album.setDeleted(resultSet.getBoolean("IsDeleted"));
        album.setFavoritesCount(resultSet.getInt("FavoritesCount"));
        album.setLikesCount(resultSet.getInt("LikesCount"));
        album.setCreatorID(resultSet.getString("CreatorID"));
        return album;
    }

    // 查询相册点赞数
    public int getLikesCount(String albumID) {
        int likesCount = 0;
        try {
            String query = "SELECT LikesCount FROM album WHERE AlbumID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, albumID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                likesCount = resultSet.getInt("LikesCount");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理查询异常
        }
        return likesCount;
    }

    // 查询相册收藏数
    public int getFavoritesCount(String albumID) {
        int favoritesCount = 0;
        try {
            String query = "SELECT FavoritesCount FROM album WHERE AlbumID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, albumID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                favoritesCount = resultSet.getInt("FavoritesCount");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理查询异常
        }
        return favoritesCount;
    }

    // 查询相册创建时间
    public Timestamp getCreatedAt(String albumID) {
        Timestamp createdAt = null;
        try {
            String query = "SELECT CreatedAt FROM album WHERE AlbumID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, albumID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                createdAt = resultSet.getTimestamp("CreatedAt");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理查询异常
        }
        return createdAt;
    }

    // 通过相册名称查找公开相册
    public List<Album> getPublicAlbumsByName(String albumName) {
        List<Album> publicAlbums = new ArrayList<>();
        try {
            String query = "SELECT * FROM album WHERE AlbumName = ? AND IsPublic = true";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, albumName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Album album = mapResultSetToAlbum(resultSet);
                publicAlbums.add(album);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理查询异常
        }
        return publicAlbums;
    }

    // 编辑相册名称
    public void updateAlbumName(String albumID, String newAlbumName) {
        try {
            String query = "UPDATE album SET AlbumName = ? WHERE AlbumID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newAlbumName);
            preparedStatement.setString(2, albumID);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理更新异常
        }
    }

    // 编辑相册描述
    public void updateAlbumDescription(String albumID, String newDescription) {
        try {
            String query = "UPDATE album SET Description = ? WHERE AlbumID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newDescription);
            preparedStatement.setString(2, albumID);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理更新异常
        }
    }

    // 关闭数据库连接
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理关闭连接异常
        }
    }

    public static void main(String[] args) {
        AlbumDAO albumDAO = new AlbumDAO();

        // 插入相册记录
        Album newAlbum = new Album("001", "Summer Vacation", "A trip to the beach", Timestamp.valueOf("2023-01-01 12:00:00"), true, false, 0, 0, "user123");
        albumDAO.insertAlbum(newAlbum);

        // 根据相册ID查询相册
        Album retrievedAlbum = albumDAO.getAlbumByID("001");
        System.out.println("Retrieved entity.Album: " + retrievedAlbum);

        // 查询相册点赞数
        int likesCount = albumDAO.getLikesCount("001");
        System.out.println("Likes Count: " + likesCount);

        // 查询相册收藏数
        int favoritesCount = albumDAO.getFavoritesCount("001");
        System.out.println("Favorites Count: " + favoritesCount);

        // 查询相册创建时间
        Timestamp createdAt = albumDAO.getCreatedAt("001");
        System.out.println("Created At: " + createdAt);

        // 通过相册名称查找公开相册
        List<Album> publicAlbums = albumDAO.getPublicAlbumsByName("Summer Vacation");
        System.out.println("Public Albums: " + publicAlbums);

        // 编辑相册名称
        albumDAO.updateAlbumName("001", "Vacation Memories");

        // 编辑相册描述
        albumDAO.updateAlbumDescription("001", "Memories from our amazing vacation");

        // 关闭数据库连接
        albumDAO.closeConnection();
    }
}
