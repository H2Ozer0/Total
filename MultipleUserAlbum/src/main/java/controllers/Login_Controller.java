//package controllers;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Controller
//public class Login_Controller {
//    /**
//     * 用于跳转到登录界面
//     * @param model
//     * @return
//     */
//    @RequestMapping("/login_page")
//    public String login_page(HttpServletRequest request, Model model)
//    {
//        return "login";
//    }
//
//    /**
//     * 用于处理前端发送的登录信息
//     * @param username
//     * @param password
//     * @return
//     */
//    @RequestMapping("/login")
//    @ResponseBody
//    public String login(@RequestParam String username, @RequestParam String password) {
//        // 处理逻辑
//
//        // 返回响应内容
//        return "Received username: " + username + ", password: " + password;
//    }
//}

package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;

@Controller
public class Login_Controller {

    /**
     * 用于跳转到登录界面
     * @return 视图名称
     */
    @RequestMapping("/login_page")
    public String loginPage() {
        return "login";
    }

    /**
     * 用于处理前端发送的登录信息
     * @param username 用户名参数
     * @param password 密码参数
     * @param model 用于传递数据给视图
     * @return 视图名称
     */
    @RequestMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        if ("demo".equals(username) && "password".equals(password)) {
            // 认证成功，将用户名添加到Model中
            model.addAttribute("username", username);
            // 返回成功登录的视图，比如用户的主页
            return "dashboard";
        } else {
            // 认证失败，将错误信息添加到Model中
            model.addAttribute("error", "Invalid username or password");
            // 返回登录页面并显示错误信息
            return "login";
        }
    }
}


