package controllers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import entity.User;

@Service
public class UserService {
    private final Map<Integer, User> userDatabase = new HashMap<>();
    private int userIdCounter = 1;

    public void registerUser(String username, String password, String email, boolean isAdmin, String description) {
        // Simulate auto-increment for UserID
        int userId = userIdCounter++;

        User user = new User(username, password, email, isAdmin, description);
        userDatabase.put(userId, user);
    }

    public boolean existsByUsername(String username) {
        return userDatabase.values().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    public void saveUser(int userId, String username, String password, String email, boolean isAdmin, String description) {
        User newUser = new User(username, password, email, isAdmin, description);
        userDatabase.put(userId, newUser);
    }

    public int getTotalNumberOfUser() {
        return userDatabase.size();
    }

    public User getUserByUsername(String username) {
        return userDatabase.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}
