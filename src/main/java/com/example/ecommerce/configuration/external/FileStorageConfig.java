package com.example.ecommerce.configuration.external;

import com.cloudinary.Cloudinary;
import com.example.ecommerce.service.file.CloudinaryFileStorageService;
import com.example.ecommerce.service.file.FileStorageService;
import com.example.ecommerce.service.file.LocalFileStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {
    @Bean
    @ConditionalOnProperty(name = "app.file-storage", havingValue = "local", matchIfMissing = true)
    public FileStorageService localFileStorageService(){
        return new LocalFileStorageService();
    }

    @Bean
    @ConditionalOnProperty(name = "app.file-storage", havingValue = "cloudinary")
    public FileStorageService cloudinaryFileStorageService(Cloudinary cloudinary){
        return new CloudinaryFileStorageService(cloudinary);
    }
}
