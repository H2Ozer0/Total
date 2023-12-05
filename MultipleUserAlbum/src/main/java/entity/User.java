package entity;

public class User {
    private String userId;
    private String username;
    private String password;
    private String email;
    private boolean isAdmin;
    private String description;

    // 默认构造函数
    public User() {
    }

    // 带参数的构造函数
    public User(String userId, String username, String password, String email, boolean isAdmin, String description) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
        this.description = description;
    }

    // 获取用户ID
    public String getUserId() {
        return userId;
    }
    // 设置用户ID
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // 获取用户名
    public String getUsername() {
        return username;
    }
    // 设置用户名
    public void setUsername(String username) {
        if (username != null && !username.trim().isEmpty()) {
            this.username = username.trim();
        } else {
            throw new IllegalArgumentException("Username cannot be empty");
        }
    }
    // 获取密码
    public String getPassword() {
        return password;
    }
    // 设置密码
    public void setPassword(String password) {
        if (password != null) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password cannot be null");
        }
    }


    // 获取邮箱
    public String getEmail() {
        return email;
    }
    // 设置邮箱
    public void setEmail(String email) {
        this.email = email;
    }

    // 判断是否为管理员
    public boolean isAdmin() {
        return isAdmin;
    }
    // 设置是否为管理员
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    // 获取用户简介
    public String getDescription() {
        return description;
    }
    // 设置用户简介
    public void setDescription(String description) {
        this.description = description;
    }




    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", description='" + description + '\'' +
                '}';
    }
}
