package com.example.articleservice.repositories;

import com.example.articleservice.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findArticleByTitleAndType(String title, String type);
    List<Article> findArticleByCreatedDateBetween(Date createdDateGT, Date createdDateLT);

}
