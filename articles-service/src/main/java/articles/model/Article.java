package articles.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.List;

@Document(collection = "articles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    public @MongoId ObjectId id;
    public String title;
    public String content;
    public String theme;
    public List<String> tags;
    public String username;
    @CreatedDate
    public Date createdDate;
}
