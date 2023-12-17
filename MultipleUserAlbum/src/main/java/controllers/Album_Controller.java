package controllers;

import entity.Album;
import dao.AlbumDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class Album_Controller {

    private final AlbumDAO albumDAO;

    @Autowired
    public Album_Controller(AlbumDAO albumDAO) {
        this.albumDAO = albumDAO;
    }

    @PostMapping("/create")
    public String createAlbum(@RequestParam String albumName,
                              @RequestParam String description,
                              @RequestParam boolean isPublic,
                              @RequestParam int creatorID) {
        Album newAlbum = new Album(albumName, description, null, isPublic, false, 0, 0, creatorID);
        albumDAO.insertAlbum(newAlbum);
        return "Album created successfully. ID: " + newAlbum.getAlbumID();
    }

    @GetMapping("/{albumID}")
    public Album getAlbumById(@PathVariable int albumID) {
        return albumDAO.getAlbumByID(albumID);
    }

    @GetMapping("/likesCount")
    public int getLikesCount(@RequestParam int albumID) {
        return albumDAO.getLikesCount(albumID);
    }

    @GetMapping("/favoritesCount")
    public int getFavoritesCount(@RequestParam int albumID) {
        return albumDAO.getFavoritesCount(albumID);
    }

    @GetMapping("/createdAt")
    public String getCreatedAt(@RequestParam int albumID) {
        return albumDAO.getCreatedAt(albumID).toString();
    }

    @GetMapping("/publicAlbums")
    public List<Album> getPublicAlbumsByName(@RequestParam String albumName) {
        return albumDAO.getPublicAlbumsByName(albumName);
    }

    @PutMapping("/updateName/{albumID}")
    public String updateAlbumName(@PathVariable int albumID,
                                  @RequestParam String newAlbumName) {
        albumDAO.updateAlbumName(albumID, newAlbumName);
        return "Album name updated successfully.";
    }

    @PutMapping("/updateDescription/{albumID}")
    public String updateAlbumDescription(@PathVariable int albumID,
                                         @RequestParam String newDescription) {
        albumDAO.updateAlbumDescription(albumID, newDescription);
        return "Album description updated successfully.";
    }
}



