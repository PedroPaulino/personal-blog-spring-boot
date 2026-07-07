package com.example.personal_blog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.example.personal_blog.dto.ArticleCreateRequest;
import com.example.personal_blog.dto.ArticleUpdateRequest;
import com.example.personal_blog.exception.ArticleException;
import com.example.personal_blog.model.Article;
import com.example.personal_blog.repository.ArticleRepository;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    
    // Creates a false object.
    @Mock
    private ArticleRepository articleRepository;

    // Another option would be using @BeforeEach notation for a setup method that could initialize the articleService passing the dependency (articleRepository)
    @InjectMocks // -> Creates a real object and injects the available mocks.
    private ArticleService articleService;

    @Captor // Tells exactly the object sent to the repository
    private ArgumentCaptor<Article> articleCaptor;
    
    private String content = "a,".repeat(250);

    @Test
    void shouldReturnListOfArticles(){
        // Arrange
        List<Article> articles = new ArrayList<>();
        Article article1 = new Article();
        Article article2 = new Article();
        Article article3 = new Article();

        article1.setId(1);
        article1.setTitle("t1");
        article1.setContent("c1");
        article1.setCategory("c1");
        article1.setTags("t1");

        article2.setId(2);
        article2.setTitle("t2");
        article2.setContent("c2");
        article2.setCategory("c2");
        article2.setTags("t2");

        article3.setId(3);
        article3.setTitle("t3");
        article3.setContent("c3");
        article3.setCategory("c3");
        article3.setTags("t3");
        
        articles.add(article1);
        articles.add(article2);
        articles.add(article3);

        when(articleRepository.findAll(Sort.by(Sort.Direction.DESC,"createdAt"))).thenReturn(articles);

        // Act
        List<Article> response = articleService.getAllArticles();

        // Assert
        assertEquals(3, response.size());
        assertEquals(article1, response.get(0));
        assertEquals(article2, response.get(1));
        assertEquals(article3, response.get(2));
    }

    @Test
    void shouldReturnArticleById(){
        // Arrange
        Article existingArticle = new Article();
        existingArticle.setId(1);
        existingArticle.setTitle("Article Title");
        existingArticle.setContent("Article Content");
        existingArticle.setCategory("Article Category");
        existingArticle.setTags("Article Tags");

        when(articleRepository.findById(1))
            .thenReturn(Optional.of(existingArticle));

        // Act
        Article response = articleService.getArticleById(1);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Article Title", response.getTitle());
        assertEquals("Article Content", response.getContent());
        assertEquals("Article Category", response.getCategory());
        assertEquals("Article Tags", response.getTags());

    }

    @Test
    void shouldThrowExceptionWhenArticleWithIdNotFound(){
        // Arrange
        when(articleRepository.findById(1))
            .thenReturn(Optional.empty());
        
        // Act
        ArticleException exception = assertThrows(ArticleException.class, () -> articleService.getArticleById(1));
        
        // Assert
        assertEquals("Article with id: " + 1 + " not found.", exception.getMessage());

    }

    @Test
    void shouldCreateArticleSuccessfully(){
        
        // Arrange
        ArticleCreateRequest request = new ArticleCreateRequest(
            "My Test Article",
            content,
            "JAVA",
            "test,test2"
        );

        Article savedArticle = new Article();
        savedArticle.setId(1);
        savedArticle.setTitle(request.title());
        savedArticle.setContent(request.content());
        savedArticle.setCategory(request.category());
        savedArticle.setTags(request.tags());
        
        // Without when Mockito returns a standard value, in this case would be null.
        when(articleRepository.save(any(Article.class)))
            .thenReturn(savedArticle);

        // Act
        Article response = articleService.createArticle(request);

        // Assert
        verify(articleRepository).save(articleCaptor.capture());
        Article articleSent = articleCaptor.getValue();

        assertEquals(request.title(), articleSent.getTitle());
        assertEquals(request.content(), articleSent.getContent());
        assertEquals(request.category(), articleSent.getCategory());
        assertEquals(request.tags(), articleSent.getTags());

        assertNotNull(response);
        assertEquals(savedArticle.getId(), response.getId());
        assertEquals(savedArticle.getTitle(), response.getTitle());

    }
    
    @Test
    void shouldUpdateArticleSuccessfully(){
        // Arrange
        ArticleUpdateRequest request = new ArticleUpdateRequest(
            "New Title",
            "Old Content",
            "Old Category",
            "Old Tags");
        
        Article existingArticle = new Article();
        existingArticle.setId(1);
        existingArticle.setTitle("Old Title");
        existingArticle.setContent("Old Content");
        existingArticle.setCategory("Old Category");
        existingArticle.setTags("Old Tags");

        // .thenReturn tells what the mock should return, but ignores the arguments
        when(articleRepository.findById(1))
            .thenReturn(Optional.of(existingArticle));

        // Doesn´t capture anything .thenAnswer also tells what the mock shold return, but the answer depends on call 
        when(articleRepository.save(any(Article.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Article response = articleService.updateArticle(1, request);
        
        // Assert
        verify(articleRepository).save(articleCaptor.capture());
        Article savedArticle = articleCaptor.getValue();

        assertEquals("New Title", savedArticle.getTitle());
        assertEquals("Old Content", savedArticle.getContent());
        assertEquals("Old Category", savedArticle.getCategory());
        assertEquals("Old Tags", savedArticle.getTags());
        
        assertEquals("New Title", response.getTitle());
        
    }

    @Test
    void shouldDeleteArticleById(){
        // Arrange
        Article existingArticle = new Article();
        existingArticle.setId(1);
        existingArticle.setTitle("Old Title");
        existingArticle.setContent("Old Content");
        existingArticle.setCategory("Old Category");
        existingArticle.setTags("Old Tags");

        when(articleRepository.findById(1))
            .thenReturn(Optional.of(existingArticle));

        // Act
        String response = articleService.deleteArticleById(1);

        // Assert
        verify(articleRepository).deleteById(1);
        assertNotNull(response);
        assertEquals("Deleted article with id: " + existingArticle.getId(), response);
    }

}
