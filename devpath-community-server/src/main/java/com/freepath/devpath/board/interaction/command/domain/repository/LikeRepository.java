package com.freepath.devpath.board.interaction.command.domain.repository;

import com.freepath.devpath.board.interaction.command.domain.aggregate.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    boolean existsByUserIdAndBoardId(Long userId, Long boardId);

    boolean existsByUserIdAndCommentId(Long userId, Long commentId);

    void deleteByUserIdAndBoardId(Long userId, Long boardId);

    void deleteByUserIdAndCommentId(Long userId, Long commentId);
}
