package server;

import dao.AlbumDAO;
import dao.FavoriteDAO;
import dao.LikeDAO;
import dao.CommentDAO;
import entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;

import java.util.List;


@Service
public class InteractServer {

    public  InteractServer(){



    }


    // Like functionality
    public DataResult insertLike(Like like) {
        try {
            LikeDAO likeDAO=new LikeDAO();
            likeDAO.insertLike(like);
            System.out.println(1);
            return DataResult.success("Like inserted successfully", null);
        } catch (Exception e) {
            return DataResult.fail("Failed to insert like: " + e.getMessage());
        }
    }

    public DataResult getLikeCountByAlbum(int albumID) {
        try {
            LikeDAO likeDAO=new LikeDAO();
            int likeCount = likeDAO.getLikeCountByAlbum(albumID);
            return DataResult.success("Like count retrieved successfully", likeCount);
        } catch (Exception e) {
            return DataResult.fail("Failed to retrieve like count: " + e.getMessage());
        }
    }


    // Comment functionality
    public DataResult insertComment(Comment comment) {
        try {
            CommentDAO commentDAO=new CommentDAO();
            commentDAO.insertComment(comment);
            return DataResult.success("Comment inserted successfully", null);
        } catch (Exception e) {
            return DataResult.fail("Failed to insert comment: " + e.getMessage());
        }
    }

    public DataResult getCommentCount(int albumID) {
        try {
            CommentDAO commentDAO=new CommentDAO();
            int commentCount = commentDAO.getCommentCount(albumID);
            return DataResult.success("Comment count retrieved successfully", commentCount);
        } catch (Exception e) {
            return DataResult.fail("Failed to retrieve comment count: " + e.getMessage());
        }
    }

    public DataResult getCommentsByUser(int commenterID) {
        try {
            CommentDAO commentDAO=new CommentDAO();
            List<Comment> userComments = commentDAO.getCommentsByUser(commenterID);
            return DataResult.success("User comments retrieved successfully", userComments);
        } catch (Exception e) {
            return DataResult.fail("Failed to retrieve user comments: " + e.getMessage());
        }
    }

    public DataResult getCommentsByAlbum(int albumID) {
        try {
            CommentDAO commentDAO=new CommentDAO();
            List<Comment> albumComments = commentDAO.getCommentsByAlbum(albumID);
            return DataResult.success("Album comments retrieved successfully", albumComments);
        } catch (Exception e) {
            return DataResult.fail("Failed to retrieve album comments: " + e.getMessage());
        }
    }

    public DataResult getCommentByID(int commentID) {
        try {
            CommentDAO commentDAO=new CommentDAO();
            Comment comment = commentDAO.getCommentByID(commentID);
            return DataResult.success("Comment retrieved successfully", comment);
        } catch (Exception e) {
            return DataResult.fail("Failed to retrieve comment: " + e.getMessage());
        }
    }

    // Comment functionality
    public DataResult getCommentsByUserName(String commenterName) {
        try {
            CommentDAO commentDAO = new CommentDAO();
            List<Comment> comments = commentDAO.getCommentsByUserName(commenterName);
            return DataResult.success("Comments retrieved successfully", comments);
        } catch (Exception e) {
            return DataResult.fail("Failed to retrieve comments by username: " + e.getMessage());
        }
    }

    public DataResult updateCommentContent(int commentID, String newContent) {
        try {
            CommentDAO commentDAO=new CommentDAO();
            commentDAO.updateCommentContent(commentID, newContent);
            return DataResult.success("Comment content updated successfully", null);
        } catch (Exception e) {
            return DataResult.fail("Failed to update comment content: " + e.getMessage());
        }
    }

    public DataResult deleteComment(int commentID) {
        try {
            CommentDAO commentDAO=new CommentDAO();
            commentDAO.deleteComment(commentID);
            return DataResult.success("Comment deleted successfully", null);
        } catch (Exception e) {
            return DataResult.fail("Failed to delete comment: " + e.getMessage());
        }
    }

    public DataResult addToFavorites(int userID, int albumID) {
        try {
            FavoriteDAO favoriteDAO=new FavoriteDAO();
            // Implement your logic to add the album to the user's favorites
            Favorite newFavorite = new Favorite(albumID, userID, getCurrentTimestamp());
            favoriteDAO.insertFavorite(newFavorite);

            return DataResult.success("Album added to favorites successfully", null);
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.fail("Failed to add album to favorites");
        }
    }
    public DataResult deleteFavorite(int userID, int albumID) {
        try {
            FavoriteDAO favoriteDAO=new FavoriteDAO();
            favoriteDAO.deleteFavorite(userID, albumID);
            return DataResult.success("Favorite deleted successfully.", null);
        } catch (Exception e) {
            return DataResult.fail("Failed to delete favorite: " + e.getMessage());
        }
    }
    private Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
