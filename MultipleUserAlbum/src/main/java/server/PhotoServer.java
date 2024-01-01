package server;

import entity.DataResult;
import entity.Photo;
import dao.PhotoDAO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

@Service
public class PhotoServer {

    private final String UPLOAD_PATH = "\\upload\\photos";  // 上传照片的基础目录
    private final String AVATAR_PATH = "/upload/avatar";  // 用户头像的基础目录
    private final String ALBUM_COVER_PATH = "/upload/album_covers";  // 相册封面的基础目录

    private final PhotoDAO photoDAO;

    public PhotoServer(PhotoDAO photoDAO) {
        this.photoDAO = photoDAO;
    }

    // 上传照片
    public DataResult uploadPhoto(MultipartFile file, int userId, int albumId,String absolutepath, String title) {
        try {
            String userUploadPath = UPLOAD_PATH + "\\" + userId + "\\albums\\" + albumId;
            String fileName = generateFileName(file.getOriginalFilename());
            String filePath = absolutepath+'\\'+userUploadPath + "\\" + fileName;

            // 确保用户和相册目录存在
            File userUploadDir = new File(absolutepath+userUploadPath);
            if (!userUploadDir.exists()) {
                userUploadDir.mkdirs();
            }

            // 保存文件
            File photoFile = new File(filePath);
            System.out.println("路径为"+filePath);
            file.transferTo(photoFile);
            // 获取当前时间
            Timestamp uploadTime = new Timestamp(System.currentTimeMillis());
            // 创建照片对象
            Photo photo = new Photo(albumId, title,filePath, uploadTime, false);
            photoDAO.insertPhoto(photo);

            return DataResult.success("照片上传成功", photo);
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.fail("照片上传失败");
        }
    }

    // 获取一个相册的所有照片
    public DataResult getPhotosInAlbum(int albumId) {
        List<Photo> photos = photoDAO.getPhotosInAlbum(albumId);
        return DataResult.success("查询相册照片成功", photos);
    }

    // 获取照片路径
    public DataResult getPhotoPath(int photoId) {
        Photo photo = photoDAO.getPhotoByID(photoId);
        if (photo != null) {
            return DataResult.success("获取照片路径成功", photo.getPath());
        } else {
            return DataResult.fail("照片不存在");
        }
    }
    // 上传用户头像
    public DataResult uploadAvatar(MultipartFile file, int userId) {
        try {
            String fileName = userId + ".jpg";
            String filePath = AVATAR_PATH + "/" + fileName;

            // 保存文件
            File avatarFile = new File(filePath);
            file.transferTo(avatarFile);

            return DataResult.success("头像上传成功", filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.fail("头像上传失败");
        }
    }

    // 获取用户头像
    public DataResult getAvatar(int userId) {
        String fileName = userId + ".jpg";
        String avatarPath = AVATAR_PATH + "/" + fileName;

        File avatarFile = new File(avatarPath);
        if (avatarFile.exists()) {
            return DataResult.success("获取用户头像成功", avatarPath);
        } else {
            return DataResult.fail("用户头像不存在");
        }
    }

    // 上传相册封面
    public DataResult uploadAlbumCover(MultipartFile file, int albumId) {
        try {
            String fileName = albumId + "_cover.jpg";
            String filePath = ALBUM_COVER_PATH + "/" + fileName;

            // 保存文件
            File coverFile = new File(filePath);
            file.transferTo(coverFile);

            return DataResult.success("相册封面上传成功", filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.fail("相册封面上传失败");
        }
    }

    // 获取相册封面
    public DataResult getAlbumCover(int albumId) {
        String fileName = albumId + "_cover.jpg";
        String coverPath = ALBUM_COVER_PATH + "/" + fileName;

        File coverFile = new File(coverPath);
        if (coverFile.exists()) {
            return DataResult.success("获取相册封面成功", coverPath);
        } else {
            return DataResult.fail("相册封面不存在");
        }
    }

    // 根据照片ID删除照片
    public DataResult deletePhoto(int photoId) {
        Photo photo = photoDAO.getPhotoByID(photoId);
        if (photo != null) {
            // 在数据库中软删除照片记录
            photoDAO.deletePhoto(photoId);

            // 删除照片文件
            File photoFile = new File(photo.getPath());
            if (photoFile.exists()) {
                photoFile.delete();
            }

            return DataResult.success("删除照片成功", null);
        } else {
            return DataResult.fail("照片不存在");
        }
    }

    // 获取一个相册的所有照片路径
    public DataResult getAlbumPhotoPaths(int albumId) {
        List<String> photoPaths = photoDAO.getAllPhotoPathsInAlbum(albumId);
        return DataResult.success("获取相册照片路径成功", photoPaths);
    }

    // 生成照片文件名
    private String generateFileName(String originalFileName) {
        // 省略具体的文件名生成逻辑
        // 这里简单地拼接时间戳和原始文件名
        long timestamp = System.currentTimeMillis();
        return timestamp + "_" + originalFileName;
    }
}
