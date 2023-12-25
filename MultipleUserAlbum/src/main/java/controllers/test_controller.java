package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import entity.User;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class test_controller {
    @RequestMapping("/test1")
    public String test(Model model, HttpServletRequest request) {
        User u=new User("smj","abc123","3185513942@qq.com",false,"测试账号");
        model.addAttribute("userInfo",u);
        return "userHome";
    }

    @RequestMapping("/test_album")
    public String test_album(Model model,HttpServletRequest request)
    {
        User u=new User("smj","abc123","3185513942@qq.com",false,"测试账号");
        model.addAttribute("userInfo",u);
        return "my_albums";
    }

}


