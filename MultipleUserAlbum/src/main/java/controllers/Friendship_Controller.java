package controllers;

import dao.FriendshipDAO;
import entity.Friendship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/friendship")
public class Friendship_Controller {

    private final FriendshipDAO friendshipDAO;

    @Autowired
    public Friendship_Controller(FriendshipDAO friendshipDAO) {
        this.friendshipDAO = friendshipDAO;
    }

    @GetMapping("/create")
    public String showFriendshipForm(Model model) {
        model.addAttribute("friendship", new Friendship());
        return "friendship-form";
    }

    @PostMapping("/create")
    public String createFriendship(@ModelAttribute Friendship friendship) {
        friendshipDAO.insertFriendship(friendship);
        return "redirect:/friendship/" + friendship.getFriendshipID();
    }

    @GetMapping("/{friendshipID}")
    public String viewFriendship(@PathVariable int friendshipID, Model model) {
        Friendship friendship = friendshipDAO.getFriendshipByID(friendshipID);
        model.addAttribute("friendship", friendship);
        return "friendship-details";
    }

    // Other methods for updating, deleting, and listing friendships can be added here

    // Exception handling example
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error-page";
    }
}
