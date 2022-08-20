package com.kuymakov.socialmedia.comments.controller;

import com.kuymakov.socialmedia.comments.model.Comment;
import com.kuymakov.socialmedia.comments.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(CommentsController.class);

    @GetMapping("/get/q")
    public ResponseEntity<List<Comment>> getById(@RequestParam("article") String articleId) {
        List<Comment> articles = repo.findByArticleId(articleId);
        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Comment>> getByUsername(@RequestHeader String username) {
        List<Comment> articles = repo.findByUsername(username);
        return ResponseEntity.ok().body(articles);
    }

    @PostMapping("/create")
    public ResponseEntity<Comment> create(
            @RequestHeader String username,
            @RequestBody Comment commentDto
    ) {
        var comment = Comment.builder()
                .username(username)
                .articleId(commentDto.getArticleId())
                .content(commentDto.getContent())
                .build();
        comment = repo.insert(comment);
        logger.info("{} created a comment on the article {}", username, comment.getArticleId());
        return ResponseEntity.ok().body(comment);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestHeader String username,
            @RequestParam String id
    ) {
        var commentOpt = repo.findById(id);
        if (commentOpt.isEmpty()) return ResponseEntity.badRequest().body("Comment doesn't exist");
        var comment = commentOpt.get();

        if (Objects.equals(comment.getUsername(), username)) {
            repo.deleteById(id);
            logger.info("{} deleted a comment on the article {}", username, comment.getArticleId());
            return ResponseEntity.ok("Comment " + comment.getId() + " deleted successfully!");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user does not have permission to delete comment");
    }

    @PutMapping("/update")
    public ResponseEntity<Comment> update(
            @RequestHeader String username,
            @RequestBody Comment commentDto
    ) {
        var comment = Comment.builder()
                .username(username)
                .articleId(commentDto.getArticleId())
                .content(commentDto.getContent())
                .build();
        comment = repo.save(comment);
        logger.info("{} updated a comment on the article {}", username, comment.getArticleId());
        return ResponseEntity.ok().body(comment);
    }

}
