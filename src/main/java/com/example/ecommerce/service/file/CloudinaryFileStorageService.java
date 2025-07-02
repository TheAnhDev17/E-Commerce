package com.example.ecommerce.service.file;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ecommerce.exception.file.FileErrorCode;
import com.example.ecommerce.exception.file.FileException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CloudinaryFileStorageService implements FileStorageService {
    Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, String folder){
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "auto"  // cho phép upload cả ảnh, video ...
                    )
            );

            return uploadResult.get("secure_url").toString();
        } catch (IOException e){
            log.error("Upload to Cloudinary failed", e);
            throw new FileException(FileErrorCode.FILE_STORAGE_ERROR, e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            log.warn("Cannot delete file: URL is null or empty");
            return;
        }

        try {
            // Extract public_id from Cloudinary URL
            String publicId = extractPublicIdFromUrl(fileUrl);
            deleteFileByPublicId(publicId);
        } catch (Exception e) {
            log.warn("Failed to delete file from URL: {}", fileUrl, e);
        }
    }

    private String extractPublicIdFromUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return null;
        }

        try {
            // Pattern để match Cloudinary URL format
            // https://res.cloudinary.com/cloud_name/resource_type/upload/version/public_id.extension
            Pattern pattern = Pattern.compile(".*/upload/(?:v\\d+/)?(.+)\\.[^.]+$");
            Matcher matcher = pattern.matcher(url);

            if (matcher.find()) {
                return matcher.group(1);
            }

            // Fallback pattern cho URL không có version
            Pattern fallbackPattern = Pattern.compile(".*/upload/(.+)\\.[^.]+$");
            Matcher fallbackMatcher = fallbackPattern.matcher(url);

            if (fallbackMatcher.find()) {
                return fallbackMatcher.group(1);
            }

            log.warn("Could not extract public_id from URL pattern: {}", url);
            return null;

        } catch (Exception e) {
            log.error("Error extracting public_id from URL: {}", url, e);
            return null;
        }
    }

    @Override
    public void deleteFileByPublicId(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            log.warn("Failed to delete file on Cloudinary", e);
        }
    }
}
