package com.playus.searchservice.domain.post.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class PostSearchResult {

    private Long postId;
    private Long writerId;
    private String writerName;
    private String title;
    private String thumbnailUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalDateTime createdAt;

    @Builder
    private PostSearchResult(Long postId, Long writerId, String writerName, String title, String thumbnailUrl, LocalDateTime createdAt) {
        this.postId = postId;
        this.writerId = writerId;
        this.writerName = writerName;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
    }

    public static PostSearchResult of(Long postId, String writerName, String title, String thumbnailUrl, LocalDateTime createdAt) {
        return PostSearchResult.builder()
                .postId(postId)
                .writerName(writerName)
                .title(title)
                .thumbnailUrl(thumbnailUrl)
                .createdAt(createdAt)
                .build();
    }

    public void updateWriterName(String writerName) {
        this.writerName = writerName;
    }
}
