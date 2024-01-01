
package controllers;

import dao.PhotoDAO;
import dao.UserDAO;
import entity.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import server.AlbumServer;
import server.InteractServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/albums")
public class Album_Controller {

    private final AlbumServer albumServer;

    @Autowired
    public Album_Controller(AlbumServer albumServer) {
        this.albumServer = albumServer;
    }
    @RequestMapping("/createalbum_page")
    public String createpate()
    {
        return "createAlbum";
    };


    @PostMapping("/createAlbum")
    public String createAlbum(
            @RequestParam("title") String title,
            @RequestParam("desc") String description,
            @RequestParam("auth") boolean isShared,
            @RequestParam("coverImage") MultipartFile coverImage,
            @RequestParam("coverImageBase64") String coverImageBase64, HttpSession session, HttpServletRequest request,Model model) throws IOException {
        File cover=new File(coverImage.getOriginalFilename());
        System.out.println(cover.getName()+"这是图片名字");
        User user = (User)session.getAttribute("myInfo");
        int CreatorID=user.getUserId();
        Album album=new Album(title, description,new Timestamp(System.currentTimeMillis()), isShared, false, 0, 0,CreatorID);
        albumServer.insertAlbum(album);
        String absolutepath = request.getServletContext().getRealPath("\\Image\\Cover");
        String filepath=absolutepath+'\\'+title+".png";
        FileUtil.deleteFile(filepath);
        FileUtil.saveFile(coverImage,filepath);
        model.addAttribute("message", "相册创建成功!");
        return "my_albums";

    }

    @RequestMapping("/album_content")
    public String enterAlbum(Model model) {
        InteractServer interactServer =new InteractServer();
        AlbumServer albumServer=new AlbumServer();
        UserDAO userDAO=new UserDAO();
        try {
            List<Comment> albumCommentList = (List<Comment>) interactServer.getCommentsByAlbum(1).getData();
            System.out.println(albumCommentList);
            model.addAttribute("commentInfo",albumCommentList);
        }catch (Exception e){
            e.printStackTrace();
        }
        Album album=(Album) albumServer.getAlbumByID(1).getData();
        User user= userDAO.getUserById(1);
        PhotoDAO photoDAO =new PhotoDAO();
        int res = 0;
//        if(user!= null){
//            if(interactServer.checkFollow(user.getId(),album.getUserId()).getStatus() == 0){
//                res = 1;
//            }
//        }
        List<Photo> photoList=photoDAO.getPhotosInAlbum(1);
        System.out.println("ALBUM FOLLOW:" + res);
//        model.addAttribute("photoList",photoList);
 //       model.addAttribute("albumInfo",album);
//        model.addAttribute("isFollow",10);
        return "album_content";
    }



//    @RequestMapping("/album")
//    public ModelAndView enterAlbum(@RequestParam("albumId") int albumId, Model model, HttpSession session) {
//        List<Photo> photoList = (List<Photo>) AlbumServer.getPhotosInAlbum(albumId).getData();
//        albumId=1;
//        Album album = (Album) AlbumServer.getAlbumByID(albumId).getData();
//
//        try {
//            List<Comment> albumCommentList = (List<Comment>) InteractServer.getCommentsByAlbum(albumId).getData();
//            model.addAttribute("commentInfo", albumCommentList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        User user = (User) session.getAttribute("myInfo");
////        int res = 0;
////        if (user != null) {
////            if (InteractServer.checkFollow(user.getId(), album.getUserId()).getStatus() == 0) {
////                res = 1;
////            }
////        }
////        System.out.println("ALBUM FOLLOW:" + res);
////        model.addAttribute("photoList", photoList);
//        model.addAttribute("albumInfo", album);
////        model.addAttribute("isFollow", res);
//        return new ModelAndView("album_content");
//    }

    @RequestMapping("/editAlbum")
    public String editAlbum(@RequestParam("albumId") String albumId, Model model) {
        Album album = (Album) AlbumServer.getAlbumByID(Integer.parseInt(albumId)).getData();
        model.addAttribute("album", album);
        return "album_edit";
    }



