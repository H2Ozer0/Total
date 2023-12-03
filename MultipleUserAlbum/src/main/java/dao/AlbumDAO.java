package dao;

import entity.Album;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/albumdb?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "18915513655Qq";

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
    public void insertAlbum(Album album) {
        try {
            String query = "INSERT INTO album (AlbumID, AlbumName, Description, CreatedAt, IsPublic, IsDeleted, FavoritesCount, LikesCount, CreatorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

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

            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录插入异常日志
            System.err.println("插入相册记录时发生异常: " + e.getMessage());
        }
    }

    // 根据相册ID查询相册
    public Album getAlbumByID(String albumID) {
        Album album = null;
        try {
            String query = "SELECT * FROM album WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, albumID);
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

    // 查询所有相册
    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();
        try {
            String query = "SELECT * FROM album";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Album album = mapResultSetToAlbum(resultSet);
                        albums.add(album);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录查询异常日志
            System.err.println("查询所有相册时发生异常: " + e.getMessage());
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
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, albumID);
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
    public int getFavoritesCount(String albumID) {
        int favoritesCount = 0;
        try {
            String query = "SELECT FavoritesCount FROM album WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, albumID);
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
    public Timestamp getCreatedAt(String albumID) {
        Timestamp createdAt = null;
        try {
            String query = "SELECT CreatedAt FROM album WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, albumID);
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
            String query = "SELECT * FROM album WHERE AlbumName = ? AND IsPublic = true";
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
    public void updateAlbumName(String albumID, String newAlbumName) {
        try {
            String query = "UPDATE album SET AlbumName = ? WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newAlbumName);
                preparedStatement.setString(2, albumID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录更新异常日志
            System.err.println("编辑相册名称时发生异常: " + e.getMessage());
        }
    }

    // 编辑相册描述
    public void updateAlbumDescription(String albumID, String newDescription) {
        try {
            String query = "UPDATE album SET Description = ? WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newDescription);
                preparedStatement.setString(2, albumID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录更新异常日志
            System.err.println("编辑相册描述时发生异常: " + e.getMessage());
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
            // 记录关闭连接异常日志
            System.err.println("关闭数据库连接时发生异常: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        AlbumDAO albumDAO = new AlbumDAO();

        // 插入相册记录
        Album newAlbum = new Album("001", "Summer Vacation", "A trip to the beach", Timestamp.valueOf("2023-01-01 12:00:00"), true, false, 0, 0, "user001");
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


