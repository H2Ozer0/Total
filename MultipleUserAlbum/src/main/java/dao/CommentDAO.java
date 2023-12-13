package dao;

import entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    private static final String URL = "jdbc:mysql://223.2.20.14:3306/albumdb?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "album";
    private static final String PASSWORD = "StrongPassword123!";

    private Connection connection;

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
            String query = "INSERT INTO Comment (AlbumID, Content, CommentTime) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, comment.getAlbumID());
                preparedStatement.setString(2, comment.getContent());
                preparedStatement.setInt(3, comment.getCommenterID());
                preparedStatement.setTimestamp(4, comment.getCommentTime());

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

    // 查询相册评论数
    public int getCommentCount(int albumID) {
        int commentCount = 0;
        try {
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

    // 将 ResultSet 映射到 Comment 对象
    private Comment mapResultSetToComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setCommentID(resultSet.getInt("CommentID"));
        comment.setAlbumID(resultSet.getInt("AlbumID"));
        comment.setContent(resultSet.getString("Content"));
        comment.setCommenterID(resultSet.getInt("CommenterID"));
        comment.setCommentTime(resultSet.getTimestamp("CommentTime"));
        return comment;
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
        Comment newComment = new Comment(1, "Great photo!", 2, Timestamp.valueOf("2023-01-02 14:30:00"));
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

        // 关闭数据库连接
        commentDAO.closeConnection();
    }
}
