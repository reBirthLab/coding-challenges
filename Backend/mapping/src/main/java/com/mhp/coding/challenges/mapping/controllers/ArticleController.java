package com.mhp.coding.challenges.mapping.controllers;

import com.mhp.coding.challenges.mapping.models.dto.ArticleDto;
import com.mhp.coding.challenges.mapping.services.ArticleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping()
    public List<ArticleDto> list() {
        return articleService.list();
    }

    @GetMapping("/{id}")
    public ArticleDto details(@PathVariable Long id) {
        ArticleDto articleDto = articleService.articleForId(id);
        if (articleDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Article [id=%d] is not found", id));
        }
        return articleDto;
    }

    @PostMapping()
    public ArticleDto create(@RequestBody ArticleDto articleDto) {
        return articleService.create(articleDto);
    }
}
