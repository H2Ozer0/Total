package server;

import java.util.HashMap;
import java.util.Map;

import dao.UserDAO;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import entity.User;

@Service
public class UserServer {
    private final Map<String, User> userDatabase = new HashMap<>();
    UserDAO userDAO = new UserDAO();
    public void registerUser(String username, String password) {

        User user = new User();
        user.setUsername(username);

        user.setPassword(password);

        userDatabase.put(username, user);
    }
    public boolean existsByUsername(String username) {
        // Check if a user with the given username already exists
        //return userList.stream().anyMatch(user -> user.getUsername().equals(username));
        return userDatabase.containsKey(username);
    }

    public void saveUser( String username, String password, String email, boolean isAdmin, String description) {
        // Save the user to the in-memory list (in a real-world scenario, you would persist it to a database)
        User newUser = new User(username, password, email, isAdmin, description);
        userDAO.insertUser(newUser);
    }
    public int getTotalNumberOfUser(){ return userDatabase.size(); }

    public User getUserByUsername(String username) {
        return userDatabase.get(username);
    }
    //在UserList中检查用户名唯一，用于登录
//    public boolean existsByUsername(String username) {
//         Check if a user with the given username already exists
//         Check in userList
//         return userList.stream().anyMatch(user -> user.getUsername().equals(username));
//    }
}
