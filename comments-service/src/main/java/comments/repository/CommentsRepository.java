package comments.repository;

import comments.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface CommentsRepository extends MongoRepository<Comment, String> {
    @Query("{'articleId' : ?0}")
    List<Comment> findByArticleId(String articleId);

    @Query("{'username' : ?0}")
    List<Comment> findByUsername(String username);
}
