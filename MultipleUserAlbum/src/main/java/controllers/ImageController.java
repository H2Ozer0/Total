package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;



@Controller
public class ImageController {
    String DEAFULT_AVATAR="/DEFAULT/img.png";
    public static final int AVATAR = 0;
    public static final int IMG = 1;
    public void handleRp(HttpServletResponse rp,String absolutepath,String filename,int type) {
        String filePath=absolutepath+filename;
        String DEFAULT_PATH=absolutepath+DEAFULT_AVATAR;
        File imageFile = new File(filePath);
        if(!imageFile.exists()) {
            imageFile = new File(DEFAULT_PATH);
        }
        if (imageFile.exists()) {
            FileInputStream fis = null;
            OutputStream os = null;
            try {
                fis = new FileInputStream(imageFile);
                os = rp.getOutputStream();
                int count = 0;
                byte[] buffer = new byte[1024 * 8];
                while ((count = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                    os.flush();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @RequestMapping(value = "/getAvatar")
    @ResponseBody
    public void getAvatar(@RequestParam("id")String id, HttpServletResponse rp, HttpServletRequest request){
        String absolutepath = request.getServletContext().getRealPath("/AVATER");
        String filename=id+".png";
        handleRp(rp,absolutepath,filename,0);
    }
}
