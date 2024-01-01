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

    @Autowired
    public MeController(AlbumServer albumServer,UserServer userServer) {
        this.albumServer= albumServer;
        this.userServer= userServer;
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
    @RequestMapping("/getMyAlbum")
    @ResponseBody
    public DataResult getMyAlbum( HttpSession session){
        User user = (User)session.getAttribute("myInfo");

        DataResult dataResult;
//        dataResult=albumServer.getAlbumsByUserID(user.getUserId());
        dataResult=albumServer.getAlbumsByUserID(user.getUserId());


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


}
