package com.freepath.devpath.board.post.command.repository;

import com.freepath.devpath.board.post.command.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Board, Integer> {

}
