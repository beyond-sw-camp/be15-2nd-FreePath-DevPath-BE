package com.freepath.devpath.board.post.command.service;

import com.freepath.devpath.board.post.command.dto.PostCreateRequest;
import com.freepath.devpath.board.post.command.entity.Board;
import com.freepath.devpath.board.post.command.repository.PostRepository;
import com.freepath.devpath.board.post.command.dto.PostCreateResponse;
import com.freepath.devpath.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final S3Service s3Service;
    private final PostRepository postRepository;

    @Transactional
    public PostCreateResponse createPost(PostCreateRequest postCreateRequest, List<MultipartFile> multipartFiles, int userId) {
        List<String> s3Keys = new ArrayList<>();

        // 파일이 존재한다면 S3에 업로드
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile file : multipartFiles) {
                if (file.isEmpty()) continue;

                String originalFilename = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String s3Key = userId + "/" + uuid + "/" + originalFilename;

                String contentType = file.getContentType();

                try (InputStream is = file.getInputStream()) {
                    s3Service.uploadFile(s3Key, is, contentType);
                    s3Keys.add(s3Key); // 나중에 이미지 DB 저장 등에 활용 가능
                } catch (IOException e) {
                    throw new RuntimeException("S3 파일 업로드 실패: " + originalFilename, e);
                }
            }
        }

        // 게시글 저장 로직 예시
        Board board = Board.builder()
                .userId(userId)
                .boardCategory(postCreateRequest.getBoardCategory())
                .boardTitle(postCreateRequest.getBoardTitle())
                .boardContents(postCreateRequest.getBoardContents())
                .build();
        Board saved = postRepository.save(board);

        return PostCreateResponse.builder()
                .postId(saved.getBoardId())
                .build();
    }

}

