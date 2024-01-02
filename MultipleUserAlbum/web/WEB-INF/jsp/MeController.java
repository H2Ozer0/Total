package controllers;
import java.io.File;
import entity.Album;
import entity.DataResult;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import server.AlbumServer;
import server.InteractServer;
import server.UserServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import server.AlbumServer;
@Controller
@RequestMapping("/me")
public class MeController {
    private final UserServer userServer;
    private  final AlbumServer albumServer;
    private  final InteractServer interactServer;
    @Autowired
    public MeController(AlbumServer albumServer,UserServer userServer,InteractServer interactServer) {
        this.albumServer= albumServer;
        this.userServer= userServer;
        this.interactServer= interactServer;
    }
    @RequestMapping  ("/albums")
    public String albums(Model model, HttpSession session)
    {
        User user = (User)session.getAttribute("myInfo");
        model.addAttribute("myInfo",user);
        return "my_albums";
    }
    @RequestMapping("/photos")
    public String enterMyPhotos(HttpSession session,Model model)
    {
        User user = (User)session.getAttribute("myInfo");
        List<Album> albumList= (List<Album>) albumServer.getPublicAlbumsByName(user.getUsername());
        model.addAttribute("myInfo",user);
        model.addAttribute("albumList",albumList);
        return "my_photos";
    }
    @RequestMapping("/getfavourite")
    public String getfavourite()
    {
        return "favourite";
    }
    @RequestMapping  ("/favourite")
    public String favorite(Model model, HttpSession session)
    {
        User user = (User)session.getAttribute("myInfo");
        model.addAttribute("myInfo",user);
        return "favourite";
    }
    @RequestMapping("/getMyAlbum")
    @ResponseBody
    public DataResult getMyAlbum( HttpSession session){
        User user = (User)session.getAttribute("myInfo");

        DataResult dataResult;
//        dataResult=albumServer.getAlbumsByUserID(user.getUserId());
        dataResult=albumServer.getAlbumsByUserID(user.getUserId());


        return dataResult;
    }
    @RequestMapping("/friendship")
    public String friendship()
    {
        return "friendship";
    }
    @RequestMapping("/getMyFriendship")
    @ResponseBody
    public DataResult getMyFriendship(HttpSession session) {
        try {
            User user = (User) session.getAttribute("myInfo");
            if (user == null) {
                return DataResult.fail("User not authenticated");
            }

            // Assuming you have a method in your UserServer to retrieve friendships for the user
            // Modify the following line according to your actual method in UserServer
            DataResult result = userServer.getAllFriends(user.getUserId());
            List<User> userlist=(List<User>)result.getData();
            System.out.println("好友个数为"+userlist.size());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.fail("An error occurred while retrieving friendships");
        }
    }
    @RequestMapping  ("/getMyFavorites")
    @ResponseBody
    public DataResult getMyFavorites(Model model, HttpSession session)
    {
        User user = (User)session.getAttribute("myInfo");
        model.addAttribute("myInfo",user);
        DataResult dataResult;
        dataResult=interactServer.getFavoriteAlbumsByUser(user.getUserId());
        return dataResult;
    }
    @PostMapping("/updateUsername")
    @ResponseBody
    public DataResult updateUsername(@RequestParam String fieldValue,HttpSession session,Model model) {
        User user = (User)session.getAttribute("myInfo");
        DataResult dataResult=userServer.editUsername(user.getUserId(),fieldValue);;
        int status=dataResult.getStatus();
        if(status==0)
        {
            user.setUsername(fieldValue);
        }
        model.addAttribute("myInfo",user);
        return  dataResult;

    }

    @PostMapping("/updateEmail")
    @ResponseBody
    public DataResult updateEmail(@RequestParam String fieldValue,HttpSession session,Model model) {
        User user = (User)session.getAttribute("myInfo");
        DataResult dataResult=userServer.editEmail(user.getUserId(),fieldValue);;
        int status=dataResult.getStatus();
        if(status==0)
        {
            user.setEmail(fieldValue);
        }
        model.addAttribute("myInfo",user);
        return  dataResult;

    }

