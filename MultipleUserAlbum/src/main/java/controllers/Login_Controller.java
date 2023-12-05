package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;

@Controller
public class Login_Controller {
    /**
     * 用于跳转到登录界面
     * @param model
     * @return
     */
    @RequestMapping("/login_page")
    public String login_page(HttpServletRequest request, Model model)
    {
        return "login";
    }

    /**
     * 用于处理前端发送的登录信息
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam String username, @RequestParam String password) {
        // 处理逻辑

        // 返回响应内容
        return "Received username: " + username + ", password: " + password;
    }
}



