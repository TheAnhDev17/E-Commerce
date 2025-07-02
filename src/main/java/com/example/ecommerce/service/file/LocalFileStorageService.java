package com.example.ecommerce.service.file;

import com.example.ecommerce.exception.file.FileErrorCode;
import com.example.ecommerce.exception.file.FileException;
import com.example.ecommerce.util.FileValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
public class LocalFileStorageService implements FileStorageService{

    @Value("${app.upload.dir:uploads}")
    private String uploadBasePath;

    public String uploadFile(MultipartFile file, String subfolder) {

        FileValidator.validateImage(file);

        try {
            // Create directory if not exists
            Path uploadPath = Paths.get(uploadBasePath, subfolder);
            Files.createDirectories(uploadPath);

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw new IllegalArgumentException("Original filename must not be null");
            }
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            // Save file
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return URL path
            return "/" + subfolder + "/" + filename;

        } catch (IOException e) {
            log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
            throw new FileException(FileErrorCode.FILE_STORAGE_ERROR, e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            Path filePath = Paths.get(uploadBasePath + fileUrl);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("Failed to delete file: {}", fileUrl, e);
        }
    }
}