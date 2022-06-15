package com.shopme.admin;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileUpLoadUtil {

    public static void saveFile(String uploadDir,
                                String fileName,
                                MultipartFile multipartFile)
            throws IOException {
        // file name
        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)) {
            // create file
            Files.createDirectories(uploadPath);
        }
        // get input stream from multipartFile
        try (InputStream inputStream = multipartFile.getInputStream()) {
            // call resolve() to create resolved Path
            Path filePath = uploadPath.resolve(fileName);

            // REPLACE_EXISTING – replace a file if it exists
            // Files.copy(source, target, ...)
            Files.copy(inputStream, filePath, REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IOException("Could not save file: " + fileName, ex);
        }
    }

    // clear directory
    public static void clearDir(String dir) {
        Path dirPath = Paths.get(dir);

        try {
            Files.list(dirPath).forEach(file -> {
                // isisDirectory
                // Check if the specified path
                // is a directory or not
                if(!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException ex) {
                        System.out.println("Could not delete file: " + file);
                    }
                }
            });
        } catch (IOException ex) {
            System.out.println("Could not dist directory: " + dirPath);
        }
    }

}
