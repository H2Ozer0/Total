package controllers;

import entity.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.InteractServer;

@Controller
@RequestMapping("/favorites")
public class Favorite_Controller {

    private final InteractServer interactServer;

    @Autowired
    public Favorite_Controller(InteractServer interactServer) {
        this.interactServer = interactServer;
    }

    @PostMapping("/add")
    @ResponseBody
    public DataResult addToFavorites(@RequestParam int userID, @RequestParam int albumID) {
        return interactServer.addToFavorites(userID, albumID);
    }

}
