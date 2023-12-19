package server;


import dao.AlbumDAO;
import entity.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.List;



@Service
public class AlbumServer {

    private final AlbumDAO albumDAO;

    @Autowired
    public AlbumServer(AlbumDAO albumDAO) {
        this.albumDAO = albumDAO;
    }

    public void createAlbum(Album album) {
        albumDAO.insertAlbum(album);
    }

    public Album getAlbumByID(int albumID) {
        return albumDAO.getAlbumByID(albumID);
    }

    public List<Album> getPublicAlbumsByName(String albumName) {
        return albumDAO.getPublicAlbumsByName(albumName);
    }

    public int getLikesCount(int albumID) {
        return albumDAO.getLikesCount(albumID);
    }

    public int getFavoritesCount(int albumID) {
        return albumDAO.getFavoritesCount(albumID);
    }

    public Timestamp getCreatedAt(int albumID) {
        return albumDAO.getCreatedAt(albumID);
    }

    public void updateAlbumName(int albumID, String newAlbumName) {
        albumDAO.updateAlbumName(albumID, newAlbumName);
    }

    public void updateAlbumDescription(int albumID, String newDescription) {
        albumDAO.updateAlbumDescription(albumID, newDescription);
    }
}
