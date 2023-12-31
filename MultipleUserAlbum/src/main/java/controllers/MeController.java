package controllers;

import entity.Album;
import entity.DataResult;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import server.AlbumServer;
import server.UserServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import server.AlbumServer;
@Controller
@RequestMapping("/me")
public class MeController {
    private final AlbumServer albumServer;
    @Autowired
    public MeController(AlbumServer albumServer) {
        this.albumServer= albumServer;
    }
    @RequestMapping  ("/albums")
    public String albums(Model model, HttpSession session)
    {
        User user = (User)session.getAttribute("myInfo");
        model.addAttribute("myInfo",user);
        return "my_albums";
    }
    @RequestMapping("/photos")
    public String enterMyPhotos(HttpSession session,Model model)
    {
        User user = (User)session.getAttribute("myInfo");
        List<Album> albumList= (List<Album>) albumServer.getPublicAlbumsByName(user.getUsername());
        model.addAttribute("myInfo",user);
        model.addAttribute("albumList",albumList);
        return "my_photos";
    }
    @RequestMapping("/getMyAlbum")
    @ResponseBody
    public DataResult getMyAlbum( HttpSession session){
        User user = (User)session.getAttribute("myInfo");

        DataResult dataResult;
        dataResult=albumServer.getPublicAlbumsByName(user.getUsername());



        return dataResult;
    }

}
