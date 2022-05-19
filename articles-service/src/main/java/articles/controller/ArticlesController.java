package articles.controller;

import articles.model.Article;
import articles.repository.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class ArticlesController {
    private final ArticlesRepository repo;

    public ArticlesController(ArticlesRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/articles/create")
    public ResponseEntity<Article> create(@RequestBody @Valid Article articleDto) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var article = Article.builder()
                .title(articleDto.title)
                .content(articleDto.content)
                .theme(articleDto.theme)
                .tags(articleDto.tags)
                .username(username)
                .build();
        repo.save(article);
        return new ResponseEntity(article,OK);
    }

    @GetMapping("/articles/get")
    public ResponseEntity<List<Article>> get() {
        List<Article> articles = repo.findAll();
        return new ResponseEntity(articles,OK);
    }
}
