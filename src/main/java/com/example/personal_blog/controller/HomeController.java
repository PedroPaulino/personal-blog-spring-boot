package com.example.personal_blog.controller;

import com.example.personal_blog.service.ArticleService;
import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
    
    @GetMapping("/home")
    public String articles(){
        return ArticleService.getAllUsers();
    }

}
