//package server;
//
//import dao.LikeDAO;
//import dao.CommentDAO;
//import entity.Like;
//import entity.Comment;
//import entity.DataResult;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Map;
//
//import static server.AlbumServer.getPublicAlbumsByName;
//
//@Service
//public class InteractServer {
//
//    private final LikeDAO likeDAO;
//    private final CommentDAO commentDAO;
//
//    @Autowired
//    public InteractServer(LikeDAO likeDAO, CommentDAO commentDAO) {
//        this.likeDAO = likeDAO;
//        this.commentDAO = commentDAO;
//    }
//
//    // Like functionality
//    public DataResult insertLike(Like like) {
//        try {
//            likeDAO.insertLike(like);
//            return DataResult.success("Like inserted successfully", null);
//        } catch (Exception e) {
//            return DataResult.fail("Failed to insert like: " + e.getMessage());
//        }
//    }
//
//    public DataResult getLikeCountByAlbum(int albumID) {
//        try {
//            int likeCount = likeDAO.getLikeCountByAlbum(albumID);
//            return DataResult.success("Like count retrieved successfully", likeCount);
//        } catch (Exception e) {
//            return DataResult.fail("Failed to retrieve like count: " + e.getMessage());
//        }
//    }
//
//
//    public static DataResult getCommentInfoByAlbumId(String albumName){
//        DataResult dataResult = new DataResult();
//        if(!getPublicAlbumsByName(albumName)){
//            dataResult.setStatus(-1);
//            dataResult.setMsg("no such comment");
//        }else{
//            dataResult.setStatus(0);
//            dataResult.setMsg("get comment info");
//            List<Comment> commentList = new ArrayList<>();
//            List<Map<String,Object>> maps = CommentDAO.getCommentInfoListByAlbumId(albumName);
//            for(Map<String,Object>map:maps){
//                commentList.add(new Comment(map));
//            }
//            dataResult.setMsg("get comment success");
//            dataResult.setData(commentList);
//        }
//        return dataResult;
//    }
//    // Comment functionality
//    public DataResult insertComment(Comment comment) {
//        try {
//            commentDAO.insertComment(comment);
//            return DataResult.success("Comment inserted successfully", null);
//        } catch (Exception e) {
//            return DataResult.fail("Failed to insert comment: " + e.getMessage());
//        }
//    }
//
//    public DataResult getCommentCount(int albumID) {
//        try {
//            int commentCount = commentDAO.getCommentCount(albumID);
//            return DataResult.success("Comment count retrieved successfully", commentCount);
//        } catch (Exception e) {
//            return DataResult.fail("Failed to retrieve comment count: " + e.getMessage());
//        }
//    }
//
//    public DataResult getCommentsByUser(int commenterID) {
//        try {
//            List<Comment> userComments = commentDAO.getCommentsByUser(commenterID);
//            return DataResult.success("User comments retrieved successfully", userComments);
//        } catch (Exception e) {
//            return DataResult.fail("Failed to retrieve user comments: " + e.getMessage());
//        }
//    }
//
//    public DataResult getCommentsByAlbum(int albumID) {
//        try {
//            List<Comment> albumComments = commentDAO.getCommentsByAlbum(albumID);
//            return DataResult.success("Album comments retrieved successfully", albumComments);
//        } catch (Exception e) {
//            return DataResult.fail("Failed to retrieve album comments: " + e.getMessage());
//        }
//    }
//
//    public DataResult getCommentByID(int commentID) {
//        try {
//            Comment comment = commentDAO.getCommentByID(commentID);
//            return DataResult.success("Comment retrieved successfully", comment);
//        } catch (Exception e) {
//            return DataResult.fail("Failed to retrieve comment: " + e.getMessage());
//        }
//    }
//
//    public DataResult updateCommentContent(int commentID, String newContent) {
//        try {
//            commentDAO.updateCommentContent(commentID, newContent);
//            return DataResult.success("Comment content updated successfully", null);
//        } catch (Exception e) {
//            return DataResult.fail("Failed to update comment content: " + e.getMessage());
//        }
//    }
//
//    public DataResult deleteComment(int commentID) {
//        try {
//            commentDAO.deleteComment(commentID);
//            return DataResult.success("Comment deleted successfully", null);
//        } catch (Exception e) {
//            return DataResult.fail("Failed to delete comment: " + e.getMessage());
//        }
//    }
//}
