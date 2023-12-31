package entity;

public class User {
    private int userId;  // 修改数据类型为 int
    private String username;
    private String password;
    private String email;
    private boolean isAdmin;
    private String description;

    // 默认构造函数
    public User() {
    }
    public User(User user) {
        this.userId = user.userId;  // 初始值可以是 0 或者其他默认值
        this.username = user.username;
        this.password = user.password;
        this.email = user.email;
        this.isAdmin = user.isAdmin;
        this.description = user.description;
    }
    // 带参数的构造函数，不再包含 userID 参数
    public User(String username, String password, String email, boolean isAdmin, String description) {
        this.userId = 0;  // 初始值可以是 0 或者其他默认值
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
        this.description = description;
    }

    // 获取用户ID
    public int getUserId() {
        return userId;
    }


    // 设置用户ID，注意不再需要检查 userID 是否为 null 或空
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void updateusername(String name)
    {
        this.username=username;
    }
    public void updatedescritption(String description)
    {
        this.description=description;
    }
    public void updateemail(String email)
    {
        this.email=email;
    }
    // 获取用户名
    public String getUsername() {
        return username;
    }

    // 设置用户名，保留了非空检查
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

    // 设置密码，保留了非空检查
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

    // 添加获取生成的自增主键值的方法，根据需要自行使用
    public int getGeneratedUserId() {
        return userId;
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
