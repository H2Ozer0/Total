package server;

import dao.PhotoDAO;
import entity.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoServer {

    private final PhotoDAO photoDAO;

    @Autowired
    public PhotoServer(PhotoDAO photoDAO) {
        this.photoDAO = photoDAO;
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
