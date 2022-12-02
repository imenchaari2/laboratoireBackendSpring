package com.example.articleservice.services;

import com.example.articleservice.entities.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IArticleService {
    Article addArticle(Article article);
    void deleteArticle(Long id);
    Article updateArticle(Article article);
    Article findArticleById(long id);
    List<Article> findArticleByTitleAndType(String title,String type);
    List<Article> findAllArticles();
    Article affectAuthorToArticle(Long idAuthor , Long idArticle );
    List<Article> getAllArticlesByMember(Long idMember);
    List<Article> getAllArticlesByAuthorName(String name);
    List<Article> findArticleByCreatedDateBetween(Date createdDateGT, Date createdDateLT);

}
