package controllers;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            return file.delete();
        }

        return false;
    }

    public static void saveFile(MultipartFile file, String destinationPath) throws IOException {
        // Using Spring's FileCopyUtils class for file copy
        FileCopyUtils.copy(file.getBytes(), new File(destinationPath));
        System.out.println("File saved successfully: " + destinationPath);
    }

}
