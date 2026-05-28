package com.example.personal_blog.dto;

public record ArticleCreateRequest(String title, String content, String category, String tags) {}
