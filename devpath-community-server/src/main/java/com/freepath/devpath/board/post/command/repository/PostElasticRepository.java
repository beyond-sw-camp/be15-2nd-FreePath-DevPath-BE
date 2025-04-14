package com.freepath.devpath.board.post.command.repository;

import com.freepath.devpath.board.post.command.domain.BoardDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostElasticRepository extends ElasticsearchRepository<BoardDocument, String> {
    Page<BoardDocument> findByBoardContentsContaining(String keyword, Pageable pageable);
}
