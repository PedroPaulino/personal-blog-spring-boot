package com.example.personal_blog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.example.personal_blog.dto.ArticleCreateRequest;
import com.example.personal_blog.dto.ArticleUpdateRequest;
import com.example.personal_blog.model.Article;
import com.example.personal_blog.repository.ArticleRepository;

// Should convert entities to DTOs before returning data to controllers.
// Validate data before repository calls
@Service
public class ArticleService {
    
    private final ArticleRepository articleRepository;

    // Loose Coupling - Spring IoC container
    ArticleService(ArticleRepository articleRepository){
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles(){
        return articleRepository.findAll();
    }

     public Article createArticle(ArticleCreateRequest newArticle){
        Article article = new Article();

        article.setTitle(newArticle.title());
        article.setCategory(newArticle.category());
        article.setContent(newArticle.content());
        article.setTags(newArticle.tags());

        return articleRepository.save(article);
    }

    public Article updateArticle(Integer id, ArticleUpdateRequest updatedArticle){
        return articleRepository.findById(id).map(article -> {
            article.setCategory(updatedArticle.category());
            article.setContent(updatedArticle.content());
            article.setTitle(updatedArticle.title());
            article.setTags(updatedArticle.tags());
            return articleRepository.save(article);
        }).orElseThrow(() -> new ResourceAccessException("Article not found"));

    }

   

}
