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
    String DEAFULT="/DEFAULT/img.png";
    public static final int AVATAR = 0;
    public static final int IMG = 1;
    public void handleRp(HttpServletResponse rp,String absolutepath,String filename,int type) {
        String filePath=absolutepath+'/'+filename;
        String DEFAULT_PATH=absolutepath+DEAFULT;
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
    public void getAvatar(@RequestParam("username")String username, HttpServletResponse rp, HttpServletRequest request){
        String absolutepath = request.getServletContext().getRealPath("/AVATER");
        String filename=username+".png";
        System.out.println("头像路径"+absolutepath+'/'+filename);
        handleRp(rp,absolutepath,filename,0);
    }
    @RequestMapping(value = "/getCover")
    @ResponseBody
    public void getImage(@RequestParam("url")String url,HttpServletResponse rp,HttpServletRequest request){
        String absolutepath = request.getServletContext().getRealPath("/Image/Cover");
        System.out.println("url为"+url);
        String filename=url+".png";
        handleRp(rp,absolutepath,filename, 1);

    }
    @RequestMapping("/getphoto")
    @ResponseBody
    public void getphoto(@RequestParam("path")String path,HttpServletResponse rp,HttpServletRequest request)
    {
        String truepath=convertToBackslash(path);
        String absolutepath = request.getServletContext().getRealPath("");
        System.out.println("接收到请求"+path);
        getPhoto(rp,absolutepath,truepath,0);
    }

    public static String convertToBackslash(String input) {
        // 将所有正斜杠替换为反斜杠
        return input.replace("/", "\\");
    }
    public void getPhoto(HttpServletResponse rp,String absolutepath,String filepath,int type) {
        String filePath=absolutepath+filepath;
        String DEFAULT_PATH=absolutepath+DEAFULT;
        File imageFile = new File(filePath);
        if(!imageFile.exists()) {
            System.out.println("照片不存在"+filePath);
            imageFile = new File(DEFAULT_PATH);
        }
        if (imageFile.exists()) {
            System.out.println("照片存在");
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
}
