package com.shopme.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * map the path of the request (URI) to
     * the absolute path of the directory on the server.
    * */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirName = "user-photos";
        Path userPhotoDir = Paths.get(dirName);

        // getAbsolutePath(): get the original link of the project
        String userPhotosPath = userPhotoDir.toFile().getAbsolutePath();
        // System.out.println(userPhotosPath);

        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/" + userPhotosPath + "/");
    }

}
