package com.freepath.devpath.board.interaction.command.domain.repository;

import com.freepath.devpath.board.interaction.command.domain.aggregate.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByUserIdAndBoardId(Long userId, Long boardId);

    void deleteByUserIdAndBoardId(Long userId, Long boardId);
}
