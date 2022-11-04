package com.daesung.api.news.repository;

import com.daesung.api.news.domain.News;
import com.daesung.api.news.domain.NewsFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsFileRepository extends JpaRepository<NewsFile,Long> {
}
