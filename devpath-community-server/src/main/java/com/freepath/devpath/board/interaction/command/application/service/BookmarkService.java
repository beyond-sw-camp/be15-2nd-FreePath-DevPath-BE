package com.freepath.devpath.board.interaction.command.application.service;

import com.freepath.devpath.board.interaction.command.application.dto.BookmarkRequest;
import com.freepath.devpath.board.interaction.command.domain.aggregate.Bookmark;
import com.freepath.devpath.board.interaction.command.domain.repository.BookmarkRepository;
import com.freepath.devpath.board.interaction.exception.AlreadyBookmarkedException;
import com.freepath.devpath.board.interaction.exception.BoardNotFoundException;
import com.freepath.devpath.board.interaction.exception.BookmarkNotFoundException;
import com.freepath.devpath.board.post.command.repository.PostRepository;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;

    @Transactional
    public void bookmark(Long userId, BookmarkRequest request) {
        if (!postRepository.existsById(request.getBoardId().intValue())) {
            throw new BoardNotFoundException(ErrorCode.POST_NOT_FOUND);
        }

        if (bookmarkRepository.existsByUserIdAndBoardId(userId, request.getBoardId())) {
            throw new AlreadyBookmarkedException(ErrorCode.ALREADY_BOOKMARKED);
        }

        bookmarkRepository.save(Bookmark.builder()
                .userId(userId)
                .boardId(request.getBoardId())
                .build());
    }

    @Transactional
    public void unbookmark(Long userId, BookmarkRequest request) {
        if (!postRepository.existsById(request.getBoardId().intValue())) {
            throw new BoardNotFoundException(ErrorCode.POST_NOT_FOUND);
        }

        if (!bookmarkRepository.existsByUserIdAndBoardId(userId, request.getBoardId())) {
            throw new BookmarkNotFoundException(ErrorCode.BOOKMARK_NOT_FOUND);
        }

        bookmarkRepository.deleteByUserIdAndBoardId(userId, request.getBoardId());
    }
}
