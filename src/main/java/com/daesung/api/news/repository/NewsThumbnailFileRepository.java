package com.daesung.api.news.repository;

import com.daesung.api.news.domain.News;
import com.daesung.api.news.domain.NewsThumbnailFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsThumbnailFileRepository extends JpaRepository<NewsThumbnailFile, Long> {
}
