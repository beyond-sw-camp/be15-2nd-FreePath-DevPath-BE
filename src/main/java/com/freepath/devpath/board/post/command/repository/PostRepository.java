package com.freepath.devpath.board.post.command.repository;

import com.freepath.devpath.board.post.command.entity.Board;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Board, Integer> {

    @Modifying
    @Query("UPDATE Board b SET b.isBoardDeleted = 'R' WHERE b.boardId = :boardId")
    void updateBoardStatusToReported(@Param("boardId") int boardId);

}
