package com.playus.searchservice.domain.post.document;

import com.playus.searchservice.domain.post.enums.TeamTag;
import com.playus.searchservice.domain.post.vo.PostSearchResult;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
//@Setting(settingPath = "/elasticsearch/post-settings.json")
@Document(indexName = "mysql-server.community_dev.post")
public class PostDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    private String description;

    @Field(name = "image_url")
    private String imageUrl;

    @Field(type = FieldType.Keyword, name = "tag")
    private TeamTag tag;

    private int view;

    @Field(type = FieldType.Boolean)
    private boolean activated;

    @Field(name = "is_secret")
    private boolean isSecret;

    @Field(type = FieldType.Long, name = "writer_id")
    private Long writerId;

    @Field(name = "twp_date")
    private LocalDate twpDate;

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    private PostDocument(Long id, String title, String description, String imageUrl, TeamTag tag, int view, boolean activated, boolean isSecret, Long writerId, LocalDate twpDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.tag = tag;
        this.view = view;
        this.activated = activated;
        this.isSecret = isSecret;
        this.writerId = writerId;
        this.twpDate = twpDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public PostSearchResult toPostSearchResult() {
        return PostSearchResult.builder()
                .postId(id)
                .writerId(writerId)
                .title(title)
                .thumbnailUrl(imageUrl)
                .createdAt(createdAt)
                .build();
    }
}
