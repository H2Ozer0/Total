package controllers;

import entity.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import server.LikeServer;

@Controller
@RequestMapping("/likes")
public class Like_Controller {

    private final LikeServer likeServer;

    @Autowired
    public Like_Controller(LikeServer likeServer) {
        this.likeServer = likeServer;
    }

    @PostMapping("/insert")
    public String insertLike(@ModelAttribute Like like) {
        likeServer.insertLike(like);
        return "redirect:/albums/" + like.getAlbumID(); // Redirect to the album details page after inserting a like.
    }

    @GetMapping("/count/{albumID}")
    @ResponseBody
    public int getLikeCountByAlbum(@PathVariable int albumID) {
        return likeServer.getLikeCountByAlbum(albumID);
    }
}
