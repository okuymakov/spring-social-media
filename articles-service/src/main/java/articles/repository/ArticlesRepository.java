package articles.repository;

import articles.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface ArticlesRepository extends MongoRepository<Article, String> {

    @Query("{'tags' : {$in : ?0}, 'isPrivate' : false}")
    List<Article> findByTags(List<String> tags);

    @Query("{'title' : ?0, 'isPrivate' : false}")
    Article findByTitle(String title);

    @Query("{'theme' : ?0, 'isPrivate' : false}")
    List<Article> findByTheme(String theme);

    @Query("{'username' : ?0, 'isPrivate' : ?1}")
    List<Article> findByUsername(String username, boolean isPrivate);

    @Query("{ $or: [{'theme': ?0}, {'tags' : {$in : ?1}}], 'isPrivate' : false}")
    List<Article> findByThemeAndTags(String theme, List<String> tags);

    @Query("{'isPrivate' : false}")
    List<Article> findAllPublic();

}
