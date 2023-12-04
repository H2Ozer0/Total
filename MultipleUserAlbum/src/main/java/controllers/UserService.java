package controllers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import entity.User;

@Service
public class UserService {
    private final Map<String, User> userDatabase = new HashMap<>();

    public void registerUser(String username, String password) {

        User user = new User();
        user.setUsername(username);

        user.setPassword(password);

        userDatabase.put(username, user);
    }

    public User getUserByUsername(String username) {
        return userDatabase.get(username);
    }
}
