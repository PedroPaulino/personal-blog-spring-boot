package com.example.personal_blog.dto;

public record ArticleUpdateRequest(String title, String content, String category, String tags) {
    
}
