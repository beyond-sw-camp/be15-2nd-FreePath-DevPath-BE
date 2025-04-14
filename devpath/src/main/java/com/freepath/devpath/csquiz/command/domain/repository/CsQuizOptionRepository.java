package com.freepath.devpath.csquiz.command.domain.repository;

import com.freepath.devpath.csquiz.command.domain.aggregate.CsQuizOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CsQuizOptionRepository extends JpaRepository<CsQuizOption, Integer> {
    List<CsQuizOption> findByCsquizId(int csquizId);
    void deleteByCsquizId(int csquizId); // cs퀴즈 삭제할 때 옵션 테이블에 있는 옵션도 같이 삭제

}
