package server;

import dao.CommentDAO;
import entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServer {

    private final CommentDAO commentDAO;

    @Autowired
    public CommentServer(CommentDAO commentDAO) {
        this.commentDAO = commentDAO;
    }

    public void insertComment(Comment comment) {
        commentDAO.insertComment(comment);
    }

    public int getCommentCount(int albumID) {
        return commentDAO.getCommentCount(albumID);
    }

    public List<Comment> getCommentsByUser(int commenterID) {
        return commentDAO.getCommentsByUser(commenterID);
    }

    public List<Comment> getCommentsByAlbum(int albumID) {
        return commentDAO.getCommentsByAlbum(albumID);
    }

    public Comment getCommentByID(int commentID) {
        return commentDAO.getCommentByID(commentID);
    }

    public void updateCommentContent(int commentID, String newContent) {
        commentDAO.updateCommentContent(commentID, newContent);
    }

    public void deleteComment(int commentID) {
        commentDAO.deleteComment(commentID);
    }
}
