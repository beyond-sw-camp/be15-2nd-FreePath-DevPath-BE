package com.freepath.devpath.board.comment.command.domain.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "COMMENT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    private int userId;
    private Integer boardId;
    private Integer parentCommentId;

    @Column(name = "comment_contents")
    private String contents;
    @Column(name = "comment_created_at")
    private Date createdAt;
    @Column(name = "comment_modified_at")
    private Date modifiedAt;
    @Column(name = "is_comment_deleted")
    private String deleted;

    public void updateContent(String newContent) {
        this.contents = newContent;
        this.modifiedAt = new Date();
    }

    public void softDelete() {
        this.deleted = "Y";
        this.modifiedAt = new Date();
    }

}
