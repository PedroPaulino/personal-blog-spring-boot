package com.example.personal_blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.personal_blog.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer>{
    
}
