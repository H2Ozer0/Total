package dao;

import entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    private static final String URL = "jdbc:mysql://223.2.20.14:3306/albumdb?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "album";
    private static final String PASSWORD = "StrongPassword123!";

    private static Connection connection;

    public CommentDAO() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("无法连接到数据库: " + e.getMessage());
        }
    }

    // 插入评论记录
    public void insertComment(Comment comment) {
        try {
            if (!albumExists(comment.getAlbumID())) {
                throw new IllegalArgumentException("相册不存在，无法插入评论。AlbumID: " + comment.getAlbumID());
            }
            if (!userExists(comment.getCommenterID())) {
                throw new IllegalArgumentException("评论人不存在，无法插入评论。CommenterID: " + comment.getCommenterID());
            }

            // 获取评论者的用户名
            String commenterName = getUsernameByUserID(comment.getCommenterID());

            String query = "INSERT INTO Comment (AlbumID, Content, CommenterID, CommenterName, CommentTime) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, comment.getAlbumID());
                preparedStatement.setString(2, comment.getContent());
                preparedStatement.setInt(3, comment.getCommenterID());
                preparedStatement.setString(4, commenterName); // 使用评论者的用户名
                preparedStatement.setTimestamp(5, comment.getCommentTime());

                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        comment.setCommentID(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("插入评论记录时发生异常: " + e.getMessage());
        }
    }

    // 新增方法，根据UserID获取Username
    private String getUsernameByUserID(int userID) throws SQLException {
        String query = "SELECT Username FROM User WHERE UserID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Username");
                }
            }
        }
        throw new IllegalArgumentException("无法获取UserID为 " + userID + " 的Username。");
    }

    // 查询相册评论数
    public int getCommentCount(int albumID) {
        int commentCount = 0;
        try {
            if (!albumExists(albumID) ){
                throw new IllegalArgumentException("相册不存在，无法查询评论。AlbumID: " + albumID);
            }
            String query = "SELECT COUNT(*) AS CommentCount FROM Comment WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        commentCount = resultSet.getInt("CommentCount");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("查询相册评论数时发生异常: " + e.getMessage());
        }
        return commentCount;
    }

    // 查询某用户的所有评论
    public List<Comment> getCommentsByUser(int commenterID) {
        List<Comment> userComments = new ArrayList<>();
        try {
            if (!userExists(commenterID)) {
                throw new IllegalArgumentException("评论人不存在，无法查询评论。CommenterID: " + commenterID);
            }
            String query = "SELECT * FROM Comment WHERE CommenterID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, commenterID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Comment comment = mapResultSetToComment(resultSet);
                        userComments.add(comment);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("查询某用户的所有评论时发生异常: " + e.getMessage());
        }
        return userComments;
    }

    // 查询某相册的所有评论
    public List<Comment> getCommentsByAlbum(int albumID) {
        List<Comment> albumComments = new ArrayList<>();
        try {
            if (!albumExists(albumID) ){
                throw new IllegalArgumentException("相册不存在，无法查询评论。AlbumID: " + albumID);
            }
            String query = "SELECT * FROM Comment WHERE AlbumID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, albumID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Comment comment = mapResultSetToComment(resultSet);
                        albumComments.add(comment);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("查询某相册的所有评论时发生异常: " + e.getMessage());
        }
        return albumComments;
    }


    // 根据评论ID查询评论
    public Comment getCommentByID(int commentID) {
        Comment comment = null;
        try {
            String query = "SELECT * FROM Comment WHERE CommentID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, commentID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        comment = mapResultSetToComment(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("根据评论ID查询评论时发生异常: " + e.getMessage());
        }
        return comment;
    }

    // 根据评论ID修改评论内容
    public void updateCommentContent(int commentID, String newContent) {
        try {
            String query = "UPDATE Comment SET Content = ? WHERE CommentID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newContent);
                preparedStatement.setInt(2, commentID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("根据评论ID修改评论内容时发生异常: " + e.getMessage());
        }
    }

    // 查询某用户的所有评论（根据评论者姓名）
    public List<Comment> getCommentsByUserName(String commenterName) {
        List<Comment> userComments = new ArrayList<>();
        try {
            if (!userNameExists(commenterName)) {
                throw new IllegalArgumentException("评论人不存在，无法查询评论。CommenterName: " + commenterName);
            }
            String query = "SELECT * FROM Comment WHERE CommenterName = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, commenterName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Comment comment = mapResultSetToComment(resultSet);
                        userComments.add(comment);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("查询某用户的所有评论时发生异常: " + e.getMessage());
        }
        return userComments;
    }

    // 将 ResultSet 映射到 Comment 对象
    private Comment mapResultSetToComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setCommentID(resultSet.getInt("CommentID"));
        comment.setAlbumID(resultSet.getInt("AlbumID"));
        comment.setContent(resultSet.getString("Content"));
        comment.setCommenterID(resultSet.getInt("CommenterID"));
        comment.setCommenterName(resultSet.getString("CommenterName")); // 获取评论者姓名
        comment.setCommentTime(resultSet.getTimestamp("CommentTime"));
        return comment;
    }
    // 新增方法，用于检查相册是否存在
    private boolean albumExists(int albumID) throws SQLException {
        String query = "SELECT COUNT(*) AS AlbumCount FROM Album WHERE AlbumID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, albumID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt("AlbumCount") > 0;
            }
        }
    }

    // 新增方法，用于检查评论人是否存在
    private boolean userExists(int commenterID) throws SQLException {
        String query = "SELECT COUNT(*) AS UserCount FROM User WHERE UserID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, commenterID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt("UserCount") > 0;
            }
        }
    }

    // 新增方法，用于检查评论人是否存在（根据用户名）
    private boolean userNameExists(String commenterName) throws SQLException {
        String query = "SELECT COUNT(*) AS UserCount FROM User WHERE Username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, commenterName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt("UserCount") > 0;
            }
        }
    }

    // 删除评论
    public void deleteComment(int commentID) {
        try {
            String query = "DELETE FROM Comment WHERE CommentID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, commentID);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected == 0) {
                    throw new IllegalArgumentException("评论不存在或已被删除。CommentID: " + commentID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("删除评论时发生异常: " + e.getMessage());
        }
    }

    // 关闭数据库连接
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("关闭数据库连接时发生异常: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        CommentDAO commentDAO = new CommentDAO();

        // 插入评论记录
        Comment newComment = new Comment(1, "Great photo!", 14, null, Timestamp.valueOf("2023-01-02 14:30:00"));
        commentDAO.insertComment(newComment);

        // 根据评论ID查询评论
        Comment retrievedComment = commentDAO.getCommentByID(newComment.getCommentID());
        System.out.println("Retrieved Comment: " + retrievedComment);

        // 查询相册评论数
        int commentCount = commentDAO.getCommentCount(newComment.getAlbumID());
        System.out.println("Comment Count: " + commentCount);

        // 查询某用户的所有评论
        List<Comment> userComments = commentDAO.getCommentsByUser(newComment.getCommenterID());
        System.out.println("User Comments: " + userComments);

        // 更新评论内容
        commentDAO.updateCommentContent(newComment.getCommentID(), "Haha");
//        // 删除评论
//        commentDAO.deleteComment(newComment.getCommentID());

        // 关闭数据库连接
        commentDAO.closeConnection();
    }
}
