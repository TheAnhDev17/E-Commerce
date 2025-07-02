package com.example.ecommerce.util;

import com.example.ecommerce.exception.file.FileErrorCode;
import com.example.ecommerce.exception.file.FileException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileValidator {
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; //5MB
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "webp");

    private FileValidator() {
        // Prevent instantiation
    }

    public static void validateImage(MultipartFile file){
        if(file == null || file.isEmpty()){
            throw new FileException(FileErrorCode.FILE_EMPTY);
        }

        String contentType = file.getContentType();
        if(contentType == null || !contentType.startsWith("image/")){
            throw new FileException(FileErrorCode.INVALID_FILE_TYPE);
        }

        if(file.getSize() > MAX_FILE_SIZE){
            throw new FileException(FileErrorCode.FILE_SIZE_EXCEEDED);
        }

        String fileName = file.getOriginalFilename();
        if(fileName == null || fileName.lastIndexOf(".") == -1){
            throw  new FileException(FileErrorCode.INVALID_FILE_EXTENSION);
        }

        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)){
            throw new FileException(FileErrorCode.INVALID_FILE_EXTENSION);
        }
    }
}
