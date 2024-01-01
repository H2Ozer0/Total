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

import entity.DataResult;
import entity.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import entity.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import server.AlbumServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
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
    @RequestMapping("/createalbum_page")
    public String createpate()
    {
        return "createAlbum";
    };


    @PostMapping("/createAlbum")
    public String createAlbum(
            @RequestParam("title") String title,
            @RequestParam("desc") String description,
            @RequestParam("auth") boolean isShared,
            @RequestParam("coverImage") MultipartFile coverImage,
            @RequestParam("coverImageBase64") String coverImageBase64, HttpSession session, HttpServletRequest request,Model model) throws IOException {
        File cover=new File(coverImage.getOriginalFilename());
        System.out.println(cover.getName()+"这是图片名字");
        User user = (User)session.getAttribute("myInfo");
        int CreatorID=user.getUserId();
        Album album=new Album(title, description,new Timestamp(System.currentTimeMillis()), isShared, false, 0, 0,CreatorID);
        albumServer.insertAlbum(album);
        String absolutepath = request.getServletContext().getRealPath("\\Image\\Cover");
        String filepath=absolutepath+'\\'+title+".png";
        FileUtil.deleteFile(filepath);
        FileUtil.saveFile(coverImage,filepath);
        model.addAttribute("message", "相册创建成功!");
        return "my_albums";

    }


//    @GetMapping("/{albumID}")
//    public String viewAlbum(@PathVariable int albumID, Model model) {
//        Album album = albumServer.getAlbumByID(albumID);
//        model.addAttribute("album", album);
//        return "albumDetails";
//    }
//
//    @GetMapping("/search")
//    public String searchPublicAlbums(@RequestParam String albumName, Model model) {
//        List<Album> publicAlbums = albumServer.getPublicAlbumsByName(albumName);
//        model.addAttribute("publicAlbums", publicAlbums);
//        return "searchResults";
//    }
//
//    @GetMapping("/{albumID}/likesCount")
//    @ResponseBody
//    public int getLikesCount(@PathVariable int albumID) {
//        return albumServer.getLikesCount(albumID);
//    }
//
//    @GetMapping("/{albumID}/favoritesCount")
//    @ResponseBody
//    public int getFavoritesCount(@PathVariable int albumID) {
//        return albumServer.getFavoritesCount(albumID);
//    }
//
//    @GetMapping("/{albumID}/createdAt")
//    @ResponseBody
//    public Timestamp getCreatedAt(@PathVariable int albumID) {
//        return albumServer.getCreatedAt(albumID);
//    }
//
//    @PostMapping("/{albumID}/updateName")
//    @ResponseBody
//    public String updateAlbumName(@PathVariable int albumID, @RequestParam String newAlbumName) {
//        albumServer.updateAlbumName(albumID, newAlbumName);
//        return "Album name updated successfully!";
//    }
//
//    @PostMapping("/{albumID}/updateDescription")
//    @ResponseBody
//    public String updateAlbumDescription(@PathVariable int albumID, @RequestParam String newDescription) {
//        albumServer.updateAlbumDescription(albumID, newDescription);
//        return "Album description updated successfully!";
//    }

}
