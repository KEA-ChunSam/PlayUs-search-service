package com.playus.searchservice.domain.post.document;

import com.playus.searchservice.domain.post.enums.TeamTag;
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
@Setting(settingPath = "/elasticsearch/post-settings.json")
@Document(indexName = "mysql-server.community_dev.post")
public class PostDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    private String description;
    private String imageUrl;

    @Field(type = FieldType.Keyword)
    private TeamTag tag;
    private int view;

    @Field(type = FieldType.Boolean)
    private boolean activated;
    private boolean isSecret;

    @Field(type = FieldType.Long)
    private Long writerId;

    private LocalDate twpDate;

    private LocalDateTime createdAt;
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


}
