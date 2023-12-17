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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



@Controller
public class Login_Controller {
    private final UserDAO  userDAO;
    /**
     * 用于跳转到登录界面
     * @return 视图名称
     */
    @RequestMapping("/login_page")
    public String loginPage() {
        return "login";
    }

    @Autowired
    public Login_Controller(UserDAO userDAO) {
        this.userDAO= userDAO;
    }
    @RequestMapping ("/login")
    @ResponseBody
    public DataResult login(@RequestParam("id") String userid, @RequestParam("pass") String password)
    {
      User user=userDAO.getUserById(userid);
      String truepasswd=user.getPassword();
      if(user==null)
      {
          return DataResult.fail("ID错误，用户不存在");
      }
       else if (!truepasswd.equals(password)) {
        System.out.println("输入密码为"+password);
        System.out.println("用户密码为"+user.getPassword());
        return DataResult.fail("密码错误");
        } else {
           return DataResult.success("登录成功",user);
        }
    }



}

