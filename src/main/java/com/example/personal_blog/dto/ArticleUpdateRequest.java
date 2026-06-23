package com.example.personal_blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ArticleUpdateRequest(
    
    @NotNull
    @NotBlank(message = "Article Title must be filled")
    String title, 
    
    @NotNull
    @Size(
        min = 240,
        message = "Article Content must be higher than 240 chars."
    )
    String content, 
    
    @NotNull
    @NotBlank(message = "Article Category must be filled.")
    String category, 
    
    String tags

) {}
