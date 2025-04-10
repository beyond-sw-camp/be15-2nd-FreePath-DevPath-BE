package com.freepath.devpath.board.interaction.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeQueryMapper {

    boolean hasUserLikedBoard(@Param("userId") Integer userId, @Param("boardId") Integer boardId);

    boolean hasUserLikedComment(@Param("userId") Integer userId, @Param("commentId") Integer commentId);

    int countLikesByBoardId(@Param("boardId") Integer boardId);

    int countLikesByCommentId(@Param("commentId") Integer commentId);
}
