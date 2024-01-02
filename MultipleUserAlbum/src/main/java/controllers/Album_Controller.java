
package controllers;

import dao.*;
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
import server.PhotoServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/albums")
public class Album_Controller {
    private final PhotoServer photoServer;
    private final AlbumServer albumServer;


    @Autowired
    public Album_Controller(AlbumServer albumServer,PhotoServer photoServer) {
        this.albumServer = albumServer;
        this.photoServer=photoServer;
    }
    @RequestMapping("/createalbum_page")
    public String createpate()
    {
        return "createAlbum";
    };
    @RequestMapping("/upload_photo")
    public String upload(HttpSession session,Model model)
    {
        User user = (User)session.getAttribute("myInfo");
        int userId = user.getUserId();
        DataResult dataResult=AlbumServer.getAlbumsByUserID(userId);

        List<Album> albumList = (List<Album>)dataResult.getData();
        model.addAttribute("albumList",albumList);
        model.addAttribute("userId",userId);
        return "photo_upload";
    };
    @RequestMapping("/upload")
    @ResponseBody
    public DataResult upload(@RequestParam("file") MultipartFile[] files,
                             @RequestParam("albumId") int albumId,
                             HttpSession session,HttpServletRequest request) throws Exception {
        String absolutepath = request.getServletContext().getRealPath("");
        User user = (User) session.getAttribute("myInfo");
        int userId = user.getUserId();  // Assuming userId is a String, modify as needed

        // Iterate through each file in the array and process
        for (MultipartFile file : files) {
            String filename=file.getOriginalFilename();
            // You can access file information such as file.getOriginalFilename(), file.getSize(), etc.
            // Call your uploadPhoto method for each file
            DataResult result = photoServer.uploadPhoto(file,userId,albumId,absolutepath,filename);
            // Process result if needed
        }

        // You can return an overall result if needed
        return DataResult.success("All files uploaded successfully",null);
    }

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
    public String enterAlbum(@RequestParam("albumId")int albumId,Model model, HttpSession session) {
        User user = (User)session.getAttribute("myInfo");
        InteractServer interactServer =new InteractServer();
        AlbumServer albumServer=new AlbumServer();
        AlbumDAO albumDAO =new AlbumDAO();
        UserDAO userDAO=new UserDAO();
        try {
            List<Comment> albumCommentList = (List<Comment>) interactServer.getCommentsByAlbum(albumId).getData();
            System.out.println(albumCommentList);
            model.addAttribute("creatorName",userDAO.getUserById(albumDAO.getCreatorIDByAlbumID(albumId)).getUsername());
            model.addAttribute("commentInfo",albumCommentList);
        }catch (Exception e){
            e.printStackTrace();
        }
        Album album=(Album) albumServer.getAlbumByID(albumId).getData();
        PhotoDAO photoDAO =new PhotoDAO();
        FavoriteDAO favoriteDAO=new FavoriteDAO();
        int res = 0;
        if(user!= null){
            if(favoriteDAO.isAlbumFavoritedByUser(user.getUserId(),album.getAlbumID())){
                res = 1;//已经收藏过
            }else{
                res=0;
            }
        }
        List<Photo> photoList=photoDAO.getPhotosInAlbum(albumId);
        System.out.println("ALBUM FOLLOW:" + res);
//        model.addAttribute("photoList",photoList);
        model.addAttribute("albumInfo",album);
        model.addAttribute("isFollow",res);
        return "album_content";
    }
    @RequestMapping("/showphotos")
    public String showphotos(@RequestParam("albumId")int albumId, Model model) {
        model.addAttribute("albumId",albumId);
        return "showphotos";
    }
@RequestMapping("/getPhotos")
@ResponseBody
public DataResult getphotos(@RequestParam("albumId")int albumId)
{
    DataResult dataResult=AlbumServer.getPhotosInAlbum(albumId);
    return dataResult;
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
    public DataResult delAlbum(@RequestParam("albumId") int albumId, HttpSession session) {
        System.out.println("删除相册中");
        User user = (User) session.getAttribute("myInfo");
        System.out.println("session myInfo:" + user.getUserId());
        System.out.println("删除相册中");
        return AlbumServer.deleteAlbum(albumId);
    }

    @RequestMapping("/addComment")
    @ResponseBody
    public DataResult addNewComment(@RequestParam("TEXT")String commentText, @RequestParam("AID")int aId,HttpSession session){

        try {
            UserDAO userDAO=new UserDAO();
            User user= (User) session.getAttribute("myInfo");
            InteractServer interactServer=new InteractServer();
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
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("添加评论时发生异常: " + e.getMessage());
        }
        return null;
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
    public DataResult addLike(@RequestParam("albumId") int albumId, @RequestParam("userId") int userId) throws SQLException {
        InteractServer interactServer =new InteractServer();
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
        Like like=new Like(albumId,userId,currentTimestamp);
        AlbumDAO albumDAO=new AlbumDAO();
        LikeDAO likeDAO=new LikeDAO();
        DataResult dataResult=new DataResult();
        if(likeDAO.userLikedAlbum(userId,albumId)){
            System.out.println("已经点过赞了");
            dataResult.setStatus(-1);
        }else{
            System.out.println("还没点赞");
            dataResult.setStatus(0);
        }
        interactServer.insertLike(like);
        albumDAO.updateAlbumLikesCount(albumId);
        return dataResult;
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
