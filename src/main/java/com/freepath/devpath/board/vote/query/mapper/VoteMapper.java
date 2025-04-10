package com.freepath.devpath.board.vote.query.mapper;

import com.freepath.devpath.board.vote.query.dto.response.VoteDetailDTO;
import com.freepath.devpath.board.vote.query.dto.response.VoteItemDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VoteMapper {

    VoteDetailDTO selectVoteDetail(int userId, int boardId);

    List<VoteItemDTO> selectVoteItemsByBoardId(int boardId);
}
