package articles.repository;

import articles.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ArticlesRepository extends MongoRepository<Article, String> { }
