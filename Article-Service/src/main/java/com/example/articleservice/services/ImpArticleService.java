package com.example.articleservice.services;

import com.example.articleservice.beans.MemberBean;
import com.example.articleservice.entities.Article;
import com.example.articleservice.proxies.MemberProxy;
import com.example.articleservice.repositories.ArticleRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ImpArticleService implements IArticleService {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    MemberProxy memberProxy;
    @PersistenceContext
    EntityManager em;


    @Override
    public Article addArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public void deleteArticle(Long id) {
        this.articleRepository.deleteById(id);
    }

    @Override
    public Article updateArticle(Article article) {
        return articleRepository.saveAndFlush(article);
    }

    @Override
    public Article findArticleById(long id) {
        return this.articleRepository.findById(id).get();
    }

    @Override
    public List<Article> findArticleByTitleAndType(String title, String type) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Article> cq = cb.createQuery(Article.class);
        Root<Article> article = cq.from(Article.class);

        if (title != null) {
            predicates.add(cb.like(article.get("title"), "%" + title + "%"));
        }
        if (type != null) {
            predicates.add(cb.like(article.get("type"), "%" + type + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }


    @Override
    public List<Article> findAllArticles() {

        List<Article> articles = articleRepository.findAll();
        articles.forEach(article -> {
            if (article.getAuthorId() != null) {
                MemberBean memberBean = memberProxy.getMemberById(article.getAuthorId());
                article.setAuthorName(memberBean.getFirstName() + " " + memberBean.getLastName());
            } else {
                article.setAuthorId(null);
                article.setAuthorName(null);
            }
            articleRepository.saveAndFlush(article);
        });
        return articles;
    }

    @Override
    public Article affectAuthorToArticle(Long idAuthor, Long idArticle) {
        MemberBean memberBean = memberProxy.getMemberById(idAuthor);
        Article article = findArticleById(idArticle);
        article.setAuthorId(idAuthor);
        article.setAuthorName(memberBean.getFirstName() + " " + memberBean.getLastName());
        return articleRepository.saveAndFlush(article);
    }

    @Override
    public List<Article> getAllArticlesByMember(Long idMember) {
        List<Article> articlesByMember = Lists.newArrayList();
        List<Article> articles = articleRepository.findAll();
        var articleList = articles.stream().filter(article -> article.getAuthorId().equals(idMember)).toList();
        articlesByMember.addAll(articleList);
        return articlesByMember;
    }

    @Override
    public List<Article> getAllArticlesByAuthorName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Article> cq = cb.createQuery(Article.class);
        Root<Article> article = cq.from(Article.class);

        if (name != null) {
            predicates.add(cb.like(article.get("authorName"), "%" + name + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Article> findArticleByCreatedDateBetween(Date createdDateGT, Date createdDateLT) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Article> cq = cb.createQuery(Article.class);
        Root<Article> article = cq.from(Article.class);

        if (createdDateGT != null && createdDateLT != null) {
            predicates.add(cb.greaterThanOrEqualTo(article.get("createdDate"), createdDateGT));
            predicates.add(cb.lessThanOrEqualTo(article.get("createdDate"), createdDateLT));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }
}
