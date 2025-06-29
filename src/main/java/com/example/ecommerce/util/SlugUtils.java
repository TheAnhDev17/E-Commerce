package com.example.ecommerce.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@Slf4j
public class SlugUtils {

    public static String generateSlug(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "slug-" + System.currentTimeMillis();
        }

        String slug = name.toLowerCase()
                .trim()
                .replaceAll("\\s+", "-")           // Replace spaces with hyphens
                .replaceAll("[^a-z0-9-]", "")      // Keep only letters, numbers, hyphens
                .replaceAll("-+", "-")             // Replace multiple hyphens with single
                .replaceAll("^-|-$", "");          // Remove leading/trailing hyphens

        // Fallback if empty after processing
        if (slug.isEmpty()) {
            slug = "slug-" + System.currentTimeMillis();
        }

        return slug;
    }

    public static String generateUniqueSlug(String name, Function<String, Boolean> existsChecker) {
        String baseSlug = generateSlug(name);
        String slug = baseSlug;
        int counter = 1;

        // Keep trying until find unique slug
        while (existsChecker.apply(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    // Advanced method với Vietnamese support
    public static String generateSlugWithVietnamese(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "slug-" + System.currentTimeMillis();
        }

        String slug = removeVietnameseAccents(name)
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");

        if (slug.isEmpty()) {
            slug = "slug-" + System.currentTimeMillis();
        }

        return slug;
    }

    private static String removeVietnameseAccents(String str) {
        String[][] accents = {
                {"à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a"},
                {"è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e"},
                {"ì|í|ị|ỉ|ĩ", "i"},
                {"ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o"},
                {"ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u"},
                {"ỳ|ý|ỵ|ỷ|ỹ", "y"},
                {"đ", "d"}
        };

        for (String[] accent : accents) {
            str = str.replaceAll(accent[0], accent[1]);
            str = str.replaceAll(accent[0].toUpperCase(), accent[1].toUpperCase());
        }

        return str;
    }
}
