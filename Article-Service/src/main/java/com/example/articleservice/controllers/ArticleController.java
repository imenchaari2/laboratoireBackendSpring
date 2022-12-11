package com.example.articleservice.controllers;

import com.example.articleservice.entities.Article;
import com.example.articleservice.entities.File;
import com.example.articleservice.proxies.MemberProxy;
import com.example.articleservice.repositories.FileRepository;
import com.example.articleservice.services.ImpArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/article")
public class ArticleController {
    @Autowired
    ImpArticleService articleService;
    @Autowired
    MemberProxy memberProxy;
    @Autowired
    FileRepository fileRepository;

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        return memberProxy.downloadFile(fileName,request);
    }

    @PostMapping(value = "/addArticle/{creatorId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Article addArticle(@PathVariable Long creatorId,
                              @ModelAttribute("article") Article article,
                              @RequestPart("file") MultipartFile file) throws IOException {
        File source = new File(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        if (fileRepository.findAll().contains(source)) {
            article.setPdfSource(source);
        } else {
            article.setPdfSource(fileRepository.save(source));
        }
        return articleService.addArticle(article,creatorId);
    }

    @PutMapping(value = "/updateArticle/{id}")
    public Article updateArticle(@PathVariable Long id,
                                 Article article,
                                 @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        if (file != null) {
        File source = new File(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        if (fileRepository.findAll().contains(source)) {
            article.setPdfSource(source);
        } else {
            article.setPdfSource(fileRepository.save(source));
        }}
        article.setArticleId(id);
        return articleService.updateArticle(article,file);
    }

    @GetMapping(value = "/findArticle/{id}")
    public Article findOneArticle(@PathVariable Long id) {
        return articleService.findArticleById(id);
    }

    @GetMapping(value = "/findArticleBySearch")
    public List<Article> findArticleByTypeMatchesRegex(@RequestParam String title, @RequestParam String type) {
        return articleService.findArticleByTitleAndType(title, type);
    }

    @GetMapping(value = "/articles")
    public List<Article> findAll() {
        return articleService.findAllArticles();
    }

    @DeleteMapping(value = "/deleteArticle/{id}")
    public void deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }

    @PutMapping(value = "/affectAuthors/{idArticle}")
    public Article affectAuthorToArticle(@RequestParam List<Long> membersIds, @PathVariable Long idArticle) {
        return articleService.affectAuthorsToArticle(membersIds, idArticle);
    }

    @GetMapping("/articlesByMember/{idMember}")
    public List<Article> getAllArticlesByMember(@PathVariable Long idMember) {
        return articleService.getAllArticlesByMember(idMember);
    }

//    @GetMapping("/articlesByAuthorName")
//    public List<Article> getAllArticlesByAuthorName(@RequestParam String name) {
//        return articleService.getAllArticlesByAuthorName(name);
//    }

    @GetMapping("/findByCreatedDatePeriod")
    public List<Article> findArticleByCreatedDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date createdDateGT,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date createdDateLT) {
        return articleService.findArticleByCreatedDateBetween(createdDateGT, createdDateLT);

    }
}
