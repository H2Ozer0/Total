package server;

import dao.PhotoDAO;
import entity.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PhotoServer {

    private final PhotoDAO photoDAO;

    @Autowired
    public PhotoServer(PhotoDAO photoDAO) {
        this.photoDAO = photoDAO;
    }

    public void uploadPhoto(Photo photo, MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                // 获取文件的字节数组
                byte[] fileBytes = file.getBytes();

                // 设置 Photo 对象的图片数据
                photo.setPhotoData(fileBytes);

                // 保存 Photo 对象到数据库
                photoDAO.insertPhoto(photo);
            } else {
                throw new EmptyFileException("上传的文件为空");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<byte[]> getAllPhotoOfAlbum(int AlbumID) {
        return photoDAO.getAllPhotoFilesInAlbum(AlbumID);
    }
    public void uploadPhoto(Photo photo) {
        photoDAO.insertPhoto(photo);
    }

    public List<Photo> getAllPhotos() {
        return photoDAO.getAllPhotos();
    }

    public List<Photo> getPhotosInAlbum(int albumID) {
        return photoDAO.getPhotosInAlbum(albumID);
    }

    public Photo getPhotoByID(int photoID) {
        return photoDAO.getPhotoByID(photoID);
    }

    public List<Photo> getDeletedPhotosInAlbum(int albumID) {
        return photoDAO.getDeletedPhotosInAlbum(albumID);
    }

    public List<Photo> getDeletedPhotosByCreatorID(int creatorID) {
        return photoDAO.getDeletedPhotosByCreatorID(creatorID);
    }

    public List<Photo> getPhotosByTitle(String title) {
        return photoDAO.getPhotosByTitle(title);
    }

    public void updatePhoto(Photo photo) {
        photoDAO.updatePhoto(photo);
    }

    public void deletePhoto(int photoID) {
        photoDAO.deletePhoto(photoID);
    }

    public void undoDeletePhoto(int photoID) {
        photoDAO.undoDeletePhoto(photoID);
    }
}

class EmptyFileException extends RuntimeException {
    public EmptyFileException(String message) {
        super(message);
    }
}