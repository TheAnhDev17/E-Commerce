package com.example.ecommerce.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String uploadFile(MultipartFile file, String subfolder);
    void deleteFile(String fileUrl);

    default void deleteFileByPublicId(String publicId) {
        throw new UnsupportedOperationException("This storage does not support publicId deletion");
    }
}
