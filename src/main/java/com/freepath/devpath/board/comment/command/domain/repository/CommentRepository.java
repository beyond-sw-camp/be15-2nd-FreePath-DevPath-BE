package com.freepath.devpath.board.comment.command.domain.repository;

import com.freepath.devpath.board.comment.command.domain.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
