package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class Register_Controller {
    /**
     * 用于跳转到注册界面
     * @param model
     * @return
     */
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
    public String register(@RequestParam String username, @RequestParam String password) {
        // 处理逻辑

        // 返回响应内容
        return "Received username: " + username + ", password: " + password;
    }
}
