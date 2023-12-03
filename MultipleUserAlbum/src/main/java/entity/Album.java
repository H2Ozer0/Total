package entity;

import java.sql.Timestamp;

public class Album {
    private String albumID;
    private String albumName;
    private String description;
    private Timestamp createdAt;
    private boolean isPublic;
    private boolean isDeleted;
    private int favoritesCount;
    private int likesCount;
    private String creatorID;

    public Album() {
    }

    public Album(String albumID, String albumName, String description, Timestamp createdAt, boolean isPublic, boolean isDeleted, int favoritesCount, int likesCount, String creatorID) {
        this.albumID = albumID;
        this.albumName = albumName;
        this.description = description;
        this.createdAt = createdAt;
        this.isPublic = isPublic;
        this.isDeleted = isDeleted;
        this.favoritesCount = favoritesCount;
        this.likesCount = likesCount;
        this.creatorID = creatorID;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(int favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    @Override
    public String toString() {
        return "entity.Album{" +
                "albumID='" + albumID + '\'' +
                ", albumName='" + albumName + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", isPublic=" + isPublic +
                ", isDeleted=" + isDeleted +
                ", favoritesCount=" + favoritesCount +
                ", likesCount=" + likesCount +
                ", creatorID='" + creatorID + '\'' +
                '}';
    }

}
