package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;

@Controller
public class Register_Controller {
    /**
     * 用于跳转到注册界面
     * @param model
     * @return
     */
    @Autowired
    private UserService userService;


    @RequestMapping("/register_page")
    public String registerPage(HttpServletRequest request, Model model) {
        return "register";
    }
    @RequestMapping("/register_page")
    public String resgister_page(HttpServletRequest request, Model model)
    {
        return "register";
    }

    /**
     * 用于处理前端发送的注册信息
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/Regestion")
    @ResponseBody
    public String register(@RequestParam String username, @RequestParam String password, @RequestParam String email, boolean isAdmin, String description) {
        // 检查用户名是否已被占用
        if (userService.existsByUsername(username)) {
            return "have existed！";
        }

        String userId = String.valueOf(userService.getTotalNumberOfUser() + 1);
        // 将用户保存到数据库（你需要在 UserService 中实现这个方法
        userService.saveUser(userId, username, password, email, isAdmin, description);
        // 返回响应内容
        return "Received username: " + username + ", password: " + password;
    }
}

