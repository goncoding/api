package com.daesung.api.news.repository;

import com.daesung.api.news.domain.News;
import com.daesung.api.news.domain.NewsThumbnailFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewsThumbnailFileRepository extends JpaRepository<NewsThumbnailFile, Long> {

    List<NewsThumbnailFile> findByThumbnailFileOriginalName(String originalName);

    List<NewsThumbnailFile> findByNewsId(Long id);

    List<NewsThumbnailFile> findByNewsIdOrderByRegDateDesc(Long id);

    @Transactional
    @Modifying
    int deleteByNewsId(Long id);

}
