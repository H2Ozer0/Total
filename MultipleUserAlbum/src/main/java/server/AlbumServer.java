package server;

import dao.AlbumDAO;
import entity.Album;
import entity.DataResult;
import entity.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

public class AlbumServer {

    // 获取相册中的照片列表
    public static DataResult getPhotosInAlbum(int albumID) {
        AlbumDAO albumDAO = new AlbumDAO();
        List<Photo> photosInAlbum = albumDAO.getPhotosInAlbum(albumID);
        if (photosInAlbum != null) {
            return DataResult.success("get photos in album success", photosInAlbum);
        } else {
            return DataResult.fail("failed to get photos in album");
        }
    }

    // 插入新相册
    public static DataResult insertAlbum(Album album) {
        AlbumDAO albumDAO = new AlbumDAO();
        if (albumDAO.insertAlbum(album)) {
            return DataResult.success("insert album success", null);
        } else {
            return DataResult.fail("failed to insert album");
        }
    }

    // 获取相册点赞数
    public static DataResult getLikesCount(int albumID) {
        AlbumDAO albumDAO = new AlbumDAO();
        int likesCount = albumDAO.getLikesCount(albumID);
        return DataResult.success("get likes count success", likesCount);
    }

    // 获取相册收藏数
    public static DataResult getFavoritesCount(int albumID) {
        AlbumDAO albumDAO = new AlbumDAO();
        int favoritesCount = albumDAO.getFavoritesCount(albumID);
        return DataResult.success("get favorites count success", favoritesCount);
    }

    // 获取相册创建时间
    public static DataResult getCreatedAt(int albumID) {
        AlbumDAO albumDAO = new AlbumDAO();
        Timestamp createdAt = albumDAO.getCreatedAt(albumID);
        return DataResult.success("get created at success", createdAt);
    }

    // 通过相册名称查找公开相册
    public static DataResult getPublicAlbumsByName(String albumName) {
        AlbumDAO albumDAO = new AlbumDAO();
        List<Album> publicAlbums = albumDAO.getPublicAlbumsByName(albumName);
        if (publicAlbums != null) {
            return DataResult.success("get public albums by name success", publicAlbums);
        } else {
            return DataResult.fail("failed to get public albums by name");
        }
    }

    public static DataResult getPublicAlbumsById(int albumID) {
        AlbumDAO albumDAO = new AlbumDAO();
        Album publicAlbum = albumDAO.getAlbumByID(albumID);
        if (publicAlbum != null) {
            return DataResult.success("get public albums by ID success", publicAlbum);
        } else {
            return DataResult.fail("failed to get public albums by ID");
        }
    }

    // 编辑相册名称
    public static DataResult updateAlbumName(int albumID, String newAlbumName) {
        AlbumDAO albumDAO = new AlbumDAO();
        if (albumDAO.updateAlbumName(albumID, newAlbumName)) {
            return DataResult.success("update album name success", null);
        } else {
            return DataResult.fail("failed to update album name");
        }
    }

    // 编辑相册描述
    public static DataResult updateAlbumDescription(int albumID, String newDescription) {
        AlbumDAO albumDAO = new AlbumDAO();
        if (albumDAO.updateAlbumDescription(albumID, newDescription)) {
            return DataResult.success("update album description success", null);
        } else {
            return DataResult.fail("failed to update album description");
        }
    }

    // 查询相册记录根据AlbumID
    public static DataResult getAlbumByID(int albumID) {
        AlbumDAO albumDAO = new AlbumDAO();
        Album album = albumDAO.getAlbumByID(albumID);
        if (album != null) {
            return DataResult.success("get album by ID success", album);
        } else {
            return DataResult.fail("failed to get album by ID");
        }
    }

    // 删除相册
    public static DataResult deleteAlbum(int albumID) {
        AlbumDAO albumDAO = new AlbumDAO();
        if (albumDAO.deleteAlbum(albumID)) {
            return DataResult.success("Delete album successful", null);
        } else {
            return DataResult.fail("Failed to delete album");
        }
    }

    // 获取用户相册列表
    public static DataResult getAlbumsByUserID(int userID) {
        AlbumDAO albumDAO = new AlbumDAO();
        List<Album> userAlbums = albumDAO.getAlbumsByUserID(userID);
        System.out.println(userID+"userID为");
        if (!userAlbums.isEmpty()) {
            System.out.println("找到相册");
            return DataResult.success("get albums by user ID success", userAlbums);
        } else {
            System.out.println("没有相册");
            return DataResult.fail("no albums found for the user");
        }
    }

    // 分享相册
    public static DataResult shareAlbum(int albumID, int userID) {
        AlbumDAO albumDAO = new AlbumDAO();
        if (albumDAO.insertShareRecord(albumID, userID)) {
            return DataResult.success("share album success", null);
        } else {
            return DataResult.fail("failed to share album");
        }
    }

    // 返回用户的好友分享的相册
    public static DataResult getFriendSharedAlbums(int userID) {
        AlbumDAO albumDAO = new AlbumDAO();
        List<Album> friendSharedAlbums = albumDAO.getFriendSharedAlbums(userID);
        return DataResult.success("get friend shared albums success", friendSharedAlbums);
    }

    // 在 AlbumServer 中添加方法
    public static DataResult getCreatorName(int creatorID) {
        AlbumDAO albumDAO = new AlbumDAO();
        String creatorName = albumDAO.getCreatorName(creatorID);
        return DataResult.success("get creator name success", creatorName);
    }

    // 在 AlbumServer 中调用方法更新相册的收藏次数
    public DataResult updateAlbumFavoritesCount(int albumId) {
        AlbumDAO albumDAO = new AlbumDAO();
        try {
            // 获取相册收藏次数
            int favoritesCount = albumDAO.getFavoritesCountByAlbumID(albumId);

            // 更新相册表中的FavoritesCount字段
            boolean updateSuccess = albumDAO.updateAlbumFavoritesCount(albumId, favoritesCount);

            if (updateSuccess) {
                return DataResult.success("更新相册收藏次数成功", null);
            } else {
                return DataResult.fail("更新相册收藏次数失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.fail("更新相册收藏次数失败");
        }
    }

    // 获取所有相册列表
    public DataResult getAllAlbums() {
        AlbumDAO albumDAO = new AlbumDAO();
        try {
            List<Album> allAlbums = albumDAO.getAllAlbums();

            return DataResult.success("获取所有相册列表成功", allAlbums);
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.fail("获取所有相册列表失败");
        }
    }

    // 获取所有公开相册
    public DataResult getAllPublicAlbums() {
        AlbumDAO albumDAO = new AlbumDAO();
        List<Album> publicAlbums = albumDAO.getAllPublicAlbums();
        if (!publicAlbums.isEmpty()) {
            return DataResult.success("获取所有公开相册成功", publicAlbums);
        } else {
            return DataResult.fail("没有公开相册");
        }
    }

    // 关闭数据库连接
    public static DataResult closeConnection() {
        AlbumDAO albumDAO = new AlbumDAO();
        if (albumDAO.closeConnection()) {
            return DataResult.success("close connection success", null);
        } else {
            return DataResult.fail("failed to close connection");
        }
    }
}
