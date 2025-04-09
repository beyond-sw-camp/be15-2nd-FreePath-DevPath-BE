package com.freepath.devpath.board.comment.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HierarchicalCommentDto {
    private int commentId;
    private Integer parentCommentId; // null 가능
    private String nickname;
    private String contents;
    private Date createdAt;
    private Date modifiedAt;
    private int depth; // 계층 깊이
}
