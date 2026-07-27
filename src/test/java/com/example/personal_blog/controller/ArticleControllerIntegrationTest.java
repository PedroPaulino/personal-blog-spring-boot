package com.example.personal_blog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.example.personal_blog.repository.ArticleRepository;
import org.springframework.http.MediaType;

import tools.jackson.databind.ObjectMapper;

import com.example.personal_blog.dto.ArticleCreateRequest;
import com.example.personal_blog.dto.ArticleUpdateRequest;
import com.example.personal_blog.model.Article;
import org.junit.jupiter.api.BeforeEach;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Tell to use application-test.properties
public class ArticleControllerIntegrationTest {
 
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private String content = "a".repeat(250);

    private String adminUser = "admin";

    private String adminPwd = "admin123";

    @BeforeEach
    void setup(){
        repository.deleteAll();
    }

    @Test
    void shouldReturnAllArticles() throws Exception{

        Article article = new Article();
        article.setTitle("Spring Boot");
        article.setContent(content);
        article.setCategory("JAVA");
        article.setTags("spring");
        repository.save(article);

        mockMvc.perform(get("/api/v1/public/articles"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Spring Boot"))
            .andExpect(jsonPath("$[0].content").value(content))
            .andExpect(jsonPath("$[0].category").value("JAVA"))
            .andExpect(jsonPath("$[0].tags").value("spring"));
    }

    @Test
    void shouldReturnArticleById() throws Exception {

        Article article = new Article();
        article.setTitle("Spring Boot");
        article.setContent(content);
        article.setCategory("JAVA");
        article.setTags("spring");

        Article response = repository.save(article);

        mockMvc.perform(get("/api/v1/public/articles/" + response.getId()))
            .andExpect(status().is(202))
            .andExpect(jsonPath("$.id").value(response.getId()))
            .andExpect(jsonPath("$.title").value("Spring Boot"))
            .andExpect(jsonPath("$.content").value(content))
            .andExpect(jsonPath("$.category").value("JAVA"))
            .andExpect(jsonPath("$.tags").value("spring"));

    }

    @Test
    void shouldThrownExceptionWhenArticleByIdNotFound() throws Exception {

        mockMvc.perform(get("/api/v1/public/articles/999"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Article with id: 999 not found."));

    }

    @Test
    void shouldReturnAdminAllArticlesWhenAuthenticated() throws Exception {

        Article article = new Article();
        article.setTitle("Spring Boot");
        article.setContent(content);
        article.setCategory("JAVA");
        article.setTags("spring");

        Article response = repository.save(article);

        assertNotNull(response.getId());
        assertEquals(1, repository.count());

        //String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(get("/api/v1/admin/articles")
            .with(httpBasic("admin","admin123")))
            //.contentType(MediaType.APPLICATION_JSON)
            //.content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Spring Boot"))
            .andExpect(jsonPath("$[0].content").value(content))
            .andExpect(jsonPath("$[0].category").value("JAVA"))
            .andExpect(jsonPath("$[0].tags").value("spring"));
    }

    @Test
    void shouldCreateArticleWhenAuthenticated() throws Exception {

        // Arrange
        ArticleCreateRequest request = new ArticleCreateRequest(
            "My Test Article",
            content,
            "JAVA",
            "test,test2"
        );

        String json = objectMapper.writeValueAsString(request);
        
        // Assert API
        mockMvc.perform(post("/api/v1/admin/articles")
                .with(httpBasic(adminUser,adminPwd))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.title").value("My Test Article"))
            .andExpect(jsonPath("$.content").value(content))
            .andExpect(jsonPath("$.category").value("JAVA"))
            .andExpect(jsonPath("$.tags").value("test,test2"));

        // Assert Persistency
        Article saved = repository.findAll().get(0);

        assertEquals(1, repository.count());
        assertEquals(request.title(), saved.getTitle());
        assertEquals(request.content(), saved.getContent());
        assertEquals(request.category(), saved.getCategory());
        assertEquals(request.tags(), saved.getTags());

    }

    @Test
    void shouldUpdateArticleWhenAuthenticated() throws Exception {

        // Arrange
        Article article = new Article();
        article.setTitle("Spring Boot");
        article.setContent(content);
        article.setCategory("JAVA");
        article.setTags("spring");

        ArticleUpdateRequest request = new ArticleUpdateRequest(
            "Spring Boot v2",
            content,
            "JAVA",
            "spring");
        
        // Act
        Article response = repository.save(article);
        
        assertNotNull(response.getId());
        assertEquals(1, repository.count());
        
        String json = objectMapper.writeValueAsString(request);
        
        // Assert Api
        mockMvc.perform(put("/api/v1/admin/articles/" + response.getId())
                .with(httpBasic(adminUser, adminPwd))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Spring Boot v2"))
            .andExpect(jsonPath("$.content").value(content))
            .andExpect(jsonPath("$.category").value("JAVA"))
            .andExpect(jsonPath("$.tags").value("spring"));

        // Assert Persistency
        Article updatedArticle = repository.findById(response.getId()).get();

        assertEquals(response.getId(), updatedArticle.getId());
        assertEquals(request.title(), updatedArticle.getTitle());
        assertEquals(request.content(), updatedArticle.getContent());
        assertEquals(request.category(), updatedArticle.getCategory());
        assertEquals(request.tags(), updatedArticle.getTags());
    }

    @Test
    void shouldDeleteArticleWhenAuthenticated() throws Exception{

        // Arrange
        Article article = new Article();
        article.setTitle("Spring Boot");
        article.setContent(content);
        article.setCategory("JAVA");
        article.setTags("spring");

        // Act
        Article response = repository.save(article);

        mockMvc.perform(delete("/api/v1/admin/articles/" + response.getId())
            .with(httpBasic(adminUser, adminPwd)))
            .andExpect(status().isOk())
            .andExpect(content().string("Deleted article with id: " + response.getId()));

        assertFalse(repository.findById(response.getId()).isPresent());

    }

    void shouldThrowExceptionWhenNotAuthorizedForGetAllArticles() throws Exception {
        mockMvc.perform(get("/api/v1/admin/articles")
            .with(httpBasic("invalid","invalidPwd")))
            .andExpect(status().isUnauthorized());
    }

    void shouldThrowExceptionWhenNotAuthorizedToCreateArticle() throws Exception {
  // Arrange
        ArticleCreateRequest request = new ArticleCreateRequest(
            "My Test Article",
            content,
            "JAVA",
            "test,test2"
        );

        String json = objectMapper.writeValueAsString(request);
        
        // Assert API
        mockMvc.perform(post("/api/v1/admin/articles")
                .with(httpBasic("invalid","invalidPwd"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isUnauthorized());

    }

    void shouldThrowExceptionWhenNotAuthorizedToUpdatedArticle() throws Exception {
          // Arrange
        Article article = new Article();
        article.setTitle("Spring Boot");
        article.setContent(content);
        article.setCategory("JAVA");
        article.setTags("spring");

        ArticleUpdateRequest request = new ArticleUpdateRequest(
            "Spring Boot v2",
            content,
            "JAVA",
            "spring");
        
        // Act
        Article response = repository.save(article);
        
        assertNotNull(response.getId());
        assertEquals(1, repository.count());
        
        String json = objectMapper.writeValueAsString(request);
        
        // Assert Api
        mockMvc.perform(put("/api/v1/admin/articles/" + response.getId())
                .with(httpBasic("invalid", "invalidPwd"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isUnauthorized());
    }

    void shouldThrowExceptionWhenNotAuthorizedToDeleteArticle() throws Exception {
        // Arrange
        Article article = new Article();
        article.setTitle("Spring Boot");
        article.setContent(content);
        article.setCategory("JAVA");
        article.setTags("spring");

        // Act
        Article response = repository.save(article);

        mockMvc.perform(delete("/api/v1/admin/articles/" + response.getId())
            .with(httpBasic("invalid", "invalidPwd")))
            .andExpect(status().isUnauthorized());
    }

    void shouldThrowExceptionWhenUpdateArticleWithNullDTO() throws Exception {
         // Arrange
        Article article = new Article();
        article.setTitle("Spring Boot");
        article.setContent(content);
        article.setCategory("JAVA");
        article.setTags("spring");

        ArticleUpdateRequest request = new ArticleUpdateRequest(
            null,
            content,
            "JAVA",
            "spring");
        
        // Act
        Article response = repository.save(article);
        
        assertNotNull(response.getId());
        assertEquals(1, repository.count());
        
        String json = objectMapper.writeValueAsString(request);
        
        // Assert Api
        mockMvc.perform(put("/api/v1/admin/articles/" + response.getId())
                .with(httpBasic(adminUser, adminPwd))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }

    void shouldThrowExceptionWhenArticleDoesNotExist() throws Exception {

        
        Article article = new Article();
        article.setTitle("Spring Boot");
        article.setContent(content);
        article.setCategory("JAVA");
        article.setTags("spring");

        repository.save(article);

        mockMvc.perform(get("/api/v1/public/articles/" + 999))
            .andExpect(status().isNotFound());
    }
}
