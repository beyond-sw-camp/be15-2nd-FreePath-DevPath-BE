package com.freepath.devpath.email.repository;

import com.freepath.devpath.email.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    @Query("SELECT n FROM News n WHERE DATE(n.mailingDate) = CURRENT_DATE")
    List<News> findNewsForToday();
}
