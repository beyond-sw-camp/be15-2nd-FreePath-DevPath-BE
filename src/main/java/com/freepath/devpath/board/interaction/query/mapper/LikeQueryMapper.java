package com.freepath.devpath.board.interaction.query.mapper;

import com.freepath.devpath.board.post.query.dto.response.PostDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LikeQueryMapper {

    boolean hasUserLikedBoard(@Param("userId") Integer userId, @Param("boardId") Integer boardId);

    boolean hasUserLikedComment(@Param("userId") Integer userId, @Param("commentId") Integer commentId);

    int countLikesByBoardId(@Param("boardId") Integer boardId);

    int countLikesByCommentId(@Param("commentId") Integer commentId);

    List<PostDto> selectLikedPostListByUserId(int userId);


}
