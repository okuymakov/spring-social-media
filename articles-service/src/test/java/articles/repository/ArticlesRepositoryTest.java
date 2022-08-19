package articles.repository;

import articles.model.Article;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@DataMongoTest
public class ArticlesRepositoryTest {

    @Autowired
    ArticlesRepository articlesRepository;
    List<Article> articles = new ArrayList<>();

    @Before
    public void fillData() {
        for (int i = 0; i < 10; i++) {
            var article = Article.builder()
                    .title("article " + (i + 1))
                    .content("content " + (i + 1))
                    .theme("theme " + (i + 1))
                    .tags(i % 2 == 0 ? List.of("tag 1", "tag 2") : List.of("tag 3", "tag 4"))
                    .username("user " + (i + 1))
                    .isPrivate(i % 2 != 0)
                    .build();
            articles.add(article);
            articlesRepository.insert(article);
        }
    }

    @Test
    public void shouldReturnAllArticles() {
        var articlesFromRepo = articlesRepository.findAll();
        Assertions.assertEquals(articles, articlesFromRepo);
    }

    @Test
    public void shouldReturnArticlesByTags() {
        var tags = List.of("tag 1", "tag 2");
        var articlesFromRepo = articlesRepository.findByTags(tags);
        var expectedArticles = articles.stream()
                .filter(a -> a.getTags().stream().anyMatch(tags::contains) && !a.isPrivate())
                .collect(Collectors.toList());
        Assertions.assertEquals(expectedArticles, articlesFromRepo);
    }

    @Test
    public void shouldReturnUpdatedArticle() {
        var article = articlesRepository.findByTitle("article 1");
        article.setTitle("article 111");
        var updatedArticle = articlesRepository.save(article);
        Assertions.assertEquals(updatedArticle, article);
    }

    @Test
    public void shouldReturnArticleWithoutDeleted() {
        var article = articlesRepository.findByTitle("article 1");
        articles.removeIf(a -> Objects.equals(a.getTitle(), article.getTitle()));
        articlesRepository.deleteById(article.getId().toString());
        Assertions.assertEquals(articles, articlesRepository.findAll());
    }
}