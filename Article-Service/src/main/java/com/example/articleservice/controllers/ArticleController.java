package com.example.articleservice.controllers;

import com.example.articleservice.beans.MemberBean;
import com.example.articleservice.entities.Article;
import com.example.articleservice.proxies.MemberProxy;
import com.example.articleservice.services.ImpArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/article")
@CrossOrigin
public class ArticleController {
    @Autowired
    ImpArticleService articleService;
    @Autowired
    MemberProxy memberProxy;
    @PostMapping(value = "/addArticle")
    public Article addArticle(@RequestBody Article article){
        return articleService.addArticle(article);
    }
    @PutMapping(value="/updateArticle/{id}")
    public Article updateArticle(@PathVariable Long id, @RequestBody Article article)
    {
        article.setArticleId(id);
        return articleService.updateArticle(article);
    }
    @GetMapping(value = "/findArticle/{id}")
    public Article findOneArticle(@PathVariable Long id)
    {
        return articleService.findArticleById(id);
    }
    @GetMapping(value = "/findArticleBySearch")
    public List<Article> findArticleByTypeMatchesRegex(@RequestParam String title,@RequestParam String type)
    {
        return articleService.findArticleByTitleAndType(title,type);
    }
    @GetMapping(value = "/articles")
    public List<Article> findAll()
    {
        return articleService.findAllArticles();
    }
    @DeleteMapping(value = "/deleteArticle/{id}")
    public void deleteArticle(@PathVariable Long id)
    {
        articleService.deleteArticle(id);
    }
    @PutMapping(value="/affectAuthor/{idMember}/{idArticle}")
	public Article affectAuthorToArticle(@PathVariable Long idMember , @PathVariable Long idArticle )
	{
        return articleService.affectAuthorToArticle(idMember, idArticle);
	}
	@GetMapping("/articlesByMember/{idMember}")
	public List<Article> getAllArticlesByMember(@PathVariable Long idMember)
	{
        return articleService.getAllArticlesByMember(idMember);
    }
    @GetMapping("/articlesByAuthorName")
    public List<Article> getAllArticlesByAuthorName(@RequestParam String name)
    {
        return articleService.getAllArticlesByAuthorName(name);
    }
    @GetMapping("/findByCreatedDatePeriod")
    public List<Article> findArticleByCreatedDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date createdDateGT,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date createdDateLT) {
       return articleService.findArticleByCreatedDateBetween(createdDateGT,createdDateLT);

    }
}
