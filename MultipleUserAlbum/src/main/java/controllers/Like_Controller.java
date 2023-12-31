package controllers;

import entity.Like;
import entity.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import server.InteractServer;

@RestController
@RequestMapping("/like")
public class Like_Controller {

    private final InteractServer interactServer;

    @Autowired
    public Like_Controller(InteractServer interactServer) {
        this.interactServer = interactServer;
    }

    @PostMapping("/insert")
    public DataResult insertLike(@RequestBody Like like) {
        return interactServer.insertLike(like);
    }

    @GetMapping("/count/{albumID}")
    public DataResult getLikeCountByAlbum(@PathVariable int albumID) {
        return interactServer.getLikeCountByAlbum(albumID);
    }
}
