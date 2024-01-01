package server;

import dao.FavoriteDAO;
import dao.LikeDAO;
import dao.CommentDAO;
import entity.Favorite;
import entity.Like;
import entity.Comment;
import entity.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;

import java.util.List;


@Service
public class InteractServer {

    private final LikeDAO likeDAO;
    private final CommentDAO commentDAO;
    private final FavoriteDAO favoriteDAO;

    @Autowired
    public InteractServer(LikeDAO likeDAO, CommentDAO commentDAO, FavoriteDAO favoriteDAO) {
        this.likeDAO = likeDAO;
        this.commentDAO = commentDAO;
        this.favoriteDAO = favoriteDAO;
    }

    // Like functionality
    public DataResult insertLike(Like like) {
        try {
            likeDAO.insertLike(like);
            return DataResult.success("Like inserted successfully", null);
        } catch (Exception e) {
            return DataResult.fail("Failed to insert like: " + e.getMessage());
        }
    }

    public DataResult getLikeCountByAlbum(int albumID) {
        try {
            int likeCount = likeDAO.getLikeCountByAlbum(albumID);
            return DataResult.success("Like count retrieved successfully", likeCount);
        } catch (Exception e) {
            return DataResult.fail("Failed to retrieve like count: " + e.getMessage());
        }
    }


    // Comment functionality
    public DataResult insertComment(Comment comment) {
        try {
            commentDAO.insertComment(comment);
            return DataResult.success("Comment inserted successfully", null);
        } catch (Exception e) {
            return DataResult.fail("Failed to insert comment: " + e.getMessage());
        }
    }

    public DataResult getCommentCount(int albumID) {
        try {
            int commentCount = commentDAO.getCommentCount(albumID);
            return DataResult.success("Comment count retrieved successfully", commentCount);
        } catch (Exception e) {
            return DataResult.fail("Failed to retrieve comment count: " + e.getMessage());
        }
    }

    public DataResult getCommentsByUser(int commenterID) {
        try {
            List<Comment> userComments = commentDAO.getCommentsByUser(commenterID);
            return DataResult.success("User comments retrieved successfully", userComments);
        } catch (Exception e) {
            return DataResult.fail("Failed to retrieve user comments: " + e.getMessage());
        }
    }

    public DataResult getCommentsByAlbum(int albumID) {
        try {
            List<Comment> albumComments = commentDAO.getCommentsByAlbum(albumID);
            return DataResult.success("Album comments retrieved successfully", albumComments);
        } catch (Exception e) {
            return DataResult.fail("Failed to retrieve album comments: " + e.getMessage());
        }
    }

    public DataResult getCommentByID(int commentID) {
        try {
            Comment comment = commentDAO.getCommentByID(commentID);
            return DataResult.success("Comment retrieved successfully", comment);
        } catch (Exception e) {
            return DataResult.fail("Failed to retrieve comment: " + e.getMessage());
        }
    }

    public DataResult updateCommentContent(int commentID, String newContent) {
        try {
            commentDAO.updateCommentContent(commentID, newContent);
            return DataResult.success("Comment content updated successfully", null);
        } catch (Exception e) {
            return DataResult.fail("Failed to update comment content: " + e.getMessage());
        }
    }

    public DataResult deleteComment(int commentID) {
        try {
            commentDAO.deleteComment(commentID);
            return DataResult.success("Comment deleted successfully", null);
        } catch (Exception e) {
            return DataResult.fail("Failed to delete comment: " + e.getMessage());
        }
    }

    public DataResult addToFavorites(int userID, int albumID) {
        try {
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
