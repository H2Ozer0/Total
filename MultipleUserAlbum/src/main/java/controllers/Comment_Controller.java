package controllers;

import entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import server.CommentServer;

import java.util.List;

@Controller
@RequestMapping("/comments")
public class Comment_Controller {

    private final CommentServer commentServer;

    @Autowired
    public Comment_Controller(CommentServer commentServer) {
        this.commentServer = commentServer;
    }

    @PostMapping("/insert")
    public String insertComment(@ModelAttribute Comment comment) {
        commentServer.insertComment(comment);
        return "redirect:/albums/" + comment.getAlbumID(); // Redirect to the album details page after inserting a comment.
    }

    @GetMapping("/count/{albumID}")
    @ResponseBody
    public int getCommentCount(@PathVariable int albumID) {
        return commentServer.getCommentCount(albumID);
    }

    @GetMapping("/user/{commenterID}")
    @ResponseBody
    public List<Comment> getCommentsByUser(@PathVariable int commenterID) {
        return commentServer.getCommentsByUser(commenterID);
    }

    @GetMapping("/album/{albumID}")
    @ResponseBody
    public List<Comment> getCommentsByAlbum(@PathVariable int albumID) {
        return commentServer.getCommentsByAlbum(albumID);
    }

    @GetMapping("/{commentID}")
    public String getCommentByID(@PathVariable int commentID, Model model) {
        Comment comment = commentServer.getCommentByID(commentID);
        model.addAttribute("comment", comment);
        return "commentDetails"; // Assuming you have a view named "commentDetails".
    }

    @PostMapping("/{commentID}/update")
    public String updateCommentContent(@PathVariable int commentID, @RequestParam String newContent) {
        commentServer.updateCommentContent(commentID, newContent);
        return "redirect:/comments/" + commentID;
    }

    @PostMapping("/{commentID}/delete")
    public String deleteComment(@PathVariable int commentID) {
        commentServer.deleteComment(commentID);
        return "redirect:/albums"; // Redirect to the albums page after deleting a comment.
    }
}
