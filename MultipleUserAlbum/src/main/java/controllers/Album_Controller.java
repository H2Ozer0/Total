//package controllers;
//
//import entity.Album;
//import dao.AlbumDAO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/albums")
//public class Album_Controller {
//
//    private final AlbumDAO albumDAO;
//
//    @Autowired
//    public Album_Controller(AlbumDAO albumDAO) {
//        this.albumDAO = albumDAO;
//    }
//
//    @PostMapping("/create")
//    public String createAlbum(@RequestParam String albumName,
//                              @RequestParam String description,
//                              @RequestParam boolean isPublic,
//                              @RequestParam int creatorID) {
//        Album newAlbum = new Album(albumName, description, null, isPublic, false, 0, 0, creatorID);
//        albumDAO.insertAlbum(newAlbum);
//        return "Album created successfully. ID: " + newAlbum.getAlbumID();
//    }
//
//    @GetMapping("/{albumID}")
//    public Album getAlbumById(@PathVariable int albumID) {
//        return albumDAO.getAlbumByID(albumID);
//    }
//
//    @GetMapping("/likesCount")
//    public int getLikesCount(@RequestParam int albumID) {
//        return albumDAO.getLikesCount(albumID);
//    }
//
//    @GetMapping("/favoritesCount")
//    public int getFavoritesCount(@RequestParam int albumID) {
//        return albumDAO.getFavoritesCount(albumID);
//    }
//
//    @GetMapping("/createdAt")
//    public String getCreatedAt(@RequestParam int albumID) {
//        return albumDAO.getCreatedAt(albumID).toString();
//    }
//
//    @GetMapping("/publicAlbums")
//    public List<Album> getPublicAlbumsByName(@RequestParam String albumName) {
//        return albumDAO.getPublicAlbumsByName(albumName);
//    }
//
//    @PutMapping("/updateName/{albumID}")
//    public String updateAlbumName(@PathVariable int albumID,
//                                  @RequestParam String newAlbumName) {
//        albumDAO.updateAlbumName(albumID, newAlbumName);
//        return "Album name updated successfully.";
//    }
//
//    @PutMapping("/updateDescription/{albumID}")
//    public String updateAlbumDescription(@PathVariable int albumID,
//                                         @RequestParam String newDescription) {
//        albumDAO.updateAlbumDescription(albumID, newDescription);
//        return "Album description updated successfully.";
//    }
//}
//
//
//
package controllers;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import entity.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import server.AlbumServer;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/albums")
public class Album_Controller {

    private final AlbumServer albumServer;

    @Autowired
    public Album_Controller(AlbumServer albumServer) {
        this.albumServer = albumServer;
    }

    @PostMapping("/create")
    public String createAlbum(@ModelAttribute Album album, Model model) {
        album.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        album.setPublic(true);  // Assuming default value for public is true
        album.setDeleted(false);  // Assuming default value for deleted is false

        albumServer.createAlbum(album);

        model.addAttribute("message", "Album created successfully!");
        return "redirect:/albums/" + album.getAlbumID();
    }

    @GetMapping("/{albumID}")
    public String viewAlbum(@PathVariable int albumID, Model model) {
        Album album = albumServer.getAlbumByID(albumID);
        model.addAttribute("album", album);
        return "albumDetails";
    }

    @GetMapping("/search")
    public String searchPublicAlbums(@RequestParam String albumName, Model model) {
        List<Album> publicAlbums = albumServer.getPublicAlbumsByName(albumName);
        model.addAttribute("publicAlbums", publicAlbums);
        return "searchResults";
    }

    @GetMapping("/{albumID}/likesCount")
    @ResponseBody
    public int getLikesCount(@PathVariable int albumID) {
        return albumServer.getLikesCount(albumID);
    }

    @GetMapping("/{albumID}/favoritesCount")
    @ResponseBody
    public int getFavoritesCount(@PathVariable int albumID) {
        return albumServer.getFavoritesCount(albumID);
    }

    @GetMapping("/{albumID}/createdAt")
    @ResponseBody
    public Timestamp getCreatedAt(@PathVariable int albumID) {
        return albumServer.getCreatedAt(albumID);
    }

    @PostMapping("/{albumID}/updateName")
    @ResponseBody
    public String updateAlbumName(@PathVariable int albumID, @RequestParam String newAlbumName) {
        albumServer.updateAlbumName(albumID, newAlbumName);
        return "Album name updated successfully!";
    }

    @PostMapping("/{albumID}/updateDescription")
    @ResponseBody
    public String updateAlbumDescription(@PathVariable int albumID, @RequestParam String newDescription) {
        albumServer.updateAlbumDescription(albumID, newDescription);
        return "Album description updated successfully!";
    }
}
