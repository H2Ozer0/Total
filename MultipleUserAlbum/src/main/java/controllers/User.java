package controllers;

public class User {
    private String username;
    private String password;


    public void setUsername(String username) {
        // 在实际应用中，你可能需要添加更多的用户名验证逻辑，比如长度、字符集等
        if (username != null && !username.trim().isEmpty()) {
            this.username = username.trim();
        } else {
            throw new IllegalArgumentException("Username cannot be empty");
        }
    }
    public void setPassword(String password) {
        // 在实际应用中，你可能需要添加更多的密码验证逻辑
        if (password != null) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password cannot be null");
        }
    }
}
