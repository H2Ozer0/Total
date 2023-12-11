package controllers;


import dao.FriendshipDAO;
import entity.Friendship;

import java.sql.Timestamp;

public class Friendship_Controller {

    private final FriendshipDAO friendshipDAO;

    public Friendship_Controller() {
        this.friendshipDAO = new FriendshipDAO();
    }

    public void createFriendship(int userID1, int userID2, String friendshipStatus, Timestamp createdAt) {
        Friendship newFriendship = new Friendship(userID1, userID2, friendshipStatus, createdAt);
        friendshipDAO.insertFriendship(newFriendship);
        System.out.println("Friendship created: " + newFriendship);
    }

    public void displayFriendshipInfo(int friendshipID) {
        Friendship friendship = friendshipDAO.getFriendshipByID(friendshipID);
        if (friendship != null) {
            System.out.println("Friendship information: " + friendship);
        } else {
            System.out.println("Friendship not found with ID: " + friendshipID);
        }
    }

    public void closeConnection() {
        friendshipDAO.closeConnection();
        System.out.println("Database connection closed.");
    }

    public static void main(String[] args) {
        Friendship_Controller friendshipController = new Friendship_Controller();

        // 示例操作
        friendshipController.createFriendship(1, 2, "Pending", Timestamp.valueOf("2023-01-01 12:00:00"));
        friendshipController.displayFriendshipInfo(1);

        // 关闭数据库连接
        friendshipController.closeConnection();
    }
}
