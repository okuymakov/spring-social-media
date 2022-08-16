package articles.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
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

    private @MongoId ObjectId id;

    @Indexed(unique = true)
    private String title;

    private String username;

    private String content;

    private String theme;

    private List<String> tags;

    private boolean isPrivate;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date updatedDate;
}
