package com.shopme.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileUpLoadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUpLoadUtil.class);

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
                        LOGGER.error("Could not delete file: " + file);
                    }
                }
            });
        } catch (IOException ex) {
            LOGGER.error("Could not dist directory: " + dirPath);
        }
    }

    public static void removeDir(String dir) {
        clearDir(dir);

        try {
            Files.delete(Paths.get(dir));
        } catch (IOException e) {
            LOGGER.error("Could not remove directory: " + dir);
        }
    }

}
