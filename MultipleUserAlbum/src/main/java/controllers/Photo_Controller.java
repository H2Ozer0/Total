package controllers;

import dao.PhotoDAO;
import entity.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Timestamp;


import java.util.List;


@Controller
@RequestMapping("/photos")
public class Photo_Controller {

    @Autowired
    private PhotoDAO photoDAO;

    @GetMapping("/list")
    public String showPhotoList(Model model) {
        List<Photo> photos = photoDAO.getAllPhotos();
        model.addAttribute("photos", photos);
        return "photo-list";
    }

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("photo", new Photo(0, "", "", null, false));
// 用于绑定上传表单的实体
        return "upload-form";
    }


    @GetMapping("/delete/{photoID}")
    public String deletePhoto(@PathVariable int photoID) {
        // 处理删除逻辑
        photoDAO.deletePhoto(photoID);
        return "redirect:/photos/list";
    }
@PostMapping("/upload")
public String handleUpload(@ModelAttribute("photo") Photo photo,
                           @RequestParam("file") MultipartFile file) {
    // 处理上传逻辑
    // 在这里你可以将上传的文件保存到磁盘或云存储，然后将文件路径设置到照片对象中
    // 例如：photo.setPath("/path/to/uploaded/file.jpg");

    // 设置上传时间等其他属性
    photo.setUploadTime(new Timestamp(System.currentTimeMillis()));
    photo.setDeleted(false);

    // 保存到数据库
    photoDAO.insertPhoto(photo);

    return "redirect:/photos/list";
}




    @GetMapping("/edit/{photoID}")
    public String showEditForm(@PathVariable int photoID, Model model) {
        // 获取要编辑的照片信息
        Photo photo = photoDAO.getPhotoByID(photoID);
        model.addAttribute("photo", photo);
        return "edit-form";
    }

    @PostMapping("/edit")
    public String handleEdit(@ModelAttribute("photo") Photo photo) {
        // 处理编辑逻辑
        photoDAO.updatePhoto(photo);
        return "redirect:/photos/list";
    }

    // 其他操作，如查看单个照片等
}
