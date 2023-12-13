package entity;

import java.sql.Timestamp;

public class Favorite {
    private int favoriteID;
    private int albumID;
    private int userID;
    private Timestamp favoriteTime;

    // 默认构造函数
    public Favorite() {
    }

    // 带参数的构造函数
    public Favorite(int albumID, int userID, Timestamp favoriteTime) {
        this.albumID = albumID;
        this.userID = userID;
        this.favoriteTime = favoriteTime;
    }

    // 获取收藏ID
    public int getFavoriteID() {
        return favoriteID;
    }

    // 设置收藏ID
    public void setFavoriteID(int favoriteID) {
        this.favoriteID = favoriteID;
    }

    // 获取相册ID
    public int getAlbumID() {
        return albumID;
    }

    // 设置相册ID
    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    // 获取用户ID
    public int getUserID() {
        return userID;
    }

    // 设置用户ID
    public void setUserID(int userID) {
        this.userID = userID;
    }

    // 获取收藏时间
    public Timestamp getFavoriteTime() {
        return favoriteTime;
    }

    // 设置收藏时间
    public void setFavoriteTime(Timestamp favoriteTime) {
        this.favoriteTime = favoriteTime;
    }

    // 添加获取生成的自增主键值的方法，根据需要自行使用
    public int getGeneratedFavoriteID() {
        return favoriteID;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "favoriteID=" + favoriteID +
                ", albumID=" + albumID +
                ", userID=" + userID +
                ", favoriteTime=" + favoriteTime +
                '}';
    }
}
