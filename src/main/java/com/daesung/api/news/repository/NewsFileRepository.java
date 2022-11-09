package com.daesung.api.news.repository;

import com.daesung.api.news.domain.NewsFile;
import com.daesung.api.news.domain.enumType.ShowYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewsFileRepository extends JpaRepository<NewsFile,Long> {

    List<NewsFile> findByNewsFileOriginalName(String nbNo);

    List<NewsFile> findByNewsId(Long id);

    List<NewsFile> findByNewsIdOrderByRegDateDesc(Long id);

    List<NewsFile> findByNewsIdAndShowYn(Long id, ShowYn showYn);

    NewsFile findByNewsFileSavedName(String savedName);

    @Transactional
    @Modifying
    int deleteByNewsId(Long id);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update NewsFile n set n.showYn = 'N' where n.news.id = :id")
    int updateShowN(@Param("id") Long id);



}
