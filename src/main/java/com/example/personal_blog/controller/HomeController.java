package com.example.personal_blog.controller;

import com.example.personal_blog.dto.ArticleCreateRequest;
import com.example.personal_blog.dto.ArticleUpdateRequest;
import com.example.personal_blog.model.Article;
import com.example.personal_blog.service.ArticleService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

// Constructor Injection (explicit dependencies)
@RestController
public class HomeController {
    private final ArticleService articleService;
    Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    // @Autowired not needed in Spring 4.3+ for single arg constructors
    HomeController(ArticleService articleService){
        this.articleService = articleService;
    }

    @GetMapping("/api/v1/articles")
    public ResponseEntity<List<Article>> articles(){
        LOGGER.info("Calling ArticleService");
        return ResponseEntity.ok().body(articleService.getAllArticles());
    }

    @PostMapping("/api/v1/articles")
    public ResponseEntity<Article> createArticle(@RequestBody ArticleCreateRequest request){
        Article response = articleService.createArticle(request);
        return ResponseEntity.ok(response);
        
    }

    @PutMapping("/api/v1/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Integer id, @RequestBody ArticleUpdateRequest request){
        Article response = articleService.updateArticle(id, request);
        return ResponseEntity.ok(response);
    }

}
