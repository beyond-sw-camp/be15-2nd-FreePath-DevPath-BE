package com.freepath.devpath.board.post.command.service;

import com.freepath.devpath.board.post.command.dto.PostCreateRequest;
import com.freepath.devpath.board.post.command.dto.PostUpdateRequest;
import com.freepath.devpath.board.post.command.entity.Attachment;
import com.freepath.devpath.board.post.command.entity.Board;
import com.freepath.devpath.board.post.command.exception.FileDeleteFailedException;
import com.freepath.devpath.board.post.command.exception.FileUpdateFailedException;
import com.freepath.devpath.board.post.command.exception.InvalidPostAuthorException;
import com.freepath.devpath.board.post.command.exception.NoSuchPostException;
import com.freepath.devpath.board.post.command.repository.AttachmentRepository;
import com.freepath.devpath.board.post.command.repository.PostRepository;
import com.freepath.devpath.board.post.command.dto.PostCreateResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostCommandService {
    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final AttachmentRepository attachmentRepository;

    @Transactional
    public PostCreateResponse createPost(PostCreateRequest postCreateRequest, List<MultipartFile> multipartFiles, int userId) {

        // 게시글 저장 로직
        Board board = Board.builder()
                .userId(userId)
                .boardCategory(postCreateRequest.getBoardCategory())
                .boardTitle(postCreateRequest.getBoardTitle())
                .boardContents(postCreateRequest.getBoardContents())
                .build();
        Board saved = postRepository.save(board);

        // 파일이 존재한다면 S3에 업로드
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile file : multipartFiles) {
                if (file.isEmpty()) continue;

                String originalFilename = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String s3Key = userId + "/" + uuid + "/" + originalFilename;

                String contentType = file.getContentType();

                try (InputStream is = file.getInputStream()) {
                    String s3Url = s3Service.uploadFile(s3Key, is, contentType);

                    // Attachment 엔티티 생성 및 저장
                    Attachment attachment = Attachment.builder()
                            .boardId(saved.getBoardId())
                            .s3_key(s3Key)
                            .s3_url(s3Url)
                            .build();
                    attachmentRepository.save(attachment);

                } catch (IOException e) {
                    throw new RuntimeException("S3 파일 업로드 실패: " + originalFilename, e);
                }
            }
        }

        return PostCreateResponse.builder()
                .postId(saved.getBoardId())
                .build();
    }

    @Transactional
    public void deletePost(int boardId, int userId) {
        Board post = postRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchPostException(ErrorCode.POST_DELETE_FAILED));

        // 다른 회원이 해당 게시물 ID를 알고 삭제하려는 경우를 필터링
        if (post.getUserId() != userId) {
            throw new InvalidPostAuthorException(ErrorCode.POST_DELETE_FORBIDDEN);
        }

        // 이미 삭제되어있는 게시물을 삭제하려는 경우 에러 반환
        if (post.getIsBoardDeleted().equals("Y")) {
            throw new FileDeleteFailedException(ErrorCode.POST_ALREADY_DELETED);
        }

        // 저장된 S3 Key를 바탕으로 저장된 첨부파일 삭제
        List<Attachment> attachments  = attachmentRepository.findByBoardId(boardId);

        // 게시글에 저장된 첨부파일이 있는 경우에만 추가적으로 삭제
        if (!attachments.isEmpty()) {
            for (Attachment attachment : attachments) {
                try {
                    s3Service.deleteFile(attachment.getS3_key());
                    attachmentRepository.delete(attachment);
                } catch (Exception e) {
                    throw new FileDeleteFailedException(ErrorCode.FILE_DELETE_FAILED);
                }
            }
        }

        // 게시글 Soft Delete
        post.delete();
    }

    @Transactional
    public void updatePost(PostUpdateRequest postUpdateRequest, int boardId, int userId) {
        Board post = postRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchPostException(ErrorCode.POST_NOT_FOUND));

        // 다른 회원이 해당 게시물 ID를 알고 수정하려는 경우를 필터링
        if (post.getUserId() != userId) {
            throw new InvalidPostAuthorException(ErrorCode.POST_UPDATE_FORBIDDEN);
        }

        // 이미 삭제되어있는 게시물을 삭제하려는 경우 에러 반환
        if (post.getIsBoardDeleted().equals("Y")) {
            throw new FileUpdateFailedException(ErrorCode.POST_ALREADY_DELETED);
        }

        String modifiedTitle = postUpdateRequest.getTitle();
        String modiifiedContents = postUpdateRequest.getContent();

        // 게시글 제목, 내용 수정
        post.modifyTitleAndContent(modifiedTitle, modiifiedContents);
    }
}

