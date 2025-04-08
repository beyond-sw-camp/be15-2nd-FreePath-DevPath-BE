package com.freepath.devpath.board.post.command.repository;

import com.freepath.devpath.board.post.command.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    List<Attachment> findByBoardId(int boardId);
}
