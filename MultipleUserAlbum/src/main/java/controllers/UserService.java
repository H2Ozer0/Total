package controllers;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<String, User> userDatabase = new HashMap<>();

    public void registerUser(String username, String password) {
        // 在实际应用中，你可能需要检查用户名是否已经存在等逻辑
        User user = new User();
        user.setUsername(username);
        // 在实际应用中，你应该使用密码哈希而不是明文密码
        user.setPassword(password);

        userDatabase.put(username, user);
    }

    public User getUserByUsername(String username) {
        return userDatabase.get(username);
    }
}