    @PostMapping("/updateDescription")
    @ResponseBody
    public DataResult updateDescription(@RequestParam String fieldValue,HttpSession session,Model model) {
        User user = (User)session.getAttribute("myInfo");
        DataResult dataResult=userServer.editDes(user.getUserId(),fieldValue);;
        int status=dataResult.getStatus();
        if(status==0)
        {
            user.setDescription(fieldValue);
        }
        model.addAttribute("myInfo",user);
        return  dataResult;

    }

    @PostMapping("/uploadAvatar")
    @ResponseBody
    public DataResult handleFileUpload(@RequestParam("avatarFile") MultipartFile file,HttpServletRequest request,HttpSession session) throws IOException {
            File avater= new File(file.getOriginalFilename());
            System.out.println("介绍到了文件");
            String absolutepath = request.getServletContext().getRealPath("/AVATER");
            User user = (User)session.getAttribute("myInfo");
            String username=user.getUsername();
            String filepath=absolutepath+"/"+username+".png";
            FileUtil.deleteFile(filepath);
            System.out.println("文件删除了");
            // 将上传的文件保存到指定路径
            FileUtil.saveFile(file, filepath);
            DataResult successResult = DataResult.success("File uploaded successfully!", null);
            return successResult;

    }

    @RequestMapping("/delfriendship")
    @ResponseBody
    public DataResult delfriendship(@RequestParam("friendid")int friendid,HttpSession session)
    {
        User user = (User)session.getAttribute("myInfo");
        int myid= user.getUserId();
        DataResult dataResult=userServer.deleteFriend(myid,friendid);

        return dataResult;
    }
    @RequestMapping("/addFriend")
    @ResponseBody
    public DataResult addFriend(@RequestParam("friendInputValue")int senderid,HttpSession session)
    {
        User user = (User)session.getAttribute("myInfo");
        int receverid= user.getUserId();
        DataResult dataResult;
        dataResult=userServer.sendFriendRequest(senderid,receverid);
        System.out.println("从"+senderid+"到"+receverid);
        return dataResult;
    }
    @RequestMapping("/friendapplication")
    public String friendshipapplication(HttpSession session)
    {
        User user = (User)session.getAttribute("myInfo");
        return "friendapplication";
    }
    @RequestMapping("/get_application")
    @ResponseBody
    public DataResult get_application(HttpSession session)
    {
        User user = (User)session.getAttribute("myInfo");
        DataResult dataResult=userServer.getPendingFriendRequests(user.getUserId());

        return dataResult;
    }
    @RequestMapping("/accept_application")
    @ResponseBody
    public DataResult accept_application(@RequestParam("receverid")int receverid, HttpSession session)
    {
        User user = (User)session.getAttribute("myInfo");
        DataResult dataResult;
        dataResult=userServer.acceptFriendRequest(receverid,user.getUserId());
        return dataResult;
    }
    @RequestMapping("/reject_application")
    @ResponseBody
    public DataResult reject_application(@RequestParam("receverid")int receverid, HttpSession session)
    {
        User user = (User)session.getAttribute("myInfo");
        DataResult dataResult;
        dataResult=userServer.rejectFriendRequest(receverid,user.getUserId());
        return dataResult;
    }
    @RequestMapping("/getfriendalbums")
    @ResponseBody
    public DataResult getfriendalbums(@RequestParam("friendid")int friendid,HttpSession session)
    {
        User user = (User)session.getAttribute("myInfo");
        DataResult dataResult=AlbumServer.getFriendSharedAlbums(user.getUserId());
        List<Album>albumList=(List<Album>)dataResult.getData();
        System.out.println("长度为"+albumList.size());
        return dataResult;
    }
    @RequestMapping("/friendalbums_page")
    public String friendalbums_page(@RequestParam("friendid")int friendid,Model model)
    {
        model.addAttribute("userid",friendid);
        return "FriendAlbums";
    }
}
