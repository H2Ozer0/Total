package entity;

import java.sql.Timestamp;

public class Comment {
    private int commentID;
    private int albumID;
    private String content;
    private int commenterID;
    private String commenterName; // 新添加的评论者姓名属性
    private Timestamp commentTime;

    public Comment() {

    }

    public Comment(int albumID, String content, int commenterID, String commenterName, Timestamp commentTime) {
        this.albumID = albumID;
        this.content = content;
        this.commenterID = commenterID;
        this.commenterName = commenterName;
        this.commentTime = commentTime;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommenterID() {
        return commenterID;
    }

    public void setCommenterID(int commenterID) {
        this.commenterID = commenterID;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public Timestamp getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Timestamp commentTime) {
        this.commentTime = commentTime;
    }
}
