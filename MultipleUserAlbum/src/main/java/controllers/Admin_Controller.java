package controllers;

import dao.UserDAO;
import entity.DataResult;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import server.AlbumServer;
import server.UserServer;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class Admin_Controller {

    private final AlbumServer albumServer;
    private final UserServer userServer;
    @Autowired
    public Admin_Controller(AlbumServer albumServer,UserServer userServer) {
        this.albumServer = albumServer;
        this.userServer=userServer;
    }

    @RequestMapping("/viewAllAlbums")
    public String viewAllAlbums() {
        return "admin_album";
    }

    @RequestMapping("/getAllAlbums")
    @ResponseBody
    public DataResult getAllAlbums(HttpSession session) {
        User user = (User)session.getAttribute("myInfo");
        DataResult dataResult = albumServer.getAllAlbums();
        return dataResult;
    }

    @RequestMapping("/delAlbum")
    @ResponseBody
    public DataResult delAlbum(@RequestParam("albumId") String albumId) {
        return AlbumServer.deleteAlbum(Integer.parseInt(albumId));
    }

    @RequestMapping("/viewAllUsers")
    public String viewAllUsers() {
        return "admin_user";
    }

    @RequestMapping("/getAllUsers")
    @ResponseBody
    public DataResult getAllUsers(HttpSession session) {
        User user = (User) session.getAttribute("myInfo");
        DataResult dataResult = userServer.getAllUsersExceptCurrent(user.getUserId());

        return dataResult;
    }

    @RequestMapping("/delUser")
    @ResponseBody
    public DataResult delUser(@RequestParam("userId") int userId, HttpSession session) {
        DataResult dataResult= userServer.deleteUserWithRelatedData(userId);
        return dataResult;
    }
    @RequestMapping("/getUserInfo")
    public String getUserInfo(@RequestParam("userId") int userId, Model model) {
        UserDAO userDAO=new UserDAO();
        User user=userDAO.getUserById(userId);
        model.addAttribute("userInfo",user);
        return "user_info";

    }

    @RequestMapping("/getAlbumsByUserId")
    @ResponseBody
    public DataResult getAlbumsByUserId(@RequestParam("userId") int userId) {
        DataResult dataResult = albumServer.getAlbumsByUserID(userId);
        return dataResult;
    }
    @RequestMapping("/getAlbum")
    @ResponseBody
    public DataResult getAlbum(@RequestParam("userId")int userId ){

        DataResult dataResult;
        dataResult=albumServer.getAlbumsByUserID(userId);
        return dataResult;
    }
    private boolean isAdmin(HttpSession session) {

        return true;
    }
}
