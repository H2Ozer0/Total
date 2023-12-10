package entity;

import java.sql.Timestamp;

public class Friendship {
    private int friendshipID;
    private int userID1;
    private int userID2;
    private String friendshipStatus;
    private Timestamp createdAt;  // 将数据类型更改为 Timestamp

    // 默认构造函数
    public Friendship() {
    }

    // 带参数的构造函数
    public Friendship(int userID1, int userID2, String friendshipStatus, Timestamp createdAt) {
        this.userID1 = userID1;
        this.userID2 = userID2;
        this.friendshipStatus = friendshipStatus;
        this.createdAt = createdAt;
    }

    // 获取好友关系ID
    public int getFriendshipID() {
        return friendshipID;
    }

    // 设置好友关系ID
    public void setFriendshipID(int friendshipID) {
        this.friendshipID = friendshipID;
    }

    // 获取用户ID1
    public int getUserID1() {
        return userID1;
    }

    // 设置用户ID1
    public void setUserID1(int userID1) {
        this.userID1 = userID1;
    }

    // 获取用户ID2
    public int getUserID2() {
        return userID2;
    }

    // 设置用户ID2
    public void setUserID2(int userID2) {
        this.userID2 = userID2;
    }

    // 获取好友关系状态
    public String getFriendshipStatus() {
        return friendshipStatus;
    }

    // 设置好友关系状态
    public void setFriendshipStatus(String friendshipStatus) {
        this.friendshipStatus = friendshipStatus;
    }

    // 获取创建时间
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // 设置创建时间
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // 添加获取生成的自增主键值的方法，根据需要自行使用
    public int getGeneratedFriendshipID() {
        return friendshipID;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "friendshipID=" + friendshipID +
                ", userID1=" + userID1 +
                ", userID2=" + userID2 +
                ", friendshipStatus='" + friendshipStatus + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
