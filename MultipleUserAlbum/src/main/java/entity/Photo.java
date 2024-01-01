package entity;

import java.sql.Timestamp;

public class Photo {
    private int photoID;
    private int albumID;
    private String title;
    private String path;
    private Timestamp uploadTime;
    private boolean isDeleted;
    private byte[] photoData;

    // 构造函数
    public Photo(int albumID, String title, String path, Timestamp uploadTime, boolean isDeleted) {
        this.albumID = albumID;
        this.title = title;
        this.path = path;
        this.uploadTime = uploadTime;
        this.isDeleted = isDeleted;
    }

    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }

    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public byte[] getPhotoData() { return photoData; }

    public void setPhotoData(byte[] photoData) { this.photoData = photoData; }
}

