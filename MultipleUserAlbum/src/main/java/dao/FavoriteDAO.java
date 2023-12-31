package dao;

import entity.Album;
import entity.Favorite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {

    private static final String URL = "jdbc:mysql://223.2.20.14:3306/albumdb?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "album";
    private static final String PASSWORD = "StrongPassword123!";
    static {
        try {
            // 注册 JDBC 驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("无法加载 MySQL JDBC 驱动程序: com.mysql.cj.jdbc.Driver");
        }
    }
    private Connection connection;

    public FavoriteDAO() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            e.printStackTrace();
            // 记录连接异常日志
            System.err.println("无法连接到数据库: " + e.getMessage());
        }
    }

    public void insertFavorite(Favorite favorite) {
        try {
            // 检查是否已存在相同的收藏记录
            if (!checkDuplicateFavorite(favorite)) {
                String query = "INSERT INTO Favorite (AlbumID, UserID, FavoriteTime) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setInt(1, favorite.getAlbumID());
                    preparedStatement.setInt(2, favorite.getUserID());
                    preparedStatement.setTimestamp(3, favorite.getFavoriteTime());

                    preparedStatement.executeUpdate();

                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            favorite.setFavoriteID(generatedKeys.getInt(1));
                        }
                    }
                }
            } else {
                System.out.println("Favorite record with the same AlbumID and UserID already exists.");
                // 可以选择抛出异常或进行其他处理，例如记录日志
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("插入收藏记录时发生异常: " + e.getMessage());
        }
    }

    public void deleteFavorite(int userID, int albumID) {
        try {
            String query = "DELETE FROM Favorite WHERE UserID = ? AND AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userID);
                preparedStatement.setInt(2, albumID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected == 0) {
                    //System.out.println("No matching Favorite record found for deletion.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("删除收藏记录时发生异常: " + e.getMessage());
        }
    }
    // 根据用户ID和相册ID判断是否已经收藏该相册
    public boolean isAlbumFavoritedByUser(int userID, int albumID) {
        boolean isFavorited = false;
        try {
            String query = "SELECT COUNT(*) FROM Favorite WHERE AlbumID = ? AND UserID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                preparedStatement.setInt(2, userID);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        isFavorited = count > 0;
                        System.out.println("userID:"+userID+"  albumID:"+albumID+"isFavourite:"+count);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("判断相册是否已被用户收藏时发生异常: " + e.getMessage());
        }
        return isFavorited;
    }

    // 检查是否已存在相同的收藏记录
    private boolean checkDuplicateFavorite(Favorite favorite) {
        boolean duplicateExists = false;
        try {
            String query = "SELECT COUNT(*) FROM Favorite WHERE AlbumID = ? AND UserID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, favorite.getAlbumID());
                preparedStatement.setInt(2, favorite.getUserID());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        duplicateExists = count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("检查收藏记录是否重复时发生异常: " + e.getMessage());
        }
        return duplicateExists;
    }


    // 根据收藏ID查询收藏记录
    public Favorite getFavoriteByID(int favoriteID) {
        Favorite favorite = null;
        try {
            String query = "SELECT * FROM Favorite WHERE FavoriteID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, favoriteID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        favorite = mapResultSetToFavorite(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 记录查询异常日志
            System.err.println("根据收藏ID查询收藏记录时发生异常: " + e.getMessage());
        }
        return favorite;
    }

    // 将 ResultSet 映射到 entity.Favorite 对象
    private Favorite mapResultSetToFavorite(ResultSet resultSet) throws SQLException {
        Favorite favorite = new Favorite();
        favorite.setFavoriteID(resultSet.getInt("FavoriteID"));
        favorite.setAlbumID(resultSet.getInt("AlbumID"));
        favorite.setUserID(resultSet.getInt("UserID"));
        favorite.setFavoriteTime(resultSet.getTimestamp("FavoriteTime"));
        return favorite;
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
    public List<Album> getFavoriteAlbumsByUser(int userID) {
        List<Album> favoriteAlbums = new ArrayList<>();
        try {
            String query = "SELECT a.* FROM Album a " +
                    "JOIN Favorite f ON a.AlbumID = f.AlbumID " +
                    "WHERE f.UserID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Album album = mapResultSetToAlbum(resultSet);
                        favoriteAlbums.add(album);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to retrieve favorite albums: " + e.getMessage());
        }
        return favoriteAlbums;
    }
    public static void main(String[] args) {
        FavoriteDAO favoriteDAO = new FavoriteDAO();

        // 插入收藏记录
        Favorite newFavorite = new Favorite(2, 1, Timestamp.valueOf("2023-01-01 12:00:00"));
        favoriteDAO.insertFavorite(newFavorite);

        // 根据收藏ID查询收藏记录
        Favorite retrievedFavorite = favoriteDAO.getFavoriteByID(newFavorite.getFavoriteID());
        System.out.println("Retrieved Favorite: " + retrievedFavorite);

        // 关闭数据库连接
        favoriteDAO.closeConnection();
    }
}