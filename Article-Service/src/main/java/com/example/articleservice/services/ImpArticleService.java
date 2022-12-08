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
import java.util.Objects;

@Service
public class ImpArticleService implements IArticleService {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    MemberProxy memberProxy;
    @PersistenceContext
    EntityManager em;


    @Override
    public Article addArticle(Article article,Long idCreator) {
        MemberBean memberBean1 = memberProxy.getMemberById(idCreator);
        article.setCreatorId(idCreator);
        article.getMembersIds().add(idCreator);
        article.getMembersNames().add(memberBean1.getFirstName() + " " + memberBean1.getLastName());
        return articleRepository.save(article);
    }

    @Override
    public void deleteArticle(Long id) {
        this.articleRepository.deleteById(id);
    }

    @Override
    public Article updateArticle(Article article) {
        Article article1 = articleRepository.findById(article.getArticleId()).get();
        article.setCreatorId(article1.getCreatorId());
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
        List<MemberBean> members = memberProxy.getAllMembers();
        List<Long> presentMembersIds = new ArrayList<>();
        List<String> presentMembersNames = new ArrayList<>();
        members.forEach(member -> {
            presentMembersIds.add(member.getId());
            presentMembersNames.add("ADMIN");
            presentMembersNames.add(member.getFirstName() + " " + member.getLastName());
        });
        articles.forEach(article -> {
                    article.getMembersIds().forEach(id -> {
                        if (!presentMembersIds.contains(id)) {
                            article.getMembersIds().remove(id);
                        }
                    });
                    article.getMembersNames().forEach(name -> {
                        System.out.println(presentMembersNames.contains(name));
                        if (!presentMembersNames.contains(name)) {
                            article.getMembersNames().remove(name);
                        }
                    });
                    articleRepository.saveAndFlush(article);
                }
        );
        return articles;
    }

    @Override
    public Article affectAuthorsToArticle(List<Long> ids, Long idArticle) {
        Article article = articleRepository.findById(idArticle).get();
        List<String> names = Lists.newArrayList();
        List<Long> membersIds = Lists.newArrayList();
        article.getMembersIds().clear();
        membersIds.add(article.getCreatorId());
        ids.forEach(id -> {
            if (!membersIds.contains(id)) {
                membersIds.add(id);
            }
        });
        article.setMembersIds(membersIds);
        return getArticle(article, names);
    }
    private Article getArticle(Article article1, List<String> names) {
        article1.getMembersIds().forEach(id -> {
            MemberBean memberBean1 = memberProxy.getMemberById(id);
            if (!Objects.equals(memberBean1.getRole(), "ROLE_ADMIN")) {
                names.add(memberBean1.getFirstName() + " " + memberBean1.getLastName());
            } else {
                names.add("ADMIN");
            }
        });
        article1.setMembersNames(null);
        article1.setMembersNames(names);
        return this.articleRepository.saveAndFlush(article1);
    }

    @Override
    public List<Article> getAllArticlesByMember(Long idMember) {
        List<Article> articlesByMember = Lists.newArrayList();
        List<Article> articles = articleRepository.findAll();
        articles.forEach(article -> {
            if (article.getMembersIds().contains(idMember)) {
                articlesByMember.add(article);
            }
        });
        return articlesByMember;
    }

//    @Override
//    public List<Article> getAllArticlesByAuthorName(String name) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        List<Predicate> predicates = new ArrayList<>();
//        CriteriaQuery<Article> cq = cb.createQuery(Article.class);
//        Root<Article> article = cq.from(Article.class);
//
//        if (name != null) {
//            predicates.add(cb.like(article.get("authorName"), "%" + name + "%"));
//        }
//
//        cq.where(predicates.toArray(new Predicate[0]));
//
//        return em.createQuery(cq).getResultList();
//    }

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
