package controllers;

import entity.DataResult;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.AlbumServer;
import server.InteractServer;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/favorites")
public class Favorite_Controller {

    private final InteractServer interactServer;
    private final AlbumServer albumServer;

    @Autowired
    public Favorite_Controller(InteractServer interactServer,AlbumServer albumServer) {
        this.albumServer=albumServer;
        this.interactServer = interactServer;
    }

    @PostMapping("/add")
    @ResponseBody
    public DataResult addMyFollows(@RequestParam("AID")int aId, HttpSession session){
        User user= (User) session.getAttribute("myInfo");
        System.out.println("AddFollow" + user.getUserId() + " " + aId);
        DataResult dataResult = interactServer.addToFavorites(user.getUserId(),aId);
        System.out.println(dataResult.getMsg());
        albumServer.updateAlbumFavoritesCount(aId);
        return dataResult;
    }

    @PostMapping("/delete")
    @ResponseBody
    public DataResult delMyFollows(@RequestParam("AID")int aId, HttpSession session){
        System.out.println("DelFollow");
        User user= (User) session.getAttribute("myInfo");
        DataResult dataResult = interactServer.deleteFavorite(user.getUserId(),aId);
        albumServer.updateAlbumFavoritesCount(aId);
        System.out.println(dataResult.getStatus());
        return dataResult;
    }



}