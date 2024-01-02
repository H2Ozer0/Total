package server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import constant.Constant;
import dao.FriendshipDAO;
import dao.UserDAO;
import entity.DataResult;
import entity.Friendship;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import entity.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;


@Service
public class UserServer implements ServletContextAware {
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
//    // 登录（使用id和用户名都可以）
//    public DataResult login(String input, String password) {
//        DataResult dataResult = new DataResult();
//
//        // 尝试通过用户名查询用户
//        User userByUsername = userDAO.getUserByUsername(input);
//
//        // 尝试通过ID查询用户
//        User userById = null;
//        if (isNumeric(input)) {
//            userById = userDAO.getUserById(input);
//        }
//
//        // 判断查询结果
//        if (userByUsername == null && userById == null) {
//            // 用户不存在
//            dataResult.setStatus(-1);
//            dataResult.setMsg("用户不存在");
//        } else {
//            User user = (userByUsername != null) ? userByUsername : userById;
//
//            if (user.getPassword().equals(password)) {
//                // 密码正确
//                dataResult.setStatus(0);
//                dataResult.setMsg("登录成功");
//                dataResult.setData(user);
//            } else {
//                // 密码不正确
//                dataResult.setStatus(-1);
//                dataResult.setMsg("密码不正确");
//            }
//        }
//
//        return dataResult;
//    }
//
//    // 判断字符串是否为数字
//    private boolean isNumeric(String str) {
//        return str.matches("\\d+");
//    }

    //登录
    public DataResult login(String username,String password) {
        UserDAO userDAO=new UserDAO();
        DataResult dataResult = new DataResult();
        if(userDAO.isUsernameUnique(username))//检查用户名是否存在
        {
            //用户名不存在
            dataResult.setStatus(-1);
            dataResult.setMsg("用户名不存在");
        }else{
            User user = new User(userDAO.getUserByUsername(username));
            if(user.getPassword().equals(password)){
                //密码正确
//                System.out.println(user.getUserId());
                dataResult.setStatus(0);
                dataResult.setMsg("登录成功");
                dataResult.setData(user);
            }else{
                dataResult.setStatus(-1);
                dataResult.setMsg("密码不正确");
            }
        }
        userDAO.closeConnection();
        return dataResult;
    }

    //注册
    public DataResult register(String username, String password,String email, boolean isAdmin, String description) {
        DataResult dataResult = new DataResult();
        UserDAO userDAO=new UserDAO();
        if(!userDAO.isUsernameUnique(username))//检查用户名是否存在
        {
            //用户名已经存在
            dataResult.setStatus(-1);
            dataResult.setMsg("用户名已存在");
        }else{
            User user = new User(username,password,email,isAdmin,description);
            userDAO.insertUser(user);//向数据库插入用户
            //密码正确
            dataResult.setStatus(0);
            dataResult.setMsg("注册成功");
            dataResult.setData(user);

        }
        userDAO.closeConnection();
        return dataResult;
    }
    //上传头像
    public DataResult uploadAvatar(MultipartFile file, int userId) throws Exception {
        String name = userId + ".jpg";
        String uploadsPath = servletContext.getRealPath("/upload/avatar");

        // 确保目录存在
        File uploadsDir = new File(uploadsPath);
        if (!uploadsDir.exists()) {
            uploadsDir.mkdirs();
        }

        // 保存文件
        file.transferTo(new File(uploadsDir, name));

        return DataResult.success("头像上传成功", null);
    }
    //检查用户是否存在
    public DataResult checkUserExist(String username)
    {
        UserDAO userDAO=new UserDAO();
        DataResult dataResult = new DataResult();
        if(userDAO.isUsernameUnique(username)){
            //用户不存在
            dataResult.setStatus(-1);
            dataResult.setMsg("用户不存在");
        }else{
            //若存在则返回该用户
            dataResult.setStatus(0);
            dataResult.setMsg("用户存在");
            dataResult.setData(userDAO.getUserByUsername(username));
        }
        userDAO.closeConnection();
        return dataResult;

    }

    // 删除用户及相关数据
    public DataResult deleteUserWithRelatedData(int userId) {
        UserDAO userDAO = new UserDAO();
        DataResult dataResult = new DataResult();

        // 删除用户及相关数据
        boolean success = userDAO.deleteUserWithRelatedData(userId);

        if (success) {
            userDAO.closeConnection();
            return DataResult.success("删除用户及相关数据成功", null);
        } else {
            userDAO.closeConnection();
            return DataResult.fail("删除用户及相关数据失败");
        }
    }

    //编辑用户名
    public DataResult editUsername(int userId,String name){
        UserDAO userDAO=new UserDAO();
        DataResult dataResult = new DataResult();
        if(userDAO.updateUsername(userId,name)){
            userDAO.closeConnection();
            return DataResult.success("修改用户名成功",null);
        }else{
            userDAO.closeConnection();
            return DataResult.fail("用户名已存在");
        }
    }

    //修改密码
    public DataResult editPassword(int userId,String password){
        UserDAO userDAO=new UserDAO();
        DataResult dataResult = new DataResult();
        if(userDAO.updatePassword(userId,password)){
            userDAO.closeConnection();
            return DataResult.success("修改密码成功",null);
        }else{
            userDAO.closeConnection();
            return DataResult.fail("修改密码失败");
        }
    }

