package com.freepath.devpath.board.comment.command.domain.repository;

import com.freepath.devpath.board.comment.command.domain.domain.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Modifying
    @Query("UPDATE Comment c SET c.deleted = 'R' WHERE c.commentId = :commentId")
    void updateCommentStatusToReported(@Param("commentId") int commentId);

}