    @RequestMapping("/delAlbum")
    @ResponseBody
    public DataResult delAlbum(@RequestParam("albumId") String albumId, HttpSession session) {
        User user = (User) session.getAttribute("myInfo");
        System.out.println("session myInfo:" + user.getUserId());
        return AlbumServer.deleteAlbum(Integer.parseInt(albumId));
    }


    @RequestMapping("/addComment")
    @ResponseBody
    public DataResult addNewComment(@RequestParam("TEXT")String commentText, @RequestParam("AID")int aId,HttpSession session){
        User user= (User) session.getAttribute("myInfo");
        InteractServer interactServer=new InteractServer();
        UserDAO userDAO=new UserDAO();
        if(user!= null){
            System.out.println("AddComment添加评论");
            int uId = user.getUserId();
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
            Comment comment=new Comment(aId, commentText, uId,userDAO.getUserById(uId).getUsername() ,currentTimestamp);
            return interactServer.insertComment(comment);
        }else{
            System.out.println("AddComment 未登录");
            DataResult dataResult = new DataResult();
            dataResult.setStatus(2);
            dataResult.setMsg("没有登录");
            return dataResult;
        }
    }

    @RequestMapping("/delcomment")
    @ResponseBody
    public DataResult delComment(@RequestParam("CID")int cId, @RequestParam("UID")int uId,HttpSession session){
        User user= (User) session.getAttribute("myInfo");
        InteractServer interactServer=new InteractServer();
        System.out.println("删除评论！！！！！" + uId + "***" + user.getUserId());
        if(user!= null && user.getUserId()==uId){
            System.out.println("DelComment删除评论");
            return interactServer.deleteComment(cId);
        }else{
            System.out.println("删除无效");
            DataResult dataResult = new DataResult();
            dataResult.setStatus(2);
            dataResult.setMsg("没有删除权限");
            return dataResult;
        }
    }

    @RequestMapping("/addLike")
    @ResponseBody
    public DataResult addLike(@RequestParam("albumId") int albumId, @RequestParam("userId") int userId){
        InteractServer interactServer =new InteractServer();
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
        Like like=new Like(albumId,userId,currentTimestamp);
        System.out.println(2222222);
        return interactServer.insertLike(like);
    }
//    @GetMapping("/{albumID}")
//    public String viewAlbum(@PathVariable int albumID, Model model) {
//        Album album = albumServer.getAlbumByID(albumID);
//        model.addAttribute("album", album);
//        return "albumDetails";
//    }
//
//    @GetMapping("/search")
//    public String searchPublicAlbums(@RequestParam String albumName, Model model) {
//        List<Album> publicAlbums = albumServer.getPublicAlbumsByName(albumName);
//        model.addAttribute("publicAlbums", publicAlbums);
//        return "searchResults";
//    }
//
//    @GetMapping("/{albumID}/likesCount")
//    @ResponseBody
//    public int getLikesCount(@PathVariable int albumID) {
//        return albumServer.getLikesCount(albumID);
//    }
//
//    @GetMapping("/{albumID}/favoritesCount")
//    @ResponseBody
//    public int getFavoritesCount(@PathVariable int albumID) {
//        return albumServer.getFavoritesCount(albumID);
//    }
//
//    @GetMapping("/{albumID}/createdAt")
//    @ResponseBody
//    public Timestamp getCreatedAt(@PathVariable int albumID) {
//        return albumServer.getCreatedAt(albumID);
//    }
//
//    @PostMapping("/{albumID}/updateName")
//    @ResponseBody
//    public String updateAlbumName(@PathVariable int albumID, @RequestParam String newAlbumName) {
//        albumServer.updateAlbumName(albumID, newAlbumName);
//        return "Album name updated successfully!";
//    }
//
//    @PostMapping("/{albumID}/updateDescription")
//    @ResponseBody
//    public String updateAlbumDescription(@PathVariable int albumID, @RequestParam String newDescription) {
//        albumServer.updateAlbumDescription(albumID, newDescription);
//        return "Album description updated successfully!";
//    }

}