    //修改邮箱
    public DataResult editEmail(int userId,String email){
        UserDAO userDAO=new UserDAO();
        DataResult dataResult = new DataResult();
        if(userDAO.updateEmail(userId,email)){
            userDAO.closeConnection();
            return DataResult.success("修改邮箱成功",null);
        }else{
            userDAO.closeConnection();
            return DataResult.fail("修改邮箱失败");
        }
    }

    //修改描述
    public DataResult editDes(int userId,String des){
        UserDAO userDAO=new UserDAO();
        DataResult dataResult = new DataResult();
        if(userDAO.updateDescription(userId,des)){
            userDAO.closeConnection();
            return DataResult.success("修改描述成功",null);
        }else{
            userDAO.closeConnection();
            return DataResult.fail("修改描述失败");
        }
    }

    // 发送好友申请
    public DataResult sendFriendRequest(int senderId, int receiverId) {
        FriendshipDAO friendShipDAO=new FriendshipDAO();
        DataResult dataResult = new DataResult();
        // 检查是否已经是好友
        if (!friendShipDAO.areFriends(senderId, receiverId)) {
            // 检查是否已经有未处理的好友申请
            if (!friendShipDAO.hasPendingFriendRequest(senderId, receiverId)) {
                // 发送好友申请
                friendShipDAO.sendFriendRequest(senderId, receiverId);
                dataResult.setStatus(0);
                dataResult.setMsg("好友申请已发送");
            } else {
                dataResult.setStatus(-1);
                dataResult.setMsg("已存在未处理的好友申请");
            }
        } else {
            dataResult.setStatus(-1);
            dataResult.setMsg("已经是好友关系");
        }
        friendShipDAO.closeConnection();
        return dataResult;
    }

    // 查看好友待接受列表
    public DataResult getPendingFriendRequests(int userId) {
        FriendshipDAO friendShipDAO=new FriendshipDAO();
        DataResult dataResult = new DataResult();
        List<Friendship> pendingRequests = friendShipDAO.getFriendRequestsToUser(userId);
        dataResult.setStatus(0);
        dataResult.setMsg("查询好友待接受列表成功");
        dataResult.setData(pendingRequests);
        friendShipDAO.closeConnection();
        return dataResult;
    }

    // 拒绝好友申请
    public DataResult rejectFriendRequest(int senderId, int receiverId) {
        DataResult dataResult = new DataResult();
        FriendshipDAO friendShipDAO=new FriendshipDAO();
        // 检查是否存在未处理的好友申请
        if (friendShipDAO.hasPendingFriendRequest(senderId, receiverId)) {
            // 拒绝好友申请
            friendShipDAO.rejectFriendRequest(senderId, receiverId);
            dataResult.setStatus(0);
            dataResult.setMsg("已拒绝好友申请");
        } else {
            dataResult.setStatus(-1);
            dataResult.setMsg("未找到对应的好友申请");
        }
        friendShipDAO.closeConnection();
        return dataResult;
    }
    public DataResult acceptFriendRequest(int senderId, int receiverId) {
        DataResult dataResult = new DataResult();
        FriendshipDAO friendShipDAO=new FriendshipDAO();
        // 检查是否存在未处理的好友申请
        if (friendShipDAO.hasPendingFriendRequest(senderId, receiverId)) {
            // 通过好友申请
            friendShipDAO.acceptFriendRequest(receiverId,senderId);
            dataResult.setStatus(0);
            dataResult.setMsg("已通过好友申请");
        } else {
            dataResult.setStatus(-1);
            dataResult.setMsg("未找到对应的好友申请");
        }
        friendShipDAO.closeConnection();
        return dataResult;
    }
    // 删除好友
    public DataResult deleteFriend(int userId, int friendId) {
        DataResult dataResult = new DataResult();
        FriendshipDAO friendshipDAO=new FriendshipDAO();
        // 检查是否是好友
        if (friendshipDAO.areFriends(userId, friendId)) {
            friendshipDAO.deleteFriendship(userId, friendId);
            dataResult.setStatus(0);
            dataResult.setMsg("删除好友成功");
        } else {
            dataResult.setStatus(-1);
            dataResult.setMsg("这两个用户不是好友关系");
        }
        friendshipDAO.closeConnection();
        return dataResult;
    }

    // 查看所有好友
    public DataResult getAllFriends(int userId) {
        DataResult dataResult = new DataResult();
        UserDAO userDAO=new UserDAO();
        FriendshipDAO friendshipDAO=new FriendshipDAO();
        List<Integer> friendIds = friendshipDAO.getAllFriends(userId);
        List<User> friends = new ArrayList<>();

        for (int friendId : friendIds) {
            User friend = userDAO.getUserById(friendId);
            friends.add(friend);
        }

        dataResult.setStatus(0);
        dataResult.setMsg("查询好友成功");
        dataResult.setData(friends);
        friendshipDAO.closeConnection();
        userDAO.closeConnection();
        return dataResult;
    }

    //获取除自己以外所有用户
    public DataResult getAllUsersExceptCurrent(int currentUserId) {
        UserDAO userDAO=new UserDAO();
        try {
            List<User> users = userDAO.getAllUsersExceptCurrentUser(currentUserId);
            return DataResult.success("获取除当前用户以外所有用户成功", users);
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.fail("获取除当前用户以外所有用户失败");
        }
    }


}