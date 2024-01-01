package controllers;

import dao.PhotoDAO;
import dao.UserDAO;
import entity.Album;
import entity.DataResult;
import entity.Photo;
import entity.Comment;
import entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import server.AlbumServer;
import server.InteractServer;
import server.UserServer;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;

@Controller
public class Album_Controller {

    // 在 AlbumController 中的 submitNewAlbum 方法中修改
    @RequestMapping("/submitNewAlbum")
    @ResponseBody
    public DataResult submitNewAlbum(@RequestParam("title") String title,
                                     @RequestParam("desc") String desc,
                                     @RequestParam("userId") String userId,
                                     @RequestParam("type") String category) {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());  // 使用当前时间作为创建时间
        boolean isPublic = true;  // 假设默认是公开的
        boolean isDeleted = false;  // 假设默认未删除
        int favoritesCount = 0;  // 初始收藏数为0
        int likesCount = 0;  // 初始点赞数为0
        int creatorID = Integer.parseInt(userId);

        Album album = new Album(title, desc, createdAt, isPublic, isDeleted, favoritesCount, likesCount, creatorID);
        return AlbumServer.insertAlbum(album);
    }

    @RequestMapping("/album_content")
    public String enterAlbum(Model model) {
        InteractServer interactServer =new InteractServer();
        AlbumServer albumServer=new AlbumServer();
        UserDAO userDAO=new UserDAO();
        try {
            List<Comment> albumCommentList = (List<Comment>) interactServer.getCommentsByAlbum(1).getData();
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
        model.addAttribute("photoList",photoList);
        model.addAttribute("albumInfo",album);
        model.addAttribute("isFollow",10);
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

    // 在 AlbumController 中的 submitEditAlbum 方法中修改
    @RequestMapping("/submitEditAlbum")
    @ResponseBody
    public DataResult submitEditAlbum(@RequestParam("title") String title,
                                      @RequestParam("desc") String desc,
                                      @RequestParam("userId") String userId,
                                      @RequestParam("albumId") String albumId,
                                      @RequestParam("type") String category) {
        Album existingAlbum = (Album) AlbumServer.getAlbumByID(Integer.parseInt(albumId)).getData();

        // 保留原有属性，修改 title 和 desc
        existingAlbum.setAlbumName(title);
        existingAlbum.setDescription(desc);

        return AlbumServer.updateAlbum(existingAlbum);
    }

    @RequestMapping("/delAlbum")
    @ResponseBody
    public DataResult delAlbum(@RequestParam("albumId") String albumId, HttpSession session) {
        User user = (User) session.getAttribute("myInfo");
        System.out.println("session myInfo:" + user.getUserId());
        return AlbumServer.deleteAlbum(Integer.parseInt(albumId));
    }

    // ... 其他方法的修改

}
