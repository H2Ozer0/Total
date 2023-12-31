package controllers;

import entity.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import server.UserServer;



import javax.servlet.http.HttpServletRequest;

@Controller
public class Register_Controller {
    /**
     * 用于跳转到注册界面
     * @param model
     * @return
     */
    @Autowired
    private UserServer userServer;



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
    public DataResult register(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email,@RequestParam("description") String description)
    {
        DataResult dataResult;
        dataResult=userServer.register(username,password,email,false,description);
        return dataResult;
    }
}

