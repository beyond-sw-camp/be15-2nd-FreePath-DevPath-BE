package com.freepath.devpath.board.post.command.service;

import com.freepath.devpath.board.post.command.entity.Attachment;
import com.freepath.devpath.board.post.command.exception.FileDeleteFailedException;
import com.freepath.devpath.board.post.command.repository.AttachmentRepository;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AttachmentService {

    private final S3Service s3Service;
    private final AttachmentRepository attachmentRepository;

    public void uploadAndSaveFiles(List<MultipartFile> files, int boardId, int userId) {
        if (files == null || files.isEmpty()) return;

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String originalFilename = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String s3Key = userId + "/" + uuid + "/" + originalFilename;

            try (InputStream is = file.getInputStream()) {
                String s3Url = s3Service.uploadFile(s3Key, is, file.getContentType());

                Attachment attachment = Attachment.builder()
                        .boardId(boardId)
                        .s3_key(s3Key)
                        .s3_url(s3Url)
                        .build();
                attachmentRepository.save(attachment);

            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 실패: " + originalFilename, e);
            }
        }
    }

    public void deleteAttachmentsByBoardId(int boardId) {
        List<Attachment> attachments = attachmentRepository.findByBoardId(boardId);

        for (Attachment attachment : attachments) {
            try {
                s3Service.deleteFile(attachment.getS3_key());
                attachmentRepository.delete(attachment);
            } catch (Exception e) {
                throw new FileDeleteFailedException(ErrorCode.FILE_DELETE_FAILED);
            }
        }
    }
}
