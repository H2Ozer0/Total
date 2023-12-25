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



import dao.AlbumDAO;
import dao.UserDAO;
import entity.DataResult;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import server.UserServer;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



@Controller
@SessionAttributes(value = {"isLogin","myInfo"})
public class Login_Controller {
    private final UserServer userServer;
    /**
     * 用于跳转到登录界面
     * @return 视图名称
     */
    @RequestMapping("/login_page")
    public String loginPage() {
        return "login";
    }

    @Autowired
    public Login_Controller(UserServer userServer) {
        this.userServer= userServer;
    }
    @RequestMapping  ("/login")
    @ResponseBody
    public DataResult login(@RequestParam("username") String username, @RequestParam("pass") String password,Model model)
    {
        //用于测试
        User u=new User("smj","abc123","3185513942@qq.com",false,"测试账号");
        DataResult dataResult_test=new DataResult();
        dataResult_test.setStatus(0);
        dataResult_test.setMsg("登录成功");
        dataResult_test.setData(u);
        model.addAttribute("isLogin",true);
        model.addAttribute("myInfo",dataResult_test.getData());
        return   dataResult_test;
//        //用于测试



//        if(userServer.existsByUsername(username))
//        {
//
//            User user=userServer.getUserByUsername(username);
//            String truePasswd=user.getPassword();
//            if(truePasswd.equals(password))
//            {
//                DataResult dataResult=new DataResult();
//                dataResult.setStatus(0);
//                dataResult.setMsg("登录成功");
//                dataResult.setData(user);
//                model.addAttribute("isLogin",true);
//                model.addAttribute("myInfo",dataResult.getData());
//                return   dataResult;
//            }
//            else
//            {
//                return DataResult.fail("登录失败，密码错误");
//            }
//        }
//        else
//        {
//            return DataResult.fail("登录失败，用户不存在");
//        }

    }

    @RequestMapping  ("/userhome")
    public String login(Model model)
    {
        return "userhome";
    }
}

