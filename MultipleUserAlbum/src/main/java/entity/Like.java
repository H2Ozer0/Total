package entity;

import java.sql.Timestamp;

public class Like {

    private int likeID;
    private int albumID;
    private int userID;
    private Timestamp likeTime;

    public Like() {
    }

    public Like(int albumID, int userID, Timestamp likeTime) {
        this.albumID = albumID;
        this.userID = userID;
        this.likeTime = likeTime;
    }

    public int getLikeID() {
        return likeID;
    }

    public void setLikeID(int likeID) {
        this.likeID = likeID;
    }

    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Timestamp getLikeTime() {
        return likeTime;
    }

    public void setLikeTime(Timestamp likeTime) {
        this.likeTime = likeTime;
    }
}
