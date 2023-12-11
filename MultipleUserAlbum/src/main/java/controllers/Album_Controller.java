package controllers;

import dao.AlbumDAO;
import entity.Album;

import java.sql.Timestamp;
import java.util.List;

public class Album_Controller {

    private AlbumDAO albumDAO;

    public Album_Controller() {
        this.albumDAO = new AlbumDAO();
    }

    public void createAlbum(String albumName, String description, Timestamp createdAt, boolean isPublic, boolean isDeleted, int favoritesCount, int likesCount, int creatorID) {
        Album newAlbum = new Album(albumName, description, createdAt, isPublic, isDeleted, favoritesCount, likesCount, creatorID);
        albumDAO.insertAlbum(newAlbum);
        System.out.println("Album created: " + newAlbum);
    }

    public void displayAlbumInfo(int albumID) {
        Album album = albumDAO.getAlbumByID(albumID);
        if (album != null) {
            System.out.println("Album information: " + album);
        } else {
            System.out.println("Album not found with ID: " + albumID);
        }
    }

    public void updateAlbumName(int albumID, String newAlbumName) {
        albumDAO.updateAlbumName(albumID, newAlbumName);
        System.out.println("Album name updated successfully.");
    }

    public void updateAlbumDescription(int albumID, String newDescription) {
        albumDAO.updateAlbumDescription(albumID, newDescription);
        System.out.println("Album description updated successfully.");
    }

    public void displayLikesCount(int albumID) {
        int likesCount = albumDAO.getLikesCount(albumID);
        System.out.println("Likes Count: " + likesCount);
    }

    public void displayFavoritesCount(int albumID) {
        int favoritesCount = albumDAO.getFavoritesCount(albumID);
        System.out.println("Favorites Count: " + favoritesCount);
    }

    public void displayPublicAlbumsByName(String albumName) {
        List<Album> publicAlbums = albumDAO.getPublicAlbumsByName(albumName);
        if (!publicAlbums.isEmpty()) {
            System.out.println("Public Albums: " + publicAlbums);
        } else {
            System.out.println("No public albums found with name: " + albumName);
        }
    }

    public void closeConnection() {
        albumDAO.closeConnection();
        System.out.println("Database connection closed.");
    }

    public static void main(String[] args) {
        Album_Controller album_Controller = new Album_Controller();

        // 示例操作
        album_Controller.createAlbum("Summer Vacation", "A trip to the beach", Timestamp.valueOf("2023-01-01 12:00:00"), true, false, 0, 0, 1);
        album_Controller.displayAlbumInfo(1);
        album_Controller.displayLikesCount(1);
        album_Controller.displayFavoritesCount(1);
        album_Controller.displayPublicAlbumsByName("Summer Vacation");
        album_Controller.updateAlbumName(1, "Vacation Memories");
        album_Controller.updateAlbumDescription(1, "Memories from our amazing vacation");

        // 关闭数据库连接
        album_Controller.closeConnection();
    }
}
