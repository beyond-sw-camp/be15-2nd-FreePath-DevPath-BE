package com.freepath.devpath.csquiz.common.repository;

import com.freepath.devpath.csquiz.common.entity.CsQuizOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsQuizOptionRepository extends JpaRepository<CsQuizOption, Integer> {
}
