package dao;

import entity.Photo;



import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PhotoDAO {

    private static final String URL = "jdbc:mysql://223.2.20.14:3306/albumdb?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "album";
    private static final String PASSWORD = "StrongPassword123!";

    private Connection connection;

    // 构造函数中初始化数据库连接
    public PhotoDAO() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("无法连接到数据库: " + e.getMessage());
        }
    }

    // 插入照片记录
    public void insertPhoto(Photo photo) {
        try {
            String query = "INSERT INTO Photo (AlbumID, Title, Path, UploadTime, IsDeleted) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, photo.getAlbumID());
                preparedStatement.setString(2, photo.getTitle());
                preparedStatement.setString(3, photo.getPath());
                preparedStatement.setTimestamp(4, photo.getUploadTime());
                preparedStatement.setBoolean(5, photo.isDeleted());

                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        photo.setPhotoID(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("插入照片记录时发生异常: " + e.getMessage());
        }
    }

    // 将 ResultSet 映射到 Photo 对象
    private Photo mapResultSetToPhoto(ResultSet resultSet) throws SQLException {
        Photo photo = new Photo(
                resultSet.getInt("AlbumID"),
                resultSet.getString("Title"),
                resultSet.getString("Path"),
                resultSet.getTimestamp("UploadTime"),
                resultSet.getBoolean("IsDeleted")
        );
        photo.setPhotoID(resultSet.getInt("PhotoID"));
        return photo;
    }

    // 查询所有照片
    public List<Photo> getAllPhotos() {
        List<Photo> photos = new ArrayList<>();
        try {
            String query = "SELECT * FROM Photo WHERE IsDeleted = false";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Photo photo = mapResultSetToPhoto(resultSet);
                    photos.add(photo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("查询所有照片失败，发生数据库异常: " + e.getMessage());
        }
        return photos;
    }
    // 查询一个相册中所有照片
    public List<Photo> getPhotosInAlbum(int albumID) {
        List<Photo> photosInAlbum = new ArrayList<>();
        try {
            String query = "SELECT * FROM Photo WHERE AlbumID = ? AND IsDeleted = false";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Photo photo = mapResultSetToPhoto(resultSet);
                        photosInAlbum.add(photo);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("查询相册中所有照片失败，发生数据库异常: " + e.getMessage());
        }
        return photosInAlbum;
    }

    // 更新照片记录
    public void updatePhoto(Photo photo) {
        try {
            String query = "UPDATE Photo SET AlbumID = ?, Title = ?, Path = ?, UploadTime = ?, IsDeleted = ? WHERE PhotoID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, photo.getAlbumID());
                preparedStatement.setString(2, photo.getTitle());
                preparedStatement.setString(3, photo.getPath());
                preparedStatement.setTimestamp(4, photo.getUploadTime());
                preparedStatement.setBoolean(5, photo.isDeleted());
                preparedStatement.setInt(6, photo.getPhotoID());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("更新照片记录失败，发生数据库异常: " + e.getMessage());
        }
    }

    //照片放入回收站（软删除）
    public void deletePhoto(int photoID) {
        try {
            String query = "UPDATE Photo SET IsDeleted = true WHERE PhotoID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, photoID);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("软删除照片记录失败，发生数据库异常: " + e.getMessage());
        }
    }

    // 撤销删除照片记录
    public void undoDeletePhoto(int photoID) {
        try {
            String query = "UPDATE Photo SET IsDeleted = false WHERE PhotoID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, photoID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("撤销删除照片记录失败，发生数据库异常: " + e.getMessage());
        }
    }

    // 获取一个相册的所有已删除照片
    public List<Photo> getDeletedPhotosInAlbum(int albumID) {
        List<Photo> deletedPhotos = new ArrayList<>();
        try {
            String query = "SELECT * FROM Photo WHERE AlbumID = ? AND IsDeleted = true";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Photo photo = mapResultSetToPhoto(resultSet);
                        deletedPhotos.add(photo);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("获取相册的已删除照片失败，发生数据库异常: " + e.getMessage());
        }
        return deletedPhotos;
    }

    // 根据用户ID获取一个用户所有已删除照片（回收站中的照片）
    public List<Photo> getDeletedPhotosByCreatorID(int creatorID) {
        List<Photo> deletedPhotos = new ArrayList<>();
        try {
            String query = "SELECT * FROM Photo P JOIN Album A ON P.AlbumID = A.AlbumID " +
                    "WHERE A.CreatorID = ? AND P.IsDeleted = true";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, creatorID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Photo photo = mapResultSetToPhoto(resultSet);
                        deletedPhotos.add(photo);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("通过CreatorID获取已删除照片失败，发生数据库异常: " + e.getMessage());
        }
        return deletedPhotos;
    }

    // 根据照片ID查询照片
    public Photo getPhotoByID(int photoID) {
        Photo photo = null;
        try {
            String query = "SELECT * FROM Photo WHERE PhotoID = ? AND IsDeleted = false";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, photoID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        photo = mapResultSetToPhoto(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("根据ID查询照片失败，发生数据库异常: " + e.getMessage());
        }
        return photo;
    }

    // 根据照片名称模糊查询照片
    public List<Photo> getPhotosByTitle(String title) {
        List<Photo> photos = new ArrayList<>();
        try {
            String query = "SELECT * FROM Photo WHERE Title LIKE ? AND IsDeleted = false";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + title + "%");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Photo photo = mapResultSetToPhoto(resultSet);
                        photos.add(photo);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("根据照片名称查询照片失败，发生数据库异常: " + e.getMessage());
        }
        return photos;
    }

    // 获取一个相册的所有照片路径
    public List<String> getAllPhotoPathsInAlbum(int albumID) {
        List<String> photoPaths = new ArrayList<>();
        try {
            String query = "SELECT Path FROM Photo WHERE AlbumID = ? AND IsDeleted = false";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String path = resultSet.getString("Path");
                        photoPaths.add(path);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("获取相册中所有照片路径失败，发生数据库异常: " + e.getMessage());
        }
        return photoPaths;
    }
    // 关闭数据库连接
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

    // 测试 PhotoDAO 类的主方法
    public static void main(String[] args) {
        PhotoDAO photoDAO = new PhotoDAO();

        // 插入照片记录
        Photo newPhoto = new Photo(1, "Sunset2", "/path/to/sunset.jpg", Timestamp.valueOf("2023-01-01 18:00:00"), false);
        photoDAO.insertPhoto(newPhoto);
        // 根据照片名称模糊查询照片
        List<Photo> photosByTitle = photoDAO.getPhotosByTitle("Sunset");
        System.out.println("根据照片名称查询照片：" + photosByTitle);

        // 根据照片ID查询照片
        int photoIDToQuery = newPhoto.getPhotoID();
        Photo queriedPhoto = photoDAO.getPhotoByID(photoIDToQuery);
        System.out.println("根据照片ID查询照片：" + queriedPhoto);

        // 获取一个相册的所有已删除照片
        int albumIDForDeletedPhotos = 1;
        List<Photo> deletedPhotosInAlbum = photoDAO.getDeletedPhotosInAlbum(albumIDForDeletedPhotos);
        System.out.println("相册的已删除照片：" + deletedPhotosInAlbum);

        // 根据用户ID获取一个用户所有已删除照片（回收站中的照片）
        int creatorIDForDeletedPhotos = 1;
        List<Photo> deletedPhotosByCreator = photoDAO.getDeletedPhotosByCreatorID(creatorIDForDeletedPhotos);
        System.out.println("用户的已删除照片：" + deletedPhotosByCreator);

        // 获取一个相册中所有照片
        int albumIDToQuery = 1;
        List<Photo> photosInAlbum = photoDAO.getPhotosInAlbum(albumIDToQuery);
        System.out.println("相册中所有照片：" + photosInAlbum);


        // 查询所有照片
        List<Photo> allPhotos = photoDAO.getAllPhotos();
        System.out.println("所有照片：" + allPhotos);

        // 更新照片记录
        newPhoto.setTitle("Beautiful Sunset2");
        newPhoto.setPath("/path/to/beautiful_sunset2.jpg");
        photoDAO.updatePhoto(newPhoto);

        // 查询更新后的照片
        Photo updatedPhoto = photoDAO.getPhotoByID(newPhoto.getPhotoID());
        System.out.println("更新后的照片：" + updatedPhoto);

        // 删除照片记录
        int photoIDToDelete = newPhoto.getPhotoID();
        photoDAO.deletePhoto(photoIDToDelete);

        // 放入回收站的照片
        Photo deletedPhoto = photoDAO.getPhotoByID(photoIDToDelete);
        System.out.println("放入回收站的照片：" + deletedPhoto);

        // 关闭数据库连接
        photoDAO.closeConnection();
    }
}
