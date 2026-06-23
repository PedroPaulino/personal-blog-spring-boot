package com.example.personal_blog.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Entity
@Table(name="article")
public class Article {
   
    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String title;
    private String content;
    private String category;
    private String tags;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamp(6) with time zone")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp(6) with time zone")
    private LocalDateTime updatedAt;

     public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if(!(o instanceof Article))
            return false;
        Article article = (Article) o;
        return Objects.equals(this.id, article.id) 
            && Objects.equals(this.title, article.title)
            && Objects.equals(this.content, article.content)
            && Objects.equals(this.category, article.category)
            && Objects.equals(this.tags, article.tags);
    }

    @Override
    public String toString(){
        return "Article{" + "id" + this.id + ", title='" + this.title + "\'" + ", content='" + this.content + "\'" + ", category='" + this.category + "\'" + "tags='" + this.tags + "\'" + "}";
    }

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        updatedAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }
}
