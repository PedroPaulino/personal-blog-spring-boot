package com.example.personal_blog.service;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.example.personal_blog.controller.HomeController;
import com.example.personal_blog.dto.ArticleCreateRequest;
import com.example.personal_blog.dto.ArticleUpdateRequest;
import com.example.personal_blog.exception.ArticleException;
import com.example.personal_blog.model.Article;
import com.example.personal_blog.repository.ArticleRepository;

// Should convert entities to DTOs before returning data to controllers.
// Validate data before repository calls
@Service
public class ArticleService {
    Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    private final ArticleRepository articleRepository;

    // Loose Coupling - Spring IoC container
    ArticleService(ArticleRepository articleRepository){
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles(){
        return articleRepository.findAll(
            Sort.by(Sort.Direction.DESC, "createdAt")
        );
    }

    public Article getArticleById(Integer id){
        return articleRepository.findById(id).orElseThrow(() -> new ResourceAccessException("Article with id: " + id + " not found.")
        );
    }

    public Article createArticle(ArticleCreateRequest newArticle){

        isValidArticleRequest(newArticle);

        Article article = new Article();

        article.setTitle(newArticle.title());
        article.setCategory(newArticle.category());
        article.setContent(newArticle.content());
        article.setTags(newArticle.tags());

        return articleRepository.save(article);
    }

    public Article updateArticle(Integer id, ArticleUpdateRequest updatedArticle){
        LOGGER.error("Article ID: " + id);
        return articleRepository.findById(id).map(article -> {
            if(Objects.nonNull(updatedArticle.category())){
                article.setCategory(updatedArticle.category());
            }
            if(Objects.nonNull(updatedArticle.content())){
                article.setContent(updatedArticle.content());
            }
            if(Objects.nonNull(updatedArticle.title())){
                article.setTitle(updatedArticle.title());
            }
            if(Objects.nonNull(updatedArticle.tags())){
                article.setTags(updatedArticle.tags());
            }   
            return articleRepository.save(article);
        }).orElseThrow(() -> new ResourceAccessException("Article not found"));
    }

    public String deleteArticleById(Integer id){
        try {
            this.getArticleById(id);
        } catch (Exception e) {
            new ResourceAccessException("Article with id: " + id + " not found.");
        }
        articleRepository.deleteById(id);

        return "Deleted article with id: " + id;
    }

    private void isValidArticleRequest(ArticleCreateRequest articleRequest) throws ArticleException{
        
        if (articleRequest.title() == null || articleRequest.content() == null || articleRequest.category() == null || articleRequest.tags() == null ){
            throw new ArticleException("The fields cannot be null");
        }else if(articleRequest.title().isEmpty() || articleRequest.title().isBlank()){
            throw new ArticleException("Article Title must be filled");
        }else if(articleRequest.content().length() < 240){
            throw new ArticleException("Article Content must be higher than 240 chars.");
        }else if(articleRequest.category().isEmpty() || articleRequest.category().isBlank()){
            throw new ArticleException("Article Category must be filled.");
        }
    }

   

}
