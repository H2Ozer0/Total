//package controllers;
//
//import entity.Comment;
//import entity.DataResult;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import server.InteractServer;
//
//
//@RestController
//@RequestMapping("/comment")
//public class Comment_Controller {
//
//    private final InteractServer interactServer;
//
//    @Autowired
//    public Comment_Controller(InteractServer interactServer) {
//        this.interactServer = interactServer;
//    }
//
//    @PostMapping("/insert")
//    public DataResult insertComment(@RequestBody Comment comment) {
//        return interactServer.insertComment(comment);
//    }
//
//    @GetMapping("/count/{albumID}")
//    public DataResult getCommentCount(@PathVariable int albumID) {
//        return interactServer.getCommentCount(albumID);
//    }
//
//    @GetMapping("/user/{commenterID}")
//    public DataResult getCommentsByUser(@PathVariable int commenterID) {
//        return interactServer.getCommentsByUser(commenterID);
//    }
//
//    @GetMapping("/album/{albumID}")
//    public DataResult getCommentsByAlbum(@PathVariable int albumID) {
//        return interactServer.getCommentsByAlbum(albumID);
//    }
//
//    @GetMapping("/{commentID}")
//    public DataResult getCommentByID(@PathVariable int commentID) {
//        return interactServer.getCommentByID(commentID);
//    }
//
//    @PutMapping("/update/{commentID}")
//    public DataResult updateCommentContent(@PathVariable int commentID, @RequestBody String newContent) {
//        return interactServer.updateCommentContent(commentID, newContent);
//    }
//
//    @DeleteMapping("/delete/{commentID}")
//    public DataResult deleteComment(@PathVariable int commentID) {
//        return interactServer.deleteComment(commentID);
//    }
//}
