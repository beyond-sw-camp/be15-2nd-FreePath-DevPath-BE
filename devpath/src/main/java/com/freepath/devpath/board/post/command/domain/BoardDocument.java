package com.freepath.devpath.board.post.command.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "board")
@Getter
@Builder
public class BoardDocument {
    @Id
    private String boardId;
    private String boardTitle;
    @Field(type = FieldType.Text)
    private String boardContents;
    private Date createdAt;
}