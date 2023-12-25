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
    private final UserDAO userDAO;
    
    public UserServer(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void registerUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userDatabase.put(username, user);
    }

    public boolean existsByUsername(String username) {
        return userDatabase.containsKey(username);
    }

    public void saveUser(String username, String password, String email, boolean isAdmin, String description) {
        User newUser = new User(username, password, email, isAdmin, description);
        userDAO.insertUser(newUser);
    }

    public int getTotalNumberOfUser() {
        return userDatabase.size();
    }

    public User getUserByUsername(String username) {
        return userDatabase.get(username);
    }
}
