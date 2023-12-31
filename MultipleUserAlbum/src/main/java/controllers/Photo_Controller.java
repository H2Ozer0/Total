//package controllers;
//
//import entity.Photo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import server.PhotoServer;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/photos")
//public class Photo_Controller {
//
//    private final PhotoServer photoServer;
//
//    @Autowired
//    public Photo_Controller(PhotoServer photoServer) {
//        this.photoServer = photoServer;
//    }
//
//    @GetMapping
//    public String getAllPhotos(Model model) {
//        List<Photo> photos = photoServer.getAllPhotos();
//        model.addAttribute("photos", photos);
//        return "photo/allPhotos"; // Assuming you have a view named "allPhotos.jsp" to display all photos.
//    }
//
//    @GetMapping("/{photoID}")
//    public String getPhotoById(@PathVariable int photoID, Model model) {
//        Photo photo = photoServer.getPhotoByID(photoID);
//        model.addAttribute("photo", photo);
//        return "photo/photoDetails"; // Assuming you have a view named "photoDetails.jsp" to display photo details.
//    }
//
//    @GetMapping("/upload")
//    public String showUploadForm(Model model) {
//        // You might want to provide additional data to the view for the upload form.
//        return "photo/uploadForm"; // Assuming you have a view named "uploadForm.jsp" for photo upload.
//    }
//
//    @PostMapping("/upload")
//    public String uploadPhoto(@ModelAttribute Photo photo) {
//        photoServer.uploadPhoto(photo);
//        return "redirect:/photos"; // Redirect to the page displaying all photos after upload.
//    }
//
//    @GetMapping("/delete/{photoID}")
//    public String deletePhoto(@PathVariable int photoID) {
//        photoServer.deletePhoto(photoID);
//        return "redirect:/photos"; // Redirect to the page displaying all photos after deletion.
//    }
//
//    @GetMapping("/edit/{photoID}")
//    public String showEditForm(@PathVariable int photoID, Model model) {
//        Photo photo = photoServer.getPhotoByID(photoID);
//        model.addAttribute("photo", photo);
//        return "photo/editForm"; // Assuming you have a view named "editForm.jsp" for photo editing.
//    }
//
//    @PostMapping("/edit")
//    public String editPhoto(@ModelAttribute Photo photo) {
//        photoServer.updatePhoto(photo);
//        return "redirect:/photos"; // Redirect to the page displaying all photos after editing.
//    }
//}
