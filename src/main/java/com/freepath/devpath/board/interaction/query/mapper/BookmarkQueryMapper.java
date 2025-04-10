package com.freepath.devpath.board.interaction.query.mapper;

import com.freepath.devpath.board.post.query.dto.response.PostDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookmarkQueryMapper {

    boolean hasUserBookmarkedPost(@Param("userId") int userId, @Param("boardId") int boardId);

    List<PostDto> selectBookmarkedPostListByUserId(int userId);

}
