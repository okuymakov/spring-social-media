package articles.controller;

import articles.model.Article;
import articles.repository.ArticlesRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticlesController {
    private final ArticlesRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(ArticlesController.class);

    @GetMapping("/get")
    public ResponseEntity<List<Article>> get() {
        List<Article> articles = repo.findAllPublic();
        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/get/q")
    public ResponseEntity<List<Article>> getByThemeAndTags(@RequestParam String theme, @RequestParam List<String> tags) {
        List<Article> articles = repo.findByThemeAndTags(theme, tags);
        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/get/q")
    public ResponseEntity<Article> getByTitle(@RequestParam String title) {
        Article article = repo.findByTitle(title);
        return ResponseEntity.ok().body(article);
    }

    @GetMapping("/{username}/")
    public ResponseEntity<List<Article>> getByUsername(
            @RequestHeader("username") Optional<String> curUsername,
            @PathVariable String username
    ) {
        List<Article> articles;
        if (curUsername.isPresent() && Objects.equals(curUsername.get(), username)) {
            articles = repo.findByUsername(username, true);
        } else {
            articles = repo.findByUsername(username, false);
        }
        return ResponseEntity.ok().body(articles);
    }

    @PostMapping("/create")
    public ResponseEntity<Article> create(@RequestHeader String username, @RequestBody @Valid Article articleDto) {
        var article = Article.builder()
                .title(articleDto.getTitle())
                .content(articleDto.getContent())
                .theme(articleDto.getTheme())
                .tags(articleDto.getTags())
                .username(username)
                .isPrivate(articleDto.isPrivate())
                .build();
        article = repo.insert(article);
        logger.info("{} created new article \"{}\"", username, article.getTitle());
        return ResponseEntity.ok().body(article);
    }

    @PutMapping("/update")
    public ResponseEntity<Article> update(@RequestHeader String username, @RequestBody @Valid Article articleDto) {
        var article = Article.builder()
                .title(articleDto.getTitle())
                .content(articleDto.getContent())
                .theme(articleDto.getTheme())
                .tags(articleDto.getTags())
                .username(username)
                .isPrivate(articleDto.isPrivate())
                .build();
        article = repo.save(article);
        logger.info("{} updated article \"{}\"", username, article.getTitle());
        return ResponseEntity.ok().body(article);
    }

    @DeleteMapping("/delete/q")
    public ResponseEntity<String> delete(
            @RequestHeader String username,
            @RequestParam String id
    ) {
        var articleOpt = repo.findById(id);
        if (articleOpt.isEmpty()) return ResponseEntity.badRequest().body("Article doesn't exist");
        var article = articleOpt.get();

        if (Objects.equals(article.getUsername(), username)) {
            repo.deleteById(id);
            logger.info("{} deleted article \"{}\"", username, article.getTitle());
            return ResponseEntity.ok("Article " + article.getTitle() + " deleted successfully!");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user does not have permission to delete article");
    }

    @DeleteMapping("/admin/delete/q")
    public ResponseEntity<String> deleteByAdmin(
            @RequestHeader String username,
            @RequestParam String id
    ) {
        var articleOptional = repo.findById(id);
        if (articleOptional.isEmpty()) return ResponseEntity.badRequest().body("Article doesn't exist");
        var article = articleOptional.get();
        repo.deleteById(id);
        logger.info("article \"{}\" was deleted by admin {}", article.getTitle(), username);
        return ResponseEntity.ok("Article " + article.getTitle() + " deleted successfully!");
    }

}
